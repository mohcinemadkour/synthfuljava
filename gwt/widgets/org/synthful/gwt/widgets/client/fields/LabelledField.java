package org.synthful.gwt.widgets.client.fields;


import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

abstract public class LabelledField<
	FldTyp extends Widget & HasName & HasText & HasValue<ValTyp>,
	ValTyp>
extends Composite {
	@UiConstructor
	public LabelledField(String name, String labelText){
		this.name = name;
		this.label= labelText;
		this.field = this.createField();
		//this.setHorizontal();
	}
	
	abstract protected FldTyp createField();
	
	public String getLabel() {
		return label;
	}
	
	public LabelledFieldGrid getGrid() {
		return grid;
	}

	public void setGrid(LabelledFieldGrid grid) {
		this.grid = grid;
	}

	public FldTyp getField() {
		return field;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void updateServer(){
		labelledFieldServicer.setValue(name, this.field.getValue(), updateServerCallback);
	}
	
	protected AsyncCallback<Void> updateServerCallback = new AsyncCallback<Void>() {
		@Override
		public void onSuccess(Void result) {
		}
		
		@Override
		public void onFailure(Throwable caught) {
		}
	};
	
	public void getFromServer(){
		labelledFieldServicer.getValue(name, getFromServerCallback );
	}
	
	protected AsyncCallback<String> getFromServerCallback = new AsyncCallback<String>() {
		@Override
		public void onSuccess(String result) {
		}
		
		@Override
		public void onFailure(Throwable caught) {
		}
	};
	
	public void setHorizontal(){
	}
	public void setVertical(){
	}

	public interface LabelledFieldServicerAsync<ValueType> {

		void getValue(String name, AsyncCallback<String> callback);
		void setValue(String name, ValueType value, AsyncCallback<Void> callback);
	}
	
	public interface LabelledFieldServicer<ValueType>
	extends RemoteService{
		String getValue(String name);
		void setValue(String name, ValueType value);
	}

	protected LabelledFieldServicerAsync<ValTyp> labelledFieldServicer;
	
	public String name;
	protected LabelledFieldGrid grid;
	protected String label;
	protected FldTyp field;
	protected String description;
}
