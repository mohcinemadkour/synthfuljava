package com.blessedgeek.gwt.gdata.client.ui;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class TableInfoDialogContents
extends DialogContents
{
    public TableInfoDialogContents(TableMgrDialog dialogbox)
    {
        super(dialogbox);
    }
    
    protected LabelParamPair[] defineFields()
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
        
        return this.paramFields;
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
}
