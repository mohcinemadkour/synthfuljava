package org.synthful.gwt.widgets.client.ui;

import org.synthful.gwt.widgets.client.ui.RadioButtonGroup.LabelValuePair;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;

public class HorizontalRadioButtonGroup
    extends HorizontalPanel
{
    public HorizontalRadioButtonGroup(
        String radioGroup,
        String[] labels,
        int defaultButtonIndex)
    {
        this.RadioButtonGrouper =
            new RadioButtonGroup(
                this, radioGroup, labels, defaultButtonIndex);
    }
    
    public HorizontalRadioButtonGroup(
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
