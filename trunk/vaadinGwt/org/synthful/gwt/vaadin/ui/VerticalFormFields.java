package org.synthful.gwt.vaadin.ui;

import org.synthful.gwt.vaadin.persistence.PersistenceHandler;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class VerticalFormFields<T>
	extends VerticalLayout
{
	public VerticalFormFields(
		PersistenceHandler<T> persistenceHandler)
	{
		this(persistenceHandler.getFormFieldProperties());
		this.persistenceHandler = persistenceHandler;
	}
	
	public VerticalFormFields(FormFieldProperty[] rentalFormFields)
	{
	    this.setMargin(true);
		final FormFields propertySet = new FormFields(rentalFormFields);

		// Create the Form
		final Form itemForm = new Form();
		itemForm.setWriteThrough(false); // we want explicit 'apply'
		itemForm.setInvalidCommitted(false); // no invalid values in datamodel

		// FieldFactory for customizing the fields and adding validators
		itemForm.setFormFieldFactory(new ItemFieldsFactory());
		itemForm.setItemDataSource(propertySet);
		
		// Determines which properties are shown, and in which order:
		itemForm.setVisibleItemProperties(
			FormFields.listVisible(rentalFormFields));

		addComponent(itemForm); // Add form to layout

		// The cancel / apply buttons
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		itemForm.getLayout().addComponent(buttons);
		
		Button discardChanges =
			new Button("Discard changes", new Button.ClickListener()
			{
				public void buttonClick(
					ClickEvent event)
				{
					itemForm.discard();
				}
			});
		discardChanges.setStyleName(Button.STYLE_LINK);
		buttons.addComponent(discardChanges);
		

		Button buttVerifyInfo =
			new Button("Verify information", new Button.ClickListener()
			{
				public void buttonClick(
					ClickEvent event)
				{
					try
					{
						itemForm.commit();
						verifyProperties(propertySet);
					}
					catch (Exception e)
					{
						// Ignored, we'll let the Form handle the errors
					}
				}
			});
		buttons.addComponent(buttVerifyInfo);

		Button apply = new Button("Apply", new Button.ClickListener()
		{
			public void buttonClick(
				ClickEvent event)
			{
				try
				{
					persistenceHandler.commit();
				}
				catch (Exception e)
				{
				}
			}
		});
		addComponent(apply);
	}
	
	private void verifyProperties(
		FormFields propertySet)
	{
		Window.Notification n =
			new Window.Notification("Verify information",
				Window.Notification.TYPE_TRAY_NOTIFICATION);

		n.setPosition(Window.Notification.POSITION_CENTERED);
		StringBuffer verbuf = propertySet.verifyProperties();

		n.setDescription(verbuf.toString());
		getWindow().showNotification(n);
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
	
	private PersistenceHandler<T> persistenceHandler;
}
