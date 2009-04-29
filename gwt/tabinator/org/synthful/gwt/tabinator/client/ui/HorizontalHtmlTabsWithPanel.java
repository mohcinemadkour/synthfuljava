package org.synthful.gwt.tabinator.client.ui;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HorizontalHtmlTabsWithPanel
    extends HtmlTabsWithPanel
{

    public HorizontalHtmlTabsWithPanel()
    {
        this.Container = new VerticalPanel();
        initWidget(Container);
        
        this.TabBar = new HorizontalPanel();
        //this.TabBar.setWidth("150px");

        this.Container.add(TabBar);
    }
}
