package org.synthful.gwt.mvp.client;

import java.io.Serializable;

import com.google.gwt.user.client.ui.Widget;

public interface WidgetFactory<W extends Widget>
extends Serializable{
	W createWidget(String key);
}
