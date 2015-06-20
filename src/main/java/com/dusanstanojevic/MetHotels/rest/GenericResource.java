package com.dusanstanojevic.MetHotels.rest;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.dusanstanojevic.MetHotels.dao.GenericDAO;
import com.dusanstanojevic.MetHotels.entities.AbstractEntity;

public class GenericResource<T extends AbstractEntity> {
	@Inject
	private GenericDAO<T> genericDAO;
	
	@SuppressWarnings("unchecked")
	private Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];;
	
	@GET
	@Produces("application/json")
	public List<T> read() {
		return genericDAO.all(entityClass);
	}
	
	@POST
	@Consumes("application/json")
	public void create(T t) {
		genericDAO.saveOrUpdate(t);
	}
	
	@PUT
	@Consumes("application/json")
	public void update(T t) {
		genericDAO.saveOrUpdate(t);
	}
	
	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") int id) {
		genericDAO.delete(id, entityClass);
	}
}
