package siddur.tool.cloud.action;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import siddur.common.jpa.JPAUtil;
import siddur.common.miscellaneous.Comment;
import siddur.common.miscellaneous.Constants;
import siddur.common.miscellaneous.Paging;
import siddur.common.miscellaneous.QueryInfo;
import siddur.common.security.DoNotAuthenticate;
import siddur.common.security.Permission;
import siddur.common.security.RequestUtil;
import siddur.common.security.UserInfo;
import siddur.common.web.DBAction;
import siddur.common.web.ActionMapper.Result;
import siddur.common.web.Perm;

public class QueryAction extends DBAction<QueryInfo>{

	@Override
	public Class<QueryInfo> getEntityClass() {
		return QueryInfo.class;
	}

	@DoNotAuthenticate
	public Result ask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		return Result.forward("/jsp/query/ask.jsp");
	}
	
	@DoNotAuthenticate
	public Result doAsk(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String id = req.getParameter("id");
		QueryInfo q;
		if(!StringUtils.isEmpty(id)){
			Integer i = Integer.parseInt(id);
			q = find(i, req);
		}else{
			q = new QueryInfo();
			String who = null;
			UserInfo u = (UserInfo)req.getSession().getAttribute("user");
			if(u != null){
				who = u.getUsername();
			}else{
				who = req.getRemoteAddr();
			}
			q.setSaidBy(who);
		}
		q.setContent(req.getParameter("content"));
		q.setTitle(req.getParameter("title"));
		add(q, req);
		return Result.redirect("query/list?pageIndex=1&pageSize=20");
	}
	
	@DoNotAuthenticate
	public Result list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		EntityManager em = getEntityManager(req);
		TypedQuery<QueryInfo> q = em.createQuery("from QueryInfo q order by q.publishAt desc", QueryInfo.class);
		TypedQuery<Long> totalQuery = em.createQuery("select count(q) from QueryInfo q", Long.class);
		Paging<QueryInfo> list = JPAUtil.getPageData(req, q, totalQuery);
		req.setAttribute("queries", list);
		req.setAttribute("canDelQuery", RequestUtil.hasPerm(req, Permission.QUERY_DEL));
		return Result.forward("/jsp/query/list.jsp");
	}
	
	
	public Result editQuery(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		Integer id = Integer.parseInt(req.getParameter("id"));
		req.setAttribute("query", find(id, req));
		return Result.forward("/jsp/query/ask.jsp");
	}
	
	@DoNotAuthenticate
	public Result detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String idStr = RequestUtil.getId(req, "id");
		Integer id = Integer.parseInt(idStr);
		EntityManager em = getEntityManager(req);
		TypedQuery<Comment> q = em.createQuery("from Comment c where c.subject = 'q" 
				+ id + "' order by c.saidAt desc", 
					Comment.class);
		
		TypedQuery<Long> totalQuery = em.createQuery("select count(c) from Comment c where c.subject = 'q" 
				+ id + "'", Long.class);
		Paging<Comment> paging = JPAUtil.getPageData(req, q, totalQuery);
		req.setAttribute("comments", paging);
		req.setAttribute("query", find(id, req));
		req.setAttribute("canDelComment", RequestUtil.hasPerm(req, Permission.COMMENT_DEL));
		return Result.forward("/jsp/query/detail.jsp");
	}
	
	@Perm(Permission.COMMENT_DEL)
	public Result delComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		Integer id = Integer.parseInt(req.getParameter("id"));
		EntityManager em = getEntityManager(req, true);
		em.remove(em.find(Comment.class, id));
		Integer queryId = Integer.parseInt(req.getParameter("subjectId"));
		return Result.redirect("query/detail?id=" + queryId);
	}
	
	@Perm(Permission.QUERY_DEL)
	public Result delQuery(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		Integer id = Integer.parseInt(req.getParameter("id"));
		delete(id, req);
		return Result.redirect("query/list");
	}
	
	
	@DoNotAuthenticate
	public Result comment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		Integer queryId = Integer.parseInt(req.getParameter("queryId"));
		Comment c = new Comment();
		c.setContent(req.getParameter("comment"));
		c.setSubject("q" + queryId);
		String saidBy = null;
		UserInfo u = (UserInfo) req.getSession().getAttribute(Constants.USER);
		if(u != null){
			saidBy = u.getName();
		}else{
			saidBy = req.getRemoteHost();
		}
		c.setSaidBy(saidBy);
		getEntityManager(req, true).persist(c);
		return Result.redirect("query/detail?id=" + queryId);
	}
}
