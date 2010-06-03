package org.synthful.gwt.widgets.client.fields;


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
	
	@SuppressWarnings("unchecked")
	public void add(Widget w){
		int r = this.getRowCount();
		
		if(w instanceof LabelledField<?, ?>){
			r = this.insertRow(r);
			LabelledField<?, ?> f = (LabelledField<?, ?>)w;
			this.setText(r, 0, f.getLabel());
			this.setWidget(r, 1, f.getField());
		}
		else if (w instanceof FieldSet){
			FieldSet fs = (FieldSet)w;
			this.resizeRows(r + fs.getFields().size() + 1);
			
			this.setHTML(r++, 0, "<br/>"+fs.getCaption());
			r = fs.fillGrid(this, r);
		}
		else{
			r = this.insertRow(r);
			this.setWidget(r, 0, w);
		}
	}

}
