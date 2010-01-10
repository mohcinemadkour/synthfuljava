package org.synthful.gwt.vaadin.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class SubWindowUtil
{	
	static public void addComponent(final Window subwindow, Component c)
	{
		VerticalLayout layout = (VerticalLayout) subwindow.getContent();
		layout.setMargin(true);
		layout.setSpacing(true);
		// undefined for auto-sizing window
		layout.setSizeUndefined();
		layout.addComponent(c);

		Button close =
			new Button("Close", new Button.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			public void buttonClick(
				ClickEvent event)
			{
				// close the window by removing it from the main window
				subwindow.getApplication().getMainWindow().removeWindow(subwindow);
			}
		});
		// The components added to the window are actually added to the window's
		// layout; you can use either. Alignments are set using the layout
		layout.addComponent(close);
		layout.setComponentAlignment(close, "right bottom");
	}
}
