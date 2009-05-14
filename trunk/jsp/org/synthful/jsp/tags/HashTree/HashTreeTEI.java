/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org
 * 
 */
package org.synthful.jsp.tags.HashTree;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.synthful.util.HashTreeNode;

// TODO: Auto-generated Javadoc
/**
 * Class HashTreeTEI.
 */
public class HashTreeTEI
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
            "org.synthful.util.HashTreeNode":type,
            declare,
            VariableInfo.AT_END);
        VariableInfo[] info = {vinfo};
        return info;
    }
}
