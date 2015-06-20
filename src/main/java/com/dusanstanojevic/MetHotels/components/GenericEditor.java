package com.dusanstanojevic.MetHotels.components;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import com.dusanstanojevic.MetHotels.dao.GenericDAO;
import com.dusanstanojevic.MetHotels.entities.AbstractEntity;

public class GenericEditor<T extends AbstractEntity> {
	@Inject
	private GenericDAO<T> genericDao;
	@Property
	@Persist
	private T bean;
	@Property
	private T row;
	@Property
	@Persist
	private String searchString;
	@InjectComponent
	private Zone zone;
	@Component(id = "form")
	private Form form;
	//@InjectComponent
	@Component(id = "beanForm")
	private BeanEditForm beanForm;
	
	@SuppressWarnings("unchecked")
	private Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

	@Inject
	private Logger logger;
	
	@Property
	@Persist
	private String currPage;
	@Property
	@Persist
	private int currPageNum;
	
	private int perPage = 3;
	
	public List<String> getAllPages() {
		List<String> toRet = new ArrayList<String>();
		for (int i = 0; i <= allSize()/perPage; i++)
			toRet.add(i+"");
		return toRet;
	}
	
	public int allSize() {
		List<T> all = genericDao.all(entityClass);
		List<T> toRet = new ArrayList<T>();
		logger.info(""+all.size());
		OUTER: for (T t : all) {
			for (Field f : entityClass.getFields()) {
				logger.info("HELLO2");
				try {
					if (searchString==null || searchString.equals("") || (f.get(t) instanceof String && ((String)f.get(t)).toLowerCase().contains(searchString.toLowerCase()))) {
						toRet.add(t);
						
						continue OUTER;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return toRet.size(); 
	} 
	
	public List<T> getGrid() {
		List<T> all = genericDao.all(entityClass);
		List<T> toRet = new ArrayList<T>();
		logger.info(""+all.size());
		OUTER: for (T t : all) {
			for (Field f : entityClass.getFields()) {
				logger.info("HELLO2");
				try {
					if (searchString==null || searchString.equals("") || (f.get(t) instanceof String && ((String)f.get(t)).toLowerCase().contains(searchString.toLowerCase()))) {
						toRet.add(t);
						
						continue OUTER;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		int max = currPageNum*perPage+perPage;
		toRet = toRet.subList(currPageNum*perPage, max>toRet.size()?toRet.size():max);
		return toRet; 
	}
	
	@CommitAfter
	Object onActionFromDelete(int id) {
		genericDao.delete(id, entityClass); 
		return zone;
	}
	
	@CommitAfter
	Object onActionFromEdit(int id) {
		bean = (T) genericDao.getElementById(id, entityClass);
		return zone;
	}
	
	@CommitAfter
	Object onActionFromPage(int id) {
		currPageNum = id;
		return zone;
	}
	
	
	
	@CommitAfter
	public Object onSuccessFromForm() {
		return zone; 
	}

	
	
	@CommitAfter
	public Object onSuccessFromBeanForm() {
		genericDao.saveOrUpdate(bean); 
		try {
			bean = (T) entityClass.newInstance(); 
		} catch (Exception ex) {
		}
		return zone; 
	}
}
