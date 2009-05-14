/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org
 * 
 */
package org.synthful.jsp.tags.Text;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.*;

// TODO: Auto-generated Javadoc
/**
 * TextTag Class.
 */
public class TextTag
extends BodyTagSupport
{
    
    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#setId(java.lang.String)
     */
    public void setId(String value)
    {
        id = value;
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#getId()
     */
    public String getId()
    {
        return (id);
    }
    
    /**
	 * Sets the ref.
	 * 
	 * @param value
	 *            the ref value
	 */
    public void setRef(String value)
    {
        ref = ("" + value).trim();
    }
    
    /**
	 * Gets the ref.
	 * 
	 * @return the ref
	 */
    public String getRef()
    {
        return (ref);
    }
    
    /**
	 * Sets the debug.
	 * 
	 * @param value
	 *            the Debug
	 */
    public void setDebug(String value)
    {
        String s = ("" + value).trim().toLowerCase();
        if (s.equals ("true"))
            debug=true;
        else if(s.equals ("false"))
            debug=false;
    }
        
    /**
	 * Sets if body is displayed.
	 * 
	 * @param value
	 *            the display switch
	 */
    public void setDisplay(String value)
    {
        String s = ("" + value).trim().toUpperCase();
        if(s.equals("TRUE")) display=true;
    }
    
    
    /**
	 * Sets the scope.
	 * 
	 * @param value
	 *            the scope
	 */
    public void setScope(String value)
    {
        scope = ("" + value).trim().toUpperCase();
        
        if (scope.equals("SESSION"))
            _scope = PageContext.SESSION_SCOPE;
        else if (scope.equals("APPLICATION"))
            _scope = PageContext.APPLICATION_SCOPE;
        else if (scope.equals("PAGE"))
            _scope = PageContext.PAGE_SCOPE;
        else if (scope.equals("REQUEST"))
            _scope = PageContext.REQUEST_SCOPE;
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    public int doStartTag()
    {
        if (ref != null )
        {
            Object o = pageContext.getAttribute(ref, _scope);
            /* o.getclass is pointless code but Bug in j1.4.0 needs 
             * getClass to trigger proper working of "instanceof" */
            body = (o!=null && o.getClass()!=null && o instanceof StringBuffer)
            ?(StringBuffer)o:new StringBuffer();
        }
        else
        {
            ref = "";
            body = new StringBuffer();
        }
        
        String name = (id==null || id.length()==0)
        ?ref:id;
        pageContext.setAttribute(name, body, _scope);
        
        return EVAL_BODY_BUFFERED;
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    public int doEndTag()
    {
        if(display)
        {
            try
            {
                bodyContent.getEnclosingWriter().write(body.toString());
            }
            catch (IOException ex)
            {
                System.out.println("text display failure:" + ex);
            }
        }
        return EVAL_PAGE;
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doAfterBody()
     */
    public int doAfterBody()
    throws JspTagException
    {
        //	pageContext.setAttribute(id, this, _scope);
        String s = readBody();
        if(s!=null)
            body.append(s);
        
        if (debug)
            System.out.println("text body:" + body);
        
        return SKIP_BODY;
    }
    
    /**
	 * Read body.
	 * 
	 * @return the body text
	 */
    String readBody()
    {
        BodyContent bc = getBodyContent();
        if (bc == null)
            return null;
        String s = bc.getString();
        bc.clearBody();
        return s;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return body.toString();
    }
    
    /**
	 * Sets the body.
	 * 
	 * @param value
	 *            the body
	 */
    public void setBody(String value)
    {
        body = new StringBuffer(value);
    }
    
    /**
	 * Gets the body.
	 * 
	 * @param value
	 *            the body value
	 * 
	 * @return the body
	 */
    public StringBuffer getBody(String value)
    {
        return body;
    }
    
    /** The scope. */
    String scope = null;
    
    /** The _scope. */
    int _scope = PageContext.REQUEST_SCOPE;
    
    /** The ref. */
    String ref = null;
    
    /** The id. */
    String id = null;
    
    /** The debug switch. */
    boolean debug = false;
    
    /** The display switch determining if body is displayed. */
    boolean display = false;
    
    /** The body. */
    StringBuffer body = null;
}
