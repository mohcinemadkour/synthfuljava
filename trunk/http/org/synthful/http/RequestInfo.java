/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org
 * 
 * Request.java
 *
 * Created on May 17, 2003, 7:54 PM
 */

package org.synthful.http;
import javax.servlet.http.*;
import java.util.Enumeration;
import org.synthful.util.HashTreeNode;

// TODO: Auto-generated Javadoc
/**
 * Resolves information per request.
 * 
 * @author Blessed Geek
 */
public class RequestInfo
{
    
    /**
	 * Creates a new instance of Request.
	 * 
	 * @param req
	 *            request handle.
	 */
    public RequestInfo(HttpServletRequest req)
    {
    }

    /**
	 * Read parameter names and their respective parameter values into request
	 * parameter hashtree. Calls and store handle to hashtree returned by
	 * GetParameters as a static reference.
	 * 
	 * @param req
	 *            request handle.
	 * 
	 * @return root node of hashtree.
	 */    
    public HashTreeNode ResolveParameters(HttpServletRequest req)
    {
        ParameterHash = GetParameters(req);
        return ParameterHash;
    }

    /**
	 * Read parameter names and their respective parameter values into and
	 * return reference to request parameter hashtree. Reference to hashtree is
	 * transient.
	 * 
	 * @param req
	 *            the req
	 * 
	 * @return root node of hashtree.
	 */    
    static public HashTreeNode GetParameters(HttpServletRequest req)
    {
        HashTreeNode ph = new HashTreeNode();
        
        Enumeration p = req.getParameterNames();
        while (p.hasMoreElements())
        {
            Object o = p.nextElement();
            if (o==null)continue;
            Object v = req.getParameterValues(""+o);
            ph.put(""+o, v);
        }
        
        return ph;
    }
    
    /**
	 * Root node of hashtree of HashTreeNodes containing
	 *  request parameter tree.
	 */    
    public HashTreeNode ParameterHash;
}
