package com.blessedgeek.gwt.examples.tabination.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Tabination
	extends Composite
{

	private static TabinationUiBinder uiBinder = GWT.create(TabinationUiBinder.class);

	interface TabinationUiBinder
		extends UiBinder<Widget, Tabination>
	{}

	@UiField
	Button button;

	public Tabination(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		button.setText(firstName);
	}

	@UiHandler("button")
	void onClick(ClickEvent e) {
		Window.alert("Hello!");
	}

}
