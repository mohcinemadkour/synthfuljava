package org.synthful.gwt.widgets.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class UIJavaScriptObject
extends Widget implements HasText {
	protected JavaScriptObject jso;

	@Override
	public String getText() {
		return jso.toSource();
	}

	@Override
	public void setText(String text) {
		jso = JsonUtils.unsafeEval(text);
	}

	public JavaScriptObject getJavaScriptObject() {
		return jso;
	}
}
