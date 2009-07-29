package com.blessedgeek.gwt.gdata.client;

import java.util.HashMap;

import org.synthful.gwt.gdata.client.FeedsBaseUrl;
import org.synthful.gwt.widgets.client.ui.AuthFormPanel;
import org.synthful.gwt.widgets.client.ui.LabelValuePair;
import org.synthful.gwt.widgets.client.ui.RadioButtonGroup;
import org.synthful.gwt.widgets.client.ui.ScrollableDialogBox;
import org.synthful.gwt.widgets.client.ui.VerticalRadioButtonGroup;
import org.synthful.gwt.widgets.client.ui.ScrollableDialogBox.CloserEventHandler;

import com.blessedgeek.gwt.gdata.client.ui.TableInfoDialogContents;
import com.blessedgeek.gwt.gdata.client.ui.TableMgrDialog;
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
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
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
    public final static String SERVER_ERROR =
        "An error occurred while "
            + "attempting to contact the server. Please check your network "
            + "connection and try again.";

    final static private String QUERY_INSTR =
        "Enter query string using format<br/>" +
        "{col-name}[=|&lt;|>]\"{literal string}\"<br/>" +
        "[and|or] ...<p/>";
    /**
     * Create a remote service proxy to talk to the server-side Greeting
     * service.
     */
    final private TableMgrServiceAsync tableMgrService =
        GWT.create(TableMgrService.class);

    final Button sendButton = new Button("Send");

    final Label currentDoc = new Label();
    final Label _currentDoc = new Label();
    final HorizontalPanel currentDocHPanel = new HorizontalPanel();
    
    final Label currentTable = new Label();
    final Label _currentTable = new Label();
    final HorizontalPanel currentTableHPanel = new HorizontalPanel();

    final Label currentWorksheet = new Label();
    final Label _currentWorksheet = new Label();
    final HorizontalPanel currentWorksheetHPanel = new HorizontalPanel();

    final Button logInOutButton = new Button();

    final HTML header = new HTML("<h3>Gdata Table Manager</h3>");

    final ChoiceRadios actions = new ChoiceRadios("Action");
    
    final CheckBox refresh = new CheckBox("Refresh");
    final public SendButtonHandler sendhandler = new SendButtonHandler();
    final TableMgrDialog dialogBox = new TableMgrDialog();
    
    Object TriggerSendSource;
    
    Actions currentAction;
    String currentDocKey;
    String currentTableId;
    
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
        this.sendButton.addStyleName("sendButton");
        this.header.setStyleName("header");
        this.currentDocHPanel.add(this._currentDoc);
        this.currentDocHPanel.add(this.currentDoc);
        this.currentTableHPanel.add(this._currentTable);
        this.currentTableHPanel.add(this.currentTable);
        this.currentWorksheetHPanel.add(this._currentWorksheet);
        this.currentWorksheetHPanel.add(this.currentWorksheet);

        // Add the wordField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        RootPanel.get("currentDoc").add(this.currentDocHPanel);
        RootPanel.get("currentDoc").add(this.currentTableHPanel);
        RootPanel.get("currentDoc").add(this.currentWorksheetHPanel);
        final HorizontalPanel hpanel1 = new HorizontalPanel();
        hpanel1.add(this.sendButton);
        hpanel1.add(this.refresh);
        RootPanel.get("sendButtonContainer").add(hpanel1);
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


        // Add a handler to send the name to the server
        this.sendButton.addClickHandler(sendhandler);
        this.header.addClickHandler(sendhandler);
        this.logInOutButton.addClickHandler(new LogInOutButtonHandler());
        this.dialogBox.Submit.addClickHandler(sendhandler);
        this.dialogBox.CloserEventHandlers.add(new CloserHandler());
        
        this.sendButton.setStyleName("sendButton");
        this.logInOutButton.setStyleName("sendButton");
        this.actions.setStyleName("actions");
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

    /**
     * Decisions on actions before sending to server
     * @param action
     * @return
     */
    private HashMap<String, String> mkSendParameters(Actions action)
    {
        HashMap<String, String> parameters =
            new HashMap<String, String>();
        
        // The following is triggered from main window choices/send button
        // this.currentAction is to be determined
        if(this.TriggerSendSource==this.sendButton)
        {
            String selectedAction = this.actions.getSelected().getFormValue();
            parameters.put("action", selectedAction);
            //parameters.put("item", this.currentDoc.getText());
            this.currentAction = TableMgr.getAction(selectedAction);
            this.sendButton.setEnabled(false);
        }
        
        // this.currentAction will be set to Actions.About
        else if(this.TriggerSendSource==header)
        {
            parameters.put("action", Actions.About.toString());
            this.currentAction = Actions.About;
            this.sendButton.setEnabled(false);
        }
        
        // Prior actions succesfully reciprocated by server
        // resulted in dialog box displayed.
        // New action triggered by dialog box submit button
        else if (TriggerSendSource==dialogBox.Submit)
        {
            parameters.put("action", this.currentAction.toString());
            switch (this.currentAction)
            {
                case Search4Record:
                    parameters.put("search", this.dialogBox.QueryBox.getText());
                    break;

                case Query4Record:
                    parameters.put("query", this.dialogBox.QueryBox.getText());
                    break;
                    
                case AddTable:
                    parameters = this.dialogBox.tableInfoStuffs.mkParameters();
                    parameters.put("action", this.currentAction.toString());
                    this.currentAction = Actions.ListTables;
                    break;
                case AddWorksheet:
                    parameters = this.dialogBox.worksheetInfoStuffs.mkParameters();
                    parameters.put("action", this.currentAction.toString());
                    this.currentAction = Actions.ListWorksheets;
                    break;
                case UpdateTable:
                    parameters = this.dialogBox.tableInfoStuffs.mkParameters();
                    parameters.put("action", this.currentAction.toString());
                    this.currentAction = Actions.ListTableInfo;
                    break;
            }
        }
        
        // Prior actions succesfully reciprocated by server
        // resulted in dialog box displayed.
        // New action is triggered by DialogBox close button.
        //
        // *note: this.actions.getSelected() cannot be used because
        // dialogbox close button onclick handler has reset it to 0.
        // this.currentAction will be set to Actions.SetSheetDoc.
        else
        {
            this.currentAction = action;
            parameters.put("action", this.currentAction.toString());
            switch (this.currentAction)
            {
                case SetSheetDoc:
                    parameters.put("sheetKey", this.currentDocKey); 
                    break;
                case SetTable:
                    parameters.put("table", this.currentTableId); 
                    break;
                case SetWorksheet:
                    parameters.put("worksheet", this.currentWorksheet.getText());
            }
        }
        if (this.refresh.getValue())
            parameters.put("refresh", "1");
        
        return parameters;
    }
    
    /**
     * Send the name from the wordField to the server and wait for a response.
     */
    private void sendToServer(Actions action)
    {
        HashMap<String, String> paramHash = mkSendParameters(action);
        
        //After parameters construction, but before sending to server,
        //determine cases of actions that result in dialogBox displayed
        //rather than sending to server.
        //For these cases, don't send to server, just display the dialogbox.
        if (this.currentAction!=null)
            switch(this.currentAction)
            {
                case ShowSearch4Record:
                    this.dialogBox.showQueryWidgets(
                        "Search for Literal", "Enter literal to search<p/>");
                    this.currentAction = Actions.Search4Record;
                    return;
                case ShowQuery4Record:
                    this.dialogBox.showQueryWidgets(
                        "Sructured Query", QUERY_INSTR);
                    this.currentAction = Actions.Query4Record;
                    return;
                case ShowAddTable:
                    this.dialogBox.showUpdateTable("Add Table", null);
                    this.currentAction=Actions.AddTable;
                    return;
                case ShowAddWorksheet:
                    this.dialogBox.showAddWorksheet("Add Worksheet");
                    this.currentAction=Actions.AddWorksheet;
                    return;
            }
                
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
                    dialogBox.showRpcFailureHtml();
                }

                public void onSuccess(
                    String result)
                {
                    if (currentAction!=null)
                        dialogBox.setText(currentAction.toString());
                    else
                        dialogBox.setText("GData Table Manager");
                    // After sending to server and on receiving successful
                    // server reciprocation.
                    switch (currentAction)
                    {
                        case ListSheetDocs:
                            dialogBox.showRadioButtons(result, "key", "title");
                            //dialogBox.setText("Select Document");
                            break;
                        case ListWorksheets:
                            dialogBox.showRadioButtons(result, "title", "title");
                            //dialogBox.setText("Select Worksheet");
                            break;
                        //case AddTable:
                        case ListTables:
                            dialogBox.showRadioButtons(result, "title", "title");
                            //dialogBox.setText("Select Table");
                            break;
                        case SetSheetDoc:
                        case SetTable:
                        case SetWorksheet:
                            break;
                        case ListTableInfo:
                            dialogBox.showTableInfo("Table Info", result);
                            break;
                        case ShowUpdateTable:
                            dialogBox.showUpdateTable("Update Table Info", result);
                            currentAction = Actions.UpdateTable;
                            return;
                            
                        default:
                            dialogBox.showRpcHtml(result);
                            break;
                    }
                }
            });
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
                    new LabelValuePair("List Worksheets", "" + Actions.ListWorksheets),
                    new LabelValuePair("List Tables", "" + Actions.ListTables),
                    new LabelValuePair("List Table Info", "" + Actions.ListTableInfo),
                    new LabelValuePair("List Table Records", "" + Actions.ListTableRecords),
                    new LabelValuePair("Search for Record", "" + Actions.ShowSearch4Record),
                    new LabelValuePair("Query for Record", "" + Actions.ShowQuery4Record),
                    new LabelValuePair("Add Table", "" + Actions.ShowAddTable),
                    new LabelValuePair("Update Table", "" + Actions.ShowUpdateTable),
                    new LabelValuePair("Add Worksheet", "" + Actions.ShowAddWorksheet),
                },
                0
            );
        }
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
            dialogBox.hide();
            sendToServer(null);
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
                sendToServer(null);
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
    
    public class CloserHandler
    implements CloserEventHandler
    {

        public void onClick(
            Event event)
        {
            TriggerSendSource = event.getEventTarget();
            sendButton.setEnabled(true);
            
            if (dialogBox.Selection==null)
                return;
            System.out.println("dialogSelectedRadio="
                + dialogBox.Selection.getSelected());
            RadioButton selected =
                dialogBox.Selection.getSelected();
            if (selected==null)
                return;
            String selectedTxt = selected.getText();
            String selectedValue = selected.getFormValue();
            
            // reset to 0 so that stale state would not propagate
            // thro a failure in any subsequent step,
            dialogBox.Selection.Buttons[0].setValue(true); 
            if (selectedTxt == null || selectedTxt.length() == 0)
                return;

            switch (currentAction)
            {
                case ListSheetDocs:
                    _currentDoc.setText("Current Doc:");
                    currentDoc.setText(selectedTxt);
                    currentDocKey = selectedValue;
                    _currentTable.setText("");
                    currentTable.setText("");
                    currentTableId = null;
                    _currentWorksheet.setText("");
                    currentWorksheet.setText("");
                    sendToServer(Actions.SetSheetDoc);
                    break;
                    
                case ListTables:
                    _currentTable.setText("current Table:");
                    currentTable.setText(selectedTxt);
                    currentTableId = selectedValue;
                    sendToServer(Actions.SetTable);
                    break;
                    
                case ListWorksheets:
                    _currentWorksheet.setText("current Worksheet:");
                    currentWorksheet.setText(selectedTxt);
                    sendToServer(Actions.SetWorksheet);
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

    static public Actions getAction(String actionStr)
    {
        try {
            return Actions.valueOf(actionStr); 
        }
        catch(Exception ex)
        {
            return Actions.NONE;
        }
    }
    
}
