/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 */
package org.synthful.lang;

public class Empty
{
	static public interface EmptyFactory
	{
	  Empty EMPTY = new Empty();
	}
	
	public Empty(){}
    
    static final public String String = "";

	public String toString()
	{
		return String;
	}

	final public Object toValue()
	{
		return null;
	}
	
	final public int toInt()
	{
	    return 0;
	}

}