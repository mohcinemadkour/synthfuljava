package org.synthful.smartgwt.client.widgets;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.form.fields.FormItem;

abstract public class UIFormItem<F extends FormItem>
extends Widget{

	public F getFormItem() {
		return this.item;
	}
	
	public void setName(String name){
		this.item.setName(name);
	}

	public void setTitle(String title){
		this.item.setTitle(title);
	}

	protected F item;
}
