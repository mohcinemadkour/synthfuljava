package org.synthful.gwt.rpc.server;

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
