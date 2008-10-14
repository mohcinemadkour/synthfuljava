/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 * Created on May 28, 2003, 10:16 AM
 */


package org.synthful.util;

/**
 * 
 * 
 * @author Blessed Geek
 */
public class toStringBuffer
{
    
    /**
	 * Creates a new instance of toStringBuffer.
	 */
    public toStringBuffer ()
    {
        StrBuf = new StringBuffer ();
    }
    
    /**
	 * Instantiates a new toStringBuffer.
	 * 
	 * @param format
	 */
    public toStringBuffer (long format)
    {
        StrBuf = new StringBuffer ();
        switchFormat (format);
    }
    
    /**
	 * Set print attributes depending on format.
	 * @param format
	 */
    protected void switchFormat (long format)
    {
    	switch ((int)format)
    	{
    		case PLAINDump:    			
				Indentation = "";
				ItemDelimiter = " ";
				NodeDelimiter = " ";
				NodeTerminatorLeft = "";
				NodeTerminatorRight = "";
				return;
				
    		case PRETTYDump:
	            Indentation = "  ";
	            ItemDelimiter = ",";
	            NodeDelimiter = "";
	            NodeTerminatorLeft = "{";
	            NodeTerminatorRight = "}";
	            return;
        
    		case CSVDump:
	            Indentation = "";
	            ItemDelimiter = ",";
	            NodeDelimiter = "\n";
	            NodeTerminatorLeft = "";
	            NodeTerminatorRight = "";
	            return;
        
    		case HTMLCell:
	            Indentation = null;
	            ItemDelimiter = "<BR>";
	            NodeDelimiter = "";
	            NodeTerminatorLeft = "<TD>";
	            NodeTerminatorRight = "</TD>";
	            return;
    	}
    }
    
    /**
	 * Converts to StringBuffer.
	 * 
	 * @param node
	 * 
	 * @return node as StringBuffer
	 */
    public StringBuffer toStringBuffer (TreeNode node)
    {
        return toStringBuffer (node, 0);
    }
    
    /**
	 * Converts to StringBuffer.
	 * 
	 * @param o
	 * @param depth
	 * 
	 * @return Object o as StringBuffer
	 */
    public StringBuffer toStringBuffer (Object o, int depth)
    {
        if (o==null)
            StrBuf.append(NullObj);
        else
            appendValues(o, depth);

        return StrBuf;
    }

    /**
	 * Append values to TreeNode or TreeNode element
	 * 
	 * @param o TreeNode or TreeNode element
	 * @param depth
	 */
    protected void appendValues (Object o, int depth)
    {
        if (NodeTerminatorLeft!=null)
            StrBuf.append (NodeTerminatorLeft);
        
        if (o instanceof TreeNode)
            append((TreeNode)o, depth);
        else if (o instanceof Object[])
            append((Object[])o, depth);
        else
        {
            if (ItemTerminatorLeft!=null)
                StrBuf.append(ItemTerminatorLeft);
        
            StrBuf.append(o);     
        
            if (ItemTerminatorRight!=null)
                StrBuf.append(ItemTerminatorRight);
        }
        
        if (NodeTerminatorRight!=null)
            StrBuf.append (NodeTerminatorRight);
    }

    /**
	 * Append.
	 * 
	 * @param node
	 * @param depth
	 */
    protected void append (TreeNode node, int depth)
    {
        StringBuffer indent = mkIndentation(depth);
        
        for (int i = 0; i < node.size (); i++)
        {            
            if (indent!=null)
                StrBuf.append(indent).append(Indentation);
            if (node instanceof HashTreeNode)
                StrBuf.append (""+ ((HashTreeNode)node).getKey (i));

            toStringBuffer (node.get (i), depth+1);

            if (i < node.size()-1 && ItemDelimiter!=null)
                StrBuf.append (ItemDelimiter);

            if (node instanceof HashTreeNode)
                if (NodeDelimiter!=null)
                    StrBuf.append (NodeDelimiter);
        }
        
        if (indent!=null)
            StrBuf.append(indent);

    }
    
    /**
	 * Append.
	 * 
	 * @param o
	 * @param depth
	 */
    protected void append (Object[] o, int depth)
    {
        StringBuffer indent = mkIndentation(depth);

        for (int i = 0; i < o.length; i++)
        {            
            toStringBuffer (o[i], depth+1);
            if (i < o.length-1 && ItemDelimiter!=null)
                StrBuf.append (ItemDelimiter);
        }
        
        if (indent!=null)
            StrBuf.append(indent);
    }
    
    /**
	 * Clear.
	 * 
	 * @return Clear as toStringBuffer
	 */
    public toStringBuffer clear()
    {
        StrBuf = new StringBuffer ();
        return this;
    }
    
    private StringBuffer mkIndentation(int depth)
    {
        if (Indentation!=null&&Indentation.length()>0)
        {
            StringBuffer indent = new StringBuffer(depth*Indentation.length());
            indent.append('\n');
            for (int i=0;i<depth;i++)
                indent.append(Indentation);
            return indent;
        }
        return null;
    }
    
    /** Variable StrBuf. */
    StringBuffer StrBuf;
    
    /** Variable Indentation. */
    public String Indentation = " ";
    
    /** Variable ItemDelimiter. */
    public String ItemDelimiter = " ";
    
    /** Variable ItemTerminatorLeft. */
    public String ItemTerminatorLeft = null;
    
    /** Variable ItemTerminatorRight. */
    public String ItemTerminatorRight = null;
    
    /** Variable NodeDelimiter. */
    public String NodeDelimiter = " ";
    
    /** Variable NodeTerminatorLeft. */
    public String NodeTerminatorLeft = "\n";
    
    /** Variable NodeTerminatorRight. */
    public String NodeTerminatorRight = "\n";
    
    /** Variable PLAINDump. */
    final static public int PLAINDump = 0;
    
    /** Variable PRETTYDump. */
    final static public int PRETTYDump = 1;
    
    /** Variable CSVDump. */
    final static public int CSVDump = 2;
    
    /** Variable HTMLCell. */
    final static public int HTMLCell = 3;
    
    /** Variable NullObj. */
    static public Object NullObj = "";
}
