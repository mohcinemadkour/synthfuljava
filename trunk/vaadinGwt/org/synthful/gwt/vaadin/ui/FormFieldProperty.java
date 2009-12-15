package org.synthful.gwt.vaadin.ui;

import org.synthful.lang.Empty;

import com.vaadin.data.Validator;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;

@SuppressWarnings("serial")
abstract public class FormFieldProperty
	extends ObjectProperty
{

	public FormFieldProperty(Object value)
	{
		super(value);
	}
	public FormFieldProperty(Object value, Class type)
	{
		super(value, type);
	}

	public FormFieldProperty(Object value, Class type, boolean readOnly)
	{
		super(value, type, readOnly);
	}

	public Field createField(
		Object propertyId, Component uiContext)
	{
		if (this.caption==null)
			this.caption =
				DefaultFieldFactory.
					createCaptionByPropertyId(propertyId);

		Field f = this.createField(propertyId);
		
		if(this.required)
			f.setRequiredError("Please enter " + this.caption);
		
		f.setCaption(this.caption);
		
		f.setRequired(this.required);
		f.setWidth(this.width);

		return f;
	}
	
	public String getStringValue()
	{
		return (String)this.getValue();
	}
	
	public int getIntValue()
	{
		String v = this.getStringValue();
		try
		{
			return Integer.parseInt(v);
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	public Object getValue()
	{
		Object o = getValue();
		return (o==null)?this.defaultValue: o;
	}
	
	abstract public Field createField(
		Object propertyId);
		
	public
		String id,
		caption=Empty.Blank,
		width="10em";
	public boolean
		required=false,
		visible=true;
	
	public Object defaultValue = null;
	
}
