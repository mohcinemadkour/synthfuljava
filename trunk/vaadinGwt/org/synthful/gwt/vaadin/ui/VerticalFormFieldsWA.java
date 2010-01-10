package org.synthful.gwt.vaadin.ui;

import org.synthful.gwt.vaadin.persistence.PersistenceHandler;
import org.synthful.gwt.vaadin.ui.FormFieldProperties.FormFieldProperty;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class VerticalFormFieldsWA<T>
extends PersistentFormFieldsAbstract<T>
{
	private static final long serialVersionUID = 1L;
	
	public VerticalFormFieldsWA(
		PersistenceHandler<T> persistenceHandler)
	{
		this.inititalise(persistenceHandler.getFormFieldProperties(), new VerticalLayout());
	}
	
	@Override
	protected void makeButtons()
	{
		// The cancel / apply buttons
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		this.getLayout().addComponent(buttons);
		
		Button discardChanges =
			new Button("Discard changes", new Button.ClickListener()
			{
				public void buttonClick(
					ClickEvent event)
				{
					form.discard();
				}
			});
		discardChanges.setStyleName(Button.STYLE_LINK);
		buttons.addComponent(discardChanges);
		
		Button editFields =
			new Button("Edit", new Button.ClickListener()
			{
				public void buttonClick(
					ClickEvent event)
				{
					setFieldsReadOnly(false);
				}
			});
		editFields.setStyleName(Button.STYLE_LINK);
		buttons.addComponent(editFields);

		Button buttVerifyInfo =
			new Button("Verify information", new Button.ClickListener()
			{
				public void buttonClick(
					ClickEvent event)
				{
					try
					{
						form.commit();
						verifyProperties(propertySet);
					}
					catch (Exception e)
					{
						// Ignored, we'll let the Form handle the errors
					}
				}
			});
		this.getLayout().addComponent(buttVerifyInfo);

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
		this.getLayout().addComponent(apply);
	}
	
}
