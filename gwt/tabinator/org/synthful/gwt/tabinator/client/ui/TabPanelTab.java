package org.synthful.gwt.tabinator.client.ui;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class TabPanelTab
    extends Composite
    implements ClickListener
{
    private HorizontalPanel contentPane;

    private TabPanel tabPanel;

    private Widget widget;

    public TabPanelTab(
        String text, Widget widget, TabPanel tabPanel)
    {
        this.tabPanel = tabPanel;
        this.widget = widget;
        this.contentPane = new HorizontalPanel();
        this.contentPane.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        Label label = this.mkTab(text, true);
        Image closer = this.mkCloser("10px", "10px");
        this.contentPane.add(label);
        this.contentPane.add(closer);
        initWidget(contentPane);
        setStyleName("gwt-TabPanelTab");
        tabPanel.add(widget, this);
    }
    
    final private Image mkCloser(String wid, String ht)
    {
        Image closeX = new Image();
        closeX.setWidth(wid);
        closeX.setHeight(ht);
        closeX.addClickListener(this);
        closeX.setUrl("images/close.gif");
        closeX.setStylePrimaryName("gwt-TabPanel-CloseX");
        return closeX;
    }
    
    final private Label mkTab(String text, boolean wordWrap)
    {
        Label label = new Label();
        label.setWordWrap(wordWrap);
        label.setText(text);
        return label;
    }

    public void onClick(
        Widget sender)
    {
        tabPanel.remove(this.widget);
        if (tabPanel.getTabBar().getTabCount() > 0)
        {
            tabPanel.selectTab(0);
        }
    }
}
