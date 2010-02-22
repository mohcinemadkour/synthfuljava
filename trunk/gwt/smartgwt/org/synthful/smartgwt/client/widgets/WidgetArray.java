package org.synthful.smartgwt.client.widgets;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

abstract public class WidgetArray<W extends Widget>
	extends Widget
	implements HasWidgets
{

	@Override
	public void clear() {
		this.widgets.clear();
	}

	@Override
	public Iterator<Widget> iterator() {
		return (Iterator<Widget>) this.widgets.iterator();
	}

	@Override
	public boolean remove(Widget w) {
		return this.widgets.remove(w);
	}

	protected ArrayList<W> widgets = new ArrayList<W>();
}
