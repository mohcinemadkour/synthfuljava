/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 */
package org.synthful.util;

import java.util.Collection;
import java.util.Vector;

import org.synthful.util.ToStringBuffer.Format;
import org.synthful.util.ToStringBuffer.ToStringBufferable;

/**
 * VectorNode Class.
 */
public class VectorNode
extends Vector
implements TreeNode, ToStringBufferable
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 3717515568044106572L;

	/**
	 * Instantiates a new VectorNode.
	 */
    public VectorNode ()
    {
        super ();
    }
    
    /**
	 * 
	 * 
	 * @param initsz
	 *            initial size of vector.
	 */
    public VectorNode (int initsz)
    {
        super (initsz);
    }
    
    /**
	 * 
	 * 
	 * @param initsz
	 *            initial size of vector.
	 * @param increm
	 *            incremental size of vector expansion.
	 */
    public VectorNode (int initsz, int increm)
    {
        super (initsz, increm);
    }
    
    /**
	 * Create a new vector from a Collection Object. Since this class is an
	 * implementation of Collection, this constructor can be used to clone a
	 * vector.
	 * 
	 * @param c
	 *            Collection Object
	 */
    public VectorNode (Collection c)
    {
        super (c);
    }
    
    /**
	 * Create a new vector from an Object array. The ordering of elements of the
	 * new vector corresponds to the order of elements of the Object array.
	 * 
	 * @param o
	 */
    public VectorNode (Object[] o)
    {
        super (o.length);
        for (int i = 0; i < o.length; i++)
            add (o[i]);
    }
    
    /**
	 * Gets the.
	 * 
	 * @param key
	 * 
	 * @return Gets the as Object
	 */
    public Object get (int[] key)
    {
        return get (0, key);
    }
    
    /**
	 * Gets the.
	 * 
	 * @param keylevel
	 * @param key
	 * 
	 * @return Gets the as Object
	 */
    public Object get (int keylevel, int[] key)
    {
        if (keylevel >= key.length || keylevel < 0)
            return null;
        Object o = get (key[keylevel]);
        
        if (keylevel == key.length - 1)
            return o;
        
        if (o instanceof VectorNode)
            return ( (VectorNode) o).get (keylevel + 1, key);
        
        return o;
    }
    
    /**
	 * Adds the.
	 * 
	 * @param keylevel
	 * @param key
	 * @param value
	 * 
	 * @return Adds the as VectorNode
	 */
    public VectorNode add (int keylevel, int[] key, Object value)
    {
        if (keylevel >= key.length || keylevel < 0)
            return null;
        
        Object o
        = (keylevel == key.length - 1)
        ? value
        : new VectorNode ().add (keylevel + 1, key, value);
        ;
        
        return this;
    }
    
    /* (non-Javadoc)
     * @see org.synthful.util.TreeNode#getKeyDelimiter()
     */
    public char getKeyDelimiter ()
    {
        return KeyDelimiter;
    }
    
    /* (non-Javadoc)
     * @see org.synthful.util.TreeNode#getParentNode()
     */
    public TreeNode getParentNode ()
    {
        return ParentNode;
    }
    
    /* (non-Javadoc)
     * @see org.synthful.util.TreeNode#getNode(int)
     */
    public TreeNode getNode (int i)
    {
        Object o = get (i);
        if (o instanceof TreeNode)
            return (TreeNode) o;
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.synthful.util.TreeNode#setKeyDelimiter(char)
     */
    public TreeNode setKeyDelimiter (char delimiter)
    {
        KeyDelimiter = delimiter;
        return this;
    }
    
    /* (non-Javadoc)
     * @see org.synthful.util.TreeNode#setParentNode(org.synthful.util.TreeNode)
     */
    public TreeNode setParentNode (TreeNode node)
    {
        ParentNode = node;
        return this;
    }
    
    /**
	 * Create elements in this vector by reading from a String. The elements of
	 * the String are separated by the specified char cSeparator.
	 * 
	 * @param s
	 *            the String from which the elements are to be read in.
	 * @param cSeparator
	 *            the char used as element separator in the String.
	 * 
	 * @return this.
	 */
    public VectorNode fromString (String s, char cSeparator)
    {
        fromString (s, "[" + cSeparator + ']');
        return this;
    }
    
    /**
	 * 
	 * 
	 * @param s
	 *            input_string String to be parsed.
	 * @param sSeparator
	 *            separator for segregating input_string into vector cells.
	 * 
	 * @return this.
	 * 
	 *         Use @link #setIgnoreNullfromString (boolean yes)
	 *         setIgnoreNullfromString (boolean) to skip null string segments.
	 *         If set true, any null segment will not create a new vector cell.
	 *         If set false, any null segment encountered will create a vector
	 *         cell with a blank string.
	 */
    public VectorNode fromString (String s, String sSeparator)
    {
        if (s == null)
            return this;
        String[] ss = s.split (sSeparator);
        for (int i = 0; i < ss.length; i++)
            add (ss[i]);
        
        return this;
    }
    
    /**
     * @see org.synthful.util.TreeNode#toString(java.lang.String, java.lang.String)
     */
    public String toString (
    String itemdelimiter, String nodedelimiter)
    {
    	ToStringBuffer tostrbuf = new ToStringBuffer(Format.PRETTYDump);
        tostrbuf.ItemDelimiter = itemdelimiter;
        tostrbuf.NodeDelimiter = nodedelimiter;
        return "" + toStringBuffer (tostrbuf, 0, 0);
    }
    
    /**
     * @see org.synthful.util.TreeNode#toStringBuffer(org.synthful.util.toStringBuffer)
     */
    public StringBuffer toStringBuffer (ToStringBuffer tostrbuf, int depth, int iteration)
    {
        StringBuffer indent = tostrbuf.mkIndentation(depth);
    	StringBuffer strBuf = tostrbuf.getStringBuffer();
    	
        if (indent!=null)
            strBuf.append(indent).append(indent);
    	strBuf.append(tostrbuf.ItemTerminatorLeft);
    	
    	for(int i=0; i<this.size(); i++)
    	{
    		Object val = this.get(i);
            
            if (i>0)
            	strBuf.append(tostrbuf.NodeDelimiter);
            
            tostrbuf.toStringBuffer(val, depth, i);
            //StrBuf.append(vbuf);
    	}
    	
        strBuf.append(tostrbuf.NodeTerminatorRight);
        return tostrbuf.StrBuf;
    }
    
    /** Variable KeyDelimiter. */
    protected char KeyDelimiter = '/';
    
    /** Variable ParentNode. */
    protected TreeNode ParentNode;
}
