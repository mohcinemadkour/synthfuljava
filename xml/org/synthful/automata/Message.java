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
public class Message
{
    
    /**
     * Instantiates a new Message.
     * 
     * @param target
     * @param text
     */
    public Message(String target, String text)
    {
        Target = target;
        Text.append(text);
    }
    
    /**
     * Instantiates a new Message.
     * 
     * @param el
     */
    public Message(Element el)
    {
        digestElement(el);
        XmlElement = el;
    }
        
    /**
     * Digest message.
     * 
     * @param ej
     * @param messages
     */
    protected static void digestMessage(Element ej, Vector messages)
    {
        Iterator kter = ej.getChildren("message").iterator();
        messages.clear();
        
        while (kter.hasNext())
        {
            Object ok = kter.next();
            if (ok instanceof Element)
                messages.add( new Message((Element)ok));
        }
    }
    
    /**
     * Digest element.
     * 
     * @param el
     * @return true, if Digest element successful
     */
    public boolean digestElement(Element el)
    {
        if (!el.getName().equalsIgnoreCase(TagName))
            return false;
        
        Target = XmlParser.getElementAttributeValue(el, "target");

        Iterator xter = el.getContent().iterator();
        Text = new StringBuffer();
        while (xter.hasNext())
        {
            Object ox = xter.next();
            if (ox instanceof Text)
            {
                Text txk = (Text)ox;
                String sk = txk.getTextNormalize();
                Text.append(sk);
            }
        }
        
        return true;
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
    
    /**
     * Gets the Target.
     * 
     * @return the Target as String
     */
    public String getTarget()
    {
        return Target;
    }
    
    /**
     * Gets the Text.
     * 
     * @return the Text as StringBuffer
     */
    public StringBuffer getText()
    {
        return Text;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuilder xbuf = new StringBuilder();
        xbuf.append("<message target='")
            .append(Target)
            .append("'>")
            .append(Text)
            .append("</message>");
        
        return xbuf.toString();
    }
    
    /** Variable Target. */
    protected String Target;
    
    /** Variable Text. */
    protected StringBuffer Text;
    
    /** Variable XmlElement. */
    protected Element XmlElement;
    
    /** Variable TagName. */
    public final String TagName = "message";
    
    /** Variable INFO$Type. */
    public final char INFO$Type = 0;
    
    /** Variable ERROR$Type. */
    public final char ERROR$Type = 1;
    
    /** Variable INFO$Type$Value. */
    public final String INFO$Type$Value = "INFO";
    
    /** Variable ERROR$Type$Value. */
    public final String ERROR$Type$Value = "ERROR";
    
}
