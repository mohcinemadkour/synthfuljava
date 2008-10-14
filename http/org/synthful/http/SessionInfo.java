/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org
 * 
 * Session.java
 *
 * Created on May 17, 2003, 7:27 PM
 */

package org.synthful.http;

import javax.servlet.http.*;
import javax.servlet.ServletContext;

// TODO: Auto-generated Javadoc
/**
 * Resolves information per session.
 * 
 * @author Blessed Geek
 */
public class SessionInfo

{
    
    /**
	 * Creates a new instance of Session.
	 */
    public SessionInfo()
    {
    }
    
    /**
	 * Resolve session information.
	 * 
	 * @param req
	 *            request handle.
	 */    
    public void ResolveSessionInfo(HttpServletRequest req)
    {
        Session = req.getSession();
        ServletContext ctx = Session.getServletContext();
        ContextInfo = new ContextInfo(ctx);
    }
    
    /**
	 * Returns true if session age==0;.
	 * 
	 * @return true if session age==0; false otherwise.
	 */    
    public boolean isNewSession()
    {
        return (Age==0)? true: false;
    }
    
    /**
	 * Sets session to specified age. Returns true if session age==0.
	 * 
	 * @param setage
	 *            Set session age to setage.
	 * 
	 * @return true if session age==0; false otherwise.
	 */    
    public boolean isNewSession(int setage)
    {
        if (Age==0)
        {
            Age=setage;
            return true;
        }
        
        Age=setage;
        return false;
   }

    /**
	 * Get age of session.
	 * 
	 * @param setage
	 *            Set session age to setage.
	 * 
	 * @return age of session.
	 */    
    public int getSessionAge(int setage)
    {
        if (Age==0)
        {
            Age=setage;
            return Age;
        }
        
        Age=setage;
        return Age;
   }

    /** HttpSession handle. */    
    public HttpSession Session;
    
    /** ContextInfo handle. */    
    public ContextInfo ContextInfo;
    
    /**
	 * Records age/stage of session. Initial value is zero. A new session
	 * invoked due to time-out is detectable by age==0. Use and set age to
	 * various stages of a session. The stages of a session is to be interpreted
	 * by the application. e.g. age=1 may denote login success. age=2 Data
	 * connections established. age=3 performing task. age==0 session time-out
	 * requires relogin and reestablishing data connections.
	 */    
    public int Age=0;
}