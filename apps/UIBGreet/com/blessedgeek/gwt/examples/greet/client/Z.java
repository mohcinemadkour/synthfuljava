package com.blessedgeek.gwt.examples.greet.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Z
	implements EntryPoint
{
	interface LilyUiBinder
	extends UiBinder<Widget, Z>{}
	private static LilyUiBinder uiBinder = GWT.create(LilyUiBinder.class);
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService =
		GWT.create(GreetingService.class);

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR =
		"An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";


	@UiField
	HorizontalPanel hPanel;
	@UiField
	Button sendButton;
	@UiField
	TextBox nameField;
	
	//Fired when user clicks send Button
	@UiHandler("sendButton") 
	public void sendOnClick(ClickEvent event){
		sendNameToServer();
	}

	//Fired when user types in the nameField.
	@UiHandler("nameField") 
	public void nameOnKeyUp(KeyUpEvent event){
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
			sendNameToServer();
		}
	}
	
	@UiField
	DialogBox dialogBox;
	@UiField
	Label textToServerLabel;
	@UiField
	HTML serverResponseLabel;
	@UiField
	Button closeButton;
	
	@UiHandler("closeButton") 
	public void closeOnClick(ClickEvent event)
	{
		dialogBox.hide();
		sendButton.setEnabled(true);
		sendButton.setFocus(true);
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		uiBinder.createAndBindUi(this);
		nameField.setText("GWT User");

		// Add the nameField and sendButton to the RootPanel
		RootPanel.get("here").add(hPanel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();
	}
	
	private void sendNameToServer(){
		HashMap<String, String> params = new HashMap<String, String>();
		String textToServer = nameField.getText();
		params.put("name", textToServer);
		textToServerLabel.setText(textToServer);
		serverResponseLabel.setText("");
		sendButton.setEnabled(false);
		
		greetingService.doServiceResponse(params,serverCallBack);
	}
	
	private final AsyncCallback<Map<String, String>> serverCallBack =
		new AsyncCallback<Map<String, String>>()
		{
			public void onFailure(Throwable caught){
				// Show the RPC error message to the user
				dialogBox
					.setText("Remote Procedure Call - Failure");
				serverResponseLabel
					.addStyleName("serverResponseLabelError");
				serverResponseLabel.setHTML(SERVER_ERROR);
				dialogBox.center();
				closeButton.setFocus(true);
			}

			public void onSuccess(Map<String, String> result){
				dialogBox.setText("Remote Procedure Call");
				serverResponseLabel
					.removeStyleName("serverResponseLabelError");
				String s = "Hello, "
				+ result.get("name") + "!<br><br>I am running " + result.get("serverInfo")
				+ ".<br><br>It looks like you are using:<br>" + result.get("userAgent");
				
				serverResponseLabel.setHTML(s);
				dialogBox.center();
				closeButton.setFocus(true);
			}
		};
	
}
