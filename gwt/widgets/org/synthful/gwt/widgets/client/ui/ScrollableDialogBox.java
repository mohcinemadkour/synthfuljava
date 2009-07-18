package org.synthful.gwt.widgets.client.ui;

import java.util.ArrayList;

import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Blessed Geek
 *
 */
public class ScrollableDialogBox
    extends DialogBox
{
    public ScrollableDialogBox()
    {
        this(false);
    }

    public ScrollableDialogBox(
        boolean autoHide)
    {
        this(autoHide, true);
    }

    public ScrollableDialogBox(
        boolean autoHide, boolean modal)
    {
        super(autoHide, modal);

        Element td01 = getCellElement(0, 1);
        Widget caption = (Widget)this.getCaption();
        DOM.removeChild(td01, caption.getElement());
        DOM.appendChild(td01, this.CaptionPanel.getElement());
        adopt(this.CaptionPanel);
        
        this.CaptionTitle = new CaptionTitle();
        CaptionPanel.add(this.CaptionTitle);
        CaptionPanel.add(this.closer);
        super.setWidget(this.BodyPanel);
        this.setWidthPx(200);
        
        this.CloserEventHandlers.add(new CloserHandler());

        this.CaptionPanel.setStyleName("Caption");
        DOM.setStyleAttribute(closer.getElement(), "color", "black");
    }
    
    public void setAlwaysShowScrollBars(
        boolean show)
    {
        this.BodyPanel.setAlwaysShowScrollBars(show);
    }

    public void setHeightPx(
        int hgt)
    {
        this.BodyPanel.setWidth(hgt + "px");
        this.Height = hgt;
    }

    public void setWidthPx(
        int wid)
    {
        this.BodyPanel.setWidth(wid + "px");
        this.initCaptionWidths(wid);
    }

    public void setSizePx(
        int wid, int hgt)
    {
        this.BodyPanel.setSize(wid + "px", hgt + "px");
        this.Height = hgt;
        this.initCaptionWidths(wid);
    }

    @Override
    public void setWidget(
        Widget widget)
    {
        this.BodyPanel.setWidget(widget);
    }

    @Override
    public Widget getWidget()
    {
        return this.BodyPanel.getWidget();
    }

    private void initCaptionWidths(
        int wid)
    {
        this.Width = wid;
        this.CaptionPanel.setWidth(this.Width + "px");
    }

    public final Caption getCaptionTitle()
    {
        return this.CaptionTitle;
    }

    public String getHTML()
    {
        return this.CaptionTitle.getHTML();
    }

    public String getText()
    {
        return this.CaptionTitle.getText();
    }

    public void setHTML(
        String html)
    {
        this.CaptionTitle.setHTML(html);
    }

    public void setText(
        String text)
    {
        this.CaptionTitle.setText(text);
    }

    protected boolean isCaptionControlEvent(
        NativeEvent event)
    {
        return isWidgetEvent(event, this.CaptionPanel.getWidget(1));
    }

    static protected boolean isWidgetEvent(
        NativeEvent event, Widget w)
    {
        EventTarget target = event.getEventTarget();
        if (Element.is(target))
        {
            boolean t = w.getElement().isOrHasChild(Element.as(target));
            // if (t)
            // System.out.println("isWidgetEvent:"+w+':'+target+':'+t);
            return t;
        }
        return false;
    }

    @Override
    public void onBrowserEvent(Event event)
    {
        if (isCaptionControlEvent(event))
        {
            for(CloserEventHandler handler: this.CloserEventHandlers)
            {
                switch (event.getTypeInt())
                {
                    case Event.ONMOUSEUP:
                    case Event.ONCLICK:
                        handler.onClick(event);
                        break;
                    case Event.ONMOUSEOVER:
                        handler.onMouseOver(event);
                        break;
                    case Event.ONMOUSEOUT:
                        handler.onMouseOut(event);
                        break;
                }
            }    
            return;
        }
        
        super.onBrowserEvent(event);
    }
    
    // The events are not firing normally because DialogBox has over-ridden
    // onBrowserEvent with some obtuse logic.
    // So we have to create our own tiny system of event handling for the closer button
    // in conjunction with List<AntiObtusedCloserHandler>
    public interface CloserEventHandler
    {
        public void onClick(Event event);
        public void onMouseOver(Event event);
        public void onMouseOut(Event event);
    }
    /*
     *     
     * @gwt.TypeArgs parameters <java.util.ArrayList<org.synthful.gwt.widgets.client.ui.ScrollableDialogBox.CloserEventHandler>>
     */
    final public ArrayList<CloserEventHandler> CloserEventHandlers =
        new ArrayList<CloserEventHandler>();
    
    private class CloserHandler
    implements CloserEventHandler
    {

        public void onClick(Event event)
        {
            hide();
            DOM.setStyleAttribute(closer.getElement(), "color", "black");
        }

        public void onMouseOver(Event event)
        {
            DOM.setStyleAttribute(closer.getElement(), "color", "red");
        }

        public void onMouseOut(Event event)
        {
            DOM.setStyleAttribute(closer.getElement(), "color", "black");
        }
    }

    protected class CaptionTitle
    extends HTML
    implements Caption
    {
    }

    final public HorizontalPanel CaptionPanel = new HorizontalPanel();
    final private Button closer = new Button("X");
    final private ScrollPanel BodyPanel = new ScrollPanel();
    public int Width, Height;
    protected CaptionTitle CaptionTitle;

}
