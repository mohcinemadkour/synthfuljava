package org.synthful.gwt.tabinator.client.ui;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HorizontalHtmlTabsWithPanel
    extends HtmlTabsWithPanel
{

    public HorizontalHtmlTabsWithPanel()
    {
        this.Container = new VerticalPanel();
    	this.Container.setStyleName(this.PanelStyles.Container);
        initWidget(Container);
        
        this.TabBar = new HorizontalPanel();
        //this.TabBar.setHeight("2em");
        

        this.Container.add(TabBar);
        this.Container.add(Content);
        //this.Container.setBorderWidth(1);
    }
}
