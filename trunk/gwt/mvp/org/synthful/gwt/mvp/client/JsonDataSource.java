package org.synthful.gwt.mvp.client;

import org.synthful.gwt.javascript.client.JsonRemoteScriptCall;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public abstract class JsonDataSource
extends JsonRemoteScriptCall {
	@Override
	public void onJsonRSCResponse(JavaScriptObject jso) {
		if (jso==null) return;
		
	    JSONObject json = new JSONObject(jso);
	    
		JSONValue jsv = json.get("schema");
		if (jsv!=null){
			JSONValue jsClasses = json.get("classes");
			this.classes = (jsClasses!=null) ? jsClasses.isArray() : null;
			doEntity(jsv.isObject());
		}

	    doEntity(json);
	}
	
	private void doEntity(JSONObject json){
		if (json==null || json.getJavaScriptObject() == null)
			return;
		
		JSONValue jsv = json.get("name");
		String name = (jsv!=null) ? jsv.toString() : "";
		createFieldPanel(jsv);

		jsv = json.get("elements");
		if (jsv==null){
			jsv = json.get("type");
			createField(jsv);
		}
		
		JSONArray elements = jsv.isArray();
		if (elements==null) return;
		
		for (int i=0; i<elements.size(); i++){
			jsv = elements.get(i);
			if (jsv==null) continue;
			
		}
	}
	
	JSONArray classes;
	abstract public void createFieldPanel(JSONValue jsv);
	abstract public void createField(JSONValue jsv);
}
