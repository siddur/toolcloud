package siddur.common.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import siddur.common.jpa.EntityManagerWrapper;
import siddur.common.jpa.JPAUtil;
import siddur.common.miscellaneous.Constants;
import siddur.common.security.DoNotAuthenticate;
import siddur.common.security.Permission;
import siddur.common.security.PermissionManager;
import siddur.common.security.PermissionManager.PermissionGroup;
import siddur.common.security.RequestUtil;
import siddur.common.security.UserInfo;

public class ActionMapper{
	Logger log4j = Logger.getLogger(ActionMapper.class);
	private static final String ACTION_CONFIG = "action-list";
	
	private Map<String, Method> methodMap = new HashMap<String, Method>();
	private Map<String, Action> actionMap = new HashMap<String, Action>();
	private Map<String, Permission> permMap = new HashMap<String, Permission>();
	private List<String> excludeAuth = new ArrayList<String>();
	private EntityManagerWrapper emWrapper = null;
	
	
	public ActionMapper(Map<String, Object> context){
		emWrapper = (EntityManagerWrapper) context.get(Constants.ENTITY_MANAGER_WRAPPER);
		
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
				
				DoNotAuthenticate classLevel = action.getClass().getAnnotation(DoNotAuthenticate.class);
				boolean classAuthLevel = true;
				if(classLevel != null && classLevel.value() == Boolean.TRUE){
					classAuthLevel = false;
				}
				
				if(path == null){
					path = action.getPath();
				}
				path = "/" + path;
				Method[] methods = clz.getDeclaredMethods();
				for(Method m : methods){
					if((m.getModifiers() & Modifier.PUBLIC) == 1){
						Class<?>[] c = m.getParameterTypes();
						if(Result.class.isAssignableFrom(m.getReturnType())
								&& c.length == 2 
								&& HttpServletRequest.class.isAssignableFrom(c[0]) 
								&& HttpServletResponse.class.isAssignableFrom(c[1])){
							String methodName = m.getName().toLowerCase(); 
							/*
							 *  /path/methodName
							 */
							String mpath = path + "/" + methodName;
							mpath = mpath.replace("\\", "/").replace("//", "/");
							methodMap.put(mpath, m);
							actionMap.put(mpath, action);
							
							Perm perm = m.getAnnotation(Perm.class);
							if(perm != null){
								permMap.put(mpath, perm.value());
							}
							
							DoNotAuthenticate methodLevel = m.getAnnotation(DoNotAuthenticate.class);
							if(methodLevel == null){
								if(!classAuthLevel){
									excludeAuth.add(mpath);
								}
							}else{
								if(methodLevel.value()){
									excludeAuth.add(mpath);
								}
							}
							
							log4j.info("find method:" + m.getName());
						}
					}
				}
			}
				
		}
		
	}
	
	public void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
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
			if(url.startsWith("/query")){
				actionPath = "/query/detail";
			}else{
				actionPath = "/tool/detail";
			}
		}
		
		Permission perm = permMap.get(actionPath);
		checkCookie(req);
		
		if(!excludeAuth.contains(actionPath)){
			boolean hasUser = authenticate(req, resp);
			if(!hasUser){
				req.getRequestDispatcher("/jsp/user/login.jsp").forward(req, resp);
				return;
			}
			
			if(!checkPermission(req, perm)){
				req.setAttribute("error", "Not permitted!");
				req.getRequestDispatcher("/error.jsp").forward(req, resp);
				return;
			}
		}
		
		Result r;
		try {
			r = exec(actionPath, req, resp);
			emWrapper.commit(req);
		} catch (Exception e) {
			emWrapper.rollback(req);
			log4j.error("An error occurs when invoking the action: " + actionPath, e);
			r = Result.error(e.getMessage());
		} finally{
			emWrapper.closeEntityManager(req);
		}
		if(r.isError()){
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
		else if(r.isAjax){
			resp.setContentType("text/plain; charset=utf-8");
			Writer w = resp.getWriter();
			log4j.info(r.getMessage());
			w.write(r.getMessage());
			w.flush();
			w.close();
		}
	}
	
	
	private void checkCookie(HttpServletRequest req){
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
		if(r.isInvoke()){
			r = exec(r.getMessage(), req, resp);
		}

		return r;
	}
	
	public enum ResultType{
		ok, success, error, redirect, forward, invoke
	}
	
	
	public static class Result {
		public final static Result NotFound = new Result("404 not found", ResultType.error);
		public final static Result Forbidden = new Result("not permitted", ResultType.error);
		
		private String message = "";
		private ResultType type = ResultType.ok;
		private boolean isAjax = false;
		
		public Result(String message) {
			this.message = message;
		}
		
		public Result(String message, ResultType type) {
			this.message = message;
			this.type = type;
		}
		

		public static Result ok(String msg){
			return new Result(msg, ResultType.ok);
		}
		
		public static Result success(String msg){
			return new Result(msg, ResultType.success);
		}
		
		public static Result error(String msg){
			if(StringUtils.isEmpty(msg)){
				msg = "操作失败";
			}
			return new Result(msg, ResultType.error);
		}
		
		public static Result redirect(String msg){
			return new Result(msg, ResultType.redirect);
		}
		
		public static Result invoke(String msg){
			return new Result(msg, ResultType.invoke);
		}
		
		public static Result ok(){
			return ok(null);
		}
		
		public static Result success(){
			return success(null);
		}
		
		public static Result error(){
			return error(null);
		}
		
		
		public static Result forward(String msg){
			return new Result(msg, ResultType.forward);
		}
		
		public static Result ajax(String msg){
			Result r = new Result(msg);
			r.isAjax = true;
			return r;
		}
		
		public boolean isForward(){
			return type == ResultType.forward;
		}
		
		public boolean isRedirect(){
			return type == ResultType.redirect;
		}
		
		public boolean isInvoke(){
			return type == ResultType.invoke;
		}
		
		public boolean isOK(){
			return type == ResultType.ok;
		}
		
		public boolean isSuccessful(){
			return type == ResultType.success;
		}
		
		public boolean isError(){
			return type == ResultType.error;
		}
		
		
		public String getMessage(){
			return message;
		}
		
		public boolean isAjax(){
			return isAjax;
		}
	}
	
	
}
