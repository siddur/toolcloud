package siddur.common.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import siddur.common.jpa.EntityManagerWrapper;
import siddur.common.jpa.JPAUtil;
import siddur.common.miscellaneous.Constants;
import siddur.common.security.DoNotAuthenticate;
import siddur.common.security.Permission;
import siddur.common.security.PermissionManager;
import siddur.common.security.PermissionManager.PermissionGroup;
import siddur.common.security.UserInfo;
import siddur.common.util.RequestUtil;
import siddur.common.util.WebPointUtil;
import siddur.common.util.WebPointUtil.DefaultWebPointFilter;
import siddur.common.util.WebPointUtil.WebPointFilter;
import siddur.common.util.WebPointUtil.WebPointHandler;
import siddur.tool.core.AppTool;
import siddur.tool.core.ITool;
import siddur.tool.core.IToolManager;

public class ActionMapper{
	private static final Logger log4j = Logger.getLogger(ActionMapper.class);
	private static ActionMapper instance;
	
	private static final String ACTION_CONFIG = "action-list";
	
	private Map<String, Method> methodMap = new HashMap<String, Method>();
	private Map<String, Action> actionMap = new HashMap<String, Action>();
	private Map<String, Permission> permMap = new HashMap<String, Permission>();
	private List<String> excludeAuth = new ArrayList<String>();
	private EntityManagerWrapper emWrapper = null;
	private IToolManager tm = null;
	
	private WebPointFilter filter = new DefaultWebPointFilter();
	private WebPointHandler handler = new WebPointHandler(){

		@Override
		public void handle(Method method, Object instance, String path) {
			Action action = (Action) instance;
			methodMap.put(path, method);
			actionMap.put(path, action);
			
			Perm perm = method.getAnnotation(Perm.class);
			if(perm != null){
				permMap.put(path, perm.value());
			}
			
			//class level auth
			DoNotAuthenticate classLevel = 
					action.getClass().getAnnotation(DoNotAuthenticate.class);
			boolean classAuthLevel = true;
			if(classLevel != null && classLevel.value() == Boolean.TRUE){
				classAuthLevel = false;
			}
			
			//method level auth
			DoNotAuthenticate methodLevel = method.getAnnotation(DoNotAuthenticate.class);
			if(methodLevel == null){
				if(!classAuthLevel){
					excludeAuth.add(path);
				}
			}else{
				if(methodLevel.value()){
					excludeAuth.add(path);
				}
			}
		}
		
	};
	
	public static ActionMapper createInstance(Map<String, Object> context){
		instance = new ActionMapper(context);
		return instance;
	}
	
	public static ActionMapper getInstance(){
		return instance;
	}
	
	private ActionMapper(Map<String, Object> context){
		emWrapper = (EntityManagerWrapper) context.get(Constants.ENTITY_MANAGER_WRAPPER);
		tm = (IToolManager) context.get(Constants.TOOL_PLUGIN_MANAGE);
		
		InputStream is = getClass().getResourceAsStream(ACTION_CONFIG);
		Properties p = new Properties();
		
		try {
			p.load(is);
			is.close();
		} catch (IOException e) {
			log4j.warn("", e);
		}
		
		for(Object keyObj :p.keySet()){
			String key = (String)keyObj;
			String path = p.getProperty(key);
			Class<?> clz;
			try {
				clz = Class.forName(key);
			} catch (ClassNotFoundException e) {
				log4j.warn(e);
				continue;
			}
			if(Action.class.isAssignableFrom(clz)){
				Action action = null;
				try {
					action = (Action)clz.newInstance();
					log4j.info("find action:" + clz.getName());
					action.init(context);
				} catch (Exception e) {
					log4j.warn(e);
					continue;
				}
				
				if(path == null){
					path = action.getPath();
				}
				if(!path.startsWith("/"))
					path = "/" + path;
				WebPointUtil.parse(path, action, filter, handler);
			}
		}
	}
	
	public void doAppAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String pathInfo = req.getPathInfo();
		int slash = pathInfo.indexOf("/", 1);
		String toolId = pathInfo.substring(1, slash);
		ITool tool = tm.getToolWrapper(toolId).getTool();
		if(tool instanceof AppTool){
			Result r = null;
			AppTool appTool = (AppTool) tool;
			try {
				r = appTool.invokeMethod(pathInfo.substring(slash), req, resp);
			} catch (Exception e) {
				log4j.warn(e.getCause(), e);
				r = Result.error(e.getCause().getMessage());
			}
			handleResult(r, req, resp);
			return;
		}
		req.setAttribute("error", "该tool不支持app功能");
		req.getRequestDispatcher("/error.jsp").forward(req, resp);;
	}
	
	
	public void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String actionPath = getActionPath(req);
		
//		Permission perm = permMap.get(actionPath);
//		checkUserInCookie(req);
//		
//		if(!excludeAuth.contains(actionPath)){
//			boolean hasUser = authenticate(req, resp);
//			if(!hasUser){
//				req.getRequestDispatcher("/jsp/user/login.jsp").forward(req, resp);
//				return;
//			}
//			
//			if(!checkPermission(req, perm)){
//				req.setAttribute("error", "Not permitted!");
//				req.getRequestDispatcher("/error.jsp").forward(req, resp);
//				return;
//			}
//		}
		
		Result r;
		try {
			r = exec(actionPath, req, resp);
			emWrapper.commit(req);
		} catch (Exception e) {
			emWrapper.rollback(req);
			log4j.error("An error occurs when invoking the action: " + actionPath, e);
			r = Result.error(e.getCause().getMessage());
		} finally{
			emWrapper.closeEntityManager(req);
		}
		handleResult(r, req, resp);
	}
	
	private void handleResult(Result r, HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
		if(r.isAjax()){
			resp.setContentType("text/plain; charset=utf-8");
			Writer w = resp.getWriter();
			String json = new Gson().toJson(r);
			log4j.info(json);
			w.write(json);
			w.flush();
			w.close();
		}
		else if(r.isError()){
			req.setAttribute("error", r.message);
			req.getRequestDispatcher("/error.jsp").forward(req, resp);
		}
		else if(r.isForward()){
			req.getRequestDispatcher(r.message).forward(req, resp);
		}
		else if(r.isSuccessful()){
			req.setAttribute("msg", r.message);
			req.getRequestDispatcher("/success.jsp").forward(req, resp);
		}
		else if(r.isRedirect()){
			String url = r.message;
			if(url.startsWith("/")){
				url = Constants.WEBSITE_ROOT + url;
			}
			else if(!url.startsWith("http")){
				url = Constants.WEBSITE_ROOT + "/ctrl/" + url;
			}
			resp.sendRedirect(url);
		}
	}
	
	private void checkUserInCookie(HttpServletRequest req){
		HttpSession s = req.getSession();
		if(s.getAttribute(Constants.CHECHED) == null){
			s.setAttribute(Constants.CHECHED, Constants.CHECHED_FLAG);
			Cookie[] cs = req.getCookies();
			if(cs != null){
				for (Cookie c : cs){
					if("username".equals(c.getName())){
						String username = c.getValue();
						UserInfo u = JPAUtil.getUser(username);
						if(u != null){
							req.getSession().setAttribute(Constants.USER, u);
						}
					}
				}
			}
		}
	}
	
	private String getActionPath(HttpServletRequest req){
		/*
		 *  path = "/path/methodName"
		 */
		String pathInfo = req.getPathInfo();
		String actionPath = null;
		if(pathInfo != null){
			actionPath = RequestUtil.getActionPath(req.getPathInfo());
		}else{
			///tool/detail/1380203034265.html
			String url = req.getRequestURI();
			int lastSlash = url.lastIndexOf("/");
			if(lastSlash == 0){
				actionPath = "/tool/detail";
			}else{
				actionPath = url.substring(0, lastSlash);
				actionPath += "/detail";
			}
		}
		return actionPath;
	}
	
	private boolean authenticate(HttpServletRequest req, HttpServletResponse resp){
		UserInfo u = (UserInfo)req.getSession().getAttribute(Constants.USER);
		if(u!=null)
			return true;
		
		return false;
	}
	
	
	private PermissionGroup getPermissionGroup(HttpServletRequest req){
		PermissionGroup pg = (PermissionGroup)req.getSession().getAttribute(Constants.PERMS);
		if(pg == null){
			UserInfo u = (UserInfo)req.getSession().getAttribute(Constants.USER);
			pg = PermissionManager.createPermissionGroup(u.getRole().getPermission());
			req.getSession().setAttribute(Constants.PERMS, pg);
		}
		return pg;
	}
	
	private boolean checkPermission(HttpServletRequest req, Permission perm){
		if(perm == null)
			return true;
		PermissionGroup pg = getPermissionGroup(req);
		return pg.hasPerm(perm);
	}
	
	public Result exec(String path, HttpServletRequest req, HttpServletResponse resp) throws Exception{
		Action action = actionMap.get(path);
		Method method = methodMap.get(path);
		
		if(log4j.isDebugEnabled()){
			log4j.debug("path:" + path + "\tdealer:" + action.getClass().getName() + "." + method.getName());
		}
		if(action == null || method == null){
			log4j.warn("Not found path:" + path);
			return Result.NotFound;
		}
		
		Object obj = method.invoke(action, req, resp);
		Result r = (Result) obj;
		//invoke another method
		if(r.isInvoke()){
			r = exec(r.getMessage(), req, resp);
		}
		return r;
	}
	
}
