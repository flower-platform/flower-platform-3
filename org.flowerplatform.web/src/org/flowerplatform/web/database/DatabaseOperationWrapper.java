/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.web.database;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.entity.EntityFactory;
import org.flowerplatform.web.entity.NamedEntity;
import org.flowerplatform.web.entity.dto.Dto;
import org.flowerplatform.web.entity.dto.NamedDto;

/**
 * Data access object that encapsulates DB access code.
 * 
 * @author Cristi
 * @author Cristina
 * @author Mariana
 * 
 */
public class DatabaseOperationWrapper {

	private Session session;
	
	private Object operationResult;

	public Session getSession() {
		return session;
	}

	public DatabaseOperationWrapper(DatabaseOperation operation) {
		operation.wrapper = this;
		session = WebPlugin.getInstance().getDatabaseManager().getFactory().openSession();
		try {
			session.beginTransaction();
			operation.run();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new RuntimeException(e);
		} finally {
			session.close();
		}
	}
	
	public Object getOperationResult() {
		return operationResult;
	}

	public void setOperationResult(Object operationResult) {
		this.operationResult = operationResult;
	}

	public Query createQuery(String query) {
		return session.createQuery(query);
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> entityClass) {
		Query q = session.createQuery(String.format("SELECT e from %s e", entityClass.getSimpleName()));			
		return q.list();
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <T> T find(Class<T> entityClass, Object primaryKey) {
		return (T) session.get(entityClass.getSimpleName(), (Serializable) primaryKey);
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByField(Class<T> entityClass, Object fieldName, Object field) {
		Query q = session.createQuery(String.format("SELECT e from %s e where e.%s='%s'", entityClass.getSimpleName(), fieldName, field));	
		return q.list();
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <T> T merge(T object) {
		return (T) session.merge(object);
	}
	
	/**
	 * 
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
	 * 
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
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <T> void delete(T object){
	    object = (T) session.merge(object);
	    session.delete(object);
	}

}