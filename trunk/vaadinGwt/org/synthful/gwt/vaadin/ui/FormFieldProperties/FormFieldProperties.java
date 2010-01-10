package org.synthful.gwt.vaadin.ui.FormFieldProperties;

import java.util.ArrayList;
import java.util.Collection;


import com.vaadin.data.Property;
import com.vaadin.data.util.PropertysetItem;

public class FormFieldProperties 
	extends PropertysetItem
{
	private static final long serialVersionUID = 1L;


	public FormFieldProperties(){}

	public FormFieldProperties(FormFieldProperty[] propertyList)
	{
		this.addProperties(propertyList);
	}

	
	public void addProperties(
		FormFieldProperty[] propertyList)
	{
		for(int i=0; i < propertyList.length; i++)
		{			
			this.addItemProperty(
				propertyList[i].id,
				propertyList[i]);
		}
	}
	
	public FormFieldProperty getFormFieldProperty(String id)
	{
		Property p = this.getItemProperty(id);
		if (FormFieldProperty.class.isAssignableFrom(p.getClass()))
			return (FormFieldProperty)p;
		return null;
	}
	
	public StringBuffer verifyProperties()
	{
		return verifyProperties(this);
	}
	
	static public ArrayList<String> listVisible(
		FormFieldProperty[] propertyList)
	{
		ArrayList<String> fdlist =
			new ArrayList<String>(1 + propertyList.length/2);
		
		for(int i=0; i< propertyList.length; i++)
			if (propertyList[i].visible)
				fdlist.add(propertyList[i].id);
		
		return fdlist;
	}
	
	static public ArrayList<FormFieldProperty> listRequired(
		FormFieldProperty[] propertyList)
	{
		ArrayList<FormFieldProperty> fdlist =
			new ArrayList<FormFieldProperty>(1 + propertyList.length/2);
		
		for(int i=0; i< propertyList.length; i++)
			if (propertyList[i].required)
				fdlist.add(propertyList[i]);
		
		return fdlist;
	}


	static public StringBuffer verifyProperties(
		FormFieldProperties propertySet)
	{
		Collection<String> props = propertySet.getItemPropertyIds();

		StringBuffer verbuf = new StringBuffer(props.size() * 15);

		for (String id : props)
		{
			verbuf.append(id).append(": ")
			    .append(
			    	propertySet.getFormFieldProperty(id))
			    .append("<br/>");
		}
		
		return verbuf;
	}


}
