package siddur.common.web;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import siddur.common.jpa.EntityManagerWrapper;
import siddur.common.miscellaneous.Constants;
import siddur.common.miscellaneous.Paging;
import siddur.common.web.Action;

public abstract class DBAction<E> extends Action{

	public abstract Class<E> getEntityClass();
	
	protected String getClassName(){
		return getEntityClass().getSimpleName();
	}
	
	protected EntityManager getEntityManager(HttpServletRequest req){
		return getEntityManager(req, false);
	}
	
	private EntityManagerWrapper getEMWrapper(){
		return (EntityManagerWrapper)getContextItem(Constants.ENTITY_MANAGER_WRAPPER);
	}
	
	protected EntityManager getEntityManager(HttpServletRequest req, boolean useTransaction){
		EntityManager em = getEMWrapper().getEntityManager(req);
		if(useTransaction){
			EntityTransaction et = em.getTransaction();
			if(!et.isActive()){
				et.begin();
			}
		}
		return em;
	}
	
	protected void commitTransaction(HttpServletRequest req){
		getEMWrapper().commit(req);
	}
	
	protected void add(E obj, HttpServletRequest req){
		getEntityManager(req, true).persist(obj);
	}
	
	protected void update(E obj, HttpServletRequest req){
		getEntityManager(req, true).merge(obj);
	}
	
	protected void delete(Integer id, HttpServletRequest req){
		getEntityManager(req, true).remove(find(id, req));
	}
	
	protected void delete(E obj, HttpServletRequest req){
		getEntityManager(req, true).remove(obj);
	}
	
	protected E find(Integer id,  HttpServletRequest req){
		return getEntityManager(req).find(getEntityClass(), id);
	}
	
	protected Paging<E> getPageData(HttpServletRequest req){
		String pageIndex = req.getParameter("pageIndex");
		String pageSize = req.getParameter("pageSize");
		
		
		int index = 1, size = 20;
		try {
			index = Integer.parseInt(pageIndex);
		} catch (NumberFormatException e) {
		}
		try {
			size = Integer.parseInt(pageSize);
		} catch (NumberFormatException e) {
		}
		TypedQuery<E> query = getEntityManager(req)
				.createQuery("from " + getClassName(), getEntityClass());
			
		if(index >= 0 && size > 0){
			int start = (index - 1) * size;
			int end = index * size;
			query.setFirstResult(start);
			query.setMaxResults(end);
		}
		
		Paging<E> p = new Paging<E>();
		p.setData(query.getResultList());
		p.setPageIndex(index);
		p.setPageSize(size);
		
		TypedQuery<Long> countQuery = getEntityManager(req)
				.createQuery("select Count(c) from " + getClassName() + " c", Long.class);
		p.setTotal(countQuery.getSingleResult().intValue());
		
		return p;
	}
	

	protected List<E> findAll(HttpServletRequest req){
		String pageIndex = req.getParameter("pageIndex");
		String pageSize = req.getParameter("pageSize");
		
		boolean needPaging = true;
		int index = -1, size = -1;
		try {
			index = Integer.parseInt(pageIndex);
			size = Integer.parseInt(pageSize);
		} catch (NumberFormatException e) {
			needPaging = false;
		}
		
		TypedQuery<E> query = getEntityManager(req, false)
			.createQuery("from " + getClassName(), getEntityClass());
		
		if(needPaging && index >= 0 && size > 0){
			int start = index * size;
			int end = (index + 1) * size - 1;
			query.setFirstResult(start);
			query.setMaxResults(end);
		}
			
		return query.getResultList();
	}
}
