package org.synthful.gwt.tabinator.client.ui;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HorizontalHtmlTabsWithFrame
    extends HtmlTabsWithFrame
{

    public HorizontalHtmlTabsWithFrame()
    {
        this.Container = new VerticalPanel();
        initWidget(Container);
        
        this.TabBar = new HorizontalPanel();
        //this.TabBar.setWidth("150px");

        this.Container.add(TabBar);
        this.ContentFrame = new Frame("about:blank");
        this.Container.add(ContentFrame);
        this.ContentFrame.setSize("100%", "100%");
        
        //this.TabContainer.setCellHeight(this.TabBar, "50px");
        this.Container.setCellVerticalAlignment(this.ContentFrame,
            VerticalPanel.ALIGN_TOP);
    }
}
