/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2008
 * 
 */

package org.synthful.xml.pjx;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;
import org.synthful.xml.XmlParser;

import org.synthful.util.HashTreeNode;

/**
 * @author Blessed Geek
 */
public class PjxParser
    extends XmlParser
{
    
    /**
     * Instantiates a new PjxParser.
     */
    public PjxParser()
    {
        LOG.info(this + " instantiated");
    }
    
    /* (non-Javadoc)
     * @see org.synthful.xml.XmlParser#parse(java.lang.String, java.lang.String)
     */
    public void parse(String folder, String filename)
    {
        super.parse(folder, filename);
        registerXmlNodes(getDocument().getRootElement());
    }
    
    /* (non-Javadoc)
     * @see org.synthful.xml.XmlParser#parse(java.lang.String)
     */
    public void parse(String filename)
    {
        super.parse(filename);
        registerXmlNodes(getDocument().getRootElement());
    }
    
    /* (non-Javadoc)
     * @see org.synthful.xml.XmlParser#parseString(java.lang.String)
     */
    public void parseString(String s)
    {
        super.parseString(s);
        registerXmlNodes(getDocument().getRootElement());
    }
    
    /**
     * Register xml nodes.
     * 
     * @param ei
     */
    public void registerXmlNodes (Element ei)
    {
        Iterator iter = ei.getContent().iterator();
        
        while (iter.hasNext())
        {
            Object oj = iter.next();
            if (oj==null )continue;
            
            if (oj instanceof Element)
            {
                Element ej = (Element) oj;
                registerXmlNode(ej);
                
            }
            else if(oj instanceof Text)
            {
//                Vector newChildren = digestText((Text)oj);
            }
        }
        
    }


    /**
     * Register xml node.
     * 
     * @param ej
     */
    public void registerXmlNode(Element ej)
    {
        Namespace nameSpace = ej.getNamespace();        
        
        if (nameSpace.getPrefix().equals("pjx"))
        {
            String ejtag = ej.getName();
            if(ejtag.equals("XmlNode"))
            {
                Attribute name=ej.getAttribute("name");
                if (name!=null && name.getValue().length()>0)
                    XmlNodes.put(name.getValue(), ej);                
            }
            else if(ejtag.equals("Bean"))
                verifyBean(ej);
        }
    }
    
    
    /**
     * Verify bean.
     * 
     * @param ej
     */
    public void verifyBean(Element ej)
    {
        Attribute beanName=ej.getAttribute("name");
        if (beanName!=null)
        {
            Object oj = getBean("beanName");
            
            if (oj == null)
            {
                LOG.warn(beanName + " not found in Pjx Beans Registry");
                return;
            }
            
            if (oj instanceof PjxBean)
            {
                LOG.info(beanName + " found in Pjx Beans Registry");
                return;
            }
            
            if (oj instanceof PjxValues)
            {
                LOG.info(beanName + " found in Pjx Beans Registry");
                return;
            }
            
            LOG.warn(beanName + " found in Pjx Beans Registry does not implement PjxBean or PjxHash interface and will not be used to resolve Pjx variables.");
            Beans.remove("beanName");
        }
    }
    
    /* (non-Javadoc)
     * @see org.synthful.xml.XmlParser#digestAttribute(org.jdom.Element, org.jdom.Attribute)
     */
    public void digestAttribute(Element ej, Attribute attribute)
    throws java.io.IOException
    {
        Vector vj = resolveStringPjxVars(attribute.getValue());
        if (vj!=null && vj.size()>0)
        {
            StringBuilder ajbuf = new StringBuilder();
            for(int i=0; i<vj.size(); i++)
                ajbuf.append(vj.get(i));
        
            ej.setAttribute(attribute.getName(), ajbuf.toString());
        }
    }
    
    /* (non-Javadoc)
     * @see org.synthful.xml.XmlParser#digestText(org.jdom.Text)
     */
    public Vector digestText(Text tj)
    throws java.io.IOException
    {
        String sj = tj.getTextNormalize();
        
        if (sj==null || sj.length()==0)
            return null;
        
        Vector tjv = resolveStringPjxVars(sj);
        StringBuilder jbuf = new StringBuilder();
        for (int i=0; i<tjv.size(); i++)
            jbuf.append(' ').append(tjv.get(i));
        
        tj.setText(jbuf.toString());
        
        return tjv;
    }
    
    /**
     * Resolve string pjx vars.
     * 
     * @param ss
     * @return Resolve string pjx vars as Vector
     */
    public Vector resolveStringPjxVars(String ss)
    {
        Vector ammendedContent = new Vector();
        if (ss==null)
            return ammendedContent;
        Matcher matcher = pp0.matcher(ss);
        boolean matchfound = false;
        int a = 0, b = 0;
        
        while( matcher.find())
        {
            matchfound = true;
            
            int matches = matcher.groupCount();
            String matched0 = matcher.group();
            String matched2 = matcher.group(2);
            String matched3 = matcher.group(3);
            
            b = ss.indexOf(matched0, a);
            String sprior = ss.substring(a,b);
            a = b + matched0.length(); // start index of next match
            if (sprior.length()>0)
                ammendedContent.add(sprior);
                
            if(matches>=2 && matched3!=null)
            {
                Object o = resolveNamespacedVars(matched2, matched3);
                if (o instanceof Collection)
                    ammendedContent.addAll((Collection) o);
                else
                    ammendedContent.add(o);
            }
            else
                ammendedContent.add(matched0);
        }
        
        if (ss.length()>0 && !matchfound)
            ammendedContent.add(ss);
        else
        {
            String sprior = ss.substring(a);
            if (sprior.length()>0)
                ammendedContent.add(sprior);
        }
        
        return ammendedContent;
    }
    
    /**
     * Resolve namespaced vars.
     * 
     * @param func
     * @param ss
     * @return Resolve namespaced vars as Object
     */
    public Object resolveNamespacedVars(String func, String ss)
    {
        Matcher matcher = pp1.matcher(ss);        
        Vector resolvedContent = new Vector();
        
        while( matcher.find())
        {
            int matches = matcher.groupCount();
            String matched0 = matcher.group();
            String matchedNameSpace = matcher.group(1);
            String matchedFieldVar = matcher.group(4);
            if(matchedNameSpace!=null && matchedFieldVar!=null)
            {
                Object o = null;
                
                if (matchedNameSpace.equals("pjx"))
                    o = getValue(matchedFieldVar);
                else 
                    o = getBean(matchedNameSpace);
                
                if (o==null) continue;
                
                Object value = null;
                
                
                if (o instanceof PjxBean || o instanceof PjxValues)
                {
                
                    if (o instanceof PjxBean)
                    {
                        PjxBean b = (PjxBean)o;

                        /* The matched field name may be in dotted hierarchy */
                        String[] fieldPath = matchedFieldVar.split("[.]");
                        value = resolveDsfPath(b,fieldPath, 0);

                        if (value!=null)
                            appendNameSpaceVars(resolvedContent, value);
                    }

                    if (o instanceof PjxValues)
                        appendNameSpaceVars(
                            resolvedContent,
                            ((PjxValues)o).getValue(matchedFieldVar));
                }
                else 
                    appendNameSpaceVars(resolvedContent, o);
            } 
        }
            
        if (func!=null)
            if (func.equals("size"))
                return new Integer(valueSize(resolvedContent));
        
        return resolvedContent;
    }
    
    /**
     * DSF = Dot separated field 
     * 
     */
    private Object resolveDsfPath(Object bean, String[] dsfElements, int level)
    {
        try{
            Field f = bean.getClass().getField(dsfElements[level]);                    
            Object value = f.get(bean);

            if (++level<dsfElements.length)
                return resolveDsfPath(value,dsfElements, level);
            
            if (value instanceof Method)
            {   // Not working well - Need further debug
                value = ((Method)value).invoke(bean, new Object[0]);
                return value;
            }
            
            return value;
        }
        catch (Exception e){}
        return null;
    }
    
    private void appendNameSpaceVars(
        Vector ammendedContent, Object value)
    {
        if (value == null) return;
        
        if (value instanceof Collection)
        {
            ammendedContent.addAll((Collection)value);
        }
        
        else if (value instanceof Object[])
        {
            Object[] vo = (Object[])value;
            for (int i=0; i<vo.length; i++)
                ammendedContent.add(vo[i]);
        }
        
        else
            ammendedContent.add(value);        
    }
    
    /**
     * Value size.
     * 
     * @param values
     * @return Value size as int
     */
    protected int valueSize(Object values)
    {
        if (values==null) return 0;
        
        if (values instanceof Object[])
            return ((Object[]) values).length;
        
        if (values instanceof Collection)
            return ((Collection)values).size();
        
        return values.toString().length();
    }
    
    /**
     * Notuseyet_resolve pjx paths.
     * 
     * @param ammendedContent
     * @param ss
     * @throws IOException
     */
    public void notuseyet_resolvePjxPaths(Vector ammendedContent, String ss)
        throws java.io.IOException
    {
        Matcher matcher = pp2.matcher(ss);        
        
        while( matcher.find())
        {
            int matches = matcher.groupCount();
            String matched0 = matcher.group();
            String matchedNameSpace = matcher.group(1);
            String matchedSubPath = matcher.group(4);
            if(matchedNameSpace!=null && matchedSubPath!=null)
            {
                Object o = getBean(matchedNameSpace);
                
                Object value = null;
                
                if (o instanceof HashTreeNode)
                {
                    HashTreeNode h = (HashTreeNode)o;
                    if (matchedSubPath.startsWith("/"))
                        value = h.get(matchedSubPath);
                }
                
                if (value == null)
                    value = matched0;
                
                if (value == null)
                    continue;
                
                if (value instanceof Collection)
                {
                    ammendedContent.addAll((Collection)value);
                    continue;
                }
                
                if (!(value instanceof Content))
                    value = new Text((String) value);
                
                ammendedContent.add(value);
            } 
        }
    }
    
    /**
     * Resolve pj xml node.
     * 
     * @param name
     * @param ignoreEmptyNodes
     * @return Resolve pj xml node as StringBuilder
     */
    public StringBuilder resolvePjXmlNode(String name, boolean ignoreEmptyNodes)
    {
        return resolveNode(getXmlNode(name), ignoreEmptyNodes);
    }
    
    /**
     * Resolve node.
     * 
     * @param ei
     * @param ignoreEmptyNodes
     * @return Resolve node as StringBuilder
     */
    public StringBuilder resolveNode (Element ei, boolean ignoreEmptyNodes)
    {
        StringBuilder sbuf = new StringBuilder();
        
        if (ei==null) return sbuf;
        
        Iterator iter = ei.getContent().iterator();
        ContentFlag lastContent = new ContentFlag();
        
        while (iter.hasNext())
        {
            Object oj = iter.next();
            resolveElement(sbuf, oj, ignoreEmptyNodes, lastContent);
            
        }
        
        // TBD: Needs further debug
        if ((lastContent.IsText) && (lastContent.HasText))
            return sbuf;

        
        if (lastContent.IsElem)
            sbuf.append("\n");
        
        return sbuf;
    }
    
    /**
     * Resolve element.
     * 
     * @param oj
     * @param ignoreEmptyNodes
     * @return Resolve element as StringBuilder
     */
    public StringBuilder resolveElement (Object oj, boolean ignoreEmptyNodes)
    {
        StringBuilder sbuf = new StringBuilder();
        
        resolveElement(
          sbuf,
          oj, ignoreEmptyNodes,
          new ContentFlag()
        );
        
        return sbuf;
    }
    
    /**
     * Resolve element.
     * 
     * @param sbuf
     * @param oj
     * @param ignoreEmptyNodes
     * @param lastContent
     */
    public void resolveElement (
      StringBuilder sbuf, Object oj,
      boolean ignoreEmptyNodes, ContentFlag lastContent)
    {
        if (oj==null )
            return;
        
        if (oj instanceof Element)
        {
            lastContent.IsText = false;
            lastContent.HasText=false;

            Element ej = (Element) oj;

            StringBuilder ebuf = resolveNodePjxVar(ej, ignoreEmptyNodes);

            if (ebuf!=null)
            {
                sbuf.append(ebuf);
                return;
            }

            ebuf = resolveNode(ej, ignoreEmptyNodes);
            StringBuilder abuf = lineariseAttributes(ej);
            //if there is more than whitespace in subelements

            boolean blankebuf = ebuf.length()==0 && ebuf.toString().replaceAll("\\s", "").length()==0;
            boolean blankabuf = abuf.length()==0 && abuf.toString().replaceAll("\\s", "").length()==0;

            if((!ignoreEmptyNodes) || (!blankebuf) || (!blankabuf) )
            {
                lastContent.IsElem = true;

                lineariseStartTag(sbuf, ej);
                sbuf
                    .append(abuf);

                if (blankebuf)
                    sbuf.append("/>\n");
                else
                    sbuf
                        .append(">")
                        .append(ebuf)
                        .append("</")
                        .append(ej.getName())
                        .append(">");
            }
        }
        else if(oj instanceof Text)
        {
            lastContent.IsText = true;

            if (lineariseText(sbuf, (Text)oj)>0)
            {
                lastContent.HasText=true;
                lastContent.IsElem = false;
            }
            else
            {
                lastContent.HasText=false;
            }
        }
    }
    

    /**
     * Resolve node pjx var.
     * 
     * @param ej
     * @param ignoreEmptyNodes
     * @return Resolve node pjx var as StringBuilder
     */
    public StringBuilder resolveNodePjxVar(Element ej, boolean ignoreEmptyNodes)
    {
        Namespace nameSpace = ej.getNamespace();        
        String ejtag = ej.getName();
        
        if (nameSpace.getPrefix().equals("pjx"))
        {
            if ( ejtag.equals("Var"))
            {
                Attribute nodeName=ej.getAttribute("node");                
                if (nodeName!=null)
                {
                    Object o = XmlNodes.get(nodeName.getValue());
                    if (o!=null && o instanceof Element)
                        return resolveNode((Element)o, ignoreEmptyNodes);
                }
            }
            else if ( ejtag.equals("Iterate"))
            {
                Attribute collectionName=ej.getAttribute("collection");

                if (collectionName!=null)
                {
                    StringBuilder sbuf = new StringBuilder();
                    Vector vj = resolveStringPjxVars(collectionName.getValue());
                    for(int k=0; k<vj.size(); k++)
                    {
                        setValue("IterationValue", vj.get(k));
                        setValue("Iteration", new Integer(k));
                        
                        Iterator iter = ej.getContent().iterator();
                        while (iter.hasNext())
                        {
                            Object ok = iter.next();
                            if (!(ok instanceof Element)) continue;
                            sbuf.append(resolveNode((Element)ej, ignoreEmptyNodes));
                        }
                    }
                    return sbuf;
                }
            }
        }
        
        return null;
    }
    
    private StringBuilder lineariseAttributes(Element ej)
    {
        StringBuilder sbuf = new StringBuilder();
        Iterator iter = ej.getAttributes().iterator();
        
        while (iter.hasNext())
        {
            Object oj = iter.next();
            if (oj!=null && oj instanceof Attribute)
            {
                Attribute aj = (Attribute) oj;
                sbuf.append(' ')
                    .append(aj.getName())
                    .append("=\"")
                    .append(lineariseAttribute(aj))
                    .append("\"")
                    ;
            }            
        }
        
        return sbuf;
    }

    /**
     * Linearise attribute.
     * 
     * @param attribute
     * @return Linearise attribute as StringBuilder
     */
    public StringBuilder lineariseAttribute(Attribute attribute)
    {
        Vector vj = resolveStringPjxVars(attribute.getValue());
        StringBuilder ajbuf = new StringBuilder();
        if (vj!=null && vj.size()>0)
            for(int i=0; i<vj.size(); i++)
                ajbuf.append(vj.get(i));
        
        return ajbuf;
    }
    
    private int lineariseText(StringBuilder sbuf, Text tj)
    {
        String sj = tj.getTextNormalize();
        int k = 0;
            
        Vector tjv = resolveStringPjxVars(sj);
        
        for (k=0; k<tjv.size(); k++)
        {
            if (k>0)
                sbuf.append(' ');
            
            sbuf.append(tjv.get(k));
        }
        
        return k;
    }

    /**
     * Register bean objects.
     * 
     * @param name
     * @param bean
     */
    public void registerBeanObjects(String name, Object bean)
    {
        if (name!=null && name.length()>0 && bean!=null)
        Beans.put(name, bean);
    }
    
    /**
     * Sets the bean.
     * 
     * @param beanName
     * @param bean
     */
    public void setBean(String beanName, Object bean)
    {
        if (beanName!=null && beanName.length()>0 && bean!=null)
            Beans.put(beanName, bean);
    }
    
    /**
     * Gets the Bean.
     * 
     * @param beanName
     * @return the Bean as Object
     */
    public Object getBean(String beanName)
    {
        if (beanName!=null && beanName.length()>0)
            return Beans.get(beanName);
        
        return null;
    }
    
    /**
     * Sets the value.
     * 
     * @param path
     * @param value
     */
    public void setValue(String path, Object value)
    {
        Values.put(verifyValuePath(path), value);
    }
    
    /**
     * Gets the Value.
     * 
     * @param path
     * @return the Value as Object
     */
    public Object getValue(String path)
    {
        return Values.get(verifyValuePath(path));
    }
    
    private String verifyValuePath(String path)
    {
        if (path==null)
            path = "root";
        else if (path.startsWith("/"))
            path = "root" + path;
        else
            path = "root/" + path;
        
        return path;
    }
    
    /**
     * Gets the XmlNode.
     * 
     * @param name
     * @return the XmlNode as Element
     */
    public Element getXmlNode(String name)
    {
        Object o = XmlNodes.get(name);
        if (o!=null && o instanceof Element)
            return (Element)o;
        
        return null;
    }
    
    /**
     * Gets the XmlNodes.
     * 
     * @return the XmlNodes as Enumeration
     */
    public Enumeration getXmlNodes()
    {
        return XmlNodes.elements();
    }
    
    /**
     * ContentFlag Class.
     */
    final static class ContentFlag
    {
        
        /** Variable IsText. */
        boolean IsText;
        
        /** Variable IsElem. */
        boolean IsElem;
        
        /** Variable HasText. */
        boolean HasText;
    }
        
    private static final Log LOG = LogFactory.getLog(PjxParser.class);
    
    /** The Constant p0. */
    static final String p0 = "(\\$([\\w][\\w0-9]*)*\\{)([\\w/][\\W\\w&&[^\\{\\}]]*)+(\\})";
    
    /** The Constant p1. */
    static final String p1 = "([\\w][\\w0-9]*)(([:])([\\w/][\\S]*)*)*";
    
    /** The Constant p2. */
    static final String p2 = "([\\w][\\w0-9]*)(([:])([\\w/][\\S&&[^\\{\\}]]*)*)*";
            
    /** The Constant pp0. */
    static final Pattern pp0 = Pattern.compile(p0);
    
    /** The Constant pp1. */
    static final Pattern pp1 = Pattern.compile(p1);
    
    /** The Constant pp2. */
    static final Pattern pp2 = Pattern.compile(p2);
    
    /** Variable Values. */
    protected final HashTreeNode Values = new HashTreeNode();
    
    /** Variable Beans. */
    protected final Hashtable Beans = new Hashtable();
    
    /** Variable XmlNodes. */
    protected final Hashtable XmlNodes = new Hashtable();
}
