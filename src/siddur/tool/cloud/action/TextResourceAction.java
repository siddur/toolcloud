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
import siddur.common.miscellaneous.Paging;
import siddur.common.miscellaneous.TextResource;
import siddur.common.util.FileSystemUtil;
import siddur.common.util.RequestUtil;
import siddur.common.web.DBAction;
import siddur.common.web.Result;

public class TextResourceAction extends DBAction<TextResource>{
	
	
	public Result list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String pageIndex = req.getParameter("pageIndex");
		String pageSize = req.getParameter("pageSize");
		String resType = req.getParameter("type");
		
		int index = 1, size = 20;
		try {
			if(pageIndex != null)
				index = Integer.parseInt(pageIndex);
		} catch (NumberFormatException e) {
		}
		try {
			if(pageSize != null)
				size = Integer.parseInt(pageSize);
		} catch (NumberFormatException e) {
		}
		TypedQuery<TextResource> query = getEntityManager(req)
				.createQuery("from " + getClassName() + " c  where c.type = '"
						+ resType + "' order by c.publishAt desc", getEntityClass());
			
		if(index >= 0 && size > 0){
			int start = (index - 1) * size;
			int end = index * size;
			query.setFirstResult(start);
			query.setMaxResults(end);
		}
		
		Paging<TextResource> p = new Paging<TextResource>();
		p.setData(query.getResultList());
		p.setPageIndex(index);
		p.setPageSize(size);
		
		TypedQuery<Long> countQuery = getEntityManager(req)
				.createQuery("select Count(c) from " + getClassName() + " c", Long.class);
		p.setTotal(countQuery.getSingleResult().intValue());
		req.setAttribute("data", p);
		return Result.forward("/jsp/resource/"+resType+"/list.jsp");
	}
	
	public Result detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String idStr = RequestUtil.getId(req, "id");
		int id = Integer.parseInt(idStr);
		EntityManager em = getEntityManager(req);
		TypedQuery<Comment> q = em.createQuery("from Comment c where c.subject = 'r" 
				+ idStr + "' order by c.saidAt desc", 
					Comment.class);
		
		TypedQuery<Long> totalQuery = em.createQuery("select count(c) from Comment c where c.subject = 'r" 
				+ idStr + "'", Long.class);
		Paging<Comment> paging = JPAUtil.getPageData(req, q, totalQuery);
		req.setAttribute("comments", paging);
		TextResource tr = find(id, req);
		req.setAttribute("res", tr);
		return Result.forward("/jsp/resource/"+tr.getType()+"/detail.jsp");
	}
	public Result toAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		return Result.forward("/jsp/resource/toAdd.jsp");
	}
	
	public Result toEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		Integer id = Integer.parseInt(req.getParameter("id"));
		TextResource tr = find(id, req);
		req.setAttribute("res", tr);
		return Result.forward("/jsp/resource/"+tr.getType()+"/toEdit.jsp");
	}
	public Result save(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String id = req.getParameter("id");
		TextResource q;
		if(!StringUtils.isEmpty(id)){
			Integer i = Integer.parseInt(id);
			q = find(i, req);
		}else{
			q = new TextResource();
			q.setSaidBy(RequestUtil.getAccesser(req));
		}
		q.setTitle(req.getParameter("title"));
		add(q, req);
		FileSystemUtil.setResourceContent(req.getParameter("content"), q.getId());
		return Result.redirect("res/list?type=" + q.getType());
	}
	
	public Result del(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		TextResource tr = find(Integer.parseInt(req.getParameter("id")), req);
		delete(tr, req);
		return Result.redirect("res/list?type=" + tr.getType());
	}
	
	public Result saveComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String resId = req.getParameter("resId");
		String commentId = req.getParameter("id");
		Comment c = null;
		if(commentId == null){
			c = new Comment();
		}else{
			c = getEntityManager(req).find(Comment.class, Integer.parseInt(commentId));
		}
		c.setContent(req.getParameter("comment"));
		c.setSubject("r" + resId);
		c.setSaidBy(RequestUtil.getAccesser(req));
		getEntityManager(req, true).persist(c);
		return Result.redirect("res/" + resId);
	}
	public Result delComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		Integer id = Integer.parseInt(req.getParameter("id"));
		EntityManager em = getEntityManager(req, true);
		em.remove(em.find(Comment.class, id));
		return Result.redirect("res/" + req.getParameter("resId"));
	}

	@Override
	public Class<TextResource> getEntityClass() {
		return TextResource.class;
	}

}
