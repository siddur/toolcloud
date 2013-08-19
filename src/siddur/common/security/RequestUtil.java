package siddur.common.security;

import javax.servlet.http.HttpServletRequest;

import siddur.common.miscellaneous.Constants;
import siddur.common.security.PermissionManager.PermissionGroup;


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
	
}
