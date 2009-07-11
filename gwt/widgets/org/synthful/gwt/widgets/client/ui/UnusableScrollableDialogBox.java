package org.synthful.gwt.widgets.client.ui;

import java.util.ArrayList;

import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class is an attempt to patch DialogBox to give it a close
 * button at the right corner of the caption.<br>
 * 
 * This class is unusable as explained in comments in the constructor
 * below. It is retained as an illustration why we have to use the
 * patch rather than subclass DialogBox.
 * 
 * The patch is found as
 * com.google.gwt.user.client.ui.ScrolledDialogBox .
 * 
 * It has to be patched rather than over-riden because all the
 * essential innards required to give a viable close button are
 * either private or protected - as evidenced in the explanation
 * in the constructor below.
 * 
 * @author Icecream
 *
 */
public class UnusableScrollableDialogBox
    extends DialogBox
{
    public UnusableScrollableDialogBox()
    {
        this(false);
    }

    public UnusableScrollableDialogBox(
        boolean autoHide)
    {
        this(autoHide, true);
    }

    public UnusableScrollableDialogBox(
        boolean autoHide, boolean modal)
    {
        super(autoHide, modal);

        Element td01 = getCellElement(0, 1);
        
        // We should reuse the caption so that getCaption, getText and getHtml
        // methods do not need to be rewritten and over-riden.
        Widget caption = (Widget)this.getCaption();
        
        // The following fails because removeFromParent requires
        // parent's private widget = caption, but caption was adopted not added
        // so the parent did not register it as a child widget.
        caption.removeFromParent(); 
        Widget p = caption.getParent();
        DOM.removeChild(td01, caption.getElement());
        DOM.appendChild(td01, this.CaptionPanel.getElement());
        
        // The following fails because add() function requires prospective child
        // widget to have null parent, but earlier parent's failure to acknowledge
        // caption as a child aborted the parent removal procedure resulting
        // in the child still acknowledging the parent.
        CaptionPanel.add(caption);
        adopt(this.CaptionPanel);
                
        CaptionPanel.add(this.closer);
        super.setWidget(this.BodyPanel);
        this.setWidthPx(200);

        this.CloserEventHandlers.add(new CloserHandler());

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

    final public HorizontalPanel CaptionPanel = new HorizontalPanel();
    final private Button closer = new Button("X");
    final private ScrollPanel BodyPanel = new ScrollPanel();
    public int Width, Height;

}
