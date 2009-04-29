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

//import com.mysql.jdbc.Driver;
import org.synthful.jdbc.JDbLogin;
import org.synthful.util.VectorNode;

// TODO: Auto-generated Javadoc
/**
 * JDataArrayVector Class.
 */
public class JDataArrayVector extends JDataBufferVector
{
	
	/**
	 * Construct a vector of Object arrays to contain rows fetched from
	 * ResultSet of a JDBC statement execution.
	 * 
	 * @param dblogin
	 *            the dblogin as JDbLogin
	 */
	public JDataArrayVector(JDbLogin dblogin)
	{
		super(dblogin);
	}
	
	/**
	 * Instantiates a new JDataArrayVector.
	 * 
	 * @param jconn
	 *            the jconn as Connection
	 */
	public JDataArrayVector(Connection jconn)
	{
		super(jconn);
	}

	/* (non-Javadoc)
	 * @see org.synthful.jdbc.JDataBufferVector#fetchDataRow()
	 */
	protected void fetchDataRow()
		throws SQLException
	{
		this.add(fetchDataRowCells());
	}

	/**
	 * Fetch data row cells.
	 * 
	 * @return Fetch data row cells} as Object[]
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	public Object[] fetchDataRowCells()
		throws SQLException
	{
		Object[] cells = new Object[numColumns + (RowNumColumn ? 1 : 0)];
		if (RowNumColumn)
			cells[0] = (new Integer(rowNum));

		for (int i = 1; i <= numColumns; i++)
			cells[i - 1 + (RowNumColumn ? 1 : 0)] =
				getFormattedCell(i);

		return cells;
	}

    
	/* (non-Javadoc)
	 * @see org.synthful.jdbc.JDataBufferVector#fetchDataRowInit()
	 */
	protected void fetchDataRowInit()
		throws SQLException
	{
		Object[] cells = new Object[numColumns + (RowNumColumn ? 1 : 0)];

		if (RowNumColumn)
            cells[0] = "ROW";

		for (int i = 1; i <= numColumns; i++)
			cells[i - 1 + (RowNumColumn ? 1 : 0)] =
                getColumnTitle(i-1);
        
        this.add(cells);
	}

	/**
	 * Gets the FormattedCell.
	 * 
	 * @param col
	 *            the col as int
	 * 
	 * @return the FormattedCell as Object
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	protected Object getFormattedCell(int col)
		throws SQLException
	{
		return rs.getObject(col);
	}

	/**
	 * Converts to TableArray.
	 * 
	 * @return TableArray as type Object[][]
	 */
	public Object[][] toTableArray()
	{
		Object[][] t = (Object[][]) toArray(new Object[0][0]);
		return t;
	}

}
