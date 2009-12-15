package org.synthful.gwt.vaadin.ui;

import org.synthful.gwt.vaadin.ui.widgets.ItemisedComboBox;
import org.synthful.lang.Empty;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

@SuppressWarnings("serial")
public class ComboBoxFieldProperty
	extends FormFieldProperty
{
	public ComboBoxFieldProperty(
		String id,
		String caption,
		boolean required, boolean visible, boolean readOnly,
		SelectableItems items)

	{
		super(Empty.Blank, String.class, readOnly);
		this.id = id;
		this.caption = caption;
		this.required = required;
		this.visible = visible;
		this.selectableItems = items;
	}

	@Override
	public Field createField(
		Object propertyId)
	{
		Field f = new ItemisedComboBox(this.caption, this.selectableItems);
		f.setCaption(this.caption);
		return f;
	}
	
	SelectableItems selectableItems;
}
