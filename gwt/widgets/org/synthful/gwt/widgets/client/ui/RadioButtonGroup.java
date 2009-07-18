package org.synthful.gwt.widgets.client.ui;

import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.RadioButton;


public class RadioButtonGroup
{
    public RadioButtonGroup(
        CellPanel panel,
        String radioGroup,
        String[] labels,
        int defaultButtonIndex)
    {
        this.Panel = panel;
        this.Buttons = new RadioButton[labels.length];
        this.RadioGroup = radioGroup;
        this.mkButtons(labels);
        this.defaultButton
            = (defaultButtonIndex< this.Buttons.length)
            ? defaultButtonIndex
            : -1;
    }
    
    public RadioButtonGroup(
        CellPanel panel,
        String radioGroup,
        LabelValuePair[] labelValuePairs,
        int defaultButtonIndex)
    {
        this.Panel = panel;
        this.Buttons = new RadioButton[labelValuePairs.length];
        this.RadioGroup = radioGroup;
        this.mkButtons(labelValuePairs);
        this.defaultButton
            = (defaultButtonIndex< this.Buttons.length)
            ? defaultButtonIndex
            : -1;
    }

    void mkButtons(String[] labels)
    {
        for (int i=0; i<labels.length; i++)
        {
            this.Buttons[i] =
                new RadioButton(this.RadioGroup, labels[i]);
            this.Panel.add(this.Buttons[i]);            
        }
        
    }
    
    void mkButtons(LabelValuePair[] labelValuePairs)
    {
        for (int i=0; i<labelValuePairs.length; i++)
        {
            LabelValuePair lv = labelValuePairs[i];
            if (lv==null)
                continue;
            String label = lv.Label;
            String value = lv.Value;
            if (label == null || label.length()==0)
            {
                if (value==null || value.length()==0)
                    continue;
                label = value;
            }
            else if (value==null || value.length()==0)
            {
                if (label == null || label.length()==0)
                    continue;
                value = label;
            }
            
            
            this.Buttons[i] =
                new RadioButton(this.RadioGroup, label);
            this.Buttons[i].setFormValue(value);
            this.Panel.add(this.Buttons[i]);            
        }
        
    }
    
    public RadioButton getSelected()
    {
        for (int i=0; i<Buttons.length; i++)
            if (Buttons[i].getValue())
                return Buttons[i];
        
        if (defaultButton<0)
            return null;
        
        Buttons[defaultButton].setValue(true);
        return Buttons[defaultButton];
    }
    
    public RadioButton[] Buttons;
    String RadioGroup;
    int defaultButton;
    
    protected CellPanel Panel;
}
