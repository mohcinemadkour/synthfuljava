package org.synthful.gwt.widgets.client.ui;

import org.synthful.gwt.widgets.client.ui.LabelValuePair;

import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;


public class VerticalRadioButtonGroup
    extends VerticalPanel
{
    public VerticalRadioButtonGroup(
        String radioGroup,
        String[] labels,
        int defaultButtonIndex)
    {
        this.RadioButtonGrouper =
            new RadioButtonGroup(
                this, radioGroup, labels, defaultButtonIndex);
    }
    
    public VerticalRadioButtonGroup(
        String radioGroup,
        LabelValuePair[] labelValuePairs,
        int defaultButtonIndex)
    {
        this.RadioButtonGrouper =
            new RadioButtonGroup(
                this, radioGroup, labelValuePairs, defaultButtonIndex);
    }

    public RadioButton getSelected()
    {
        return this.RadioButtonGrouper.getSelected();
    }
        

    protected RadioButtonGroup RadioButtonGrouper;
}
