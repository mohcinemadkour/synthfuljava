package org.synthful.gwt.vaadin.ui;

import java.io.Serializable;

public interface SelectableItems
extends Serializable
{
	@SuppressWarnings("serial")
	class SelectableItem
	implements Serializable
	{
		public SelectableItem(String caption)
		{
			this.caption = caption;
			this.value = caption;
		}
		public SelectableItem(String caption, String value)
		{
			this.caption = caption;
			this.value = value;
		}
		protected String caption, value;
		
		public String toString()
		{
			return this.caption;
		}
	}
	
	static public String VALUEProperty = "value";
	public SelectableItem[] getSelectableItems();
	public SelectableItem getDefaultItem();
	public String getDefaultWidth();
}
