package org.synthful.gwt.jsObjects.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;

public class JsObjectArray
    extends JsArray<JavaScriptObject>
{
    
    protected JsObjectArray(){}
    
    final public native JsArray<JsObjectArray> getJsObjArray(int i)
    /*-{return this[i];
    }-*/;

    final public native JsArray<JsObjectArray> getJsObjArray(String key)
    /*-{return this[key];
    }-*/;

    final public native JsObjectArray getJsArray(String key)
    /*-{return this[key];
    }-*/;

    final public native JavaScriptObject getObject(String key)
    /*-{return this[key];
    }-*/;
    
    final public native String get(String key)
    /*-{return this[key];
    }-*/;
    
    final static public String[] toStringArray(JsArrayString jsastring)
    {
        String[] values = new String[jsastring.length()];
        for (int i=0; i<jsastring.length(); i++)
        {
            values[i] = jsastring.get(i).toString();
        }
        
        return values;
    }

}
