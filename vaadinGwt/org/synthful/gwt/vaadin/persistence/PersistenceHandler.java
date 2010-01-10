package org.synthful.gwt.vaadin.persistence;

import org.synthful.gwt.vaadin.ui.FormFieldProperties.FormFieldProperty;

public interface PersistenceHandler<T>
{
	public FormFieldProperty[] getFormFieldProperties();
	public void commit();
	public void fetch();
}
