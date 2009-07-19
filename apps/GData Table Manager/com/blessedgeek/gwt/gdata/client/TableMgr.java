package com.blessedgeek.gwt.gdata.client;

import java.util.HashMap;

import org.synthful.gwt.domElements.client.DomUtils;
import org.synthful.gwt.gdata.client.FeedsBaseUrl;
import org.synthful.gwt.widgets.client.ui.AuthFormPanel;
import org.synthful.gwt.widgets.client.ui.LabelValuePair;
import org.synthful.gwt.widgets.client.ui.RadioButtonGroup;
import org.synthful.gwt.widgets.client.ui.ScrollableDialogBox;
import org.synthful.gwt.widgets.client.ui.VerticalRadioButtonGroup;
import org.synthful.gwt.widgets.client.ui.ScrollableDialogBox.CloserEventHandler;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
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

    final Label currentDoc = new Label();
    final Label _currentDoc = new Label();
    final HorizontalPanel currentDocHPanel = new HorizontalPanel();
    
    final Label currentTable = new Label();
    final Label _currentTable = new Label();
    final HorizontalPanel currentTableHPanel = new HorizontalPanel();

    final ScrollableDialogBox dialogBox = new ScrollableDialogBox();

    final VerticalPanel dialogVPanel = new VerticalPanel();

    final HTML serverResponseHtml = new HTML();

    final Label separator = new Label();

    final Button logInOutButton = new Button();

    final HTML header = new HTML("<h3>Gdata Table Manager</h3>");

    final ChoiceRadios actions = new ChoiceRadios("Action");
    
    RadioButtonGroup dialogSelection;

    Object TriggerSendSource;
    
    Actions currentAction;
    
    final AuthFormPanel authform = new AuthFormPanel("_top");
    
    final String pageHref = Location.getHref();
    String pageBaseHref;
    String logInOutUrl;


    /**
     * This is the entry point method.
     */
    public void onModuleLoad()
    {
        //currentDoc.setText("");
        //_currentDoc.setText("");
        this.initPageHref();

        // We can add style names to widgets
        sendButton.addStyleName("sendButton");
        header.setStyleName("header");
        this.currentDocHPanel.add(this._currentDoc);
        this.currentDocHPanel.add(this.currentDoc);
        this.currentTableHPanel.add(this._currentTable);
        this.currentTableHPanel.add(this.currentTable);

        // Add the wordField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        RootPanel.get("currentDoc").add(this.currentDocHPanel);
        RootPanel.get("currentDoc").add(this.currentTableHPanel);
        RootPanel.get("sendButtonContainer").add(this.sendButton);
        RootPanel.get("header").add(this.header);
        RootPanel.get("actions").add(this.actions);
        
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
        //currentDoc.setFocus(true);
        //currentDoc.selectAll();

        this.furnishDialogBox();

        // Add a handler to send the name to the server
        SendButtonHandler sendhandler = new SendButtonHandler();
        this.sendButton.addClickHandler(sendhandler);
        //this.currentDoc.addKeyUpHandler(sendhandler);
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
        
        dialogVPanel.add(new HTML("Item Value: "));
        //dialogVPanel.add(textToServerLabel);
        //dialogVPanel.add(separator);        
        //dialogVPanel.add(serverResponseHtml);
        dialogVPanel.addStyleName("dialogVPanel");
        dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
                
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
                    new LabelValuePair("List Sheet Documents", "" + Actions.ListSheetDocs),
                    new LabelValuePair("List Tables", "" + Actions.ListTables),
                    new LabelValuePair("Set Table", "" + Actions.SetTable),
                    new LabelValuePair("Search for Record", "" + Actions.Search4Record),
                    new LabelValuePair("Query for Record", "" + Actions.Query4Record),
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
        
        if(this.TriggerSendSource==this.sendButton)
        {
            String selectedAction = this.actions.getSelected().getFormValue();
            parameters.put("action", selectedAction);
            //parameters.put("item", this.currentDoc.getText());
            this.currentAction = TableMgr.getAction(selectedAction);
            this.sendButton.setEnabled(false);
        }
        else if(this.TriggerSendSource==header)
        {
            parameters.put("action", ""+Actions.About);
            this.sendButton.setEnabled(false);
        }
        
        else if (this.currentAction==Actions.SetSheetDoc)
        {
            String dialogSelectedSheet = this.dialogSelection.getSelected().getFormValue();
            parameters.put("action", Actions.SetSheetDoc.toString());
            parameters.put("sheetKey", dialogSelectedSheet);           
        }
        
        return parameters;
    }
    
    /**
     * Send the name from the wordField to the server and wait for a response.
     */
    private void sendToServer()
    {
        serverResponseHtml.setText("");
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
                    dialogVPanel.clear();
                    dialogVPanel.add(serverResponseHtml);

                    // Show the RPC error message to the user
                    dialogBox
                        .setText("Remote Procedure Call - Failure");
                    serverResponseHtml
                        .addStyleName("serverResponseLabelError");
                    serverResponseHtml.setHTML(SERVER_ERROR);
                    dialogBox.center();
                    //setSeprWid();
                }

                public void onSuccess(
                    String result)
                {
                    dialogBox.setText(
                        "GData Table Manager: " +
                        currentDoc.getText());
                    serverResponseHtml
                        .removeStyleName("serverResponseLabelError");
                    dialogVPanel.clear();
                    // setSeprWid();
                    
                    switch (currentAction)
                    {
                        case ListSheetDocs:
                            listDialogSelection(result, "key", "title");
                            dialogBox.setText("Select Document");
                            dialogBox.center();
                            break;
                        case ListTables:
                            listDialogSelection(result, "table", "title");
                            dialogBox.setText("Select Table");
                            dialogBox.center();
                            break;
                        case SetSheetDoc:
                        case SetTable:
                            break;

                        default:
                            dialogBox.center();
                            dialogBox.setText(currentAction.toString());
                            break;
                    }
                }
                
                private void setSeprWid()
                {
                    int seprWid = serverResponseHtml.getOffsetWidth()-250;
                    if (seprWid<0)
                        seprWid = 1;
                    separator.setWidth(seprWid+"px");
                }
                
                void listDialogSelection(String result, String hashkey1, String hashkey2)
                {
                    try
                    {
                        JSONValue resultjs = JSONParser.parse(result);
                        System.out.println(resultjs);
                        JSONArray entriesjs = resultjs.isArray();
                        if (entriesjs != null)
                        {
                            JSONObject entryjs = entriesjs.get(0).isObject();
                            JSONValue dialogMsg = entryjs.get("message");
                            dialogBox.setText(dialogMsg.toString());
                            
                            LabelValuePair[] options = new LabelValuePair[entriesjs.size()];
                            options[0] =
                                new LabelValuePair("None", "");
                            
                            for (int i = 1; i < entriesjs.size(); i++)
                            {
                                entryjs = entriesjs.get(i).isObject();
                                JSONValue key = entryjs.get(hashkey1);
                                JSONValue title = entryjs.get(hashkey2);
                                if (key==null || key.toString().length()==0)
                                    continue;
                                    
                                options[i] =
                                    new LabelValuePair(title.toString(), key.toString());
                            }
                            
                            // attach choice buttons to dialogVPanel, without default button
                            // with radio group name = SelectSheet
                            dialogSelection =
                                new RadioButtonGroup(dialogVPanel, "SelectSheet", options, 0);
                        }
                    }
                    catch (Exception e)
                    {
                        dialogVPanel.add(serverResponseHtml);
                        serverResponseHtml.setHTML(result);
                    }        
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
            sendToServer();
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
                sendToServer();
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

        public void onClick(
            Event event)
        {
            TriggerSendSource = event.getEventTarget();
            sendButton.setEnabled(true);
            
            if (dialogSelection==null)
                return;
            System.out.println("dialogSelectedRadio="
                + dialogSelection.getSelected());
            RadioButton selected =
                dialogSelection.getSelected();
            if (selected==null)
                return;
            String selectedTxt = selected.getText();
            if (selectedTxt == null || selectedTxt.length() == 0)
                return;

            switch (currentAction)
            {
                case ListSheetDocs:
                    _currentDoc.setText("Current Doc:");
                    currentDoc.setText(selectedTxt);
                    _currentTable.setText("");
                    currentTable.setText("");
                    currentAction = Actions.SetSheetDoc;
                    sendToServer();
                    break;
                    
                case ListTables:
                    _currentTable.setText("current Table:");
                    currentTable.setText(selectedTxt);
                    currentAction = Actions.SetTable;
                    sendToServer();
                    break;
                    

            }
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
        NONE, LogIn, LogOut, About,
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
    
    public static native void alertSheetEntries()
    /*-{$wnd.alert("sheetEntries:"+sheetEntries);
     }-*/;
}
