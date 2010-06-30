package org.synthful.gwt.mvp.server;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.apache.xerces.dom.DocumentImpl;
import org.synthful.gwt.mvp.client.PresentationEntity;
import org.synthful.gwt.mvp.client.PresentationEntity.Presentable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.appengine.api.datastore.Key;

public class UISchema
{
	public UISchema(PresentationEntity[] xx){
		this.setRootAttrs();
		for (PresentationEntity x : xx)
			this.doEntity(x.getClass());
		Iterator<String> entkeys = this.entities.navigableKeySet().iterator();
		while(entkeys.hasNext())
			this.root.appendChild(
				this.entities.get(entkeys.next())
			);
		xmldoc.appendChild(root);
	}
	
	private void setRootAttrs(){
		root.setAttribute(
			"xmlns", "http://www.w3.org/2001/XMLSchema");
		root.setAttribute(
			"targetNamespace", "urn:datasources.smartclient.com");
		root.setAttribute(
			"xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		root.setAttribute(
			"xmlns:tns", "urn:datasources.smartclient.com");
	}
	
	private Element doEntity(Class<? extends PresentationEntity> cls){
		String name = this.getPresentableCaption(cls);
		if (this.entities.containsKey(name))
			return this.entities.get(name);
				
		Element entity = xmldoc.createElement("xsd:complexType");
		entity.setAttribute("name", name);
		
		TreeMap<Integer, Element> elems = new TreeMap<Integer, Element>();
		Field[] ff = cls.getFields();
		boolean hasSeq = this.hasSequence(ff);
		
		Presentable presFld;
		int seq = 0;
		for(Field f : ff){
			Class<?> fcls = f.getType();
			//System.out.println(fcls);
			
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
			
			Element elem = xmldoc.createElement("xsd:element");
			//if Field is an entity
			if (PresentationEntity.class.isAssignableFrom(fcls)) {
				Element ent = this.doEntity(
					(Class<? extends PresentationEntity>) fcls);
				if (ent!=null && ent.hasChildNodes()){
					elem.setAttribute("name", this.getPresentableCaption(f));
					//elem.setAttribute("name", ent.getAttribute("name"));
					elem.setAttribute("type", ent.getAttribute("name"));
					elems.put(seq, elem);
				}
				continue;
			}
						
			Element e1 = xmldoc.createElement("xsd:simpleType");
			Type type = f.getType();
			
			if (fcls.isPrimitive() || fcls == String.class) {

				if (presFld != null) {
					this.doRestrictions(e1, presFld);
				}
			}
			else if (fcls.isEnum())
				doChoice(e1, fcls);
			else if (fcls.isArray()) {
				// TBD: Need to retrieve array for choices from db
			}
			else if (com.googlecode.objectify.Key.class.isAssignableFrom(fcls)){
				type = f.getGenericType();
				if (type instanceof ParameterizedType) {
					ParameterizedType ptype = (ParameterizedType) type;
					Type[] targs = ptype.getActualTypeArguments();
					Element ent =
						this.doEntity(
							(Class<? extends PresentationEntity>) targs[0]);
					if (ent!=null && ent.hasChildNodes()){
						elem.setAttribute("name", ent.getAttribute("name"));
						elem.setAttribute("type", ent.getAttribute("name"));
						elems.put(seq, elem);
					}
					continue;
				}
			}
			else if (Key.class.isAssignableFrom(fcls)){
				
			}
			
			// if is a List
			else if (List.class.isAssignableFrom(fcls)) {
				type = f.getGenericType();
				if (type instanceof ParameterizedType) {
					ParameterizedType ptype = (ParameterizedType) type;
					Type[] targs = ptype.getActualTypeArguments();
					
					// annotated as multichoice list
					if (presFld != null && presFld.multiValue()){
						Element ent = doMultiChoice(f, (Class<?>)targs[0]);
						if (ent!=null && ent.hasChildNodes()){
							elem.setAttribute("name", this.getPresentableCaption(f));
							//elem.setAttribute("name", ent.getAttribute("name"));
							elem.setAttribute("type", ent.getAttribute("name"));
							elems.put(seq, elem);
						}
						continue;
					}
					// otherwise, it's a single choice list
					doChoice(e1, (Class<?>)targs[0]);
				}
			}
			else
				continue;
			
			// use node iff it has acquired child nodes
			if (e1.hasChildNodes()){
				elem.setAttribute("minOccurs", "1");
				elem.appendChild(e1);
			}
			else
				elem.setAttribute("type", "xsd:"+((Class<?>)type).getSimpleName().toLowerCase());
			
			elems.put(seq, elem);
			
			String fcaption=null;
			if (presFld!=null)
				fcaption = presFld.caption();
			if (fcaption==null || fcaption.length()==0)
				fcaption = f.getName();
			
			elem.setAttribute("name", fcaption);
		}
		
		// append nodes in specified seq using self-sorted keys of TreeMap
		Iterator<Integer> elemkeys = elems.navigableKeySet().iterator();
		while(elemkeys.hasNext())
			entity.appendChild(
				elems.get(elemkeys.next())
			);
		
		if  (entity.hasChildNodes()){
			entity.setAttribute("name", name);
			this.entities.put(name, entity);
		}
		
		return entity;
	}
	
	private Element doMultiChoice(Field f, Class<?> fcls){
		String name = this.getPresentableCaption(f);
		if (this.entities.containsKey(name))
			return this.entities.get(name);
		
		Element entity = xmldoc.createElement("xsd:complexType");
		Object[] econs = fcls.getEnumConstants();
		for(Object econ: econs){
			Element elem = xmldoc.createElement("xsd:element");
			elem.setAttribute("type", "xsd:boolean");
			elem.setAttribute("name", ""+econ);
			entity.appendChild(elem);
		}
		if  (entity.hasChildNodes()){
			entity.setAttribute("name", name);
			this.entities.put(name, entity);
		}
		
		return entity;
	}
		
	private void doChoice(Element e1, Class<?> fcls){
		Object[] econs = fcls.getEnumConstants();
		Element e2 = xmldoc.createElement("xsd:restriction");
		for(Object econ: econs){
			Element e3 = xmldoc.createElement("xsd:enumeration");
			e3.setAttribute("value", ""+econ);
			e2.appendChild(e3);
		}
		
		if (e2.hasChildNodes()){
			e2.setAttribute("base", "xsd:string");
			e1.appendChild(e2);	
		}
	}
		
	private void doChoice(Element e1, Class<?>[] oo){
		Element e2 = xmldoc.createElement("xsd:restriction");
		e2.setAttribute("base", "xsd:string");
		for(Class<?> o: oo){
			Element e3 = xmldoc.createElement("xsd:enumeration");
			e3.setAttribute("value", ""+o);
			e2.appendChild(e3);
		}
		
		if (e2.hasChildNodes())
			e1.appendChild(e2);			
	}
	private void doRestrictions(Element e1, Presentable presFld){
		boolean hasRestrict = false;
		int z = -1;
		float zf = -1;
		String zs = null;
		
		Element e2 = xmldoc.createElement("xsd:restriction");
		
		z = presFld.length();
		if (z>=0)
			hasRestrict = createE3(e2, "length", "string", ""+z);

		z = presFld.maxLen();
		if (z>=0)
			hasRestrict = createE3(e2, "maxLength", "string", ""+z);

		z = presFld.minLen();
		if (z>=0)
			hasRestrict = createE3(e2, "minLength", "string", ""+z);
		
		zs = presFld.pattern();
		if (zs!=null && zs.length()>0)
			hasRestrict = createE3(e2, "pattern", "string", zs);

		//zs = presFld.whiteSpace();
		
		if (e2.hasChildNodes())
			e1.appendChild(e2);

		
		e2 = xmldoc.createElement("xsd:restriction");
		z = presFld.totalDigits();
		if (z>=0)
			hasRestrict = createE3(e2, "totalDigits", "integer", ""+z);
		
		if (e2.hasChildNodes())
			e1.appendChild(e2);

		e2 = xmldoc.createElement("xsd:restriction");
		z = presFld.fractionDigits();
		if (z>=0)
			hasRestrict = createE3(e2, "fractionDigits", "float", ""+z);

		zf = presFld.maxVal();
		if (zf>=0)
			hasRestrict = createE3(e2, "maxInclusive", "float", ""+zf);
		
		zf = presFld.minVal();
		if (zf>=0)
			hasRestrict = createE3(e2, "minInclusive", "float", ""+zf);
		
		if (e2.hasChildNodes())
			e1.appendChild(e2);

		//String[]zss = presFld.enumeration();
	}
	
	/*
	<xsd:element name="category" minOccurs="1">
	    <xsd:simpleType>
	        <xsd:restriction base="xsd:string">
	            <xsd:minLength value="1"/>
	            <xsd:maxLength value="128"/>
	        </xsd:restriction>
	    </xsd:simpleType>
	</xsd:element>	
	<xsd:element name="inStock"        type="xsd:boolean"/>
	<xsd:element name="nextShipment"   type="xsd:date"/>
     */
	private boolean createE3(Element e2, String restriction, String type, String value){
		e2.setAttribute("base", "xsd:"+type);
		Element e3 = xmldoc.createElement("xsd:"+restriction);
		e3.setAttribute("value", value);
		e2.appendChild(e3);
		return true;
	}
	
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
	
	final public Document xmldoc= new DocumentImpl();
	final Element root = xmldoc.createElementNS("xsd", "xsd:schema");
	final TreeMap<String, Element> entities =
		new TreeMap<String, Element>();
}
