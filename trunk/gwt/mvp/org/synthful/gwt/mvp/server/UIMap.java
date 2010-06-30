package org.synthful.gwt.mvp.server;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import org.synthful.gwt.mvp.client.PresentationEntity;
import org.synthful.gwt.mvp.client.PresentationEntity.Presentable;

import com.google.appengine.api.datastore.Key;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class UIMap
{
	public UIMap(Class<? extends PresentationEntity>[] xx){
		// attach the elements to the root in seq order.
		JsonArray elements = new JsonArray();
		this.schema.add("elements", elements);
		
		for (Class<? extends PresentationEntity> x : xx){
			JsonObject elem = new JsonObject();
			String name = this.getPresentableCaption(x);
			elem.addProperty("name", name);
			elem.addProperty("type", this.doEntity(x).get("name").getAsString());
			elements.add(elem);
		}
		
		// attach class defns to the root
		this.schema.add("classes", classes);
		this.jsonRoot.add("schema", schema);
	}
	
	private void setRootAttrs(){
		JsonObject defaults = new JsonObject();
		this.jsonRoot.add("defaults", defaults);
	}
	
	private JsonObject doEntity(Class<? extends PresentationEntity> cls){
		String id = cls.getCanonicalName();
		String name = this.getPresentableCaption(cls);
		name = this.classMap.getXClassName(id, name);
		
		if (this.classes.has(name))
			return this.classes.get(name).getAsJsonObject();
				
		//Store in self sorting map first, where the key is the annotated seq#,
		//since we are not assured that Reflection cannot assure us that
		//class members will be drawn out in sequence of source-code.
		TreeMap<Integer, JsonObject> elems = new TreeMap<Integer, JsonObject>();
		Field[] ff = cls.getFields();
		boolean hasSeq = this.hasSequence(ff);
		
		Presentable presFld;
		int seq = 0;
		for(Field f : ff){
			Class<?> fcls = f.getType();
			
			// if all members are devoid of @Presentable annotation
			// all members will be used.
			presFld = f.getAnnotation(Presentable.class);
			
			// if all fields has no seq, auto-seq and list them all
			// otherwise, list only sequenced fields
			if (presFld==null){
				if (hasSeq)
					continue;
				else
					seq++;
			}
			else {
				seq = presFld.sequence();
				// when more than one field specifies the same seq#
				while (elems.containsKey(seq))
					seq++;//TBD: improve efficiency how seq is incremented
			}
			// End of seq check
			
			JsonObject elem = new JsonObject();
			
			//if Field is an entity
			if (PresentationEntity.class.isAssignableFrom(fcls)) {
				JsonObject ent = this.doEntity(
					(Class<? extends PresentationEntity>) fcls);
				if (ent!=null && ent.entrySet().size()>0){
					elem.addProperty("name", this.getPresentableCaption(f));
					elem.addProperty("type", ent.get("name").getAsString());
					elems.put(seq, elem);
				}
				continue;
			}
						
			Type type = f.getType();
			
			if (fcls.isPrimitive() || fcls == String.class) {

				if (presFld != null) {
					this.doStringRestrictions(elem, presFld);
				}
			}
			else if (fcls.isEnum())
				doChoice(elem, fcls);
			else if (fcls.isArray()) {
				// TBD: Need to retrieve array for choices from db
			}
			
			// if field is objectify key
			else if (com.googlecode.objectify.Key.class.isAssignableFrom(fcls)){
				type = f.getGenericType();
				if (type instanceof ParameterizedType) {
					//find the class embedded as generic parameter of an objectify key
					ParameterizedType ptype = (ParameterizedType) type;
					Type[] targs = ptype.getActualTypeArguments();
					JsonObject ent =
						this.doEntity(
							(Class<? extends PresentationEntity>) targs[0]);
					if (ent!=null && ent.entrySet().size()>0){
						elem.addProperty("name", name);
						elem.addProperty("type", ent.get("name").getAsString());
						elems.put(seq, elem);
					}
					continue;
				}
			}
			
			// if field is google datastore key
			else if (Key.class.isAssignableFrom(fcls)){
				
			}
			
			// if is a List
			else if (List.class.isAssignableFrom(fcls)) {
				type = f.getGenericType();
				if (type instanceof ParameterizedType) {
					// get the class stored by the list 
					ParameterizedType ptype = (ParameterizedType) type;
					Type[] targs = ptype.getActualTypeArguments();
					
					// annotated as multichoice list
					if (presFld != null && presFld.multiValue()){
						JsonObject ent = doMultiChoice(f, (Class<?>)targs[0]);
						if (ent!=null && ent.entrySet().size()>0){
							String fid = fcls.getCanonicalName()+'.'+((Class<?>)targs[0]).getCanonicalName();
							String fname = this.getPresentableCaption(f);
							elem.addProperty("name", fname);
														
							String xfname = this.classMap.getXClassName(fid, fname);
							this.classes.add(xfname, ent);
							elem.addProperty("type", xfname);
							elems.put(seq, elem);
						}
						continue;
					}
					// otherwise, it's a single choice list
					doChoice(elem, (Class<?>)targs[0]);
				}
			}
			else
				continue;
			
			// use node iff it has acquired child nodes
			if (elem.entrySet().size()==0)
				elem.addProperty("type", ((Class<?>)type).getSimpleName().toLowerCase());
			
			elems.put(seq, elem);
			
			String fcaption=null;
			if (presFld!=null)
				fcaption = presFld.caption();
			if (fcaption==null || fcaption.length()==0)
				fcaption = f.getName();
			
			elem.addProperty("name", fcaption);
		}
		
		JsonObject entity = new JsonObject();
		entity.addProperty("name", name);
		
		// append nodes in specified seq using self-sorted keys of TreeMap
		// to the elements branch of the entity
		JsonArray elements = new JsonArray();
		Iterator<Integer> elemkeys = elems.navigableKeySet().iterator();
		while(elemkeys.hasNext())
			elements.add(
				elems.get(elemkeys.next())
			);
		
		if  (elements.size()>0){
			entity.addProperty("name", name);
			//entity.addProperty("id", cls.getCanonicalName());
			entity.add("elements", elements);
			//String ename = entity;
			this.classes.add(name, entity);
		}
		
		return entity;
	}
	
	
	private JsonObject doMultiChoice(Field f, Class<?> fcls){
		String name = this.getPresentableCaption(f);
		if (this.classes.has(name))
			return this.classes.get(name).getAsJsonObject();
		
		JsonObject elem = new JsonObject();
		elem.addProperty("name", name);
		doChoice(elem,  fcls);		
		return elem;
	}
	
	/**
	 * 
		{
			name: st,
			type: {
				enum:[
					AK, CA, GA, MA, PA
				],
				enum:[
					{caption: China, value: zh, description: Made in China},
					{caption: India, value: in, description: Made in India},
					{caption: Switzerland, value: ch, description: Swiss made},
					US,
					UK
				]
			}
		}
	 * 
	 * 
	 * @param e1
	 * @param oo
	 *
	 * 
	 * @param e1
	 * @param fcls
	 */
	private void doChoice(JsonObject elem, Class<?> fcls){
		Object[] econs = fcls.getEnumConstants();
		JsonArray e2 = new JsonArray();
		for(Object econ: econs){
			e2.add( new JsonPrimitive(""+econ));
		}
		
		if (econs.length>0){
			JsonObject e1 = new JsonObject();
			e1.add("enum", e2);
			elem.add("type", e1);
		}
	}
	
	/**
	 * 
		{
			name: st,
			type: {
				enum:[
					AK, CA, GA, MA, PA
				]
			}
		}
	 * 
	 * 
	 * @param e1
	 * @param oo
	 */
	private void doChoice(JsonObject elem, Class<?>[] oo){
		
		JsonArray e2 = new JsonArray();
		for(Class<?> o: oo){
			e2.add( new JsonPrimitive(""+o));
		}
		
		if (oo.length>0){
			JsonObject e1 = new JsonObject();
			e1.add("enum", e2);
			elem.add("type", e1);
		}
	}
	
	/**
	 * 
		{
			name: street,
			type: String,
			restrictions: {
				maxLength: '20',
			 	minLength: '2
			}
		}
	 * 
	 * @param elem
	 * @param presFld
	 */
	private void doStringRestrictions(JsonObject elem, Presentable presFld){
		int z = -1;
		String zs = presFld.pattern();
		JsonObject e2 = new JsonObject();
		
		z = presFld.length();
		if (z>=0)
			e2.addProperty("length", z);

		z = presFld.maxLen();
		if (z>=0)
			e2.addProperty("maxLength", z);

		z = presFld.minLen();
		if (z>=0)
			e2.addProperty("minLength", z);
		
		if (zs!=null && zs.length()>0)
			e2.addProperty("pattern", zs);

		//zs = presFld.whiteSpace();
		
		if (e2.entrySet().size()>0){
			elem.add("restrictions", e2);
		}
	}
	
	
	/**
	 * 
		{
			name: seq,
			type: int,
			restrictions: {
				totalDigits: '2',
			}
		}

	 * @param elem
	 * @param presFld
	 */
	private void doIntRestrictions(JsonObject elem, Presentable presFld){
		int z = -1;
		float zf = -1;
		
		JsonObject e2 = new JsonObject();
		
		z = presFld.totalDigits();
		if (z>=0)
			e2.addProperty("totalDigits", z);
		
		if (e2.entrySet().size()>0){
			elem.add("restrictions", e2);
		}
	}
	
	/**
	 * 
		{
			name: improvement,
			type: float,
			restrictions: {
				maxInclusive: '10',
				minxInclusive: '-10',
			 	fractionDigits: '2
			}
		}
	 * 
	 * @param elem
	 * @param presFld
	 */
	private void doFloatRestrictions(JsonObject elem, Presentable presFld){
		int z = -1;
		float zf = -1;
		
		JsonObject e2 = new JsonObject();

		z = presFld.fractionDigits();
		if (z>=0)
			e2.addProperty("fractionDigits", z);

		zf = presFld.maxVal();
		if (zf>=0)
			e2.addProperty("maxInclusive", zf);
		
		zf = presFld.minVal();
		if (zf>=0)
			e2.addProperty("minInclusive", zf);
		
		
		if (e2.entrySet().size()>0){
			elem.add("restrictions", e2);
		}
	}
	
	/*
	 * JSON:
	 * 
	 canvas:{
	 	classes:{
			Sepr:{
				type: Label,
				value: -
			},
			Label:{
				type: String,
				parameters:[value],
				restrictions:{readOnly}
			},
			ValidProd:{
				type:{
					enum:[
						12345,
						P289,
						Jaguar
					]
				}
			}
			ValidCn:{
				type:{
					enum:[
						{caption: China, value: zh, description: Made in China},
						{caption: India, value: in, description: Made in India},
						{caption: Switzerland, value: ch, description: Swiss made},
						US,
						UK
					]
				}
			}
	 	},
		elements:[
			{
				name: product,
				type: String,
				restrictions: {
					maxLength: '20',
				 	minLength: '2
				}
			},
			{
				name: shipTo
				type:postalAddress
			},
			{
				name: order,
				elements:[
					{
						name: prd,
						type: ValidProd
					},
					Sepr,
					{
						name: st,
						type: {
							enum:[
								AK, CA, GA, MA, PA
							]
						}
					}
					Sepr,
					{
						name: seq,
						type: String,
						restrictions: {
							maxLength: '2',
						 	minLength: '2
						}
					}
				]
			}
		]
	}
     */
	
	private String getPresentableCaption(Field f){
		Presentable presFld = f.getAnnotation(Presentable.class);
		String caption=null;
		if (presFld!=null)
			caption = presFld.caption();
		
		if (caption==null || caption.length()==0)
			caption=f.getName();
		
		return caption;
	}

	private String getPresentableCaption(Class<?> cls){
		String caption=null;
		Presentable presFld = cls.getAnnotation(Presentable.class);
		if (presFld!=null)
			caption = presFld.caption();
		
		if (caption==null || caption.length()==0)
			caption=cls.getSimpleName();
		
		return caption;
	}
	
	private boolean hasSequence(Field[] ff){
		Presentable presFld;
		for(Field f : ff){
			presFld = f.getAnnotation(Presentable.class);
			if (presFld==null) continue;
			return true;
		}
		return false;
	}
	

	class XClassMap {

		String getXClassName(String canonicalName, String name) {
			NameVec vname = nameVecHash.get(canonicalName);
			if (vname != null) {
				int i = vname.indexOf(canonicalName);
				name = nameHash.get(canonicalName);
				return name + i;
			}

			vname = new NameVec(canonicalName);
			nameVecHash.put(canonicalName, vname);
			nameHash.put(canonicalName, name);
			return name + 0;
		}

		final HashMap<String, String> nameHash = new HashMap<String, String>();
		final HashMap<String, NameVec> nameVecHash = new HashMap<String, UIMap.NameVec>();
	}

	@SuppressWarnings("serial")
	class NameVec extends ArrayList<String> {

		NameVec(String canonicalName) {
			super.add(canonicalName);
		}

		int indexOf(String canonicalName) {
			int i = super.indexOf(canonicalName);
			if (i >= 0)
				return i;
			super.add(canonicalName);
			return size() - 1;
		}
	}

	final public XClassMap classMap = new XClassMap();
	final public JsonObject classes = new JsonObject(); // class defns
	final public JsonObject schema = new JsonObject();
	final public JsonObject jsonRoot = new JsonObject();
}
