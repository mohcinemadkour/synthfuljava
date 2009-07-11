package com.blessedgeek.gwt.gdata.client;

import java.util.HashMap;

import org.synthful.gwt.gdata.client.FeedsBaseUrl;
import org.synthful.gwt.widgets.client.ui.AuthFormPanel;
import org.synthful.gwt.widgets.client.ui.ScrollableDialogBox;
import org.synthful.gwt.widgets.client.ui.VerticalRadioButtonGroup;
import org.synthful.gwt.widgets.client.ui.RadioButtonGroup.LabelValuePair;
import org.synthful.gwt.widgets.client.ui.ScrollableDialogBox.CloserEventHandler;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * @author Icecream
 *
 * Notes:
 * Login AuthForm is triggered in MkParameters.
 * MkParameters detect the choice to send action to TableMgrService.
 * But if MkParameters find that the choice is login, it would submit AuthForm.
 * 
 */
public class TableMgr
    implements EntryPoint
{

    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    private static final String SERVER_ERROR =
        "An error occurred while "
            + "attempting to contact the server. Please check your network "
            + "connection and try again.";

    /**
     * Create a remote service proxy to talk to the server-side Greeting
     * service.
     */
    private final TableMgrServiceAsync tableMgrService =
        GWT.create(TableMgrService.class);

    final Button sendButton = new Button("Send");

    final TextBox wordField = new TextBox();

    final ScrollableDialogBox dialogBox = new ScrollableDialogBox();

    final Label textToServerLabel = new Label();

    final HTML serverResponseLabel = new HTML();

    final Label separator = new Label();

    final Button logInOutButton = new Button();

    final HTML header = new HTML("<h3>Gdata Table Manager</h3>");

    final ChoiceRadios actions = new ChoiceRadios("Action");

    Object TriggerSendSource;
    
    final AuthFormPanel authform = new AuthFormPanel("_top");
    
    final String pageHref = Location.getHref();
    String pageBaseHref;
    String logInOutUrl;


    /**
     * This is the entry point method.
     */
    public void onModuleLoad()
    {
        wordField.setText("");
        this.initPageHref();

        // We can add style names to widgets
        sendButton.addStyleName("sendButton");
        header.setStyleName("header");

        // Add the wordField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        RootPanel.get("itemInput").add(wordField);
        RootPanel.get("sendButtonContainer").add(sendButton);
        RootPanel.get("header").add(header);
        RootPanel.get("action").add(actions);
        
        RootPanel logInOut = RootPanel.get("logIn");
        if (logInOut!=null)
        {
            this.logInOutButton.setText("Log In");
            this.logInOutUrl = "LogIn.jsp?callBackBaseHref="+ this.pageBaseHref;
            logInOut.add(this.logInOutButton);
        }
        else
        {
            logInOut = RootPanel.get("logOut");
            this.logInOutButton.setText("Log Out");
            this.logInOutUrl = "LogOut.jsp?callBackLocation="+ this.pageHref;
            logInOut.add(this.logInOutButton);
        }
        
        this.mkAuthParams();

        // Focus the cursor on the name field when the app loads
        wordField.setFocus(true);
        wordField.selectAll();

        this.furnishDialogBox();

        // Add a handler to send the name to the server
        SendButtonHandler sendhandler = new SendButtonHandler();
        this.sendButton.addClickHandler(sendhandler);
        this.wordField.addKeyUpHandler(sendhandler);
        this.header.addClickHandler(sendhandler);
        this.logInOutButton.addClickHandler(new LogInOutButtonHandler());
        this.dialogBox.CloserEventHandlers.add(new CloserHandler());
    }
    
    private void initPageHref()
    {
        int ix = this.pageHref.indexOf('?');
        if (ix>10)
            this.pageBaseHref = this.pageHref.substring(0, ix);
        else
            this.pageBaseHref = this.pageHref;
        ix = this.pageBaseHref.lastIndexOf('/');
        this.pageBaseHref = this.pageBaseHref.substring(0, ix);
        System.out.println(this.pageHref);
        System.out.println(this.pageBaseHref);
    }
    
    private void furnishDialogBox()
    {
        dialogBox.setText("GData Table Manager");
        dialogBox.setAnimationEnabled(true);

        VerticalPanel dialogVPanel = new VerticalPanel();
        
        dialogVPanel.add(new HTML("Item Value: "));
        dialogVPanel.add(textToServerLabel);
        dialogVPanel.add(separator);        
        dialogVPanel.add(serverResponseLabel);
        dialogVPanel.addStyleName("dialogVPanel");
        dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
                
        dialogBox.setSizePx(400,300);
        //dialogBox.setAlwaysShowScrollBars(true);
        
        dialogBox.setWidget(dialogVPanel);
    }

    class ChoiceRadios
        extends VerticalRadioButtonGroup
    {
        public ChoiceRadios(
            String groupName)
        {
            super(
                groupName,
                    new LabelValuePair[]
                {
                    //new LabelValuePair("Log in", "" + Actions.LogIn),
                    new LabelValuePair("List Sheet Documents", "" + Actions.ListSheetDocs),
                    new LabelValuePair("Set Sheet Document", "" + Actions.SetSheetDoc),
                    new LabelValuePair("List Tables", "" + Actions.ListTables),
                    new LabelValuePair("Set Table", "" + Actions.SetTable),
                    new LabelValuePair("Search for Record", "" + Actions.Search4Record),
                    new LabelValuePair("Query for Record", "" + Actions.Query4Record),
                    //new LabelValuePair("Log out", "" + Actions.LogOut)
                },
                0
            );
        }
    }
    
    private void mkAuthParams()
    {
        //RootPanel.get("authform").add(this.authform);
        this.authform.Session.setValue("0");
        this.authform.Secure.setValue("1");
        this.authform.Scope.setValue(FeedsBaseUrl.SpreadSheets);
        this.authform.NextUrl.setValue(Location.getHref());
        RootPanel.get("authform").add(this.authform);
        //this.authform.setAction("AuthFormDebug.jsp");
        this.authform.setMethod("get");
    }

    
    private HashMap<String, String> mkSendParameters()
    {
        HashMap<String, String> parameters =
            new HashMap<String, String>();
        
        if(TriggerSendSource==sendButton || TriggerSendSource==wordField)
        {
            String selectedAction = this.actions.getSelected().getFormValue();            
            parameters.put("action", selectedAction);
            parameters.put("item", wordField.getText());
        }
        else if(TriggerSendSource==header)
        {
            parameters.put("action", ""+Actions.Intro);
        }
        
        return parameters;
    }
    
    /**
     * Send the name from the wordField to the server and wait for a response.
     */
    private void sendWordToServer()
    {
        sendButton.setEnabled(false);
        String textToServer = wordField.getText();
        textToServerLabel.setText(textToServer);
        serverResponseLabel.setText("");
        HashMap<String, String> paramHash = mkSendParameters();
        if(paramHash==null)
            return;
        
        //Call servlet/jsp path (TableActionService.jsp) defined in TableMgrService.java
        tableMgrService.doServiceResponse(
            paramHash,
            new AsyncCallback<String>()
            {
                public void onFailure(
                    Throwable caught)
                {
                    // Show the RPC error message to the user
                    dialogBox
                        .setText("Remote Procedure Call - Failure");
                    serverResponseLabel
                        .addStyleName("serverResponseLabelError");
                    serverResponseLabel.setHTML(SERVER_ERROR);
                    dialogBox.center();
                    setSeprWid();
                }

                public void onSuccess(
                    String result)
                {
                    dialogBox.setText(
                        "GData Table Manager: " +
                        wordField.getText());
                    serverResponseLabel
                        .removeStyleName("serverResponseLabelError");
                    serverResponseLabel.setHTML(result);
                    dialogBox.center();
                    setSeprWid();
                }
                
                private void setSeprWid()
                {
                    int seprWid = serverResponseLabel.getOffsetWidth()-250;
                    if (seprWid<0)
                        seprWid = 1;
                    separator.setWidth(seprWid+"px");
                }
            });
    }

    class SendButtonHandler
        implements ClickHandler, KeyUpHandler
    {
        /**
         * Fired when the user clicks on the sendButton.
         */
        public void onClick(
            ClickEvent event)
        {
            TriggerSendSource = event.getSource();
            sendWordToServer();
        }

        /**
         * Fired when the user types in the wordField.
         */
        public void onKeyUp(
            KeyUpEvent event)
        {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
            {
                TriggerSendSource = event.getSource();
                sendWordToServer();
            }
        }
    }
    
    class LogInOutButtonHandler
        implements ClickHandler
    {
        /**
         * Fired when the user clicks on logInOutButton.
         */
        public void onClick(
            ClickEvent event)
        {
            TriggerSendSource = event.getSource();
            Location.replace(logInOutUrl);
        }
    }
    
    private class CloserHandler
    implements CloserEventHandler
    {

        public void onClick(Event event)
        {
            sendButton.setEnabled(true);
        }

        public void onMouseOver(Event event)
        {
        }

        public void onMouseOut(Event event)
        {
        }
    }

    
    static public enum Actions
    {
        NONE, LogIn, LogOut, Intro,
        ListSheetDocs, SetSheetDoc,
        ListTables, SetTable, AddTable, DeleteTable,
        ListRecords, AddRecord, DeleteRecord,
        Search4Record, Query4Record
    };
    
    static public TableMgr.Actions getAction(String actionStr)
    {
        try {
            return TableMgr.Actions.valueOf(actionStr); 
        }
        catch(Exception ex)
        {
            return TableMgr.Actions.NONE;
        }
    }
    
}
