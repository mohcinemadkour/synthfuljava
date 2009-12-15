package org.synthful.gwt.vaadin.ui;

import org.synthful.lang.Empty;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class FormTextFieldProperty
	extends FormFieldProperty
{

	public FormTextFieldProperty(Object value)
	{
		super(value);
	}

	public FormTextFieldProperty(
		String id, Object value, 
		String caption, String width,
		boolean visible)

	{
		super(value, String.class, true);
		this.id = id;
		this.caption = caption;
		this.width = width;
		this.visible = visible;
	}

	public FormTextFieldProperty(
		String id,
		String caption, String width,
		int minLength, int maxLength,
		boolean required, boolean visible, boolean readOnly)

	{
		super(Empty.Blank, String.class, readOnly);
		this.id = id;
		this.caption = caption;
		this.width = width;
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.required = required;
		this.visible = visible;
	}

	public Field createField(
		Object propertyId)
	{
        Field f = DefaultFieldFactory.createFieldByPropertyType(this.getType());
        //TextField f = new TextField();
        
		StringLengthValidator validator =
			new StringLengthValidator(
				this.caption + " must be " +
					this.minLength + " - " +
					this.maxLength + " characters",
				this.minLength, this.maxLength, false);
		
		f.addValidator(validator);

		return f;
	}
	
	public int
		minLength = 0,
		maxLength = 10;
}
