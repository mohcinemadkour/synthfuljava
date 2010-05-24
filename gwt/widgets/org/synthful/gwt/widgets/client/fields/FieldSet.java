package org.synthful.gwt.widgets.client.fields;

import java.util.ArrayList;


import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.syntercourse.user.persistence.shared.Profile.FieldEntity;

public class FieldSet<T extends FieldEntity>
extends Composite {

	@UiConstructor
	public FieldSet(String caption) {
		this.caption = caption;
	}
	
	public void add(Widget w){
		if (w instanceof LabelledField<?, ?>){
			LabelledField<?, ?> f = (LabelledField<?, ?>)w;
			this.fields.add(f);
		}
	}
	
	public ArrayList<LabelledField<?, ?>> getFields() {
		return fields;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	final private ArrayList<LabelledField<?, ?>> fields =
		new ArrayList<LabelledField<?,?>>();
	
	protected String caption;
	protected String name;
	protected String description;
	protected T fieldEntity;
}
