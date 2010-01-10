package org.synthful.gwt.vaadin.ui.FormFieldProperties;

import java.util.Date;

import org.synthful.lang.Empty;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;

abstract public class FormFieldProperty
	extends ObjectProperty
{
	private static final long serialVersionUID = 1L;

	public FormFieldProperty(Object value)
	{
		super(value);
	}
	public FormFieldProperty(Object value, Class type)
	{
		super(value, type);
	}

	public FormFieldProperty(Object value, Class type, boolean readOnlyOnly)
	{
		super(value, type, readOnlyOnly);
		this.readOnlyOnly = readOnlyOnly;
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
		
		if (this.height!=null)
			f.setHeight(this.height);

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
		Object o = super.getValue();
		return (o==null)?this.defaultValue: o;
	}
	
	abstract public Field createField(
		Object propertyId);
		
	public void setModified(
		boolean modified)
	{
		this.modified = modified;
	}
	public boolean isModified()
	{
		return modified;
	}

	public
		String id,
		caption=Empty.Blank,
		width="10em",
		height=null;
	public boolean
		readOnlyOnly,
		required=false,
		visible=true;
	
	public Object defaultValue = null;
	
	private ValueChangeListener whenValueChanges =
		new ValueChangeListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(
				ValueChangeEvent event)
			{
				setModified(true);
			}
		};
		
	private boolean modified = false;
	
}
