package org.synthful.gwt.vaadin.ui;

import org.synthful.gwt.vaadin.SynApplication;

import com.syntercourse.StartMenuBar;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

/**
 * This window has a body attached to the window layout,
 * so that body contents could be cleared without clearing the
 * standard components of the window.
 * 
 * To clear window contents, clear contents of body.
 * 
 * @author icecream
 *
 */
abstract public class BodyLayoutSubWindowWithButton
<S extends SynApplication
	<?,BodyLayoutWindowWithMenu
		<SynApplication<?,?>, StartMenuBar>>>
	extends BodyLayoutWindowWithButton
		<SynApplication<?,BodyLayoutWindowWithMenu<SynApplication<?,?>,StartMenuBar>>>
{
	private static final long serialVersionUID = 1L;

	public BodyLayoutSubWindowWithButton(S synapp)
	{
		super(synapp);
	}
	
}
