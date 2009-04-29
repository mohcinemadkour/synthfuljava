/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2008
 * 
 */

package org.synthful.automata;

import java.util.Hashtable;

/**
 * @author Blessed Geek
 */
public interface FiStBeanRegistryInterface
{
    
    /**
     * Gets the ActionHash.
     * 
     * @return the ActionHash as Hashtable
     */
    public Hashtable getActionHash();
    
    /**
     * Gets the VerificationHash.
     * 
     * @return the VerificationHash as Hashtable
     */
    public Hashtable getVerificationHash();
}
