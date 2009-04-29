package org.synthful.gwt.tabinator.client.ui;

import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;

abstract public class BaseHtmlTabPanel
    extends Composite
    implements MouseListener
{
    public BaseHtmlTabPanel()
    {
    }
    
    public void addTabSpacer(
        Widget w)
    {
        this.TabBar.add(w);
    }

    public boolean selectTab(int tabIndex)
    {
        Widget w = this.TabBar.getWidget(tabIndex);
        if (w instanceof HTMLTab)
        {
            this.onMouseDown((HTMLTab)w, -1, -1);
            return true;
        }
        
        return false;
    }
    
    public void onMouseMove(Widget sender, int x, int y){};
    
    public void onMouseDown(
        final Widget sender, final int x, final int y){};

    public void onMouseUp(
        final Widget sender, final int x, final int y){};

    public void onMouseEnter(final Widget sender)
    {
        if (this.ActiveTab != sender)
            ((HTMLTab)sender).setStyleName(this.PanelStyles.Hover);
    }
    public void onMouseLeave(final Widget sender)
    {
        if (this.ActiveTab != sender)
            ((HTMLTab)sender).setStyleName(this.PanelStyles.Up);
    }
    
    public void alignTabBarVertically(VerticalAlignmentConstant a)
    {
        this.Container.setCellVerticalAlignment(this.TabBar, a);
    }

    public void alignTabBarHorizontally(HorizontalAlignmentConstant a)
    {
        this.Container.setCellHorizontalAlignment(this.TabBar, a);
    }

    protected CellPanel Container;

    public CellPanel TabBar;

    public HTML ActiveTab;
    
    public TabPanelStyles PanelStyles = new TabPanelStyles();
}
