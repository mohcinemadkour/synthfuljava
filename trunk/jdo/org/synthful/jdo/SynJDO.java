package org.synthful.jdo;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;


public class SynJDO<T extends SynJDO<?>>
{

	public SynJDO<?> makePersistent()
	{
		PersistenceManager pm =
			PersistenceManagerFactorySingleton.getPersistenceManager();
		return pm.makePersistent(this);
	}
	
	static public <T extends SynJDO<?>>
		T getFirst(Query query, String param)
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
		List<T> list(Query query, String param)
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
}
