package org.synthful.gwt.widgets.client.ui;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.NamedFrame;

public class UINamedFrame
	extends NamedFrame
{
	public @UiConstructor
	UINamedFrame(String name) {
		super(name);
	}

	public final native void clear() /*-{
		if ( $wnd.history.length > 0){
			$wnd.history.go(-history.length);}
	}-*/;
}
