package com.blessedgeek.gwt.gdata.client;

import java.util.HashMap;

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

    final Button logInOutButton = new Button();

    final HTML header = new HTML("<h3>Gdata Table Manager</h3>");

    final ChoiceRadios actions = new ChoiceRadios("Action");
    
    Object TriggerSendSource;
    
    Actions currentAction;
    String currentDocKey;
    String currentTableId;
    
    final AuthFormPanel authform = new AuthFormPanel("_top");
    
    final String pageHref = Location.getHref();
    String pageBaseHref;
    String logInOutUrl;

    final SendButtonHandler sendhandler = new SendButtonHandler();
    final TableMgrDialog dialogBox = new TableMgrDialog();

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


        // Add a handler to send the name to the server
        this.sendButton.addClickHandler(sendhandler);
        this.header.addClickHandler(sendhandler);
        this.logInOutButton.addClickHandler(new LogInOutButtonHandler());
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
                    new LabelValuePair("List Table Info", "" + Actions.ListTableInfo),
                    new LabelValuePair("List Table Records", "" + Actions.ListTableRecords),
                    new LabelValuePair("Search for Record", "" + Actions.ShowSearch4Record),
                    new LabelValuePair("Query for Record", "" + Actions.ShowQuery4Record),
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
            }
        }
        // the following is triggered from DialogBox close button.
        // this.actions.getSelected() cannot be used because dialogbox
        // close button onclick handler resets it to 0.
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
            }
        }
        
        
        return parameters;
    }
    
    /**
     * Send the name from the wordField to the server and wait for a response.
     */
    private void sendToServer(Actions action)
    {
        HashMap<String, String> paramHash = mkSendParameters(action);
        
        //for these cases, don't send to server, just display the dialogbox
        if(this.currentAction==Actions.ShowSearch4Record)
        {
            dialogBox.showQueryWidgets("Enter literal to search<p/>");
            currentAction = Actions.Search4Record;
            return;
        }
        if(this.currentAction==Actions.ShowQuery4Record)
        {
            dialogBox.showQueryWidgets(
                "Enter query string using format<br/>{col-name}[=|&lt;|>]\"{literal string}\"<br/>[and|or] ...<p/>");
            currentAction = Actions.Query4Record;
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
                    
                    switch (currentAction)
                    {
                        case ListSheetDocs:
                            listDialogSelection(result, "key", "title");
                            dialogBox.setText("Select Document");
                            dialogBox.center();
                            break;
                        case ListTables:
                            listDialogSelection(result, "title", "title");
                            dialogBox.setText("Select Table");
                            dialogBox.center();
                            break;
                        case SetSheetDoc:
                        case SetTable:
                            break;
                            
                        default:
                            dialogBox.showRpcHtml(result);
                            break;
                    }
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
                                    new LabelValuePair(
                                        title.isString().stringValue(),
                                        key.isString().stringValue());
                            }
                            
                            // attach choice buttons to dialogVPanel, without default button
                            // with radio group name = SelectSheet
                            dialogBox.clear();
                            dialogBox.Selection =
                                new RadioButtonGroup(dialogBox.VPanel, "SelectSheet", options, 0);
                            dialogBox.center();
                        }
                    }
                    catch (Exception e)
                    {
                        dialogBox.showRpcHtml(result);
                    }        
                }
                
            
            });
    }
    
    class TableMgrDialog
    extends ScrollableDialogBox
    {
        public TableMgrDialog()
        {
            this.setText("GData Table Manager");
            this.setAnimationEnabled(true);
            
            VPanel.addStyleName("dialogVPanel");
            VPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
                    
            this.setSizePx(400,300);            
            this.setWidget(VPanel);
            
            this.QueryBox.setTextAlignment(TextBox.ALIGN_LEFT);
            
            this.CloserEventHandlers.add(new CloserHandler());
            
            this.Submit.setHTML("Send");
            this.Submit.addClickHandler(sendhandler);
        }
        
        @Override
        final public void add(Widget w)
        {
            this.VPanel.add(w);
        }
        
        @Override
        final public boolean remove(Widget w)
        {
            return this.VPanel.remove(w);
        }
        
        @Override
        final public void clear()
        {
            this.VPanel.clear();
        }
        
        final public void showQueryWidgets(String instructions)
        {
            this.clear();
            this.htmlContent.setHTML(instructions);
            this.add(this.htmlContent);
            this.add(this.QueryBox);
            this.add(this.Submit);
            this.QueryBox.setText("");
            this.center();
        }
        
        final void showHtml(String text)
        {
            this.clear();
            this.add(htmlContent);
            this.htmlContent.setHTML(text);
            this.center();
        }
        
        final public void showRpcHtml(String text)
        {
            this.htmlContent.removeStyleName("serverResponseLabelError");
            this.showHtml(text);
        }
        
        final public void showRpcFailureHtml()
        {
            this.htmlContent.addStyleName("serverResponseLabelError");
            this.showHtml(SERVER_ERROR);
        }
        
        final public VerticalPanel VPanel = new VerticalPanel();
        
        public RadioButtonGroup Selection;

        final public TextBox QueryBox = new TextBox();
        final public Button Submit = new Button();
        
        final HTML htmlContent = new HTML();
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
    
    private class CloserHandler
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
                    sendToServer(Actions.SetSheetDoc);
                    break;
                    
                case ListTables:
                    _currentTable.setText("current Table:");
                    currentTable.setText(selectedTxt);
                    currentTableId = selectedValue;
                    sendToServer(Actions.SetTable);
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
        ListTables, SetTable,
        AddTable, DeleteTable,
        ListTableInfo, ListTableRecords,
        AddRecord, DeleteRecord,
        ShowSearch4Record, Search4Record,
        ShowQuery4Record, Query4Record
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
