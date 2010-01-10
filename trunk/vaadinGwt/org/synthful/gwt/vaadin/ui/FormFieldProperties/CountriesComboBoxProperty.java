package org.synthful.gwt.vaadin.ui.FormFieldProperties;

import org.synthful.gwt.vaadin.ui.widgets.CountriesComboBox;
import org.synthful.lang.Empty;

import com.vaadin.ui.Field;

public class CountriesComboBoxProperty
	extends FormFieldProperty
{
	private static final long serialVersionUID = 1L;

	public CountriesComboBoxProperty(
		String id,
		String caption, String width,
		boolean required, boolean visible, boolean readOnly)
	{
		super(Empty.Blank, String.class, readOnly);
		this.id = id;
		this.caption = caption;
		this.width = width;
		this.required = required;
		this.visible = visible;
	}

	@Override
	public Field createField(
		Object propertyId)
	{
		return new CountriesComboBox(
			this.caption, this.width);
	}
}
