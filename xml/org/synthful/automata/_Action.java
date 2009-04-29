/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2008
 * 
 */

package org.synthful.automata;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Blessed Geek
 */
public class _Action
{
    
    /**
     * Creates a new instance of Action.
     */
    public _Action()
    {
    }
    
    /**
     * Instantiates a new _Action.
     * 
     * @param bean
     * @param methodname
     */
    public _Action(Object bean, String methodname)
    {
        Bean = bean;
        try
        {
            Method = bean.getClass().getMethod(methodname, ArgumentTypes);
        }
        catch (SecurityException ex)
        {
            ex.printStackTrace();
        }
        catch (NoSuchMethodException ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * Instantiates a new _Action.
     * 
     * @param url
     */
    public _Action(String url)
    {
        try
        {
            UrlAction = new URL(url);
        } catch (MalformedURLException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Invoke.
     * 
     * @return Invoke as Object
     */
    public Object invoke()
    {
        if (UrlAction != null)
            return invokeUrl();
        else
            return invokeMethod();       
    }
    
    /**
     * Invoke method.
     * 
     * @return Invoke method as Object
     */
    public Object invokeMethod()
    {
        Object r = null;
        try
        {
            r =  Method.invoke(Bean,Arguments);
        }
        catch (IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
        catch (IllegalAccessException ex)
        {
            ex.printStackTrace();
        }
        catch (InvocationTargetException ex)
        {
            ex.printStackTrace();
        }
        
        return r;
    }
    
    /**
     * Invoke url.
     * 
     * @return Invoke url as Object
     */
    public Object invokeUrl()
    {
        try
        {
            return UrlAction.getContent();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    /** Variable Bean. */
    Object Bean;
    
    /** Variable Method. */
    Method Method ;
    
    /** Variable Arguments. */
    Object[] Arguments = new Object[0];
    
    /** Variable ArgumentTypes. */
    Class[] ArgumentTypes = new Class[0];
    
    /** Variable Type. */
    Type Type = String.class;
    
    /** Variable UrlAction. */
    URL UrlAction;
}
