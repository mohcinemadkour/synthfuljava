package org.synthful.gwt.rpc.server;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.el.ELContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.HttpJspPage;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspEngineInfo;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * 
 * @author BlessedGeek
 *
 */
abstract public class JspServiceBeanable
    extends RemoteServiceServlet
    implements HttpJspPage
{
    private static final long serialVersionUID = -5732211255519681476L;
    protected Hashtable<String, String> parameters;
    protected PageContext jspContext;
    protected JspFactory jspFactory;
    protected String jspInput;
    protected JspResponseWriter jspOut = new JspResponseWriter();
    protected boolean isGWTRPCService;
    protected boolean bodyNeedsPopping;
    
    protected String doJspService(HashMap<String, String> parameters)
    {
        this.parameters = new Hashtable<String, String>(parameters);
        
        JspFactory.setDefaultFactory(new JspFactoryShunt());
        this.jspFactory = JspFactory.getDefaultFactory();
        
        try
        {
            // Page/jsp context is set here ...
            // You cannot enquire for page/jsp context until this is run
            this._jspService(
                this.getThreadLocalRequest(),
                this.getThreadLocalResponse());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //this.jspContext = this.jspFactory.getPageContext();
        if (this.jspContext!=null && this.bodyNeedsPopping)
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
    
    /**
     * 
     * @author BlessedGeek
     *
     */
    public class JspFactoryShunt
    extends JspFactory
    {

        @Override
        public JspEngineInfo getEngineInfo()
        {
            return this.shuntedJspFactory.getEngineInfo();
        }

        @Override
        public JspApplicationContext getJspApplicationContext(
            ServletContext arg0)
        {
            return this.shuntedJspFactory.getJspApplicationContext(arg0);
        }

        @Override
        public PageContext getPageContext(
            Servlet arg0, ServletRequest arg1, ServletResponse arg2,
            String arg3, boolean arg4, int arg5, boolean arg6)
        {
            Class<? extends Servlet> cls = arg0.getClass();
            
            this.shuntedPageContext =
                this.shuntedJspFactory.getPageContext(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
            
            if (!cls.getName().contains("_gwtrpc_jsp"))
                return this.shuntedPageContext;
            
            jspContext = new PageContextShunt(this.shuntedPageContext);
            return jspContext;
        }

        @Override
        public void releasePageContext(
            PageContext arg0)
        {
            this.shuntedJspFactory.releasePageContext(arg0);
        }
        
        public PageContext getPageContext()
        {
            return jspContext;
        }
        
        final private JspFactory shuntedJspFactory = JspFactory.getDefaultFactory();
        private PageContext shuntedPageContext;
    }
    
    /**
     * 
     * @author BlessedGeek
     *
     */
    public class PageContextShunt
    extends PageContext
    {
        public PageContextShunt(PageContext ctx)
        {
            this.shuntedPageContext = ctx;
        }

        @Override
        public void forward(
            String arg0)
            throws ServletException, IOException
        {
            this.shuntedPageContext.forward(arg0);
        }

        @Override
        public Exception getException()
        {
            return this.shuntedPageContext.getException();
        }

        @Override
        public Object getPage()
        {
            return this.shuntedPageContext.getPage();
        }

        @Override
        public ServletRequest getRequest()
        {
            return this.shuntedPageContext.getRequest();
        }

        @Override
        public ServletResponse getResponse()
        {
            return this.shuntedPageContext.getResponse();
        }

        @Override
        public ServletConfig getServletConfig()
        {
            return this.shuntedPageContext.getServletConfig();
        }

        @Override
        public ServletContext getServletContext()
        {
            return this.shuntedPageContext.getServletContext();
        }

        @Override
        public HttpSession getSession()
        {
            return this.shuntedPageContext.getSession();
        }

        @Override
        public void handlePageException(
            Exception arg0)
            throws ServletException, IOException
        {
            this.shuntedPageContext.handlePageException(arg0);        }

        @Override
        public void handlePageException(
            Throwable arg0)
            throws ServletException, IOException
        {
            this.shuntedPageContext.handlePageException(arg0);        }

        @Override
        public void include(
            String arg0)
            throws ServletException, IOException
        {
            this.shuntedPageContext.include(arg0);
        }

        @Override
        public void include(
            String arg0, boolean arg1)
            throws ServletException, IOException
        {
            this.shuntedPageContext.include(arg0, arg1);
        }

        @Override
        public void initialize(
            Servlet arg0, ServletRequest arg1, ServletResponse arg2,
            String arg3, boolean arg4, int arg5, boolean arg6)
            throws IOException, IllegalStateException, IllegalArgumentException
        {
            this.shuntedPageContext.initialize(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }

        @Override
        public void release()
        {
            this.shuntedPageContext.release();        }

        @Override
        public Object findAttribute(
            String arg0)
        {
            return this.shuntedPageContext.findAttribute(arg0);
        }

        @Override
        public Object getAttribute(
            String arg0)
        {
            return this.shuntedPageContext.getAttribute(arg0);
        }

        @Override
        public Object getAttribute(
            String arg0, int arg1)
        {
            return getAttribute(arg0, arg1);
        }

        @Override
        public Enumeration<String> getAttributeNamesInScope(
            int arg0)
        {
            return this.shuntedPageContext.getAttributeNamesInScope(arg0);
        }

        @Override
        public int getAttributesScope(
            String arg0)
        {
            return this.shuntedPageContext.getAttributesScope(arg0);
        }

        @Override
        public ELContext getELContext()
        {
            return this.shuntedPageContext.getELContext();
        }

        @Override
        public ExpressionEvaluator getExpressionEvaluator()
        {
            return null;
        }

        @Override
        public JspWriter getOut()
        {            
            try {
                jspOut.clearBuffer();
            }
            catch(Exception e)
            {
                jspOut = new JspResponseWriter();
            }
            
            this.shuntedPageContext.pushBody(jspOut);
            bodyNeedsPopping = true;
            return jspOut;
        }

        @Override
        public VariableResolver getVariableResolver()
        {
            return this.shuntedPageContext.getVariableResolver();
        }

        @Override
        public void removeAttribute(
            String arg0)
        {
            this.shuntedPageContext.removeAttribute(arg0);        }

        @Override
        public void removeAttribute(
            String arg0, int arg1)
        {
            this.shuntedPageContext.removeAttribute(arg0, arg1);
        }

        @Override
        public void setAttribute(
            String arg0, Object arg1)
        {
            this.shuntedPageContext.setAttribute(arg0, arg1);            
        }

        @Override
        public void setAttribute(
            String arg0, Object arg1, int arg2)
        {
            this.shuntedPageContext.setAttribute(arg0, arg1, arg2);            
        }
        
        private PageContext shuntedPageContext;
    }
}
