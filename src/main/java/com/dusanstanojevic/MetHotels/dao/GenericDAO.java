package com.dusanstanojevic.MetHotels.dao;

import java.util.List;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import com.dusanstanojevic.MetHotels.entities.AbstractEntity;

public interface GenericDAO<T extends AbstractEntity> { 
	@CommitAfter
	public void saveOrUpdate(T toUpdate);
	@CommitAfter
	public T delete(Integer idOfObj, Class<T> entityClass);
	@CommitAfter
	public List<T> all(Class<T> entityClass);
	@CommitAfter
	public T getElementById(Integer id, Class<T> entityClass);
}