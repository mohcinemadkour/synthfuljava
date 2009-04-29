/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 */
package org.synthful.jdbc;

import java.sql.Types;
import java.util.Enumeration;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * JRowBufferVector Class.
 */
public class JRowBufferVector 
extends Vector
{
	
	/**
	 * Instantiates a new JRowBufferVector.
	 * 
	 * @param numcol
	 * @param colidx
	 * @param coltyp
	 */
	public JRowBufferVector(int numcol, Hashtable colidx, int[] coltyp)
	{
		super ();
		numColumns = numcol;
		ColumnIndex = colidx;
		ColumnType = coltyp;
	}

	/**
	 * Gets the Type.
	 * 
	 * @param col
	 * 
	 * @return the Type as int
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 *             the array index out of bounds exception
	 */
	public int getType (int col) throws ArrayIndexOutOfBoundsException
	{
		return ColumnType[col];
	}

	/**
	 * Gets the Name.
	 * 
	 * @param col
	 * 
	 * @return the Name as String
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 *             the array index out of bounds exception
	 */
	public String getName (int col) throws ArrayIndexOutOfBoundsException
	{
		return ColumnName[col];
	}

	/**
	 * Gets the Column.
	 * 
	 * @param name
	 *            the Column name as String
	 * 
	 * @return the Column as Object
	 */
	public Object getColumn (String name)
	{
		Integer col = (Integer) ColumnIndex.get(name);
		if (col!=null) return this.get(col.intValue());
		return null;
	}


	private int       numColumns =0;
	private int[]     ColumnType;
	private String[]  ColumnName;
	private Hashtable ColumnIndex;
}

