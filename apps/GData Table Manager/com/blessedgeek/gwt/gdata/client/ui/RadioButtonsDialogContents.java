package com.blessedgeek.gwt.gdata.client.ui;

import org.synthful.gwt.widgets.client.ui.LabelValuePair;
import org.synthful.gwt.widgets.client.ui.RadioButtonGroup;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class RadioButtonsDialogContents
    extends DialogContents
{
    public RadioButtonsDialogContents(
        TableMgrDialog dialogbox)
    {
        super(dialogbox);
    }

    protected LabelParamPair[] defineFields()
    {
        this.paramFields = new LabelParamPair[0];
        return this.paramFields;
    }

    void listDialogSelection(
        String result, String hashkey1, String hashkey2)
    {
        JSONValue resultjs = JSONParser.parse(result);
        System.out.println(resultjs);
        JSONArray entriesjs = resultjs.isArray();
        if (entriesjs != null)
        {
            JSONObject entryjs = entriesjs.get(0).isObject();
            JSONValue dialogMsg = entryjs.get("message");
            dialogBox.setText(dialogMsg.isString().stringValue());

            LabelValuePair[] options = new LabelValuePair[entriesjs.size()];
            options[0] = new LabelValuePair("None", "");

            for (int i = 1; i < entriesjs.size(); i++)
            {
                entryjs = entriesjs.get(i).isObject();
                JSONValue key = entryjs.get(hashkey1);
                JSONValue title = entryjs.get(hashkey2);
                if (key == null || key.toString().length() == 0) continue;

                options[i] =
                    new LabelValuePair(title.isString().stringValue(), key
                        .isString().stringValue());
            }

            dialogBox.Selection =
                new RadioButtonGroup(dialogBox.VPanel, "SelectSheet", options,
                    0);
        }
    }
}
