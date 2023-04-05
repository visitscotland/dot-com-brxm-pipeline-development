package com.visitscotland.brxm.components.navigation;

import org.hippoecm.hst.core.sitemenu.EditableMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;
import org.hippoecm.hst.core.sitemenu.HstSiteMenus;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RootMenuItem implements HstSiteMenu {

    private HstSiteMenu hstMenu;

    private List<HstSiteMenuItem> menuItems;

    private Date timeStamp;

    public RootMenuItem(HstSiteMenu hstMenu) {
        this.hstMenu = hstMenu;
        timeStamp = new Date();
    }

    public void setSiteMenuItems(List<HstSiteMenuItem> items) {
        this.menuItems = items;
    }

    @Override
    public List<HstSiteMenuItem> getSiteMenuItems() {
        return menuItems;
    }


    //--- Delegated methods

    @Override
    public HstSiteMenuItem getSelectSiteMenuItem() {
        return hstMenu.getSelectSiteMenuItem();
    }


    @Override
    public HstSiteMenus getHstSiteMenus() {
        return hstMenu.getHstSiteMenus();
    }

    @Override
    public HstSiteMenuItem getDeepestExpandedItem() {
        return hstMenu.getDeepestExpandedItem();
    }

    @Override
    public EditableMenu getEditableMenu() {
        return hstMenu.getEditableMenu();
    }

    @Override
    public String getName() {
        return hstMenu.getName();
    }

    @Override
    public boolean isExpanded() {
        return hstMenu.isExpanded();
    }

    public Date getTimeStamp(){
        return timeStamp;
    }
}
