package siddur.common.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;

public class EntityManagerWrapper {
	private static final String ENTITY_MANAGER = "entitymanager";
	
	public void useTransaction(HttpServletRequest req){
		EntityTransaction et = getEntityManager(req).getTransaction();
		et.begin();
	}
	
	public EntityManager getEntityManager(HttpServletRequest req){
		EntityManager em = (EntityManager)req.getAttribute(ENTITY_MANAGER);
		if(em == null){
			em = JPAUtil.newEntityMgr();
			req.setAttribute(ENTITY_MANAGER, em);
		}
		return em;
	}
	
	public void closeEntityManager(HttpServletRequest req){
		EntityManager em = (EntityManager)req.getAttribute(ENTITY_MANAGER);
		if(em != null && em.isOpen()){
			em.close();
			req.setAttribute(ENTITY_MANAGER, null);
		}
	}
	
	public void commit(HttpServletRequest req){
		EntityManager em = (EntityManager)req.getAttribute(ENTITY_MANAGER);
		if(em != null){
			EntityTransaction et = em.getTransaction();
			if(et.isActive()){
				et.commit();
			}
		}
	}
	
	public void rollback(HttpServletRequest req){
		EntityManager em = (EntityManager)req.getAttribute(ENTITY_MANAGER);
		if(em != null){
			EntityTransaction et = em.getTransaction();
			if(et.isActive()){
				et.rollback();
			}
		}
	}
}
