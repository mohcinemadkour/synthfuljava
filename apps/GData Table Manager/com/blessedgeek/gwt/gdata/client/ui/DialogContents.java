package com.blessedgeek.gwt.gdata.client.ui;

import java.util.HashMap;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

abstract public class DialogContents
extends AbsolutePanel
{
    public DialogContents(TableMgrDialog dialogbox)
    {
        this.dialogBox = dialogbox;
        this.defineFields();
        this.clearTextFields();
        this.addTextFields();
    }
    
    protected int margin=5, row = 0, colWidth = 100, rowHeight = 30;
    
    public void useDialogBoxButton(boolean use)
    {
        if (use)
            this.add(this.dialogBox.Submit, this.margin, this.margin + (row*rowHeight));
        else
            this.remove(this.dialogBox.Submit);
    }
    
    protected void add(Label l, TextBoxBase t)
    {
        if (l!=null)
        {
            this.add(l, 5, 5 + (row*rowHeight));
            l.setWidth(colWidth+"px");
            l.setHorizontalAlignment(Label.ALIGN_RIGHT);
        }
        if (t!=null)
        {
            this.add(t, 10 + colWidth, 5 + (row*rowHeight));
            if (t instanceof TextArea)
                row++;
        }
        
        row++;
    }
    
    protected void addTextFields()
    {
        for(LabelParamPair f: this.paramFields)
        {
            this.add(f.FieldLabel, f.Field);
        }
    }
    
    static protected String getJSONHashValue(JSONObject jsob, String hashkey)
    {
        JSONValue val = jsob.get(hashkey);
        if (val==null) return "";
        return val.isString().stringValue();
    }
    
    protected void mkParameter(String key, TextBoxBase t)
    {
        String s = t.getText();
        if (s.trim().length()>0)
            this.sendParameters.put(t.getName(), s);
    }

    public HashMap<String, String> mkParameters()
    {
        for(LabelParamPair f: this.paramFields)
        {
            this.mkParameter(f.Field.getName(), f.Field);
        }
        
        return this.sendParameters;
    }
    
    public void clearTextFields()
    {
        for(LabelParamPair f: this.paramFields)
        {
            f.Field.setText(blank);
        }
    }
    
    public void enableTextFields(boolean enable)
    {
        for(LabelParamPair f: this.paramFields)
        {
            f.Field.setEnabled(enable);
        }
    }
    
    abstract protected void defineFields();
       
    final protected HashMap<String, String> sendParameters = new HashMap<String, String>(5);
    
    protected TableMgrDialog dialogBox;
    
    protected LabelParamPair[] paramFields;
    
    final static protected class LabelParamPair
    {
        public LabelParamPair(String label, String name, TextBoxBase field)
        {
            this.FieldLabel = new Label(label);
            this.Field = field;
            this.Field.setName(name);
        }
        
        static public LabelParamPair mkTextBox(String label, String name)
        {
            return new LabelParamPair(label, name, new TextBox());
        }
        
        static public LabelParamPair mkTextArea(String label, String name)
        {
            return new LabelParamPair(label, name, new TextArea());
        }

        public Label FieldLabel;
        public TextBoxBase Field;
    }
    
    static private String blank = "";
}
