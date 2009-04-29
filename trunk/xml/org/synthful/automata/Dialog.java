/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2008
 * 
 */

package org.synthful.automata;

import java.util.Iterator;
import java.util.Vector;
import org.jdom.Text;
import org.jdom.Element;
import org.synthful.xml.XmlParser;

/**
 * @author Blessed Geek
 */
public class Dialog
{
    
    /**
     * Instantiates a new Dialog.
     * 
     * @param el
     */
    public Dialog(Element el)
    {
        XmlElement = el;
    }
        
    /**
     * Digest dialog.
     * 
     * @param ej
     * @param dialogs
     */
    protected static void digestDialog(Element ej, Vector dialogs)
    {
        Iterator kter = ej.getChildren(TagName).iterator();
        dialogs.clear();

        while (kter.hasNext())
        {
            Object ok = kter.next();
            if (ok instanceof Element)
                dialogs.add( new Dialog((Element)ok));
        }
    }
    
    /**
     * Gets the XmlElement.
     * 
     * @return the XmlElement as Element
     */
    public Element getXmlElement()
    {
        return XmlElement;
    }
        
    /** Variable XmlElement. */
    protected Element XmlElement;
    
    /** The Constant TagName. */
    public final static String TagName = "dialog";    
}
