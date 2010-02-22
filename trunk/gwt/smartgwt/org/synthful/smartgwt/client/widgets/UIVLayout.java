package org.synthful.smartgwt.client.widgets;

import java.util.Iterator;

import org.synthful.smartgwt.client.HasWidgetsUtil;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.layout.VLayout;

public class UIVLayout
	extends VLayout
	implements HasWidgets
{
	public void add(Widget w){
		this.addMember(w);
	}

	@Override
	public Iterator<Widget> iterator()
	{
		return HasWidgetsUtil.iterator(this);
	}

	@Override
	public boolean remove(Widget w)
	{
		return HasWidgetsUtil.remove(this, w);
	}
}
