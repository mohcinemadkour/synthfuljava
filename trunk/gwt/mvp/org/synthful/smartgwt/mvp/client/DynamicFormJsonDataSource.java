package org.synthful.smartgwt.mvp.client;

import java.util.ArrayList;

import org.synthful.gwt.mvp.client.JsonDataSource;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SectionItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

abstract public class DynamicFormJsonDataSource
extends JsonDataSource<String> {
	
	@Override
	public ArrayList<String> igniteItemIdList() {
		return new ArrayList<String>();
	}
	
	@Override
	public EntitySection getEntitySection(JSONObject json, String sectionName) {
		return new DynamicFormEntitySection(json, sectionName);
	}
	
	private class DynamicFormEntitySection
	extends EntitySection{

		public DynamicFormEntitySection(JSONObject json, String sectionName) {
			super(json, sectionName);
		}
		
		@Override
		public void beginEntity(JSONObject json) {
			sectItem = new SectionItem();
			if (name!=null)
				sectItem.setDefaultValue(name);
			formItems.add(this.sectItem);
		}

		@Override
		public void endEntity() {
			this.sectItem.setItemIds(this.itemIdList.toArray(new String[0]));
		}
		
		SectionItem sectItem;
	}
	
	
	@Override
	public String createPrimitiveField(String className, String name){
		if (className.equals("String")){
			formItems.add(new TextItem(name));
			return name;
		}
		if (className.equals("boolean")){
			formItems.add(new CheckboxItem(name));
			return name;
		}
		if (className.equals("char")){
			TextItem t = new TextItem(name);
			t.setLength(1);
			//t.setWidth(30);
			formItems.add(t);
			return name;
		}
		if (className.equals("double")){
			TextItem t = new TextItem(name);
	        t.setType("float");
			formItems.add(t);
			return name;
		}
		if (className.equals("float")){
			TextItem t = new TextItem(name);
	        t.setType("float");
			formItems.add(t);
			return name;
		}
		if (className.equals("int")){
			TextItem t = new TextItem(name);
	        t.setType("integer");
	        t.setAttribute("editorType", "TextItem");
			formItems.add(t);
			return name;
		}
		if (className.equals("short")){
			TextItem t = new TextItem(name);
	        t.setType("integer");
	        t.setAttribute("editorType", "TextItem");
			formItems.add(t);
			return name;
		}
		if (className.equals("long")){
			TextItem t = new TextItem(name);
	        t.setType("integer");
	        t.setAttribute("editorType", "TextItem");
			formItems.add(t);
			return name;
		}
		return null;
	}
	
	@Override
	public String createTypeField(JSONObject json, JSONObject typejso, String name){
		
		JSONObject restrjso = getJSONObject(json, "restrictions");

		// type:{ "enum" : [ .... ] }
		JSONArray jsa = getJSONArray(typejso,"enum");
		if (jsa!=null && jsa.size()>0){
			String[] choices = new String[jsa.size()];
			for(int i=0; i<jsa.size(); i++){
				JSONValue jsv = jsa.get(i);
				JSONString jss = jsv.isString();
				choices[i] = (jss!=null) ? jss.stringValue() : jsv.toString();
			}
			
			SelectItem select = new SelectItem(name);
			select.setValueMap(choices);
			String multiValue  = getStringValue(restrjso, "multiValue");
			if (multiValue!=null && multiValue.equals("true"))
				select.setMultiple(true);
			
			formItems.add(select);
			
			return name;
			}
		return null;
	}

	@Override
	public void gnite() {
		final FormItem[] ff = {};
		if (formItems.size()>0)
			this.form.setItems(formItems.toArray(ff));
	}

	public DynamicForm getForm() {
		return form;
	}

	public void setForm(DynamicForm form) {
		this.form = form;
	}
	
	private DynamicForm form;
	final private ArrayList<FormItem> formItems = new ArrayList<FormItem>();
}
