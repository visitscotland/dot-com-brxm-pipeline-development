package com.visitscotland.brxm.components.navigation;

import org.hippoecm.hst.core.sitemenu.EditableMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;
import org.hippoecm.hst.core.sitemenu.HstSiteMenus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class RootMenuItemTest {

    @Test
    void encapsulatedMethods() {
        // The following method are encapsulated from the original HstMenuItem to ensure the out-of-the-box menu
        // functionality keeps working
        HstSiteMenu hsm = mock(HstSiteMenu.class);
        RootMenuItem item = new RootMenuItem(hsm);

        HstSiteMenus hsmMock = mock(HstSiteMenus.class);
        HstSiteMenuItem hsmiMock = mock(HstSiteMenuItem.class);
        EditableMenu editableMenu = mock(EditableMenu.class);

        when(hsm.getHstSiteMenus()).thenReturn(hsmMock);
        Assertions.assertEquals(hsmMock, item.getHstSiteMenus());
        verify(hsm).getHstSiteMenus();

        when(hsm.getDeepestExpandedItem()).thenReturn(hsmiMock);
        Assertions.assertEquals(hsmiMock, item.getDeepestExpandedItem());
        verify(hsm).getDeepestExpandedItem();

        when(hsm.getSelectSiteMenuItem()).thenReturn(hsmiMock);
        Assertions.assertEquals(hsmiMock, item.getSelectSiteMenuItem());
        verify(hsm).getSelectSiteMenuItem();

        when(hsm.getEditableMenu()).thenReturn(editableMenu);
        Assertions.assertEquals(editableMenu, item.getEditableMenu());
        verify(hsm).getEditableMenu();

        when(hsm.getName()).thenReturn("name");
        Assertions.assertEquals("name", item.getName());
        verify(hsm).getName();

        when(hsm.isExpanded()).thenReturn(true);
        Assertions.assertTrue(item.isExpanded());
        verify(hsm).isExpanded();
    }
}
