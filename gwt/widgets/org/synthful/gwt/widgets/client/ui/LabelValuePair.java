package org.synthful.gwt.widgets.client.ui;

import com.google.gwt.user.client.ui.Label;

public class LabelValuePair
    extends Label
{
    public LabelValuePair(String label, String value)
    {
        this.Label = label;
        this.Value = value;
    }
    
    String Label;
    String Value;
}
