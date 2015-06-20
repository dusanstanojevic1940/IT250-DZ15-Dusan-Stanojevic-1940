package com.dusanstanojevic.MetHotels.dao.impl;

import java.util.List;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.dusanstanojevic.MetHotels.dao.GenericDAO;
import com.dusanstanojevic.MetHotels.entities.AbstractEntity;

public class GenericDAOimpl<T extends AbstractEntity> implements GenericDAO<T> {
	@Inject
	private Session hibernate;
	
	@CommitAfter
	public void saveOrUpdate(AbstractEntity toUpdate) {
		hibernate.saveOrUpdate(toUpdate); 
	}
	
	@CommitAfter
	public T delete(Integer idOfObj, Class<T> entityClass) {
		@SuppressWarnings("unchecked")
		T tmpEntity = (T) hibernate.createCriteria(entityClass).add(Restrictions.eq("id", idOfObj)).uniqueResult();
		hibernate.delete(tmpEntity); 
		return tmpEntity;
	}
	
	@CommitAfter
	public T getElementById(Integer id, Class<T> entityClass) {
		@SuppressWarnings("unchecked")
		T tmpEntity = (T) hibernate.createCriteria(entityClass).add(Restrictions.eq("id", id)).list().get(0);
		return tmpEntity; 
	}
	
	@CommitAfter
	public List<T> all(Class<T> entityClass) {
		@SuppressWarnings("unchecked")
		List<T> lista = (List<T>) hibernate.createCriteria(entityClass).list();
		return lista; 
	}
}