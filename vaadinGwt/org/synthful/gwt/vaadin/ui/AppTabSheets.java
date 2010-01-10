package org.synthful.gwt.vaadin.ui;

import java.io.Serializable;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

public class AppTabSheets
	extends TabSheet
	implements TabSheet.SelectedTabChangeListener
{
	private static final long serialVersionUID = 1L;

	public AppTabSheets(AbstractLayout layout)
	{
		layout.addComponent(this);
	}
	
	protected void addTabs(AppTabComponent[] appTabComponents)
	{
		for (int i = 0; i < appTabComponents.length; i++)
			this.addTab(
				appTabComponents[i].component,
				appTabComponents[i].caption,
				appTabComponents[i].icon
			);		
	}

	@Override
	public void selectedTabChange(
		SelectedTabChangeEvent event)
	{
		TabSheet tabsheet = event.getTabSheet();
		Tab tab = tabsheet.getTab(tabsheet.getSelectedTab());
		if (tab != null)
		{
			getWindow().showNotification("Selected tab: " + tab.getCaption());
		}
	}

	protected AppTabComponent[] appTabComponents;

	static public class AppTabComponent
	implements Serializable
	{
		private static final long serialVersionUID = 1L;
		public AppTabComponent(
			Component component,
			String caption,
			ThemeResource icon)
		{
			this.component = component;
			this.caption = caption;
			this.icon = icon;
		}
		
		public Component component;
		public String caption;
		public ThemeResource icon;
	}

}
