package siddur.tool.cloud.action;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import siddur.common.jpa.JPAUtil;
import siddur.common.miscellaneous.Constants;
import siddur.common.security.DoNotAuthenticate;
import siddur.common.security.Permission;
import siddur.common.security.RequestUtil;
import siddur.common.security.RoleInfo;
import siddur.common.security.UserInfo;
import siddur.common.web.ActionMapper.Result;
import siddur.common.web.DBAction;
import siddur.common.web.Perm;

public class UserAction extends DBAction<UserInfo>{
	
	@DoNotAuthenticate
	public Result login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		EntityManager em = getEntityManager(req);
		TypedQuery<UserInfo> q = em.createQuery("from UserInfo u where u.username=?", UserInfo.class);
		q.setParameter(1, username);
		UserInfo u = null;
		try {
			u = q.getSingleResult();
		} catch (Exception e) {
		}
		if(u != null){
			if(u.getPassword().equals(password)){
				req.getSession().setAttribute(Constants.USER, u);
				Cookie c = new Cookie("username", username);
				c.setPath(Constants.WEBSITE_ROOT + "/");
				c.setMaxAge(60 * 60 * 24 * 7);
				resp.addCookie(c);
				return Result.redirect("tool/home");
			}
		}
		return Result.redirect("/jsp/user/login.jsp");
	}
	
	public Result logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		UserInfo u = (UserInfo)req.getSession().getAttribute(Constants.USER);
		if(u != null){
			Cookie c = new Cookie("username", u.getUsername());
			c.setPath("Constants.WEBSITE_ROOT");
			c.setMaxAge(0);
			resp.addCookie(c);
		}
		req.getSession().invalidate();
		req.getSession().setAttribute(Constants.CHECHED, Constants.CHECHED_FLAG);
		return Result.redirect("/jsp/user/login.jsp");
	}
	
	@Perm(Permission.USER_ADD)
	public Result add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		UserInfo u = new UserInfo();
		u.setUsername(req.getParameter("username"));
		u.setPassword(req.getParameter("password"));
		String roleId = req.getParameter("role");
		RoleInfo role = getEntityManager(req).find(RoleInfo.class, Integer.parseInt(roleId));
		u.setRole(role);
		u.setEmail(req.getParameter("email"));
		u.setNickname(req.getParameter("nickname"));
		u.setRealname(req.getParameter("realname"));
		getEntityManager(req, true).persist(u);
		return list(req, resp);
	}
	
	@DoNotAuthenticate
	public Result register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String input = req.getParameter(UtilAction.AUTHENTICODE);
		String origin = (String)req.getSession().getAttribute(UtilAction.AUTHENTICODE);
		boolean check = false;
		if(input != null && input.equalsIgnoreCase(origin)){
			check = true;
		}
		if(!check){
			return Result.error("验证码不正确");
		}
		
		UserInfo u = new UserInfo();
		u.setUsername(req.getParameter("username"));
		u.setPassword(req.getParameter("password"));
		RoleInfo role = getEntityManager(req).find(RoleInfo.class, 2);
		u.setRole(role);
		u.setEmail(req.getParameter("email"));
		u.setNickname(req.getParameter("nickname"));
		u.setRealname(req.getParameter("realname"));
		getEntityManager(req, true).persist(u);
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
		return Result.ok();
	}
	
	@Perm(Permission.USER_DEL)
	public Result delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String userId = req.getParameter("userId");
		EntityManager em = getEntityManager(req, true);
		UserInfo u = em.find(UserInfo.class, Integer.parseInt(userId));
		em.remove(u);
		return list(req, resp);
	}
	
	@Perm(Permission.USER_VIEW)
	public Result list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		List<UserInfo> list = findAll(req);
		req.setAttribute("list", list);
		
		List<RoleInfo> roles = getEntityManager(req).createQuery("from RoleInfo", RoleInfo.class).getResultList();
		req.setAttribute("roles", roles);
		req.setAttribute("crumb", "manage > user");
		boolean canDelete = RequestUtil.hasPerm(req, Permission.USER_DEL);
		boolean canAdd = RequestUtil.hasPerm(req, Permission.USER_ADD);
		req.setAttribute("canDelete", canDelete);
		req.setAttribute("canAdd", canAdd);
		req.getRequestDispatcher("/jsp/user/user-list.jsp").forward(req, resp);
		
		return Result.ok();
	}
	
	public Result me(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		req.getRequestDispatcher("/jsp/user/user-me.jsp").forward(req, resp);
		return Result.ok();
	}
	
	@Perm(Permission.USER_VIEW)
	public Result detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		if(RequestUtil.hasPerm(req, Permission.ROLE_LIST_VIEW)){
			List<RoleInfo> roles = JPAUtil.findAll(RoleInfo.class);
			req.setAttribute("roles", roles);
		}
		
		String userIdStr = req.getParameter("userId");
		if(userIdStr != null){
			int userId = Integer.parseInt(userIdStr);
			UserInfo u = find(userId, req);
			req.setAttribute(Constants.USER, u);
			req.setAttribute("crumb","manage > user("+u.getUsername()+")");
		}
		
		boolean updateMsg = RequestUtil.hasPerm(req, Permission.USER_EDIT);
		boolean updateRole = RequestUtil.hasPerm(req, Permission.ROLE_EDIT);
		boolean canUpdate = updateMsg || updateRole;
		req.setAttribute("updatable", canUpdate);
		req.getRequestDispatcher("/jsp/user/user-detail.jsp").forward(req, resp);
		return Result.ok();
	}

	
	public Result selfUpdate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		UserInfo u = (UserInfo)req.getSession().getAttribute(Constants.USER);
		u.setEmail(req.getParameter("email"));
		u.setNickname(req.getParameter("nickname"));
		u.setRealname(req.getParameter("realname"));
		update(u, req);
		me(req, resp);
		return Result.ok();
	}
	
	public Result updatePwd(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
		UserInfo u = (UserInfo)req.getSession().getAttribute(Constants.USER);
		
		String oldPassword = req.getParameter("oldPassword");
		String DbPwd = u.getPassword();
		if(oldPassword != null){
			if(oldPassword.equals(DbPwd)){
				u.setPassword(req.getParameter("newPassword"));
				update(u, req);
				return me(req,resp);
			}else{
				req.setAttribute("noMatch", "The original password is incorrect");
			}
		}
		return Result.forward("/jsp/user/user-pwd.jsp");
	}
	
	@Perm(Permission.USER_EDIT)
	public Result update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String userIdStr = req.getParameter("userId");
		int userId = Integer.parseInt(userIdStr);
		EntityManager em = getEntityManager(req, true);
		UserInfo u = em.find(UserInfo.class, userId);
		u.setEmail(req.getParameter("email"));
		u.setNickname(req.getParameter("nickname"));
		u.setRealname(req.getParameter("realname"));
		
		if(RequestUtil.hasPerm(req, Permission.ROLE_LIST_VIEW)){
			String roleId = req.getParameter("role");
			if(roleId != null && !"".equals(roleId)){
				RoleInfo role = getEntityManager(req).find(RoleInfo.class, Integer.parseInt(roleId));
				u.setRole(role);
			}
		}
		
		update(u, req);
		detail(req, resp);
		return Result.ok();
	}

	@Override
	public Class<UserInfo> getEntityClass() {
		return UserInfo.class;
	}


	
}
