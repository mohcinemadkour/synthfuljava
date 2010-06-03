package org.synthful.smartgwt.client.widgets;

import java.util.ArrayList;
import java.util.Iterator;

import org.synthful.smartgwt.client.HasWidgetsUtil;
import org.synthful.smartgwt.client.UIMasquerade;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tab.Tab;

public class UITab
	extends Widget
	implements HasWidgets, UIMasquerade<Tab>
{
	public UITab(){
		this.tab = new Tab();
	}
		
	public void add(Canvas w){
		if (this.canvas==null){
				this.canvas = (Canvas)w;
				tab.setPane(this.canvas);
			}
	}
	
	@Override
	public void add(Widget w){
		try{
			this.add((Canvas)w);
		}
		catch(Exception e){}
	}
	
	@Override
	public void clear()	{
	}
	
	@Override
	public Iterator<Widget> iterator()	{
		ArrayList<Widget> wx = new ArrayList<Widget>();
		wx.add(this.canvas);
		return wx.iterator();
	}

	@Override
	public boolean remove(Widget w){
		return HasWidgetsUtil.remove(this, w);
	}

	public void setCloseable(boolean closeable){
		this.tab.setCanClose(closeable);
	}
	
	public void setCloseIcon(String closeIcon){
		this.tab.setCloseIcon(closeIcon);
	}
	
	public void setCloseIconSize(String closeIconSize){
		try{
			int z = Integer.parseInt(closeIconSize);
			this.tab.setCloseIconSize(z);
		}
		catch (Exception e){}
	}
	
	public void setDisabled(boolean disabled){
		this.tab.setDisabled(disabled);
	}
	
	public void setTitle(String title){
		this.tab.setTitle(title);
	}
	
	public void setWidth(String width){
		try{
			int z = Integer.parseInt(width);
			this.tab.setWidth(z);
		}
		catch (Exception e){}
	}
	
	@Override
	public Tab getSmartObject() {
		return this.tab;
	}
	
	final protected Tab tab;
	private Canvas canvas;
}
