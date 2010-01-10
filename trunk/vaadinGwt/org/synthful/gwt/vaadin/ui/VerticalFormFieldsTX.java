package org.synthful.gwt.vaadin.ui;

import org.synthful.gwt.vaadin.persistence.PersistenceHandler;
import org.synthful.gwt.vaadin.ui.FormFieldsAbstract;
import org.synthful.gwt.vaadin.ui.FormFieldProperties.FormFieldProperty;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class VerticalFormFieldsTX<T>
extends PersistentFormFieldsAbstract<T>
{
	private static final long serialVersionUID = 1L;
	
	public VerticalFormFieldsTX(
		PersistenceHandler<T> persistenceHandler)
	{
		this.inititalise(persistenceHandler.getFormFieldProperties(), new VerticalLayout());
	}
	

	@Override
	protected void makeButtons()
	{

		Button apply = new Button("Apply", new Button.ClickListener()
		{
			public void buttonClick(
				ClickEvent event)
			{
				try
				{
				}
				catch (Exception e)
				{
				}
			}
		});
		this.getLayout().addComponent(apply);
	}
	
}
