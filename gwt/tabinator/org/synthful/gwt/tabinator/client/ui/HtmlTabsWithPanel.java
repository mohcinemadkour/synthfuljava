package org.synthful.gwt.tabinator.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

abstract public class HtmlTabsWithPanel
extends BaseHtmlTabPanel {
	public HtmlTabsWithPanel() {
	}

	public void onMouseDown(final Widget sender, final int x, final int y) {
		this.TabOnMouseDown((HTMLTab) sender);
	}

	public void TabOnMouseDown(HTMLTab tab) {
		this.ActiveTab = tab;
		this.Content.setWidget(tab.Content);
		for (int i = 0; i < this.TabBar.getWidgetCount(); i++) {
			Widget wid = this.TabBar.getWidget(i);
			if (wid != tab && wid instanceof HTMLTab) {
				wid.setStyleName(this.PanelStyles.Up);
				((HTMLTab) wid).Content.setVisible(false);
			}
		}
		this.setStyleName(null);
		this.Content.setStyleName(this.PanelStyles.Content);
		tab.setStyleName(this.PanelStyles.Down);
		tab.Content.setVisible(true);
	}

	public void addHtmlTab(HTMLTab htmlTab, Composite content) {
		this.TabBar.add(htmlTab);
		this.ignite(htmlTab, content);
	}

	final public void ignite(HTMLTab tab, Composite content) {
		tab.setStyleName(this.PanelStyles.Up);
		tab.Content = content;
		tab.TabPanel = this;
		tab.addMouseListener(this);

		tab.Content.setSize("100%", "100%");
		// this.Content = tab.Content;

		this.Container.add(tab.Content);
		this.Container.setCellVerticalAlignment(tab.Content,
				VerticalPanel.ALIGN_TOP);
		tab.Content.setVisible(false);
	}

	public FocusPanel Content = new FocusPanel();
}
