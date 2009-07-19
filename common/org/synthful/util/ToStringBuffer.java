/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 * Created on May 28, 2003, 10:16 AM
 */


package org.synthful.util;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.synthful.lang.Empty;
import org.synthful.lang.Empty.EmptyFactory;

/**
 * 
 * 
 * @author Blessed Geek
 */
public class ToStringBuffer
implements EmptyFactory
{
    
    /**
	 * Creates a new instance of toStringBuffer.
	 */
    public ToStringBuffer ()
    {
        StrBuf = new StringBuffer ();
    }
    
    /**
	 * Instantiates a new toStringBuffer.
	 * 
	 * @param format
	 */
    public ToStringBuffer (Format format)
    {
        StrBuf = new StringBuffer ();
        switchFormat (format);
    }
    
    /**
	 * Set print attributes depending on format.
	 * @param format
	 */
    protected void switchFormat (Format format)
    {
    	switch (format)
    	{
    		case PLAINDump:    			
				this.Indentation = "";
				this.ItemDelimiter = " ";
				this.NodeDelimiter = " ";
				this.NodeTerminatorLeft = "";
				this.NodeTerminatorRight = "";
				return;
				
    		case PRETTYDump:
    			this.Indentation = "  ";
    			this.ItemDelimiter = ",";
    			this.NodeDelimiter = ",\n@i(+1)";
    			this.NodeTerminatorLeft = "{\n@i(+1)";
    			this.NodeTerminatorRight = "\n@i}";
    			this.ItemTerminatorLeft = "[";
    			this.ItemTerminatorRight = "]";
	            return;
        
    		case CSVDump:
    			this.Indentation = "";
    			this.ItemDelimiter = ",";
    			this.NodeDelimiter = ",";
    			this.NodeTerminatorLeft = "";
    			this.NodeTerminatorRight = "";
	            return;
        
    	}
    }
    
    
    public StringBuffer toStringBuffer (ToStringBufferable o, Format format)
    {
    	this.switchFormat(format);
    	o.toStringBuffer (this, 0, 0);
    	return this.StrBuf;
    }

    /**
	 * Converts to StringBuffer.
	 * 
	 * @param o
	 * @param depth
	 * 
	 * @return Object o as StringBuffer
	 */
    public StringBuffer toStringBuffer (Object o, int depth, int iteration)
    {
        if (o==null)
            o = EMPTY;
        else if (o instanceof ToStringBufferable)
        	((ToStringBufferable)o).toStringBuffer(this, depth, iteration);
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
        if (o instanceof Object[])
        {
        	this.appendNotNull(this.ItemTerminatorLeft);
        
            append((Object[])o, depth);
        
            this.appendNotNull(this.ItemTerminatorRight);
        }
        else
        {
            StrBuf.append(this.StringQuote).append(o).append(this.StringQuote);     
        }        
    }
    
    public void appendNotNull(Object o)
    {
    	if(o!=null)
    	{
    		this.StrBuf.append(o.toString());
    	}
    }

    
    /**
	 * Append.
	 * 
	 * @param o
	 * @param depth
	 */
    protected void append (Object[] o, int depth)
    {
        for (int i = 0; i < o.length; i++)
        {            
            toStringBuffer (o[i], depth+1, i);
            if (i < o.length-1 && ItemDelimiter!=null)
                StrBuf.append (ItemDelimiter);
        }
        
        StringBuffer indent = mkIndentation(depth);
        this.appendNotNull(indent);
    }
    
    public void appendFillers(Fillers filler, int indentDepth)
    {
    	String fillerStr;
    	
    	switch (filler)
		{
		    case Indentation:
				fillerStr = this.Indentation;
				break;
		    case PairSeparator:
				fillerStr = this.PairSeparator;
				break;
		    case ItemDelimiter:
				fillerStr = this.ItemDelimiter;
				break;
		    case ItemTerminatorLeft:
				fillerStr = this.ItemTerminatorLeft;
				break;
		    case ItemTerminatorRight:
				fillerStr = this.ItemTerminatorRight;
				break;
		    case NodeDelimiter:
				fillerStr = this.NodeDelimiter;
				break;
		    case NodeTerminatorLeft:
				fillerStr = this.NodeTerminatorLeft;
				break;
		    case NodeTerminatorRight:
				fillerStr = this.NodeTerminatorRight;
				break;
			default:
				fillerStr = null;
		}
    	
    	Matcher m = indentMatcher.matcher(fillerStr);
    	this.StrBuf.ensureCapacity(this.StrBuf.length() + fillerStr.length());
    	int c0 = 0;
    	
    	while(m.find())
    	{
    		String g6 = m.group(6);
    		String g5 = m.group(5);
    		String g3 = m.group(3);
    		
    		//Regex string is structured so thata all matches also matches g1
    		//Use g0 start/end for substitution.
    		if (m.start()>0)
    			this.StrBuf.append(fillerStr.subSequence(c0, m.start()));
    		c0 = m.end();
    		
    		//g6 match = @i requires no change in indentDepth
    		
    		//g5:n match = g4:(n) = g0:@i(n)
    		if (g5!=null)
    		{
    			indentDepth = Integer.parseInt(g5);
    		}
    		//g3:+/-n match = g2:(+/-n) = g0:@(i+/-n)
    		else if (g3!=null)
    		{
    			indentDepth += Integer.parseInt(g3);
    		}
    		
    		this.StrBuf.append(this.mkIndentation(indentDepth));
    	}
    	
    	this.StrBuf.append(fillerStr.substring(c0));
    }
    
    /**
	 * Clear.
	 * 
	 * @return Clear as toStringBuffer
	 */
    public ToStringBuffer clear()
    {
        StrBuf = new StringBuffer ();
        return this;
    }
    
    public StringBuffer mkIndentation(int depth)
    {
        if (Indentation!=null&&Indentation.length()>0)
        {
            StringBuffer indent = new StringBuffer(depth*Indentation.length());
            //indent.append('\n');
            for (int i=0;i<depth;i++)
                indent.append(Indentation);
            return indent;
        }
        return null;
    }
    
    public StringBuffer getStringBuffer()
    {
    	return StrBuf;
    }
    
    static public interface  ToStringBufferable
    {
    	StringBuffer toStringBuffer(ToStringBuffer tostrbuf, int depth, int iteration);
    }
    
    /** Variable StrBuf. */
    StringBuffer StrBuf;
    
    /** Variable Indentation. */
    public String Indentation = " ";
    
    public String PairSeparator = ":";
    
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
    
    public char StringQuote = '"';
    
    final static private String indentMatchStr =
    	"@i((\\([+-]([0-9]+)\\))|(\\(([0-9]+)\\))|())";
    
	final static private Pattern indentMatcher =
		Pattern.compile(ToStringBuffer.indentMatchStr);
	
    static public enum Fillers
    {
        Indentation,
        PairSeparator,
        ItemDelimiter,
        ItemTerminatorLeft,
        ItemTerminatorRight,
        NodeDelimiter,
        NodeTerminatorLeft,
        NodeTerminatorRight
    }
        
    static public enum Format
    {
    	PLAINDump,
    	PRETTYDump,
    	CSVDump
    }
}
