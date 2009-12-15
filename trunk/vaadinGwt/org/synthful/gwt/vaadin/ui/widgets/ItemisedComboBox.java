package org.synthful.gwt.vaadin.ui.widgets;

import java.util.Collection;

import org.synthful.gwt.vaadin.ui.CommonSelectables;
import org.synthful.gwt.vaadin.ui.SelectableItems;


import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.ComboBox;

@SuppressWarnings("serial")
public class ItemisedComboBox
	extends ComboBox
{
	public ItemisedComboBox()
	{
	}

	public ItemisedComboBox(String caption, SelectableItems items)
	{
		super(caption);
		this.ignite(items);
	}

	public ItemisedComboBox(String caption, Collection options)
	{
		super(caption, options);
		this.ignite(null);
	}


	protected void ignite(SelectableItems items)
	{
		this.selectableItems
		= (items==null)
		? new CommonSelectables(CommonSelectables.intensity)
		: items;
		
		this.setWidth(this.selectableItems.getDefaultWidth());
		IndexedContainer xc = new IndexedContainer();
		xc.addContainerProperty(
			CommonSelectables.VALUEProperty,
			String.class,
			this.selectableItems.getDefaultItem());
		
		for (SelectableItems.SelectableItem selitem: this.selectableItems.getSelectableItems())
		{
			Item item = xc.addItem(selitem);
			item.getItemProperty(CommonSelectables.VALUEProperty).setValue(selitem);
		}
		
		this.setContainerDataSource(xc);
		this.setItemCaptionPropertyId(CommonSelectables.VALUEProperty);
		//this.setItemIconPropertyId();
		this.setFilteringMode(ComboBox.FILTERINGMODE_STARTSWITH);
	}
	
	SelectableItems selectableItems;
}
