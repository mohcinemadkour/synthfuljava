package org.synthful.gwt.vaadin.ui;

import org.synthful.util.Iso3166;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;

public class UiIso3166
	extends Iso3166
{

	public static IndexedContainer getISO3166Container()
	{
		if (indexOfCountries == null)
		{
			indexOfCountries = new IndexedContainer();
			fillIso3166Container(indexOfCountries);
		}
		return indexOfCountries;
	}

	private static void fillIso3166Container(
		IndexedContainer container)
	{
		container.addContainerProperty(
			iso3166_PROPERTY_NAME, String.class, null);
		container.addContainerProperty(
			iso3166_PROPERTY_SHORT, String.class, null);
		container.addContainerProperty(
			iso3166_PROPERTY_FLAG, Resource.class, null);
		for (int i = 0; i < Iso3166.countries.length; i++)
		{
			String name = Iso3166.countries[i][0];
			String id = Iso3166.countries[i][1];
			Item item = container.addItem(id);
			item.getItemProperty(iso3166_PROPERTY_NAME).setValue(name);
			item.getItemProperty(iso3166_PROPERTY_SHORT).setValue(id);
			item.getItemProperty(iso3166_PROPERTY_FLAG).setValue(
				new ThemeResource("flags/" + id.toLowerCase() + ".gif"));
		}
		
		container.sort(
			new Object[] {iso3166_PROPERTY_NAME},
			new boolean[] {true}
		);
	}

	static public IndexedContainer indexOfCountries;
}
