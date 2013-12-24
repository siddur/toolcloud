package siddur.common.util;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import siddur.common.miscellaneous.Constants;
import siddur.common.security.Permission;
import siddur.common.security.PermissionManager;
import siddur.common.security.UserInfo;
import siddur.common.security.PermissionManager.PermissionGroup;
import siddur.tool.cloud.action.UtilAction;
import siddur.tool.core.exception.ToolCloudException;


public class RequestUtil {
	public static boolean hasPerm(HttpServletRequest req, Permission perm){
		PermissionGroup pg = (PermissionGroup) req.getSession().getAttribute(Constants.PERMS);
		if(pg == null){
			UserInfo u = (UserInfo)req.getSession().getAttribute(Constants.USER);
			if(u != null){
				pg = PermissionManager.createPermissionGroup(u.getRole().getPermission());
				req.getSession().setAttribute(Constants.PERMS, pg);
			}
		}
		if(pg != null){
			return pg.hasPerm(perm);
		}
		return false;
	}
	
	public static boolean isSelf(HttpServletRequest req, Integer userId){
		UserInfo u = (UserInfo)req.getSession().getAttribute(Constants.USER);
		if(u != null){
			return u.getUserId() == userId;
		}
		return false;
	}
	
	public static String getId(HttpServletRequest req, String idKey){
		String id = req.getParameter(idKey);
		if(id == null){
			id = getId(req.getPathInfo());
		}
		if(id == null){
			//".html".length = 5;
			// /1380203034265.html = 13 + 5
			String url = req.getRequestURI();
			int html = url.indexOf(".html");
			int slash = url.lastIndexOf("/", html);
			id = url.substring(slash + 1, html);
		}
		return id;
	}
	
	public static String getId(String path){
		if(path == null) return null;
		int slash = path.lastIndexOf("/");
		return path.substring(slash + 1);
	}
	
	public static String getActionPath(String path){
		int slash = path.indexOf("/", 1);
		slash = path.indexOf("/", slash + 1);
		if(slash > 0){
			path = path.substring(0, slash);
		}
		return path;
	}
	
	
	public static String findNode(String path, int index){
		StringTokenizer st = new StringTokenizer(path, "/");
		while(st.hasMoreTokens()){
			String node = st.nextToken();
			if(index -- == 0){
				return node;
			}
		}
		return null;
	}
	
	public static String substring(String path, int start){
		int i = findLocation(path, start);
		if(i == -1){
			return null;
		}
		return path.substring(i);
	}
	
	public static String substring(String path, int start, int end){
		int i = findLocation(path, start);
		if(i == -1){
			return null;
		}
		int j = findLocation(path, end);
		if(j == -1){
			return null;
		}
		return path.substring(i, j);
	}
	
	public static int findLocation(String path, int index){
		int location = -1;
		while(index -- >= 0){
			location = path.indexOf("/", location + 1);
			if(location == -1){
				break;
			}
		}
		return location;
	}
	
	public static void checkCaptcha(HttpServletRequest req){
		String input = req.getParameter(UtilAction.AUTHENTICODE);
		String origin = (String)req.getSession().getAttribute(UtilAction.AUTHENTICODE);
		boolean check = false;
		if(input != null && input.equalsIgnoreCase(origin)){
			check = true;
		}
		if(!check){
			throw new ToolCloudException("验证码不正确");
		}
	}
	
	public static String getAccesser(HttpServletRequest req){
		String saidBy = null;
		UserInfo u = (UserInfo) req.getSession().getAttribute(Constants.USER);
		if(u != null){
			saidBy = u.getName();
		}else{
			saidBy = req.getRemoteHost();
		}
		return saidBy;
	}
	
	public static void main(String[] args) {
		String path = "/a/b/123/de";
		System.out.println(findLocation(path, 4));
	}
}
