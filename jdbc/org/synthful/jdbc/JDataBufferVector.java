/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 */
package org.synthful.jdbc;

import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;

import org.synthful.jdbc.JDbLogin;
import org.synthful.jdbc.JRowBufferVector;

// TODO: Auto-generated Javadoc
/**
 * JDataBufferVector Class.
 */
public class JDataBufferVector
extends Vector
{
	
	/**
	 * The Constructor.
	 * 
	 * @param jconn
	 *            the jconn as Connection
	 */
	public JDataBufferVector(Connection jconn)
	{
		ResultStatus = new Vector();
		JConn = jconn;
		
		try{
		    jStmt = JConn.createStatement();
		}
		catch (SQLException sqle)
		{
			ResultStatus.add(sqle);
		}
		catch (Exception e)
		{
			ResultStatus.add(e);
		}
	}
	
	/**
	 * Instantiates a new JDataBufferVector.
	 * 
	 * @param dblogin
	 *            the dblogin as JDbLogin
	 */
	public JDataBufferVector(JDbLogin dblogin)
	{
		dbLogin = dblogin;
		jStmt = dbLogin.getStatement();
		ResultStatus = new Vector();
	}

	/**
	 * Perform re-login.
	 * 
	 * @return true if Relogin is successful.
	 */
	public boolean ReConnect()
	{
		if (dbLogin == null)
			return false;
		dbLogin.loginConnect();
		jStmt = dbLogin.getStatement();
		ResultStatus = new Vector();
		if (jStmt == null)
			return false;
		return true;
	}

	/**
	 * Release connection.
	 * Logout, causing JDBC Connection to be invalid.
	 * ReConnect() must be performed to re-login for
	 * future SQL Statements.
	 */
	public void ReleaseConnection()
	{
		dbLogin.logout();
	}

	/**
	 * Execute SQL by resetting row and column counts.
	 * 
	 * @param sql
	 *            the sql as String
	 * 
	 * @return this.
	 */
	public JDataBufferVector exec(String sql)
	{
		return exec(sql, true);
	}

	/**
	 * Exec.
	 * 
	 * @param sql
	 *            to be executed.
	 * @param reset
	 *            true if row and column count is to be reset.
	 * 
	 * @return this.
	 */
	public JDataBufferVector exec(
		String sql, boolean reset)
	{
		try
		{
			results = jStmt.execute(sql);
			if (reset)
			{
				rowsAffected = 0;
				rowNum = 0;
				numColumns = 0;
			}
		}
		catch (SQLException sqle)
		{
			ResultStatus.add(sqle);
		}
		catch (Exception e)
		{
			ResultStatus.add(e);
		}
		return this;
	}

	/**
	 * Perform fetchResult with reset set to true to clear away results of
	 * previous queries.
	 * 
	 * @param query
	 *            SQL query to be executed.
	 * 
	 * @return this.
	 */
	public JDataBufferVector fetch(String query)
	{
		fetchResult(query, true, true);
		return this;
	}

	/**
	 * Perform fetchResult with reset set to false to allow accumulation of
	 * results with previous queries.
	 * 
	 * @param query
	 *            SQL query to be executed.
	 * 
	 * @return this.
	 */
	public JDataBufferVector fetchAppend(String query)
	{
		fetchResult(query, false, true);
		return this;
	}

	/**
	 * Fetch result.
	 * 
	 * @param query
	 *            SQL query to be executed.
	 * @param reset
	 *            true if row and column counts are to be reset and buffer
	 *            cleared. By setting false, allows rows to be accumulated
	 *            across multiple queries. This can be used when an SQL union is
	 *            not preferred, or when there would be different datatypes and
	 *            column counts across queries.
	 * @param fetchrows
	 *            true if rows are to be fetched from result set. By setting
	 *            fetchrows to false allows a subclass to perform its own fetch
	 *            operations.
	 * 
	 * @return JDBC ResultSet, the handle used to fetch the rows. ResultSet
	 *         would no longer be valid if Connection is closed.
	 */
	public ResultSet fetchResult(
		String query, boolean reset, boolean fetchrows)
	{
		if (reset)
			clear();
		try
		{
			exec(query, reset);

			do
			{
				if (results)
				{
					rs = jStmt.getResultSet();
					rsmd = rs.getMetaData();
                    if (dbLogin!=null)
                    {
                        dbLogin.ResultSet = rs;
                        dbLogin.ResultSetMetaData = rsmd;                        
                    }

					if (reset || ColumnNames.length == 0)
					{
						fetchDataRowInit(rsmd);
                        if (HeaderRow)
                            fetchDataRowInit();
					}

					if (fetchrows)
						while (rs.next())
						{
							rowNum++;
							fetchDataRow();
						}

					if (Debug)
						System.out.println("DataBuffVec rows:" + rowNum);
				}
				else
				{
					rowsAffected = jStmt.getUpdateCount();

					if (Debug)
						System.out.println("DataBuffVec: No Results:" + rowsAffected);
				}
				results = jStmt.getMoreResults();
			}
			while (results || rowsAffected != -1);
		}
		catch (SQLException sqle)
		{
			ResultStatus.add(sqle);
		}
		catch (Exception e)
		{
			ResultStatus.add(e);
		}

		return rs;
	}

	/**
	 * Gets the num columns.
	 * 
	 * @return number of columns executed query has returned.
	 */
	public int getNumColumns()
	{
		return numColumns;
	}

	/**
	 * Gets the number of rows.
	 * 
	 * @return number of rows executed query has returned.
	 */
	public int getNumRows()
	{
		return rowNum;
	}

	/**
	 * Get index of column from column name.
	 * 
	 * @param var
	 *            name of column of executed query.
	 * 
	 * @return index of column of executed query.
	 */
	public int getColumnIndex(String var)
	{
		Integer I = (Integer) ColumnIndex.get(var);
		return (I == null) ? -1 : I.intValue();
	}

	/**
	 * Gets the column types.
	 * 
	 * @return int array of datatypes of all columns of executed query in order
	 *         of selected columns.
	 */
	public int[] getColumnTypes()
	{
		return ColumnTypes;
	}

	/**
	 * Gets the column type.
	 * 
	 * @param col
	 *            index of column of executed query.
	 * 
	 * @return datatype of specified column.
	 */
	public int getColumnType(int col)
	{
		if (col < 0 || col >= ColumnNames.length)
			return -1;
		return ColumnTypes[col];
	}

	/**
	 * Gets the column type.
	 * 
	 * @param var
	 *            name of column of executed query.
	 * 
	 * @return datatype of specified column.
	 */
	public int getColumnType(String var)
	{
		int col = getColumnIndex(var);
		if (col < 0)
			return -1;
		return ColumnTypes[col];
	}

	/**
	 * Gets the column labels.
	 * 
	 * @return String array of labels all columns of executed query in order of
	 *         selected columns.
	 */
	public String[] getColumnLabels()
	{
		return ColumnLabels;
	}

	/**
	 * Gets the column label.
	 * 
	 * @param col
	 *            index of column of executed query.
	 * 
	 * @return label of column.
	 */
	public String getColumnLabel(int col)
	{
		if (col < 0 || col >= ColumnLabels.length)
			return null;
		return ColumnLabels[col];
	}

	/**
	 * Gets the column label.
	 * 
	 * @param var
	 *            name of column of executed query.
	 * 
	 * @return label of column.
	 */
	public String getColumnLabel(String var)
	{
		int col = getColumnIndex(var);
		if (col < 0)
			return null;
		return ColumnLabels[col];
	}

	/**
	 * Gets the column names.
	 * 
	 * @return String array of names all columns of executed query in order of
	 *         selected columns.
	 */
	public String[] getColumnNames()
	{
		return ColumnNames;
	}

	/**
	 * Gets the column name.
	 * 
	 * @param col
	 *            index of column of executed query.
	 * 
	 * @return name of specified column of executed query.
	 */
	public String getColumnName(int col)
	{
		if (col < 0 || col >= ColumnNames.length)
			return null;
		return ColumnNames[col];
	}

	/**
	 * Returns label or name of column of executed query.
	 * 
	 * @param col
	 *            column for which title is to be returned.
	 * 
	 * @return label of specified column if defined, otherwise name of column.
	 */
	public String getColumnTitle(int col)
	{
		if (col < 0 || col >= ColumnNames.length)
			return null;
		if (ColumnLabels[col] != null && ColumnLabels[col].length() > 0)
			return ColumnLabels[col];
		return ColumnNames[col].toUpperCase();
	}

	/**
	 * Gets the column titles.
	 * 
	 * @return String array of titles all columns of executed query in order.
	 *         Column title is column label if defined otherwise, column name.
	 */
	public String[] getColumnTitles()
	{
		String[] coltits = new String[ColumnNames.length];
		for (int i = 0; i < ColumnNames.length; i++)
			coltits[i] = getColumnTitle(i);
		return coltits;
	}

	/**
	 * A header row of column names is inserted as the first row of this results
	 * vector if desired.
	 * 
	 * @param yes
	 *            the yes as boolean
	 * 
	 * @return the j data buffer vector
	 */
	public JDataBufferVector setHeaderRow(boolean yes)
	{
		HeaderRow = yes;
        HeaderRowSource = ColumnTitlesHeaderRow;
		return this;
	}

	/**
	 * A header row of column names is inserted as the first row of this results
	 * vector if desired.
	 * 
	 * @param headerRowSource
	 *            the headerRowSource as char
	 * 
	 * @return the j data buffer vector
	 */
	public JDataBufferVector setHeaderRowSource(char headerRowSource)
	{
		HeaderRow = headerRowSource==0?false:true;
        if (headerRowSource>ColumnTitlesHeaderRow)
            headerRowSource = ColumnTitlesHeaderRow;
        HeaderRowSource = headerRowSource;
		return this;
	}

    /**
	 * A row number column denoting the row number is inserted as the first
	 * column of each row in this results vector. Row number column is of type
	 * int.
	 * 
	 * @param yes
	 *            the yes as boolean
	 * 
	 * @return the j data buffer vector
	 */
	public JDataBufferVector setRowNumColumn(boolean yes)
	{
		RowNumColumn = yes;
		return this;
	}

	/**
	 * Used internally by this as a standard method to treat SQLExceptions.
	 * 
	 * @param sqle
	 *            the sqle as SQLException
	 */
	protected void catchSQLException(SQLException sqle)
	{
		ResultStatus.add(sqle);
	}

	/**
	 * Used internally by this as a standard method to treat Exceptions.
	 * 
	 * @param e
	 *            the e as Exception
	 */
	protected void catchException(Exception e)
	{
		ResultStatus.add(e);
	}

	/*
	 RowNumColumn must be equitably treated by fetchDataRow,
	 fetchDataRowInit(rsmd) and fetchDataRowInit()
	 */
	/**
	 * Fetch a row of data by instantiating a new SynRowBufferVector to be
	 * inserted as a new record of this SynBufferVector Fetch each column from
	 * resultset rs to be inserted into new SynRowBufferVector.
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	protected void fetchDataRow()
		throws SQLException
	{
		this.add(fetchDataRowVector());
	}

	/**
	 * Fetch a row of data. Used internally by this class. into a new
	 * SynRowBufferVector
	 * 
	 * Used internally by this class.
	 * 
	 * @return the j row buffer vector
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */

	public JRowBufferVector fetchDataRowVector()
		throws SQLException
	{
		JRowBufferVector rowvec
			= new JRowBufferVector
			(numColumns + 1 + (RowNumColumn ? 1 : 0), ColumnIndex, ColumnTypes);

		if (RowNumColumn)
			rowvec.add(new Integer(rowNum));

		for (int i = 1; i <= numColumns; i++)
			rowvec.add(rs.getObject(i));

		return rowvec;
	}

	/**
	 * Set up column lookups hashes and arrays. Used internally by this class.
	 * 
	 * @param rsmd
	 *            the rsmd as ResultSetMetaData
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	protected void fetchDataRowInit(ResultSetMetaData rsmd)
		throws SQLException
	{
		numColumns = rsmd.getColumnCount();
		int rownumcol = RowNumColumn ? 1 : 0;
		int cols = numColumns + rownumcol;
		ColumnTypes = new int[cols];
		ColumnNames = new String[cols];
		ColumnLabels = new String[cols];
		ColumnIndex = new Hashtable(cols);

		if (RowNumColumn)
		{
			ColumnTypes[0] = 0;
			ColumnNames[0] = "ROW";
			ColumnIndex.put(ColumnNames[0], new Integer(0));
		}

		for (int i = 0; i < numColumns; i++)
		{
			ColumnTypes[i + rownumcol] = rsmd.getColumnType(i + 1);
			ColumnNames[i + rownumcol] = rsmd.getColumnName(i + 1);
			ColumnLabels[i + rownumcol] = rsmd.getColumnLabel(i + 1);
			ColumnIndex.put(ColumnNames[i + rownumcol], new Integer(i + rownumcol));
		}
	}

	/**
	 * If HeaderRow requested, instantiate a new SynRowBufferVector to be
	 * inserted into this SynBufferVector and fetch column names into the new
	 * SynRowBufferVector. Used internally by this class.
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	protected void fetchDataRowInit()
		throws SQLException
	{
		JRowBufferVector rowvec
			= new JRowBufferVector
			(numColumns + 1 + (RowNumColumn ? 1 : 0), ColumnIndex, ColumnTypes);
		this.add(rowvec);

		if (RowNumColumn)
			rowvec.add("ROW");

		for (int i = + (RowNumColumn ? 1 : 0);
				 i < numColumns + (RowNumColumn ? 1 : 0); i++)
			rowvec.add(getColumnTitle(i));
	}

	/**
	 * Gets the result status.
	 * 
	 * @return this as Vector.
	 */
	public Vector getResultStatus()
	{
		return ResultStatus;
	}

	/**
	 * Debug mode allows fetch counts to be tracked row by row as they are
	 * fetched.
	 * 
	 * @param yes
	 *            the yes as boolean
	 * 
	 * @return this as SynVector.
	 */
	public Vector setDebug(boolean yes)
	{
		Debug = yes;
		return this;
	}

	protected Connection JConn;
	
	protected JDbLogin dbLogin;
	
	protected Statement jStmt;
	
	/** The row num. */
	protected int rowNum = 0;
	
	/** The num columns. */
	protected int numColumns = 0;
	
	/** The RowNum column. */
	protected boolean RowNumColumn;
	
	/** The Column types. */
	protected int[] ColumnTypes;
	
	/** The Column names. */
	protected String[] ColumnNames;
	
	/** The Column labels. */
	protected String[] ColumnLabels;
	
	/** The Column index. */
	protected Hashtable ColumnIndex;
	
	protected Vector ResultStatus;
	
	protected ResultSet rs;
	
	protected ResultSetMetaData rsmd;
	
	protected boolean results;
	
	protected int rowsAffected;
	
	protected boolean Debug = false;
    
	/** True if show Header row. */
	protected boolean HeaderRow = false;
	
	/** The Header row source. */
	protected char HeaderRowSource = 0;
    
    /** The No-Header row. */
    public static char NoHeaderRow = 0;
    
    /** The Column names header row. */
    public static char ColumnNamesHeaderRow = 1;
    
    /** The Column labels header row. */
    public static char ColumnLabelsHeaderRow = 2;
    
    /** The Column titles header row. */
    public static char ColumnTitlesHeaderRow = 3;
}
