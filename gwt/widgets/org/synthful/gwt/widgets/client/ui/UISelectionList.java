package org.synthful.gwt.widgets.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

public class UISelectionList
extends ListBox
implements HasText, HasValue<String> {

	public UISelectionList() {
	}

	public UISelectionList(boolean isMultipleSelect) {
		super(isMultipleSelect);
	}

	public UISelectionList(Element element) {
		super(element);
	}

	@Override
	public String getText() {
		return super.getItemText(super.getSelectedIndex());
	}

	@Override
	public void setText(String text) {
		super.setItemText(super.getSelectedIndex(), text);
	}

	@Override
	public String getValue() {
		return super.getValue(super.getSelectedIndex());
	}

	@Override
	public void setValue(String value) {
		super.setValue(super.getSelectedIndex(), value);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		super.setValue(super.getSelectedIndex(), value);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return null;
	}

}
