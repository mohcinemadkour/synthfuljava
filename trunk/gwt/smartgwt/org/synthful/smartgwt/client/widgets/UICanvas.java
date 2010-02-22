package org.synthful.smartgwt.client.widgets;

import java.util.Iterator;

import org.synthful.smartgwt.client.HasWidgetsUtil;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;

public class UICanvas
	extends Canvas
	implements HasWidgets
{
	@Override
	public void add(Widget w) {
		super.addChild(w);
	}

	@Override
	public Iterator<Widget> iterator() {
		return HasWidgetsUtil.iterator(this);
	}

	@Override
	public boolean remove(Widget w) {
		return HasWidgetsUtil.remove(this, w);
	}

}
