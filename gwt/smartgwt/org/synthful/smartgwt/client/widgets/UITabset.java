package org.synthful.smartgwt.client.widgets;

import java.util.Iterator;

import org.synthful.smartgwt.client.HasWidgetsUtil;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.tab.TabSet;

public class UITabset
	extends TabSet
	implements HasWidgets
{	
	@Override
	public void add(Widget w){
		if (w instanceof UITab)
			super.addTab(((UITab)w).tab);
		else
			super.addChild(w);
	}
	
	@Override
	public Iterator<Widget> iterator(){
		return HasWidgetsUtil.iterator(this);
	}

	@Override
	public boolean remove(Widget w){
		return HasWidgetsUtil.remove(this, w);
	}

}
