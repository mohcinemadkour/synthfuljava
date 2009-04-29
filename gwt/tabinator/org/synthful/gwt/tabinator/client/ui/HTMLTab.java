package org.synthful.gwt.tabinator.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class HTMLTab
    extends HTML
{

    public HTMLTab()
    {
    }

    public HTMLTab(
        String titleHtml, String location, int contentHeight)
    {
        super(titleHtml);
        this.Location = location;
        this.ContentHeight = contentHeight;
    }

    public HTMLTab(
        String titleHtml, String location)
    {
        super(titleHtml);
        this.Location = location;
    }

    public HTMLTab(
        Element titleElement, String location)
    {
        super(titleElement);
        this.Location = location;
    }

    public HTMLTab(
        String titleHtml, String location, boolean wordWrap)
    {
        super(titleHtml, wordWrap);
        this.Location = location;
    }

    
    public HTMLTab(String titleHtml)
    {
        super(titleHtml);
    }

    public HTMLTab(
        Element titleElement)
    {
        super(titleElement);
    }

    public HTMLTab(
        String titleHtml, boolean wordWrap)
    {
        super(titleHtml, wordWrap);
    }
    
    public BaseHtmlTabPanel TabPanel;
    public String Location;
    public Composite Content;
    public int ContentHeight = 500;
    public String Mode;
}
