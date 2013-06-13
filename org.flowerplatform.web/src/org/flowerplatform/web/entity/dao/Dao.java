package org.flowerplatform.web.entity.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.flowerplatform.web.entity.EntityFactory;
import org.flowerplatform.web.entity.NamedEntity;
import org.flowerplatform.web.entity.dto.Dto;
import org.flowerplatform.web.entity.dto.NamedDto;

/**
 * Data access object that encapsulates DB access code.
 * 
 * @author Cristi
 * @author Cristina
 * @flowerModelElementId _CaEpUVyIEeGwx-0cTKUc5w
 */
public class Dao {

	/**
	 * @flowerModelElementId _CaEpVFyIEeGwx-0cTKUc5w
	 */
	public SessionFactory factory;

	/**
	 * @flowerModelElementId _CaEpV1yIEeGwx-0cTKUc5w
	 */
	public SessionFactory getFactory() {
		return factory;
	}

	/**
	 * @flowerModelElementId _CaEpWVyIEeGwx-0cTKUc5w
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> entityClass) {
		// Create a session and a transaction
		Session session = getFactory().getCurrentSession();
		beginTransaction(session);
		Query q = session.createQuery(String.format("SELECT e from %s e", entityClass.getSimpleName()));			
		return q.list();
	}
	
	/**
	 * @flowerModelElementId _CaEpXlyIEeGwx-0cTKUc5w
	 */
	@SuppressWarnings("unchecked")
	public <T> T find(Class<T> entityClass, Object primaryKey) {
		Session session = getFactory().getCurrentSession();
		beginTransaction(session);
		return (T) session.get(entityClass.getSimpleName(), (Serializable) primaryKey);
	}
	
	/**
	 * @flowerModelElementId _QOTiAF34EeGwLIVyv_iqEg
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByField(Class<T> entityClass, Object fieldName, Object field) {
		Session session = getFactory().getCurrentSession();
		beginTransaction(session);
		Query q = session.createQuery(String.format("SELECT e from %s e where e.%s='%s'", entityClass.getSimpleName(), fieldName, field));	
		return q.list();
	}
	
	/**
	 * @flowerModelElementId _QOUwI134EeGwLIVyv_iqEg
	 */
	@SuppressWarnings("unchecked")
	public <T> T merge(T object) {
		Session session = getFactory().getCurrentSession();
		
		try {
			beginTransaction(session);
			object = (T) session.merge(object);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
		}
		return object;
	}
	
	/**
	 * @flowerModelElementId _4LBAIF5PEeGQXpwILsA5AA
	 */
	@SuppressWarnings("unchecked")
	public <T> T mergeDto(Class<T> entityClass, Dto dto) {
		T instance;
		if (dto.getId() == 0) {
			try {
				instance = (T) EntityFactory.eINSTANCE.create(entityClass.getSimpleName());
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		} else { // find it in database
			instance = find(entityClass, dto.getId());
			if (instance == null)
				throw new RuntimeException(String.format("%s with id=%s was not found in the DB.", entityClass.getSimpleName(), dto.getId()));
		}		
		return instance;
	}
		
	/**
	 * @flowerModelElementId _4LCOQF5PEeGQXpwILsA5AA
	 */
	@SuppressWarnings("unchecked")
	public <T> T mergeDto(Class<T> entityClass, NamedDto dto) {
		T instance;
		if (dto.getId() == 0) {
			try {
				instance = (T) EntityFactory.eINSTANCE.create(entityClass.getSimpleName());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else { // find it in database
			instance = find(entityClass, dto.getId());
			if (instance == null)
				throw new RuntimeException(String.format("%s with id=%s was not found in the DB.", entityClass.getSimpleName(), dto.getId()));
		}
		((NamedEntity)instance).setName(dto.getName());
		return instance;
	}
	
	/**
	 * @flowerModelElementId _QOVXMV34EeGwLIVyv_iqEg
	 */
	@SuppressWarnings("unchecked")
	public <T> void delete(T object){
	    Session session = getFactory().getCurrentSession();
	    try {
	        beginTransaction(session);	 
	        object = (T) session.merge(object);
	        session.delete(object);
	        session.getTransaction().commit();
	    } catch (Exception e) {
	    	session.getTransaction().rollback();
	    }
	}
	
	/**
	 * @author Mariana
	 */
	private void beginTransaction(Session session) {
		if (!session.getTransaction().isActive()) {
			session.beginTransaction();
		}
	}

}
