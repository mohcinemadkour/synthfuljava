package org.synthful.gwt.vaadin.ui;

import org.synthful.gwt.vaadin.persistence.PersistenceHandler;

import com.vaadin.ui.AbstractLayout;

abstract public class PersistentFormFieldsAbstract<T>
	extends FormFieldsAbstract<T>
{
	private static final long serialVersionUID = 1L;
	
	protected PersistentFormFieldsAbstract(){}

	public PersistentFormFieldsAbstract(
		PersistenceHandler<T> persistenceHandler, AbstractLayout layout)
	{
		this.inititalise(persistenceHandler.getFormFieldProperties(), layout);
		this.persistenceHandler = persistenceHandler;
	}
	
	protected PersistenceHandler<T> persistenceHandler;

}
