package org.synthful.gwt.mvp.server;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

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
		final JsonObject schema = new JsonObject();
		schema.add("elements", elements);
		
		for (Class<? extends PresentationEntity> x : xx){
			JsonObject elem = new JsonObject();
			String name = this.getPresentableCaption(x);
			elem.addProperty("name", name);
			elem.addProperty("type", this.doEntity(x).get("name").getAsString());
			elements.add(elem);
		}
		
		// attach class defns to the root
		schema.add("classes", classes);
		this.jsonRoot.add("schema", schema);
	}
	
	/*****************************************************************
	 * doEntity
	 * @param cls
	 * @return
	 ******************************************************************/
	private JsonObject doEntity(Class<? extends PresentationEntity> cls){
		EntArgs entArgs = new EntArgs(cls);
		
		if (this.classes.has(entArgs.name))
			return this.classes.get(entArgs.name).getAsJsonObject();
				
		for(Field f : entArgs.fields){
			FieldArgs fldArgs = new FieldArgs(f);
			
			// if all fields has no seq, auto-seq and list them all
			// otherwise, list only sequenced fields
			if (!entArgs.useField(fldArgs))
				continue;
			
			//if Field is an entity
			if (PresentationEntity.class.isAssignableFrom(fldArgs.cls)) {
				@SuppressWarnings("unchecked")
				JsonObject ent = this.doEntity(
					(Class<? extends PresentationEntity>) fldArgs.cls);
				if (ent!=null && ent.entrySet().size()>0){
					fldArgs.elem.addProperty("name", this.getPresentableCaption(f));
					fldArgs.elem.addProperty("type", ent.get("name").getAsString());
					entArgs.elems.put(entArgs.seq, fldArgs.elem);
				}
				continue;
			}
						
			Type type = fldArgs.field.getType();
			// String and primitive types
			if (fldArgs.cls == String.class) {
				fldArgs.elem.addProperty("type", fldArgs.cls.getSimpleName());

				if (fldArgs.presFld != null)
						this.doStringRestrictions(fldArgs);
			}
			else if (fldArgs.cls.isPrimitive()) {
				fldArgs.elem.addProperty("type", fldArgs.cls.getName());

				if (fldArgs.presFld != null) {
					if (fldArgs.isInt())
						this.doIntRestrictions(fldArgs);
					else if (fldArgs.isFloat())
						this.doFloatRestrictions(fldArgs);
				}
			}
			
			else if (fldArgs.cls.isEnum()){
				doChoice(fldArgs.elem, fldArgs.cls);
			}
			
			else if (fldArgs.cls.isArray()) {
				// TBD: Need to retrieve array for choices from db
			}
			
			// if field is objectify key
			else if (com.googlecode.objectify.Key.class.isAssignableFrom(fldArgs.cls)){
				if (doObjectifyKey(entArgs, fldArgs)) continue;
			}
			
			// if field is google datastore key
			else if (Key.class.isAssignableFrom(fldArgs.cls)){
				// TBD
			}
			
			// if is a List
			else if (List.class.isAssignableFrom(fldArgs.cls)) {
				if (doList(entArgs, fldArgs)) continue;
			}
			else
				continue;
			
			// use node iff it has acquired child nodes
			// otherwise, use its type.
			if (fldArgs.elem.entrySet().size()==0)
				fldArgs.elem.addProperty("type", ((Class<?>)type).getSimpleName());
			
			entArgs.elems.put(entArgs.seq, fldArgs.elem);
			
			String fcaption=null;
			if (fldArgs.presFld!=null)
				fcaption = fldArgs.presFld.caption();
			if (fcaption==null || fcaption.length()==0)
				fcaption = fldArgs.field.getName();
			
			fldArgs.elem.addProperty("name", fcaption);
		}
		
		JsonObject entity = new JsonObject();
		entity.addProperty("name", entArgs.name);
		
		// append nodes in specified seq using self-sorted keys of TreeMap
		// to the elements branch of the entity
		JsonArray elements = new JsonArray();
		Iterator<Integer> elemkeys = entArgs.elems.navigableKeySet().iterator();
		while(elemkeys.hasNext())
			elements.add(
					entArgs.elems.get(elemkeys.next())
			);
		
		if  (elements.size()>0){
			entity.addProperty("name", entArgs.name);
			//entity.addProperty("id", cls.getCanonicalName());
			entity.add("elements", elements);
			//String ename = entity;
			this.classes.add(entArgs.name, entity);
		}
		
		return entity;
	}
	
	
	/*==========================================================
	 * do-Subroutines
	 ==========================================================*/
	
	
	private boolean doObjectifyKey(
			EntArgs eargs, FieldArgs fargs){
		Type type = fargs.field.getGenericType();
		if (type instanceof ParameterizedType) {
			//find the class embedded as generic parameter of an objectify key
			ParameterizedType ptype = (ParameterizedType) type;
			Type[] targs = ptype.getActualTypeArguments();
			@SuppressWarnings("unchecked")
			JsonObject ent =
				this.doEntity(
					(Class<? extends PresentationEntity>) targs[0]);
			if (ent!=null && ent.entrySet().size()>0){
				fargs.elem.addProperty("name", eargs.name);
				fargs.elem.addProperty("type", ent.get("name").getAsString());
				eargs.elems.put(eargs.seq, fargs.elem);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * doList
	 * @param elems
	 * @param fargs
	 * @param seq
	 * @return
	 */
	private boolean doList(
			EntArgs eargs, FieldArgs fargs){

		Type type = fargs.field.getGenericType();
		if (type instanceof ParameterizedType) {
			// get the class stored by the list 
			ParameterizedType ptype = (ParameterizedType) type;
			Type[] targs = ptype.getActualTypeArguments();
			
			// annotated as multichoice list
			if (fargs.presFld != null && fargs.presFld.multiValue()){
				JsonObject ent = doMultiChoice(fargs.field, (Class<?>)targs[0]);
				if (ent!=null && ent.entrySet().size()>0){
					String fid = fargs.cls.getCanonicalName()+'.'+((Class<?>)targs[0]).getCanonicalName();
					String fname = this.getPresentableCaption(fargs.field);
					fargs.elem.addProperty("name", fname);
												
					String xfname = this.classMap.getXClassName(fid, fname);
					this.classes.add(xfname, ent);
					fargs.elem.addProperty("type", xfname);
					eargs.elems.put(eargs.seq, fargs.elem);
				}
				return true;
			}
			// otherwise, it's a single choice list
			doChoice(fargs.elem, (Class<?>)targs[0]);
		}
		return false;
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
	 * @param cls
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
	private void doStringRestrictions(FieldArgs fargs){
		int z = -1;
		String zs = fargs.presFld.pattern();
		JsonObject e2 = new JsonObject();
		
		z = fargs.presFld.length();
		if (z>=0)
			e2.addProperty("length", z);

		z = fargs.presFld.maxLen();
		if (z>=0)
			e2.addProperty("maxLength", z);

		z = fargs.presFld.minLen();
		if (z>=0)
			e2.addProperty("minLength", z);
		
		if (zs!=null && zs.length()>0)
			e2.addProperty("pattern", zs);

		//zs = presFld.whiteSpace();
		
		if (e2.entrySet().size()>0){
			fargs.elem.add("restrictions", e2);
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
	private void doIntRestrictions(FieldArgs fargs){
		int z = -1;
		
		JsonObject e2 = new JsonObject();
		
		z = fargs.presFld.totalDigits();
		if (z>=0)
			e2.addProperty("totalDigits", z);
		
		if (e2.entrySet().size()>0){
			fargs.elem.add("restrictions", e2);
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
	private void doFloatRestrictions(FieldArgs fargs){
		int z = -1;
		float zf = -1;
		
		JsonObject e2 = new JsonObject();

		z = fargs.presFld.fractionDigits();
		if (z>=0)
			e2.addProperty("fractionDigits", z);

		zf = fargs.presFld.maxVal();
		if (zf>=0)
			e2.addProperty("maxInclusive", zf);
		
		zf = fargs.presFld.minVal();
		if (zf>=0)
			e2.addProperty("minInclusive", zf);
		
		
		if (e2.entrySet().size()>0){
			fargs.elem.add("restrictions", e2);
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
	
	
	/*===============================================================
	 * Classes 
	 ===============================================================*/
	/**
	 * EntArgs
	 */
	class EntArgs{
		EntArgs(Class<? extends PresentationEntity> cls){
			this.cls = cls;
			id = cls.getCanonicalName();
			name = getPresentableCaption(cls);
			name = classMap.getXClassName(id, name);
			elems = new TreeMap<Integer, JsonObject>();
			fields = cls.getFields();
			hasSeq = this.hasSequence(fields);
		}
		
		Class<? extends PresentationEntity> cls;
		Field[] fields;
		String id, name;
		boolean hasSeq;
		int seq = 0;
		
		private boolean hasSequence(Field[] ff){
			Presentable presFld;
			for(Field f : ff){
				presFld = f.getAnnotation(Presentable.class);
				if (presFld==null) continue;
				return true;
			}
			return false;
		}
		
		// if all fields has no seq, auto-seq and list them all
		// otherwise, list only sequenced fields
		public boolean useField(FieldArgs fieldArgs) {
			if (fieldArgs.presFld==null){
				if (hasSeq)
					return false;
				else
					seq++;
			}
			else {
				seq = fieldArgs.presFld.sequence();
				// when more than one field specifies the same seq#
				while (elems.containsKey(seq))
					seq++;//TBD: improve efficiency how seq is incremented
			}
			
			return true;
		}

		//Store in self sorting map first, where the key is the annotated seq#,
		//since we are not assured that Reflection cannot assure us that
		//class members will be drawn out in sequence of source-code.
		TreeMap<Integer, JsonObject> elems;
	}
	
	/**
	 * Field args per iteration
	 */
	class FieldArgs{
		FieldArgs(Field f){
			this.field = f;
			cls = f.getType();
			elem = new JsonObject();
			
			// if all members are devoid of @Presentable annotation
			// all members will be used.
			presFld = f.getAnnotation(Presentable.class);
		}
		
		boolean isInt(){
			return (cls == short.class || cls == int.class || cls == long.class);
		}
		boolean isFloat(){
			return (cls == double.class || cls == float.class);
		}
		
		Presentable presFld; JsonObject elem;
		Field field; Class<?> cls;
		
	}
	

	/**
	 * Store of classnames - the names of the UI classes (not java or gwt classes).
	 * 
	 * For privacy/security, a canonical name should not be exposed to the gwt client JSON.
	 * Therefore a classname has to be concocted from the canonical name.
	 * This class is a lookup if a canonical name is already mapped to a classname.
	 * If not, concoct one.<br/>
	 * 
	 * Two or more UI elements may have the same UI class. Therefore, they should refer
	 * to the same UI class rather than duplicating it in the JSON schema.<br/>
	 * 
	 * Uses NameVec class to prevent duplicate classnames. Duplicate classnames can
	 * occur because two or more canonical name coud suggest the same classname.
	 * @author blessedgeek
	 *
	 */
	class XClassMap {

		/**
		 * If classname is already found, do not store it.
		 * @param canonicalName
		 * @param name classname prefix
		 * @return the classname for a canonical name.
		 */
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

	/**
	 * Store a list of canonical names with the same classname prefix.
	 * To prevent duplicate classname, a numeric suffix is appended to constitute the classname.
	 * 
	 * @author blessedgeek
	 *
	 */
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
	final public JsonObject jsonRoot = new JsonObject();
}
