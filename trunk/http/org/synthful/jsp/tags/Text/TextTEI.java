/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org
 * 
 */
package org.synthful.jsp.tags.Text;

import javax.servlet.jsp.tagext.*;

// TODO: Auto-generated Javadoc
/**
 * Class TextTEI.
 */
public class TextTEI
extends TagExtraInfo
{
    
    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(javax.servlet.jsp.tagext.TagData)
     */
    public VariableInfo[] getVariableInfo(TagData data)
    {
        String type = data.getAttributeString("type");
        String s = data.getAttributeString("id");
        boolean declare = true;
        
        if(s==null)
        {
            s = data.getAttributeString("ref");
            declare=false;
        }
        
        VariableInfo vinfo =
        new VariableInfo(
            s,
            type ==null?
            "StringBuffer":type,
            declare,
            VariableInfo.AT_END);
        VariableInfo[] info = {vinfo};
        return info;
    }
}
