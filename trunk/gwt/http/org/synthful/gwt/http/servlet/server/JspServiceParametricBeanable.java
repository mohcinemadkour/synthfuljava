package org.synthful.gwt.http.servlet.server;

import java.util.HashMap;



abstract public class JspServiceParametricBeanable
    extends JspServiceBeanable
{

    public String doServiceResponse(
        HashMap<String, String> parameters)
    {   
        return this.doJspService(parameters);
    }
}
