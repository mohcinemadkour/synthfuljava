package com.blessedgeek.sitebuilder.client;

import org.synthful.gwt.jsObjects.client.JsObjectArray;
import org.synthful.gwt.json.client.JsonHttpResponseHandler;
import org.synthful.gwt.json.client.JsonRemoteScriptCall;
import org.synthful.gwt.tabinator.client.ui.HorizontalHtmlTabsWithPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SiteBuilder
    implements EntryPoint
{

    public void onModuleLoad()
    {
        final RootPanel rootPanel = RootPanel.get();
        rootPanel.setSize("100%", "100%");

        // final TabPanel tabPanel = new TabPanel();
        final HorizontalHtmlTabsWithPanel tabPanel =
            new HorizontalHtmlTabsWithPanel();
        tabPanel.alignTabBarHorizontally(HorizontalPanel.ALIGN_RIGHT);
        rootPanel.add(tabPanel, 2, 95);
        tabPanel.setSize("98%", "95%");
        tabPanel.PanelStyles = JsonSiteMap.htabStyle;

        // generate htabs using data read in
        // from <script> tag in html file
        
        final JsonRemoteScriptCall SiteTabsJRSC =
            new JsonRemoteScriptCall()
        {
            public void onJsonRSCResponse( JavaScriptObject json )
            {
                final JsObjectArray siteMapArray = json.cast();
                
                this.SiteMap =
                    new JsonSiteMap(siteMapArray, tabPanel);
                
                final HTML header = new HTML(siteMapArray.get("header"));
                rootPanel.add(header, 229, 15);
                header.setSize("256px", "45px");
        
                final Image logo = new Image();
                rootPanel.add(logo, 0, 0);
                logo.setSize("222px", "112px");
                final JsObjectArray logoUrls = siteMapArray.getJsArray("logoUrl");
                logo.setUrl(logoUrls.get("onMouseOver"));
                
                cfgLogoMouseEvents(logo, logoUrls);
                
                // instantiate-extend JsonHttpResponseHandler to async read data file
                // and provide over-ride to handle the response data,
                // which in this case is to insert the last htab
                final JsonHttpResponseHandler auxTabJson =
                    new JsonHttpResponseHandler("/Geeknology.json")
                {
                    protected void onJsonResponse(
                        JsonHttpResponseHandler jsonHttpResponseHandler)
                    {
                        JsObjectArray auxtab =
                            jsonHttpResponseHandler.JsonData.getJavaScriptObject().cast();
                        SiteMap.doHTab(tabPanel, auxtab);                    
                    }
                    
                };
                
                tabPanel.selectTab(0);
            }
            
            void cfgLogoMouseEvents(final Image logo, final JsObjectArray logoUrls)
            {
                logo.addMouseListener(new MouseListenerAdapter()
                {
                    public void onMouseEnter(
                        final Widget sender)
                    {
                        logo.setUrl(logoUrls.get("onMouseOver"));
                    }

                    public void onMouseLeave(
                        final Widget sender)
                    {
                        logo.setUrl(logoUrls.get("onMouseOut"));
                    }
                });
                
            }
            
            JsonSiteMap SiteMap;
        };
        
        SiteTabsJRSC.call(JsonSiteMap.getSiteMapUrl(), "generateSite");
        
        /*
        String googleAdCfg =
            "google_ad_client = 'pub-9964601845928512';google_ad_slot = '8376418493';google_ad_width = 234;google_ad_height = 60;";
        String googleAdSrc =
            "/dummy.js";
        JavascriptWidget googleAdWidg = new JavascriptWidget(googleAdCfg, false);
        rootPanel.add(googleAdWidg, 500,5);
        
        JavascriptWidget googleAdSrcWidg = new JavascriptWidget(googleAdSrc, true);
        rootPanel.add(googleAdSrcWidg, 500,5);
        */
    }
    
}
