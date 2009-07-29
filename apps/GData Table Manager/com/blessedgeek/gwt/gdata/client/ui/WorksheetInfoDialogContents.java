package com.blessedgeek.gwt.gdata.client.ui;


public class WorksheetInfoDialogContents
extends DialogContents
{
    public WorksheetInfoDialogContents(TableMgrDialog dialogbox)
    {          
        super(dialogbox);
    }
    
    protected LabelParamPair[] defineFields()
    {
        this.paramFields = new LabelParamPair[]
        {
            LabelParamPair.mkTextBox("Title", "title"),
            LabelParamPair.mkTextBox("Number of Rows", "numRows"),
            LabelParamPair.mkTextArea("Number of Columns", "numCols"),
        };
        
        return this.paramFields;
    }
}
