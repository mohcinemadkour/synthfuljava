package org.synthful.gwt.rpc.server;

import java.util.Map;



abstract public class JspServiceParametricBeanable
    extends JspServiceBeanable
{

    public String doServiceResponse(
        Map<String, String> parameters)
    {   
        return this.doJspService(parameters);
    }
}
