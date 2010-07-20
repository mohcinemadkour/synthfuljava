package org.synthful.gwt.widgets.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class UITab
extends SimplePanel
//implements HasWidgets
{
	private Element inner;

	public UITab(){}
	
	public UITab(Widget child) {
		this.add(child);
	}
	
	@Override
	public void add(Widget child){
		if (child instanceof UITabHeader){
			
			return;
		}
		super.setElement(Document.get().createDivElement());
		getElement().appendChild(inner = Document.get().createDivElement());

		setWidget(child);
		setStyleName(UITabLayoutPanel.TAB_STYLE);
		inner.setClassName(UITabLayoutPanel.TAB_INNER_STYLE);

		// TODO: float:left may not be enough. If there are tabs of differing
		// heights, the shorter ones will top-align, rather than bottom-align,
		// which is what we would want. display:inline-block fixes this, but
		// needs lots of cross-browser hacks to work properly.
		getElement().getStyle().setFloat(Style.Float.LEFT);		
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	public void setSelected(boolean selected) {
		if (selected) {
			addStyleDependentName("selected");
		} else {
			removeStyleDependentName("selected");
		}
	}

	@Override
	protected com.google.gwt.user.client.Element getContainerElement() {
		return inner.cast();
	}

}
