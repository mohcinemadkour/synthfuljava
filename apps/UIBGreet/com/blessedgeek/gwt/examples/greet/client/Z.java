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
	DialogBox dialogBox;
	@UiField
	Label textToServerLabel;
	@UiField
	HTML serverResponseLabel;
	@UiField
	Button closeButton;
	
	@UiHandler("closeButton") 
	public void onClick(ClickEvent event)
	{
		dialogBox.hide();
		sendButton.setEnabled(true);
		sendButton.setFocus(true);
	}
	
	
	final Button sendButton = new Button("Send");
	final TextBox nameField = new TextBox();
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		uiBinder.createAndBindUi(this);
		nameField.setText("GWT User");

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
	
	@Deprecated // not used, replaced by UIBinder dialogbox
	private void initDialogBox(){
		// Create the popup dialog box
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
	}
	
	class MyHandler
	implements ClickHandler, KeyUpHandler
	{
		/**
		 * Fired when the user clicks on the sendButton.
		 */
		public void onClick(ClickEvent event){
			sendNameToServer();
		}
	
		/**
		 * Fired when the user types in the nameField.
		 */
		public void onKeyUp(KeyUpEvent event){
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
				sendNameToServer();
			}
		}
	
		/**
		 * Send the name from the nameField to the server and wait for a response.
		 */
		private void sendNameToServer(){
			HashMap<String, String> params = new HashMap<String, String>();
			sendButton.setEnabled(false);
			String textToServer = nameField.getText();
			textToServerLabel.setText(textToServer);
			serverResponseLabel.setText("");
			params.put("name", textToServer);
			
			greetingService.doServiceResponse(
				params,
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
				}//AsyncCallback
			);
		}//sendNameToServer
	}//class MyHandler
}
