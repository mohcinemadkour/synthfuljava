package org.synthful.gwt.mvp.client;

import java.util.ArrayList;

import org.synthful.gwt.javascript.client.JsonRemoteScriptCall;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public abstract class JsonDataSource<ItemIdType>
extends JsonRemoteScriptCall {
	@Override
	public void onJsonRSCResponse(JavaScriptObject jso) {
		if (jso==null) return;
		this.ignite();
		
	    JSONObject json = new JSONObject(jso);
	    
		JSONObject jsonSchema = getJSONObject(json, "schema");
		if (jsonSchema!=null){
			this.classes = getJSONObject(jsonSchema, "classes");
			doEntity(jsonSchema, null, null);
		}
		
		this.gnite();
	}
	
	void doEntity (
			JSONObject json,
			ArrayList<ItemIdType> parentItems,
			String sectionName){
		if (json==null || json.getJavaScriptObject() == null)
			return;
		
		String name = (sectionName!=null)?sectionName:getStringValue(json, "name");

		// classname : {name: classname, elements:[ ....]}
		EntitySection entSect = getEntitySection(json, name);
		if (entSect.isEntity) return;
		
		// type : {elements:[ ....]}
		entSect = getEntitySection(getJSONObject(json, "type"), name);
		if (entSect.isEntity) return;
		
		//Occurrence of element and type nodes are mutually exclusive
		doType(json, parentItems, name);
	}
	
	void doType(
			JSONObject json,
			ArrayList<ItemIdType> parentItems,
			String sectionName){
		
		ItemIdType itemId = null;
		String name = (sectionName!=null)?sectionName:getStringValue(json, "name");
		
		String className = getStringValue(json, "type");
		if(className!= null){
			// is className found in classes store?
			JSONValue typejsv = classes.get(className);

			// if so, process the stored class node as an entity
			if (typejsv!=null){
				JSONObject typejso = typejsv.isObject();
				if (typejso!=null){
					doEntity(typejso, null, name);
				}
			}
			
			// otherwise class not found, leaf/primitive type
			// .. time to actually create a field
			// or type is not a String name but another level of jsonobj
			// attempt to create field from jsonobj
			else
				itemId = createPrimitiveField(className, name);
		}
		
		else{
			JSONObject typejso = getJSONObject(json, "type");
			itemId = createTypeField(json, typejso, name);
		}

		if (parentItems!=null&& itemId!=null)
			parentItems.add(itemId);
	}
		

	/*==============================================
	 * Static util functions
	 ==============================================*/
	static public String getStringValue(JSONObject json, String key){
		if (json == null || key == null )
			return null;
		
		JSONValue typejsv = json.get(key);
		if (typejsv!=null){
			JSONString typestr = typejsv.isString();
			if (typestr!=null)
				return typestr.stringValue();
		}
		return null;
	}
	
	static public JSONArray getJSONArray(JSONObject json, String key){
		if (json == null || key == null )
			return null;
		
		JSONValue jsv = json.get(key);
		if (jsv!=null){
			return jsv.isArray();
		}
		return null;
	}
	
	static public JSONObject getJSONObject(JSONObject json, String key){
		if (json == null || key == null )
			return null;
		
		JSONValue jsv = json.get(key);
		if (jsv!=null){
			return jsv.isObject();
		}
		return null;
	}
	
	/**
	 * EntitySection class for recursive/hierarchical ui entity sections
	 *
	 */
	abstract public class EntitySection{
		public EntitySection(JSONObject json, String sectionName){
			this.itemIdList = igniteItemIdList();
			this.name = (sectionName!=null)?sectionName:getStringValue(json, "name");
			
			JSONArray elements = getJSONArray(json, "elements");
			if (elements==null)return;
			
			beginEntity(json);
			
			for (int i=0; i<elements.size(); i++){
				JSONValue jsv = elements.get(i);
				if (jsv!=null){
					doEntity(jsv.isObject(), itemIdList, null);
				}
			}
			
			endEntity();
			isEntity = true;
		}
		
		abstract public void beginEntity(JSONObject json);
		abstract public void endEntity();
		
		final public String name;
		final protected ArrayList<ItemIdType> itemIdList;
		public boolean isEntity=false;
	}
	
	abstract public void ignite();
	abstract public void gnite();
	abstract public ItemIdType createPrimitiveField(String className, String name);
	abstract public ItemIdType createTypeField(JSONObject json, JSONObject typejso, String name);
	abstract public ArrayList<ItemIdType> igniteItemIdList();
	abstract public EntitySection getEntitySection(JSONObject json, String sectionName);
	
	JSONObject classes;
}
