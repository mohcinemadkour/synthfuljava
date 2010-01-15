package org.synthful.jdo;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.synthful.gwt.vaadin.SynApplication;

import com.syntercourse.jdo.userInfo.UserValidation;


public abstract class SynJDO<T extends SynJDO<?>>
{
	public SynJDO<?> makePersistent()
	{
		PersistenceManager pm =
			PersistenceManagerFactorySingleton.getPersistenceManager();
		
		SynJDO<?> o = pm.makePersistent(this);
		pm.close();
		return o;
	}

	public void deletePersistent()
	{
		PersistenceManager pm =
			PersistenceManagerFactorySingleton.getPersistenceManager();
		Object o = pm.getObjectById( this.getClass(), this.getId());

		try
		{
			pm.deletePersistent(o);
			logger.info("deleted "+o);
		}
		catch (Exception ex)
		{
			logger.warning(ex.getMessage());
		}
		finally
		{
			pm.close();
		}
	}
	
	abstract protected Object getId();
	
	static public PersistenceManager getPm()
	{
		return
			PersistenceManagerFactorySingleton.getPersistenceManager();
	}
	
	@SuppressWarnings("null")
	static public <T extends SynJDO<?>>
		T exists(
			Class<T> entityClass, Class paramClass,
			String paramName, Object paramValue)
	{
		PersistenceManager pm =
			PersistenceManagerFactorySingleton.getPersistenceManager();
		return exists(pm, entityClass, paramClass, paramName, paramValue);
	}
	
	@SuppressWarnings("null")
	static public <T extends SynJDO<?>>
		T exists(PersistenceManager pm,
			Class<T> entityClass, Class paramClass,
			String paramName, Object paramValue)
	{
		Query query = pm.newQuery(entityClass);
		query.setFilter(paramName+ " == param");
		query.declareParameters(paramClass.getCanonicalName()+ " param");

		logger.info("paramClass=" + paramClass.getCanonicalName());
		return getFirst(query, paramValue);
	}
	
	@SuppressWarnings("unchecked")
	static public <T extends SynJDO<?>>
		T getFirst(Query query, Object param)
	{	
		try
		{
			List<T> results =
				(List<T>) query.execute(param);
			
			if (results.iterator().hasNext())
			{
				T e = results.get(0);
				return e;
			}
			else
			{
				return null;
			}
		}
		finally
		{
			query.closeAll();
		}
	}
	
	static public <T extends SynJDO<?>>
		List<T> list(Query query, Object param)
	{	
		try
		{
			return
				(List<T>) query.execute(param);
		}
		finally
		{
			query.closeAll();
		}
	}
	
	@SuppressWarnings("null")
	static public <T extends SynJDO<?>>
		List<T> listAll(Class<T> entityClass, PersistenceManager pm)
	{	
		Query query = pm.newQuery(entityClass);

		try	

		{
			return
				(List<T>) query.execute();
		}
		finally
		{
			query.closeAll();
		}
	}
	
	final static private Logger logger = Logger.getLogger(SynJDO.class.getName());
}
