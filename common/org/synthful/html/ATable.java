/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org
 *
 * ATable.java
 *
 * Created on April 26, 2003, 5:50 PM
 */

package org.synthful.html;

import org.apache.ecs.html.Table;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.TH; 
import org.apache.ecs.html.TD;

/**
 * The Class ATable.
 * 
 * @author Blessed Geek
 */
public class ATable {
    
    /**
	 * Creates a new instance of ATable.
	 */
    public ATable() 
    {	
    }
    
    /**
	 * Instantiates a new a table from a 2D array.
	 * 
	 * @param array2d
	 *            the 2D array
	 */
    public ATable(Object[][] array2d) 
    {
	Table tab = new Table(1);
	for (int i=0; i<array2d.length; i++)
	{
	    TR row = new TR();
	    tab.addElement(row);
	    for(int j=0; j<array2d[i].length; j++)
	    {
		row.addElement(new TD(""+array2d[i][j]));
	    }
	}
    }

}
