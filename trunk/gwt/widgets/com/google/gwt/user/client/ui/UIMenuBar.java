package com.google.gwt.user.client.ui;


public class UIMenuBar
	extends MenuBar
{

	public UIMenuBar() {}

	public UIMenuBar(boolean vertical) {
		super(vertical);
	}

	public UIMenuBar(Resources resources) {
		super(resources);
	}

	public UIMenuBar(boolean vertical, Resources resources) {
		super(vertical, resources);
	}

	public MenuItem addItem(MenuItem item) {
		if (item.getText().equals("!")){
			super.addSeparator();
			return null;
		}
			
		return super.addItem(item);
	}
}
