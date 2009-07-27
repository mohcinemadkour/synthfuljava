package com.blessedgeek.gwt.gdata.client.ui;

import org.synthful.gwt.widgets.client.ui.RadioButtonGroup;
import org.synthful.gwt.widgets.client.ui.ScrollableDialogBox;

import com.blessedgeek.gwt.gdata.client.TableMgr;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TableMgrDialog
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
            
            
            this.Submit.setHTML("Send");
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
            this.setText(instructions);
            this.center();
        }
        
        final public void showUpdateTable(String caption, String result)
        {
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
            this.setText(caption);
            this.center();  
        }
        
        final public void showTableInfo(String caption, String result)
        {
            this.clear();
            this.tableInfoStuffs.clearTextFields();
            result = result.trim();
            if (result!=null && result.length()>2)
                this.tableInfoStuffs.listTableInfo(result);
            this.add(tableInfoStuffs);
            this.tableInfoStuffs.useDialogBoxButton(false);
            this.tableInfoStuffs.enableTextFields(false);
            this.setText(caption);
            this.center();  
        }
        
        final public void showAddWorksheet(String caption)
        {
            this.clear();
            this.worksheetInfoStuffs.clearTextFields();
            this.add(worksheetInfoStuffs);
            this.worksheetInfoStuffs.addButton(this.Submit);
            this.worksheetInfoStuffs.enableTextFields(true);
            this.setText(caption);
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
            this.showHtml(TableMgr.SERVER_ERROR);
        }
        
        final public VerticalPanel VPanel = new VerticalPanel();
        
        public RadioButtonGroup Selection;

        final public TextArea QueryBox = new TextArea();
        final public Button Submit = new Button();
        
        final HTML htmlContent = new HTML();
        
        final public TableInfoDialogContents tableInfoStuffs =
            new TableInfoDialogContents(this);
        final public WorksheetInfoDialogContents worksheetInfoStuffs =
            new WorksheetInfoDialogContents();
}
