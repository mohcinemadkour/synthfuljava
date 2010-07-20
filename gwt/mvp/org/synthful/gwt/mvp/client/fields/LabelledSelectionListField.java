package org.synthful.gwt.mvp.client.fields;


import org.synthful.gwt.widgets.client.ui.UISelectionList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

public class LabelledSelectionListField<
	Multi extends LabelledSelectionListField.MultiSelect>
extends LabelledField<UISelectionList, String> {
	
	@UiConstructor
	public LabelledSelectionListField(
		String name, String labelText) {
		
		super(name, labelText);
	}
	
	@RemoteServiceRelativePath("LabelledFieldServicer")
	public interface labelledTextFieldServicer
	extends LabelledFieldServicer<String>{}
	public interface labelledTextFieldServicerAsync
	extends LabelledFieldServicerAsync<String>{}
	
	protected LabelledFieldServicerAsync<String> labelledFieldServicer =
		GWT.create(labelledTextFieldServicer.class);

	@Override
	protected UISelectionList createField() {
		return new UISelectionList(Multi.is);
	}
	
	static public interface MultiSelect{
		boolean is = true;
	}
	static public interface NotMultiSelect
	extends MultiSelect{
		boolean is = false;
	}
}
