package org.synthful.gwt.widgets.client.fields;


import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.syntercourse.user.persistence.shared.Profile.FieldEntity;

public class LabelledFieldGrid<T extends FieldEntity>
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
			FieldSet<T> fs = (FieldSet<T>)w;
			this.resizeRows(r + fs.getFields().size() + 1);
			
			this.setHTML(r++, 0, "<br/>"+fs.getCaption());
			for (LabelledField<?, ?> f : fs.getFields()){
				this.setText(r, 0, f.getLabel());
				this.setWidget(r++, 1, f.getField());
			}
		}
		else{
			r = this.insertRow(r);
			this.setWidget(r, 0, w);
		}
	}

}
