package org.synthful.gwt.tabinator.client.ui;

import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

abstract public class HtmlTabsWithFrame
    extends BaseHtmlTabPanel
{
    public HtmlTabsWithFrame()
    {
    }
    
    public void onMouseDown(
        final Widget sender, final int x, final int y)
    {
        this.TabOnMouseDown((HTMLTab)sender);
    }

    public void TabOnMouseDown(HTMLTab tab)
    {
        if(tab.Mode!=null && tab.Mode.equals("replace"))
        {
            Location.assign(tab.Location);
            return;
        }
        
        this.ActiveTab = tab;
        this.ContentFrame.setUrl(tab.Location);
        for(int i=0;i<this.TabBar.getWidgetCount();i++)
        {
            Widget wid = this.TabBar.getWidget(i);
            if (wid!=tab && wid instanceof HTMLTab)
            {
                wid.setStyleName(this.PanelStyles.Up);
            }
        }
        this.setStyleName(null);
        this.ContentFrame.setStyleName("tabPanel");
        this.setHeight(tab.ContentHeight+"px");
        tab.setStyleName("tabPanel");
    }

    public void addHtmlTab(HTMLTab htmlTab)
    {
        this.TabBar.add(htmlTab);
        htmlTab.setStyleName(this.PanelStyles.Up);
        htmlTab.TabPanel = this;
        htmlTab.addMouseListener(this);
    }

    public void addHtmlTab(HTMLTab[] htmlTabs)
    {
        for(int i=0; i<htmlTabs.length; i++)
            addHtmlTab(htmlTabs[i]);
    }
    
    public Frame ContentFrame;
}
