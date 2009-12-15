package org.synthful.gwt.vaadin.ui.widgets;

import org.synthful.gwt.vaadin.ui.UiIso3166;

import com.vaadin.ui.ComboBox;

@SuppressWarnings("serial")
public class CountriesComboBox
	extends ComboBox
{

	public CountriesComboBox()
	{
		this("Country", "20em");
	}

	public CountriesComboBox(String caption, String width)
	{
		super(caption);
		this.ignite(width);
	}

	protected void ignite(String width)
	{
		this.setWidth(width);
		this.setContainerDataSource(UiIso3166.getISO3166Container());
		this.setItemCaptionPropertyId(UiIso3166.iso3166_PROPERTY_NAME);
		this.setItemIconPropertyId(UiIso3166.iso3166_PROPERTY_FLAG);
		this.setFilteringMode(ComboBox.FILTERINGMODE_STARTSWITH);
	}

}
