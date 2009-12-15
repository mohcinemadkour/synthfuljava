package org.synthful.gwt.vaadin.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.synthful.gwt.vaadin.persistence.PersistenceHandler;
import org.synthful.lang.Empty;

import com.vaadin.data.Property;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class FormFields 
	extends PropertysetItem
{
	public FormFields(){}

	public FormFields(FormFieldProperty[] propertyList)
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
		FormFields propertySet)
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
