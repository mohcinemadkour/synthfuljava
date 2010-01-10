package org.synthful.jdo;

import java.util.Collection;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

final public class PersistenceManagerFactorySingleton
{
	private static final PersistenceManagerFactory pmfInstance =
		JDOHelper.getPersistenceManagerFactory("transactions-optional");

	private PersistenceManagerFactorySingleton()
	{}

	public static PersistenceManager getPersistenceManager()
	{
		return pmfInstance.getPersistenceManager();
	}

	public static PersistenceManagerFactory get()
	{
		return pmfInstance;
	}

	static public void makePersistent(Object e)
	{
        PersistenceManager pm = pmfInstance.getPersistenceManager();

		try
		{
			pm.makePersistent(e);
		}
		finally
		{
			pm.close();
		}
	}

	static public void makePersistent(Object[] e)
	{
        PersistenceManager pm = pmfInstance.getPersistenceManager();

		try
		{
			pm.makePersistentAll(e);
		}
		finally
		{
			pm.close();
		}
	}

	static public void makePersistent(Collection e)
	{
        PersistenceManager pm = pmfInstance.getPersistenceManager();

		try
		{
			pm.makePersistentAll(e);
		}
		finally
		{
			pm.close();
		}
	}
}
