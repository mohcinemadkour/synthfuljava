/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 */
package org.synthful.lang;

public class Empty<T>
{
	static public interface EmptyFactory<W>
	{
	  Empty<?> EMPTY = new Empty();
	}
	
	public Empty(){}
	
	public Empty(T t)
	{
		this.value = t;
	}

    
	public String toString()
	{
		return Blank;
	}

	final public T toValue()
	{
		return this.value;
	}
	
	final public int toInt()
	{
	    return 0;
	}
	
	private T value;

	final static public <U> Empty<U> init(U u)
	{
		Empty<U> EMPTY = new Empty<U>(u);
		return EMPTY;
	}

	final static public String Blank = "";
	
}