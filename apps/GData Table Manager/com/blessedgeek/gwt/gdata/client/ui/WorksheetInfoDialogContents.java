package com.blessedgeek.gwt.gdata.client.ui;

import java.util.HashMap;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

public class WorksheetInfoDialogContents
extends AbsolutePanel
{
    public WorksheetInfoDialogContents()
    {          
        this.clearTextFields();
        
        this.add(_Title, Title);
        this.add(_numrows, numrows);
        this.row ++;
        this.add(_numcols, numcols);
        this.row ++;
    }
    
    public void addButton(Button submit)
    {
        this.add(submit, 5, 5 + (row*rowHeight));            
    }
    
    int row = 0, colWidth = 100, rowHeight = 30;
    void add(Label l, TextBoxBase t)
    {
        if (l!=null)
        {
            this.add(l, 5, 5 + (row*rowHeight));
            l.setWidth(colWidth+"px");
            l.setHorizontalAlignment(Label.ALIGN_RIGHT);
        }
        if (t!=null)
            this.add(t, 10 + colWidth, 5 + (row*rowHeight));
        
        row++;
    }
    
    public HashMap<String, String> mkParameters()
    {
        this.sendParameters.put("title", this.Title.getText());
        this.sendParameters.put("numrows", this.numrows.getText());            
        this.sendParameters.put("numcols", this.numcols.getText());

        return this.sendParameters;
    }
    
    public void clearTextFields()
    {
        this.Title.setText("");
        this.numrows.setText("");
        this.numcols.setText("");
    }
    
    public void enableTextFields(boolean on)
    {
        this.Title.setEnabled(on);
        this.numrows.setEnabled(on);
        this.numcols.setEnabled(on);
    }
    
    final private Label _Title = new Label("Title");
    final private Label _numrows = new Label("Number of Rows");
    final private Label _numcols = new Label("Number of Columns");
    
    final public TextBox Title = new TextBox();
    final public TextBox numrows = new TextBox();
    final public TextBox numcols = new TextBox();
    
    
    final private HashMap<String, String> sendParameters = new HashMap<String, String>(5);
    
    final public Button Submit = new Button("Add Table");
}
