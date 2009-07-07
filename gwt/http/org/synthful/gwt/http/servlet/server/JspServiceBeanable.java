package org.synthful.gwt.http.servlet.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.jsp.HttpJspPage;
import javax.servlet.jsp.PageContext;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

abstract public class JspServiceBeanable
    extends RemoteServiceServlet
    implements HttpJspPage
{
    protected PageContext jspContext;
    protected String jspInput;
    protected JspResponseWriter jspOut = new JspResponseWriter();
    protected Hashtable<String, String> parameters;

    protected JspResponseWriter shuntJspOutput(PageContext pageContext)
    {
        this.jspContext = pageContext;
        try {
            this.jspOut.clearBuffer();
        }
        catch(IOException e)
        {
            this.jspOut = new JspResponseWriter();
        }
        
        pageContext.pushBody(this.jspOut);
        return jspOut;
    }
    
    protected String doJspService(HashMap<String, String> parameters)
    {
        this.parameters = new Hashtable<String, String>(parameters);
        
        try
        {
            this._jspService(
                this.getThreadLocalRequest(),
                this.getThreadLocalResponse());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        this.jspContext.popBody();
        
        return this.jspOut.Body.toString();
    }

    @Override
    public void jspDestroy()
    {
        System.out.println("jspDestroy");
    }

    @Override
    public void jspInit()
    {
        System.out.println("jspInit");
    }
}
