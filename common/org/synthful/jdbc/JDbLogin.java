/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 */
package org.synthful.jdbc;

import java.sql.*;
import java.util.Properties;
import java.lang.Integer;


// TODO: Auto-generated Javadoc
/**
 * JDbLogin Class.
 */
public class JDbLogin
{
	
	/**
	 * Read connection properties.
	 * 
	 * @param p
	 *            Properties Object contain server login properties
	 *            
	 * Required properties are:
	 * <pre>
	 * url="jdbc:<driver>://url";
	 * user="<user login name of server";
	 * password="<password>";
	 * driver="<driver class>";
	 * </pre>
	 *
	 * <pre>
	 * Examples of driver property
	 * driver="com.mysql.jdbc.Driver";
	 * driver="com.sybase.jdbc.SybDriver";
	 * driver="com.sas.net.sharenet.ShareNetDriver";
	 * </pre>
	 */
	public JDbLogin(Properties p)
	{
		connectInfo = p;
	}

    /**
	 * Connection Already made.
	 * 
	 * @param jconn
	 *            the jconn as Connection
	 */
	public JDbLogin(Connection jconn)
	{
		jCon = jconn;
        try{
            jStmt = jCon.createStatement();
		}
		catch (SQLException sqle)
		{
			connectStatus += sqle.toString();
		}
		catch (Exception e)
		{
			connectStatus += e.toString();
		}

		connectStatus
			= jCon.toString() + ";\n"
			+ jStmt.toString() + ";\n"
			;
    }
    
	/**
	 * Perform login to server and obtain JDBC Connection Object.
	 * 
	 * @return this.
	 */
	public JDbLogin loginConnect()
	{
    if(connectInfo==null)
      return null;
		connectStatus = "";
		String url = connectInfo.getProperty("url");
		String timeoutStr = connectInfo.getProperty("timeout");
		String osname = (System.getProperty("os.name"));
		String dbdrv = connectInfo.getProperty("driver");

    if (timeoutStr!=null)
			loginTimeout = Integer.parseInt(timeoutStr);
    if(loginTimeout<=0)
      loginTimeout = 10;

    DriverManager.setLoginTimeout(loginTimeout);
//    DriverManager.setLoginTimeout(10);

		try
		{
			if (jCon != null)
			{
				jCon.close();
			}

			if (!DriverRegistered)
			{
				DriverManager.registerDriver
					( (Driver) Class.forName(dbdrv).newInstance()
					 );
				DriverRegistered = true;
			}

			jCon = DriverManager.getConnection(url, connectInfo);
//			jCon = DriverManager.getConnection(url);
			jStmt = jCon.createStatement();
		}
		catch (SQLException sqle)
		{
			connectStatus += sqle.toString();
			return this;
		}
		catch (Exception e)
		{
			connectStatus += e.toString();
			return this;
		}

		connectStatus
			= jCon.toString() + ";\n"
			+ jStmt.toString() + ";\n"
			;
		return this;
	}

	/**
	 * Close Connection. Connection would no longer be functional. New
	 * Connection must be obtained be performing loginConnect again.
	 * 
	 * @return true if successful and false otherwise.
	 */
	public boolean logout()
	{
		try
		{
			jCon.close();
		}
		catch (SQLException sqle)
		{
			connectStatus += sqle.toString();
			return false;
		}
		catch (NullPointerException e)
		{
			connectStatus += e.toString();
			return false;
		}
		connectStatus += "Connection closed\n";
		return true;
	}

	/**
	 * Release all server resources held by Connection without releasing
	 * Connection. Connection can be reused by getting new statement from
	 * Connection.
	 */
	public void release()
	{
		jStmt = getStatement();
		try
		{
			jStmt.close();
			jStmt = null;
			connectStatus += "Connection closed\n";
		}
		catch (SQLException e)
		{
			connectStatus += e.toString();
		}
	}

	/**
	 * Return String which accumulates SQL errors and warnings. The String is
	 * cleared during loginConnect()
	 * 
	 * @return the connection status
	 */
	public String getConnectionStatus()
	{
		return connectStatus;
	}

	/**
	 * Gets the connection.
	 * 
	 * @return JDBC Connection. Connection returned would be invalid if a logout
	 *         has occurred.
	 */
	public Connection getConnection()
	{
		return jCon;
	}

	/**
	 * Obtain JDBC Statement executor.<br>
	 * JDBC Statement is used for performing SQL execs.<br>
	 * If Statement had been earlier released, a new Statement would be created
	 * from a presumed valid Connection. If Connection is invalid, Statement
	 * would also be invalid, in which case, loginConnect() must be performed
	 * again.
	 * 
	 * @return JDBC Statement.
	 */
	public Statement getStatement()
	{
		try
		{
			if (jStmt == null)
				jStmt = jCon.createStatement();
		}
		catch (SQLException e)
		{
			connectStatus += e.toString();
		}
		return jStmt;
	}

	/** The connect info. */
	private Properties connectInfo;
	public Connection jCon = null;
	public Statement jStmt = null;
	public ResultSet ResultSet;
	public ResultSetMetaData ResultSetMetaData;
    
	/** The login timeout. */
	private int loginTimeout = 10;
	private String connectStatus = "";
	
	/** Flag to test to avoid repeating registeration of Driver. */
	private static boolean DriverRegistered = false;
}
