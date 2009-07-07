package org.synthful.gwt.widgets.client.ui;

import com.google.gwt.dom.client.FormElement;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;

public class AuthFormPanel
    extends FormPanel
{
    public AuthFormPanel(String targetFrame)
    {
        super(targetFrame);
        this.setAction("https://www.google.com/accounts/AuthSubRequest");
        this.setMethod("get");
        this.add(this.p);
        this.init();
    }
    
    public void init()
    {
        this.p.add(this.NextUrl);
        this.p.add(this.Scope);
        this.p.add(this.Secure);
        this.p.add(this.Session);
    }
    
    public void init(boolean mobile, boolean hostedDomain)
    {
        if (hostedDomain)
            this.p.add(this.hd);
        if (mobile)
            this.p.add(this.btmpl);            
    }
    
    public void setTarget(String targetFrame)
    {
        FormElement.as(this.getElement()).setTarget(targetFrame); 
    }

    final public Hidden NextUrl = new Hidden("next");
    final public Hidden Scope = new Hidden("scope");
    final public Hidden Secure = new Hidden("secure");
    final public Hidden Session = new Hidden("session");
    final public Hidden hd = new Hidden("hd");
    final public Hidden btmpl = new Hidden("btmpl");
    final private FlowPanel p = new FlowPanel();
}
