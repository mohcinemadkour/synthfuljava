package org.synthful.gwt.widgets.client.ui;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

public class UIGrid
	extends Grid
{
	public @UiConstructor UIGrid(int rowCount, int columnCount){
		this.resize(rowCount, columnCount);
	}

	public void add(Widget w){
		int row = this.count/this.numColumns;
		int col = this.count - row*this.numColumns ;
		this.count++;
		if (this.numRows<row)
			this.numRows = row;
		this.setWidget(row, col, w);
	}
	
	public void add(String t){
		int row = this.count/this.numColumns;
		int col = this.count - row*this.numColumns;
		this.count++;
		this.setText(row, col, t);
	}
	protected int count=0;
}
