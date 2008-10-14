/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org
 * 
 * Context.java
 *
 * Created on May 17, 2003, 7:54 PM
 */

package org.synthful.http;

import java.io.File;
import javax.servlet.ServletContext;

/**
 * ContextInfo Class.
 * Resolves information per context per session.
 * Called by SessionInfo.
 * @author Blessed Geek
 */
public class ContextInfo
{
    
    /**
     * Creates a new instance of ContextInfo from ServletContext handle.
     * Resolves the resource handles available to the context of application.
     * @param ctx ServletContext handle.
     */
    public ContextInfo(ServletContext ctx)
    {
        Context = ctx;
        ContextName = Context.getServletContextName();
        String syspath = Context.getRealPath("/");
        ContextSysPath = new File(syspath);
        WebInf = new File(ContextSysPath, "WEB-INF");
        MetaInf = new File(ContextSysPath, "META-INF");
    }
    
    /** Context handle */    
    public ServletContext Context;
    /** Context root name */    
    public String ContextName;
    /** File system path of context root. */    
    public File ContextSysPath;
    /** File system path of WEB-INF folder in the application. */    
    public File WebInf;
    /** File system path of META-INF folder in the application. */    
    public File MetaInf;
}
