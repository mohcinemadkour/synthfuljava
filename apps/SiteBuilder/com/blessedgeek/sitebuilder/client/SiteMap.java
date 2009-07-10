package com.blessedgeek.sitebuilder.client;

import org.synthful.gwt.jsObjects.client.JsObjectArray;
import org.synthful.gwt.tabinator.client.ui.HTMLTab;
import org.synthful.gwt.tabinator.client.ui.HtmlTabsWithPanel;
import org.synthful.gwt.tabinator.client.ui.VerticalHtmlTabsWithFrame;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.HTML;

public class SiteMap
{
    public SiteMap(HtmlTabsWithPanel tabPanel)
    {
        this.Logo.onMouseOut =
            SiteMap.getLogoUrl("onMouseOut");
        this.Logo.onMouseOver =
            SiteMap.getLogoUrl("onMouseOver");
        
        this.Header = SiteMap.getHeader();
        
        doHTabs(tabPanel);
    }
    
    final static void doHTabs(HtmlTabsWithPanel tabPanel)
    {
        JsArray<JsObjectArray> hTabs = SiteMap.getHTabs();
        for (int i=0; i< hTabs.length(); i++)
        {
            JsObjectArray htab = hTabs.get(i);
            doHTab(tabPanel, htab);
        }
    }

    final static void doHTab(
        HtmlTabsWithPanel tabPanel,
        JsObjectArray htab)
    {
        String tabname = htab.get("htab");
        final VerticalHtmlTabsWithFrame vTabPanel =
            SiteMap.initHTab(tabPanel, tabname);
        doVSections(vTabPanel, htab);        
    }
    
    final static void doVSections(
        VerticalHtmlTabsWithFrame vTabPanel,
        JsObjectArray htab)
    {
        JsObjectArray vsections = htab.getJsArray("vsections");
        for (int i=0; i< vsections.length(); i++)
        {
            JavaScriptObject vsecobj = vsections.get(i);
            JsObjectArray vsection = (JsObjectArray) vsecobj.cast();
            String vsecname = vsection.get("vsection");
            SiteMap.sectionTitle(vTabPanel, vsecname);
            HTMLTab[] vtabs = doVTabs(vTabPanel,vsection);
            vTabPanel.selectTab(2);
        }
    }
    
    final static HTMLTab[] doVTabs(
        VerticalHtmlTabsWithFrame vTabPanel,
        JsObjectArray vsection)
    {
        JsObjectArray jsvtabs = vsection.getJsArray("vtabs");
        HTMLTab[] vtabs = new HTMLTab[jsvtabs.length()];
        for (int i=0; i< jsvtabs.length(); i++)
        {
            JsObjectArray vtab = jsvtabs.get(i).cast();
            if (vtab==null)
                continue;

            String tabname = vtab.get("vtab");
            String url = vtab.get("url");
            String mode = vtab.get("mode");
            vtabs[i] = new HTMLTab(tabname, url);
            vtabs[i].Mode = mode;
            try
            {
                vtabs[i].ContentHeight =
                    Integer.parseInt(vtab.get("ContentHeight"));
            }
            catch (Exception e) {}
            vTabPanel.addHtmlTab(vtabs[i]);
        }
        
        return vtabs;
    }
        

    public String Header;
    public Logo Logo = new Logo();
    public class Logo
    {
        public String onMouseOver;
        public String onMouseOut;
    }
    
    final static public VerticalHtmlTabsWithFrame initHTab(
        HtmlTabsWithPanel tabPanel, String title)
    {
        final HTMLTab htab = new HTMLTab(title);
        final VerticalHtmlTabsWithFrame VTabPanel =
            new VerticalHtmlTabsWithFrame();

        VTabPanel.PanelStyles = vtabStyle;

        tabPanel.addHtmlTab(htab, VTabPanel);

        // VTabPanel.setHeight("500");
        // VTabPanel.setStyleName("tabPanel");

        return VTabPanel;
    }

    final static public void sectionTitle(
        VerticalHtmlTabsWithFrame VTabPanel, String title)
    {
        final HTML vspacerGeeks = new HTML();
        vspacerGeeks.setHeight("50px");
        VTabPanel.addTabSpacer(vspacerGeeks);

        final HTML sectionspacerGeeks = new HTML(title);
        sectionspacerGeeks.setHeight("30px");
        sectionspacerGeeks.setStyleName(VTabPanel.PanelStyles.Spacer);
        VTabPanel.addTabSpacer(sectionspacerGeeks);
    }

    final static VTabBlessedStyles vtabStyle = new VTabBlessedStyles();

    final static HTabBlessedStyles htabStyle = new HTabBlessedStyles();
    
    public static native JsObjectArray getSiteMap()
    /*-{return ($wnd.Site);
     }-*/;

    public static native String getLogoUrl(String state)
    /*-{return ($wnd.Site.logoUrl[state]);
     }-*/;
    public static native String getTitle()
    /*-{return ($wnd.Site.title);
     }-*/;
    public static native String getHeader()
    /*-{return ($wnd.Site.header);
     }-*/;
    
    public static native String getSiteMapUrl()
    /*-{return ($wnd.SiteMap);
     }-*/;
    
    public static native JsArray<JsObjectArray> getHTabs()
    /*-{return ($wnd.Site.htabs);
     }-*/;

    public static native JsObjectArray getAuxTab()
    /*-{return ($wnd.AuxTab);
     }-*/;

}
