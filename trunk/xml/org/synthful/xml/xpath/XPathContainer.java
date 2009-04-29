/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2008
 * 
 */
package org.synthful.xml.xpath;

import java.io.StringReader;
import java.util.Hashtable;

import org.xml.sax.InputSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

/**
 * @author Blessed Geek
 */
public class XPathContainer
extends Hashtable
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * Creates a new instance of XPathContainer.
     */
    public XPathContainer(){}
    
    /**
     * Creates a new instance of XPathContainer.
     * 
     * @param initialCapacity
     */
    public XPathContainer(int initialCapacity)
    {
        super(initialCapacity);
    }
    
    /**
     * Put x path.
     * 
     * @param key
     * @param sxpath
     * @return Put x path as XPathExpression
     */
    public final XPathExpression putXPath(String key, String sxpath)
    {
        return (XPathExpression) put(key,sxpath);
    }
    
    /**
     * Put x path.
     * 
     * @param key
     * @param sxpath
     * @return Put x path as XPathExpression
     */
    public final XPathExpression putXPath(Object key, String sxpath)
    {
        return (XPathExpression) put(key,sxpath);
    }
    
    /* (non-Javadoc)
     * @see java.util.Hashtable#put(java.lang.Object, java.lang.Object)
     */
    public final Object put(Object key, Object sxpath)
    {
        if (key!=null && sxpath!=null)
            try{
                XPathExpression xpathexp =
                    XPathCompiler.compile(sxpath.toString());
                Object o = super.put(key,xpathexp);
                if (o instanceof XPathExpression)
                    return (XPathExpression)o;
            }
            catch (XPathExpressionException e)
            {
                return (XPathExpression) null;
            }
            catch (Exception e)
            {
                return (XPathExpression) null;
            }
        
        return (XPathExpression) null;
    }
    
    /**
     * Gets the XPath.
     * 
     * @param key
     * @return the XPath as XPathExpression
     */
    public final XPathExpression getXPath(String key)
    {
        return getXPath(key);
    }
    
    /**
     * Gets the XPath.
     * 
     * @param key
     * @return the XPath as XPathExpression
     */
    public final XPathExpression getXPath(Object key)
    {
        Object o = super.get (key);
        if (o instanceof XPathExpression)
            return (XPathExpression)o;
        else
        {
            remove(key);
            return null;
        }
    }
    
    /**
     * Evaluate.
     * 
     * @param key
     * @param xstr
     * @return Evaluate as String
     */
    public final String evaluate(Object key, String xstr)
    {
        if (xstr.length()==0) return null;
        
        InputSource ixx = new InputSource(new StringReader(xstr));
        return evaluate (key, ixx);
    }
    
    /**
     * Evaluate.
     * 
     * @param key
     * @param xsrc
     * @return Evaluate as String
     */
    public final String evaluate(Object key, InputSource xsrc)
    {
        XPathExpression xpathexp = this.getXPath(key);
        if (xpathexp==null)
            return null;
        try{
            return xpathexp.evaluate(xsrc);
        }
        catch (XPathExpressionException e)
        {
            return null;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /** The Constant XPathMaker. */
    public static final XPathFactory XPathMaker = XPathFactory.newInstance();
    
    /** The Constant XPathCompiler. */
    public static final XPath XPathCompiler = XPathMaker.newXPath();
}
