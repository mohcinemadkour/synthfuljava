package org.synthful.gwt.widgets.client;

import org.synthful.gwt.widgets.client.fields.LabelledField;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

public class LabelledFieldGrid
extends Grid
{

	public LabelledFieldGrid() {
		this.resizeColumns(2);
	}

	public LabelledFieldGrid(int rows, int columns) {
		super(rows, columns);
	}
	
	public void add(Widget w){
		int r = this.getRowCount();
		r = this.insertRow(r);
		
		if(w instanceof LabelledField<?, ?>){
			LabelledField<?, ?> f = (LabelledField<?, ?>)w;
			this.setText(r, 0, f.getLabel());
			this.setWidget(r, 1, f.getField());
		}
		else{
			this.setWidget(r, 0, w);
		}
	}

}
