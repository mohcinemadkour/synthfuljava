package org.synthful.gwt.domElements.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class JavascriptWidget
    extends Widget
{
    public JavascriptWidget(String code, boolean isSrc)
    {
        Element jse;
        
        if (isSrc)
        {
            jse = DomUtils.createJavascriptSrcElement(code);
            DomUtils.getDocumentElement().appendChild(jse);
        }
        else
            jse = DomUtils.createJavascriptCodeElement(code);
        
        this.setElement(jse);
    }
}
