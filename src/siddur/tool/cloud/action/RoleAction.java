package siddur.tool.cloud.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import siddur.common.miscellaneous.Constants;
import siddur.common.security.Permission;
import siddur.common.security.PermissionManager;
import siddur.common.security.PermissionManager.PermissionGroup;
import siddur.common.security.RoleInfo;
import siddur.common.web.Result;
import siddur.common.web.DBAction;
import siddur.common.web.Perm;

public class RoleAction extends DBAction<RoleInfo>{

	@Override
	public Class<RoleInfo> getEntityClass() {
		return RoleInfo.class;
	}
	
	private int extractPerm(HttpServletRequest req){
		String[] perms = req.getParameterValues("perm");
		if(perms == null) return 0;
		int sum = 0;
		for(String p : perms){
			int i = Integer.parseInt(p);
			sum += (1 << i);
		}
		return sum;
	}

	@Perm(Permission.ROLE_ADD)
	public Result add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String name = req.getParameter("rolename");
		RoleInfo role = new RoleInfo();
		role.setRolename(name);
		role.setPermission(extractPerm(req));
		
		getEntityManager(req, true).persist(role);
		return list(req, resp);
	}
	
	@Perm(Permission.ROLE_LIST_VIEW)
	public Result list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		List<RoleInfo> list = findAll(req);
		req.setAttribute("list", list);
		
		req.setAttribute("crumb", "manage > role");
		req.getRequestDispatcher("/jsp/role/role-list.jsp").forward(req, resp);
		return Result.ok();
	}
	
	@Perm(Permission.ROLE_EDIT)
	public Result update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String roleIdStr = req.getParameter("roleId");
		if(roleIdStr != null){
			getEntityManager(req, true);
			RoleInfo role = find(Integer.parseInt(roleIdStr), req);
			role.setPermission(extractPerm(req));
			update(role, req);
			
			//update session
			PermissionGroup pg = PermissionManager.createPermissionGroup(role.getPermission());
			req.getSession().setAttribute(Constants.PERMS, pg);
		}
		return list(req, resp);
	}
	
	@Perm(Permission.ROLE_VIEW)
	public Result detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String crumb = null;
		String roleIdStr = req.getParameter("roleId");
		if(roleIdStr != null){
			RoleInfo role = find(Integer.parseInt(roleIdStr), req);
			req.setAttribute("role", role);
			PermissionGroup pg = PermissionManager.createPermissionGroup(role.getPermission());
			List<Permission> allPerms = PermissionManager.getAllPermissions();
			boolean[] perms = new boolean[allPerms.size()];
			for (int i = 0; i < perms.length; i++) {
				perms[i] = pg.hasPerm(i);
			}
			req.setAttribute("perms", perms);
			req.setAttribute("allPerms", allPerms);
			crumb = "manage > role("+role.getRolename()+")";
		}
		else{
			crumb = "manage > role(add)";
		}
		req.setAttribute("crumb", crumb);
		req.getRequestDispatcher("/jsp/role/role-detail.jsp").forward(req, resp);
		return Result.ok();
	}
}
