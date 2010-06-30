package org.synthful.gwt.javascript.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class DomUtils
{
    /**
     * createJavascriptxxxElement methods are preferred as they use GWT API
     * and return the Element which can be setElement in a Widget. The Widget
     * can then be positioned in a GWT panel.
     * 
     * Note: Instantiate a new Widget and setElement on that widget. A widget
     * that has no Element set in it cannot be used.
     * 
     * @param src
     * @return
     */
    final public static ScriptElement createJavascriptSrcElement(String src)
    {
        ScriptElement script = Document.get().createScriptElement();
        script.setSrc(src);
        script.setType("text/javascript");
        return script;
    }
    
    /**
     * createJavascriptxxxElement methods are preferred as they use GWT API
     * and return the Element which can be setElement in a Widget. The Widget
     * can then be positioned in a GWT panel.
     * 
     * Note: Instantiate a new Widget and setElement on that widget. A widget
     * that has no Element set in it cannot be used.
     * 
     * @param code
     * @return
     */
    final public static ScriptElement createJavascriptCodeElement(String code)
    {
        ScriptElement script = Document.get().createScriptElement();
        script.setText(code);
        script.setType("text/javascript");
        return script;
    }


    /**
     * DomAddScript methods do the same as createJavascriptxxxElement
     * methods by using GWT DOM APIs rather than GWT Document APIs
     * 
     * Note: Instantiate a new Widget and setElement on that widget. A widget
     * that has no Element set in it cannot be used.
     * 
     * @param url
     * @return
     */
    final public static Element DOMaddScript(String url)
    {
        Element e = DOM.createElement("script");
        DOM.setElementProperty(e, "language", "JavaScript");
        DOM.setElementProperty(e, "src", url);
        DOM.appendChild(RootPanel.get().getElement(), e);
        return e;
    }
    
    /**
     * DomAddScript methods do the same as createJavascriptxxxElement
     * methods by using GWT DOM APIs rather than GWT Document APIs
     * 
     * Note: Instantiate a new Widget and setElement on that widget. A widget
     * that has no Element set in it cannot be used.
     * 
     * @param url
     * @return
     */
    final public static Element DOMaddScriptCode(String code)
    {
        Element e = DOM.createElement("script");
        DOM.setElementProperty(e, "language", "JavaScript");
        DOM.setInnerText(e, code);
        DOM.appendChild(RootPanel.get().getElement(), e);
        return e;
    }

    /**
     * The jsAddScript methods are less preferably used as they launch
     * into javascript "native" mode and do not return an Element.
     * @param url
     */
    final public static native void jsAddScript(String url)
    /*-{
    var scr = document.createElement("script");
    scr.setAttribute("language", "JavaScript");
    scr.setAttribute("src", url);
    document.getElementsByTagName("body")[0].appendChild(scr);
    }-*/;

    /**
     * The jsAddScript methods are less preferably used as they launch
     * into javascript "native" mode and do not return an Element.
     * @param url
     */
    final public static native void jsAddScriptCode(String code)
    /*-{
    var scr = document.createElement("script");
    scr.setAttribute("language", "JavaScript");
    scr.Text = code;
    document.getElementsByTagName("body")[0].appendChild(scr);
    }-*/;
    
    
    /**
     * Hey! How about a good'ol javascript alert!!!
     * @param msg
     */
    public static native void alert(String msg)
    /*-{$wnd.alert(msg);
     }-*/;

    public static native Node getDocumentElement()
    /*-{
    return $doc.documentElement;
    }-*/;
}
