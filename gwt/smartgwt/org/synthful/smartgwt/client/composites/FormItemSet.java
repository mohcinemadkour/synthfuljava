package org.synthful.smartgwt.client.composites;

import org.synthful.gwt.widgets.client.fields.FieldEntity;
import org.synthful.gwt.widgets.client.fields.FieldSetDescriptor;
import org.synthful.smartgwt.client.widgets.UIFormItem;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.form.fields.FormItem;

public class FormItemSet<F extends FieldEntity>
extends FieldSetDescriptor<F, FormItem>{
	@UiConstructor
	public FormItemSet(String caption) {
		this.caption = caption;
	}
	
	public void add(Widget w){
		if (w instanceof UIFormItem<?>){
			UIFormItem<?> f = (UIFormItem<?>)w;
			this.fields.add(f.getFormItem());
		}
	}
	
	public void addItems(FormItem[] items){
		for (FormItem i: items){
			this.fields.add(i);
		}
	}
	

}
