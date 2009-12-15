package org.synthful.gwt.vaadin.persistence;

import java.util.Map;

import org.synthful.gwt.vaadin.ui.FormFieldProperty;

public interface PersistenceHandler<T>
{
	public FormFieldProperty[] getFormFieldProperties();
	public void commit();
}
