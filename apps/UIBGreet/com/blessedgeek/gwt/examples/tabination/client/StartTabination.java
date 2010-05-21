package com.blessedgeek.gwt.examples.tabination.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

public class StartTabination
	implements EntryPoint
{
	Tabination tabber = new Tabination("Hello");
	@Override
	public void onModuleLoad() {
		/*
	    TabLayoutPanel p = new TabLayoutPanel(1.5, Unit.EM);
	    p.add(new HTML("this"), "[this]");
	    p.add(new HTML("that"), "[that]");
	    p.add(new HTML("the other"), "[the other]");
	    */
		
	    RootPanel.get().add(tabber);
	}

}
