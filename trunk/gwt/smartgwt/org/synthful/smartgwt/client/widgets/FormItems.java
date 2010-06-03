package org.synthful.smartgwt.client.widgets;

import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public interface FormItems {
	
	class UITextItem
	extends UIFormItem<TextItem>{
		public UITextItem(){
			super.item = new TextItem();
		}
	}
	
	class UICheckboxItem
	extends UIFormItem<CheckboxItem>{
		public UICheckboxItem(){
			super.item = new CheckboxItem();
		}
	}
	

}
