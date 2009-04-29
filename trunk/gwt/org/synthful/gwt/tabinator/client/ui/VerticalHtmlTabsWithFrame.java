package org.synthful.gwt.tabinator.client.ui;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VerticalHtmlTabsWithFrame
    extends HtmlTabsWithFrame
{

    public VerticalHtmlTabsWithFrame()
    {

        this.Container = new HorizontalPanel();
        initWidget(Container);

        this.TabBar = new VerticalPanel();
        this.TabBar.setWidth("150px");

        this.Container.add(TabBar);
        this.ContentFrame = new Frame("about:blank");
        this.Container.add(ContentFrame);
        this.ContentFrame.setSize("100%", "100%");
        
        this.Container.setCellWidth(this.TabBar, "150px");
        this.Container.setCellHorizontalAlignment(
            this.ContentFrame, HorizontalPanel.ALIGN_LEFT);
    }
}
