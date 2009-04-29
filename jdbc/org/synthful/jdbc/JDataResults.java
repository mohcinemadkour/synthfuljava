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
 * JDataResults Class.
 */
public class JDataResults
{
	
	/**
	 * The Constructor.
	 * 
	 * @param jconn
	 *            the jconn as Connection
	 */
	public JDataResults(Connection jconn)
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
	 * Execute query
	 * 
	 * @param sql
	 *            to be executed.
	 * 
	 * @return this.
	 */
	public JDataResults exec(String sql)
	{
		try
		{
			results = jStmt.execute(sql);
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
	public JDataResults fetch(String query)
	{
		fetchResult(query, true);
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
	public JDataResults fetchAppend(String query)
	{
		fetchResult(query, true);
		return this;
	}

	/**
	 * Fetch result.
	 * 
	 * @param query
	 *            SQL query to be executed.
	 * @param fetchrows
	 *            true if rows are to be fetched from result set. By setting
	 *            fetchrows to false allows a subclass to perform its own fetch
	 *            operations.
	 * 
	 * @return JDBC ResultSet, the handle used to fetch the rows. ResultSet
	 *         would no longer be valid if Connection is closed.
	 */
	public ResultSet fetchResult(
		String query, boolean fetchrows)
	{
		try
		{
			exec(query);

			do
			{               
				if (results)
				{
                    rs = jStmt.getResultSet();
                    rsmd = rs.getMetaData();
                    
                    if (fetchrows)
                    {
                        if (ColumnNames==null  || ColumnNames.length == 0)
                            fetchResultsInit();
                        
                        if(HeaderRow)
                            fetchHeaderRow();
                       
                        fetchDataRows();
                    }
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

	/*
     Reminder when over-riding any of the following routines.
	 RowNumColumn must be equitably treated by fetchDataRow,
	 fetchDataRowInit(rsmd) and fetchDataRowInit()
	 */
    
 	/**
	 * Fetch header row.
	 */
	protected void fetchHeaderRow()
	{
        
    }   
    
    
 	/**
	 * Fetch data rows.
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	 protected void fetchDataRows()
		throws SQLException
	{
        while (rs.next())
        {
            rowNum++;
            fetchDataRow();
        }

        if (Debug)
            System.out.println("DataBuffVec rows:" + rowNum);
        
    }   
    
    
	/**
	 * Fetch a row of data into Object array and absorb the new Object array.
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	protected void fetchDataRow()
		throws SQLException
	{
        int offset 
            = RowNumColumn? 1: 0;
            
		Object[] fields = new Object[numColumns+offset];
        
        if (RowNumColumn)
            fields[0] = new Integer(rowNum);
        
		for (int i = 1; i <= numColumns; i++)
			fields[i+offset] = rs.getObject(i);

//		this.add(fields);
	}

	/**
	 * Set up column lookups hashes and arrays. Used internally by this class.
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	protected void fetchResultsInit()
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
	 * Gets the num columns.
	 * 
	 * @return number of columns executed query has returned.
	 */
	public int getNumColumns()
	{
		return numColumns;
	}

	/**
	 * Gets the num rows.
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
		return ColumnNames[col];
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
	 * @return the j data results
	 */
	public JDataResults setHeaderRow(boolean yes)
	{
		HeaderRow = yes;
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
	 * @return the j data results
	 */
	public JDataResults setRowNumColumn(boolean yes)
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
	public void setDebug(boolean yes)
	{
		Debug = yes;
	}

	protected Connection JConn;
	
	protected Statement jStmt;
	
	protected int rowNum = 0;
	
	protected int numColumns = 0;
	
	/** The Header row. */
	protected boolean HeaderRow;
	
	/** The Row number column. */
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
}
