package org.synthful.gwt.widgets.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.NamedFrame;
import com.google.gwt.user.client.ui.Widget;

public class UIPopup
	extends DialogBox
{

	private static PopupUiBinder uiBinder =
		GWT.create(PopupUiBinder.class);

	interface PopupUiBinder
		extends UiBinder<Widget, UIPopup>
	{}

	@UiField
	HTML captionTitle;
	@UiField
	FocusPanel contentPanel;
	@UiField
	Label closeButton;
	@UiField
	HTMLPanel captionPanel;
	
	Widget contentWidget;

	public Label getCaptionTitle() {return captionTitle; }
	public FocusPanel getContentPanel() {return contentPanel;}
	public Label getCloseButton() {return closeButton;}
	public HTMLPanel getCaptionPanel() {return captionPanel;}
	public Widget getContentWidget() {return contentWidget;}
	
	public UIPopup() {
		uiBinder.createAndBindUi(this);
		
		Element td01 = getCellElement(0, 1);
		Widget caption = (Widget) this.getCaption();
		DOM.removeChild(td01, caption.getElement());
		DOM.appendChild(td01, this.captionPanel.getElement());
		//adopt(this.closeButton);
		super.setWidget(this.contentPanel);
		this.captionPanel.setStyleName("Caption");
	}
	@UiFactory
	public UIPopup getThisBean(){
		return this;
	}
	
	@UiHandler("closeButton")
	void closeButtonClick(ClickEvent e) {
		this.hide(false);
		this.contentPanel.clear();
	}
	@UiHandler("closeButton")
	void closeButtonMouseOver(MouseOverEvent e) {
		DOM.setStyleAttribute(this.closeButton.getElement(), "color", "red");
	}
	@UiHandler("closeButton")
	void closeButtonMouseOut(MouseOutEvent e) {
		DOM.setStyleAttribute(this.closeButton.getElement(), "color", "black");
	}
	
	public void setCaption(String s){
		this.captionTitle.setText(s);
	}
	
	public void showPage(String url, String title){
		this.contentPanel.clear();
		NamedFrame frame = new NamedFrame("popupFrame");
		this.contentWidget = frame;
		frame.setSize("99%", defaultHeight);
		this.contentPanel.setWidget(this.contentWidget);
		frame.setUrl("about:blank");
		frame.setUrl(url);
		this.setCaption(title);
		this.show();
	}
	public void showWidget(Widget w, String title){
		this.contentPanel.clear();
		this.contentWidget = w;
		w.setSize("99%", defaultHeight);
		this.contentPanel.setWidget(w);
		this.setCaption(title);
		this.show();
	}

	public String getDefaultWidth() {
		return defaultWidth;
	}
	public void setDefaultWidth(String defaultWidth) {
		this.defaultWidth = defaultWidth;
	}
	public String getDefaultHeight() {
		return defaultHeight;
	}
	public void setDefaultHeight(String defaultHeight) {
		this.defaultHeight = defaultHeight;
	}

	String defaultWidth = "100px", defaultHeight = "100px";
}
