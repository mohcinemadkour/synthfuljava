/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 * Created on January 18, 2003, 2:01 PM
 */

package org.synthful.jdbc;
import java.sql.*;

/**
 * The Class JDataColumns.
 * 
 * @author Blessed Geek
 */
public class JDataColumns
{
    
    /**
	 * Creates a new instance of JDataColumn.
	 * 
	 * @param s
	 *            s as Statement
	 * 
	 * @throws SQLException
	 */
    public JDataColumns(Statement s)
    throws SQLException
    {
        RSMD = s.getResultSet().getMetaData();
        getDataColumns();
    }
    
    /**
	 * Instantiates a new JDataColumns.
	 * 
	 * @param rsmd
	 *            rsmd as ResultSetMetaData
	 * 
	 * @throws SQLException
	 */
    public JDataColumns(ResultSetMetaData rsmd)
    throws SQLException
    {
        RSMD = rsmd;
        getDataColumns();
    }
    
    /**
	 * Gets data and store into this.DataColumns.
	 * 
	 * 
	 * @throws SQLException
	 */
    public void getDataColumns()
    throws SQLException
    {
        ColumnCount = RSMD.getColumnCount();
        for (int i=1; i<=ColumnCount; i++)
        {
            DataColumns[i] = new JDataColumn();
            DataColumns[i].ClassName = RSMD.getColumnClassName(i);
            DataColumns[i].length = RSMD.getColumnDisplaySize(i);
            DataColumns[i].Label = RSMD.getColumnLabel(i);
            DataColumns[i].Type = RSMD.getColumnType(i);
            DataColumns[i].TypeName = RSMD.getColumnTypeName(i);
        }
        
    }
    
    /**
	 * Gets the Column.
	 * 
	 * @param i
	 *            the i as int
	 * 
	 * @return the Column as JDataColumn
	 */
    public JDataColumn getColumn(int i)
    {
        if (i>ColumnCount)
            return null;
        return DataColumns[i];
    }
    
    /** The Data columns. */
    JDataColumn[] DataColumns;
    
    /** The Column count. */
    int ColumnCount;
    
    ResultSetMetaData RSMD;
}
