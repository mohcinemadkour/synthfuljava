package com.blessedgeek.gwt.gdata.client.ui;

import org.synthful.gwt.widgets.client.ui.RadioButtonGroup;
import org.synthful.gwt.widgets.client.ui.ScrollableDialogBox;

import com.blessedgeek.gwt.gdata.client.TableMgr;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TableMgrDialog
    extends ScrollableDialogBox
{

        public TableMgrDialog(TableMgr tmgr)
        {
            this.tableMgr = tmgr;
            this.setText("GData Table Manager");
            this.setAnimationEnabled(true);
            
            VPanel.addStyleName("dialogVPanel");
            VPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
                    
            this.setSizePx(400,300);            
            this.setWidget(VPanel);
            
            this.QueryBox.setTextAlignment(TextBox.ALIGN_LEFT);
            
            
            this.Submit.setHTML("Send");
            this.Submit.setStyleName("sendButton");
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
        
        final public void showRadioButtons(String result, String hashkey1, String hashkey2)
        {
            if (tableMgr.Debug.getValue())
            {
                Window.alert("showRadioButtons");
            }
            this.clear();
            try {
                this.radioButtonJson.listDialogSelection(result, hashkey1, hashkey2);
            }
            catch (Exception e)
            {
                this.showRpcHtml(result);
            }        

            this.center();
        }
        
        final public void showQueryWidgets(String caption, String instructions)
        {
            this.clear();
            this.htmlContent.setHTML(instructions);
            this.add(this.htmlContent);
            this.add(this.QueryBox);
            this.add(this.Submit);
            this.QueryBox.setText("");
            this.setText(caption);
            this.center();
        }
        
        final public void showUpdateTable(String caption, String result)
        {
            if (tableMgr.Debug.getValue())
            {
                Window.alert("showUpdateTable");
            }
            this.clear();
            this.tableInfoStuffs.clearTextFields();
            if (result!=null)
            {
                result = result.trim();
                if (result.length()>2)
                    this.tableInfoStuffs.listTableInfo(result);
            }
            this.add(tableInfoStuffs);
            this.tableInfoStuffs.useDialogBoxButton(true);
            this.tableInfoStuffs.enableTextFields(true);
            this.tableInfoStuffs.adjustSize();
            this.setText(caption);
            this.center();  
        }
        
        final public void showTableInfo(String caption, String result)
        {
            if (tableMgr.Debug.getValue())
            {
                Window.alert("showTableInfo");
            }
            this.clear();
            this.tableInfoStuffs.clearTextFields();
            if (result!=null)
            {
                result = result.trim();
                if (result.length()>2)
                    this.tableInfoStuffs.listTableInfo(result);
            }
            this.add(tableInfoStuffs);
            this.tableInfoStuffs.useDialogBoxButton(false);
            this.tableInfoStuffs.enableTextFields(false);
            this.tableInfoStuffs.adjustSize();
            this.setText(caption);
            this.center();  
        }
        
        final public void showAddWorksheet(String caption)
        {
            if (tableMgr.Debug.getValue())
            {
                Window.alert("showAddWorksheet");
            }
            this.clear();
            this.worksheetInfoStuffs.clearTextFields();
            this.add(worksheetInfoStuffs);
            this.worksheetInfoStuffs.enableTextFields(true);
            this.worksheetInfoStuffs.useDialogBoxButton(true);
            this.worksheetInfoStuffs.adjustSize();
            this.setText(caption);
            this.center();  
        }
        
        final void showHtml(String text)
        {
            if (tableMgr.Debug.getValue())
            {
                Window.alert("showHtml");
            }
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
            if (tableMgr.Debug.getValue())
            {
                Window.alert("showRpcFailureHtml");
            }
            this.htmlContent.addStyleName("serverResponseLabelError");
            this.showHtml(TableMgr.SERVER_ERROR);
        }
        
        final public VerticalPanel VPanel = new VerticalPanel();
        
        public RadioButtonGroup Selection;
        public TableMgr tableMgr;

        final public TextArea QueryBox = new TextArea();
        final public Button Submit = new Button();
        
        final HTML htmlContent = new HTML();
        
        final public TableInfoDialogContents tableInfoStuffs =
            new TableInfoDialogContents(this);
        final public WorksheetInfoDialogContents worksheetInfoStuffs =
            new WorksheetInfoDialogContents(this);
        final private RadioButtonsDialogContents radioButtonJson =
            new RadioButtonsDialogContents(this);
}
