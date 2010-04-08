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
abstract public class BodyLayoutWindowWithButton
<S extends SynApplication
	<?,? extends Window>>
	extends Window
{
	private static final long serialVersionUID = 1L;

	public BodyLayoutWindowWithButton(S synapp)
	{
		this.synApp = synapp;
		VerticalLayout layout = (VerticalLayout) this.getContent();
		layout.setSizeFull();
		
		this.body = new VerticalLayout();
		layout.addComponent(this.body);
		layout.addComponent(this.button);
		layout.setComponentAlignment(button, "right bottom");
		layout.setExpandRatio(this.body, 100);
		layout.setExpandRatio(this.button, 1);
		
		this.body.setMargin(true);
		this.body.setSpacing(true);
		this.body.setSizeFull();
		this.body.setMargin(true, false, true, false);
		
		this.addListener(
			new Window.CloseListener(){
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e){
					onWindowClose(e);
				}
			}
		);
	}
	
	public void addEmbedded(String caption, String uri, int type)
	{
		ExternalResource page = new ExternalResource(uri);
		Embedded e =
			new Embedded(caption, blankPage);
		
		e.setType(type);
		this.addComponent(e);
		e.setSizeFull();
		e.setSource(page);
	}
	
	public void addComponent(Component c)
	{
		this.body.addComponent(c);
	}

	/**
	 * Replace buttonClickListener with another Button.ClickListener
	 * if more complex operations need to be performed when button clicked.
	 */
	protected Button.ClickListener buttonClickListener =
		new Button.ClickListener(){
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent e)
			{
				onButtonClick(e);
			}
		};
	
	final protected Button button =
		new Button("Close", buttonClickListener);
	
	abstract protected void onButtonClick(ClickEvent e);
	abstract protected void onWindowClose(CloseEvent e);
	
	final protected BodyLayoutWindowWithButton<S> thisWin = this;
	final protected S synApp;
	final public VerticalLayout body;
	final static ExternalResource blankPage = new ExternalResource("about:blank");
}
