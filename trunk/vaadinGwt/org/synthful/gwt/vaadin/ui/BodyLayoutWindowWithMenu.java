package org.synthful.gwt.vaadin.ui;

import org.synthful.gwt.vaadin.SynApplication;

import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * This window has a body attached to the window layout,
 * so that body contents could be cleared without clearing the
 * menu bar of the window.
 * 
 * To clear window contents, clear contents of body.
 * 
 * @author icecream
 *
 */
public class BodyLayoutWindowWithMenu
<S extends SynApplication<?,?>, M extends MenuBar>
	extends Window
{
	private static final long serialVersionUID = 1L;

	public BodyLayoutWindowWithMenu(
		S synapp,
		M menubar,
		String caption)
	{
		this.synApp = synapp;
		this.menuBar = menubar;
		this.setCaption(caption);
		
		VerticalLayout layout = (VerticalLayout) this.getContent();
		layout.setSizeFull();
		
		this.body = new VerticalLayout();
		layout.addComponent(this.menuBar);
		layout.addComponent(this.body);
		layout.setSizeFull();
		layout.setMargin(true, false, true, false);
		layout.setExpandRatio(this.menuBar, 1);
		layout.setExpandRatio(this.body, 1000);
		
		this.body.setMargin(true);
		this.body.setSpacing(true);
		this.body.setSizeFull();
		this.body.setMargin(true, false, true, false);
	}
		
	public void removeAllComponents()
	{
		this.body.removeAllComponents();
	}
	public void addComponent(Component c)
	{
		this.body.addComponent(c);
	}

	
	final public M menuBar;
	final public VerticalLayout body;
	final protected BodyLayoutWindowWithMenu<S, M> thisWin = this;
	final protected S synApp;
}
