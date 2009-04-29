/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2008
 * 
 */

package org.synthful.xml.pjx;

/**
 * @author Blessed Geek
 */
public interface PjxValues
{
    
    /**
     * Gets the Value.
     * 
     * @param path
     * @return the Value as Object
     */
    public Object getValue(String path);
    
    /**
     * Sets the value.
     * 
     * @param path
     * @param value
     * @return Sets the value as Object
     */
    public Object setValue(String path, Object value);
}
