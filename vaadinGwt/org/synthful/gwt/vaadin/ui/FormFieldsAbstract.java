package org.synthful.gwt.vaadin.ui;

import java.util.Collection;

import org.synthful.gwt.vaadin.persistence.PersistenceHandler;
import org.synthful.gwt.vaadin.ui.FormFieldProperties.FormFieldProperties;
import org.synthful.gwt.vaadin.ui.FormFieldProperties.FormFieldProperty;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

abstract public class FormFieldsAbstract<T>
	extends Form
{
	private static final long serialVersionUID = 1L;
	
	protected FormFieldsAbstract(){}
	
	public void inititalise(FormFieldProperty[] formFields, AbstractLayout layout)
	{
		layout.addComponent(this);
	    this.propertySet = new FormFieldProperties(formFields);
	    this.configForm(formFields);
	    this.makeButtons();
	}
	
	abstract protected void makeButtons();
	
	protected Form configForm(FormFieldProperty[] rentalFormFields)
	{
		this.setWriteThrough(false); // we want explicit 'apply'
		this.setInvalidCommitted(false); // no invalid values in datamodel

		// FieldFactory for customizing the fields and adding validators
		this.setFormFieldFactory(new ItemFieldsFactory());
		this.setItemDataSource(propertySet);
		
		// Determines which properties are shown, and in which order:
		this.setVisibleItemProperties(
			FormFieldProperties.listVisible(rentalFormFields));
		
		return this;
	}
	
	protected void verifyProperties(
		FormFieldProperties propertySet)
	{
		Window.Notification n =
			new Window.Notification("Verify information",
				Window.Notification.TYPE_TRAY_NOTIFICATION);

		n.setPosition(Window.Notification.POSITION_CENTERED);
		StringBuffer verbuf = propertySet.verifyProperties();

		n.setDescription(verbuf.toString());
		getWindow().showNotification(n);
	}

	/**
	 * Sets all the FormFieldProperty fields of the form to read only or not.
	 * FormFieldProperty fields whose readOnlyOnly is true will not be changed.
	 * Allows a form to function in two modes: input mode and info mode.
	 * 
	 * @param readOnly
	 */
	public void setFieldsReadOnly(boolean readOnly)
	{
		Collection ids = this.propertySet.getItemPropertyIds();
		for(Object id:ids)
		{
			FormFieldProperty p = this.propertySet.getFormFieldProperty((String)id);
			if (p==null || p.readOnlyOnly)
				continue;
			Field f = this.form.getField(id);
			f.setReadOnly(readOnly);						
		}
		requestRepaint();
	}

	static private class ItemFieldsFactory
		extends DefaultFieldFactory
	{
		@Override
		public Field createField(
			Item item, Object propertyId, Component uiContext)
		{
			Property p = item.getItemProperty(propertyId);

			if (p instanceof FormFieldProperty)
			{
				return ((FormFieldProperty)p).createField(propertyId, uiContext);
			}
			
			// if not a FormFieldProperty, return a text field.
			return new TextField();
		}
	}
	
	protected FormFieldProperties propertySet;
	final protected Form form=this;
}
