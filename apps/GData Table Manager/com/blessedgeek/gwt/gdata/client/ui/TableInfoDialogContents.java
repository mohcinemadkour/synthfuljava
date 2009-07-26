package com.blessedgeek.gwt.gdata.client.ui;

import java.util.HashMap;

import org.synthful.gwt.widgets.client.ui.LabelValuePair;
import org.synthful.gwt.widgets.client.ui.RadioButtonGroup;

import com.blessedgeek.gwt.gdata.client.ui.DialogContents.LabelParamPair;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

public class TableInfoDialogContents
extends DialogContents
{
    public TableInfoDialogContents(TableMgrDialog dialogbox)
    {
        super(dialogbox);
    }
    
    protected void defineFields()
    {
        this.paramFields = new LabelParamPair[]
        {
            LabelParamPair.mkTextBox("Title", "title"),
            LabelParamPair.mkTextArea("Summary", "summary"),
            LabelParamPair.mkTextBox("Worksheet", "worksheet"),
            LabelParamPair.mkTextBox("Header", "header"),
            LabelParamPair.mkTextBox("StartRow", "startRow"),
            LabelParamPair.mkTextBox("Number of Rows", "numRows"),
            LabelParamPair.mkTextArea("Columns", "columns"),
        };
    }
   
    void listTableInfo(String result)
    {
        JSONValue resultjs = JSONParser.parse(result);
        System.out.println(resultjs);
        JSONObject entriesjs = resultjs.isObject();
                 
        for(LabelParamPair f: this.paramFields)
        {
            f.Field.setText(
                getJSONHashValue(
                    entriesjs, f.Field.getName()
                )
            );
        }
    }
    
    /*
    public HashMap<String, String> mkParameters()
    {
        this.sendParameters.put("title", this.Title.getText());
        this.sendParameters.put("summary", this.Summary.getText());
        this.sendParameters.put("worksheet", this.Worksheet.getText());
        this.sendParameters.put("header", this.Header.getText());
        this.sendParameters.put("startrow", this.Startrow.getText());            
        this.sendParameters.put("columns", this.Columns.getText());
        this.sendParameters.put("numrows", "2");            

        return this.sendParameters;
    }

    public void clearTextFields()
    {
        this.Title.setText("");
        this.Summary.setText("");
        this.Worksheet.setText("");
        this.Header.setText("");
        this.Startrow.setText("");
        this.Columns.setText("");
    }
    
    public void enableTextFields(boolean on)
    {
        this.Title.setEnabled(on);
        this.Summary.setEnabled(on);
        this.Worksheet.setEnabled(on);
        this.Header.setEnabled(on);
        this.Startrow.setEnabled(on);
        this.Columns.setEnabled(on);
    }
    
    final private Label _Title = new Label("Title");
    final private Label _Summary = new Label("Summary");
    final private Label _Worksheet = new Label("Worksheet");
    final private Label _Header = new Label("Header");
    final private Label _Startrow = new Label("Startrow");
    final private Label _Columns = new Label("Columns");
    final private Label _NumRows = new Label("Number of Rows");
    
    final public TextBox Title = new TextBox();
    final public TextArea Summary = new TextArea();
    final public TextBox Worksheet = new TextBox();
    final public TextBox Header = new TextBox();
    final public TextBox Startrow = new TextBox();
    final public TextArea Columns = new TextArea();
    final private TextBox NumRows = new TextBox();
    */
}
