package org.synthful.gwt.mvp.client.fields;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.ui.TextBox;

public class LabelledTextField
extends LabelledField<TextBox, String> {

	
	@UiConstructor
	public LabelledTextField(String name, String labelText) {
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
	protected TextBox createField() {
		return new TextBox();
	}
}
