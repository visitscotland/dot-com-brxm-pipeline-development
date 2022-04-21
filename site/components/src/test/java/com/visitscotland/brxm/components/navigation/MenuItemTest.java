package com.visitscotland.brxm.components.navigation;

import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuItemTest {

    @Test
    void add_remove_ChildMenuItem() {
        //Addition and removal of children is correct
        MenuItem parent = new MenuItem(null);

        MenuItem child1 = new MenuItem(null);
        MenuItem child2 = new MenuItem(null);
        MenuItem child3 = new MenuItem(null);

        assertNotNull(parent.getChildMenuItems());
        assertEquals(0, parent.getChildMenuItems().size());

        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);

        assertEquals(3, parent.getChildMenuItems().size());

        assertTrue(parent.removeChild(child1));
        assertTrue(parent.removeChild(child2));
        assertTrue(parent.removeChild(child3));

        assertEquals(0, parent.getChildMenuItems().size());
    }

    @Test
    void add_remove_duplicated_ChildMenuItem() {
        //Adding a child twice does not cause duplicates
        //Removing a non existing child does not break the parent
        MenuItem parent = new MenuItem(null);

        MenuItem child1 = new MenuItem(parent);

        parent.addChild(child1);
        parent.addChild(new MenuItem(parent));
        assertEquals(2, parent.getChildMenuItems().size());

        parent.addChild(child1);
        assertEquals(2, parent.getChildMenuItems().size());

        assertTrue(parent.removeChild(child1));
        assertEquals(1, parent.getChildMenuItems().size());

        assertFalse(parent.removeChild(child1));
        assertEquals(1, parent.getChildMenuItems().size());
    }

    @Test
    void parentDefinition() {
        //Add a children to the parent updates the children parent field
        MenuItem adoptiveParent = new MenuItem(null);
        MenuItem orphan = new MenuItem(null);

        assertNull(orphan.getParentItem());

        adoptiveParent.addChild(orphan);
        assertEquals(adoptiveParent, orphan.getParentItem());
    }

    @Test
    void encapsulatedMethods() {
        // The following method are encapsulated from the original HstMenuItem to ensure the out-of-the-box menu
        // functionality keeps working
        HstSiteMenuItem hsmi = mock(HstSiteMenuItem.class);
        MenuItem item = new MenuItem(hsmi);

        item.getHstSiteMenu();
        verify(hsmi).getHstSiteMenu();

        item.getParameter("p");
        verify(hsmi).getParameter("p");

        item.getLocalParameter("local");
        verify(hsmi).getLocalParameter("local");

        item.getParameters();
        verify(hsmi).getParameters();

        item.getLocalParameters();
        verify(hsmi).getLocalParameters();

        item.getName();
        verify(hsmi).getName();

        item.getHstLink();
        verify(hsmi).getHstLink();

        item.getExternalLink();
        verify(hsmi).getExternalLink();

        item.resolveToSiteMapItem();
        verify(hsmi).resolveToSiteMapItem();

        item.isExpanded();
        verify(hsmi).isExpanded();

        item.getProperties();
        verify(hsmi).getProperties();

        item.isRepositoryBased();
        verify(hsmi).isRepositoryBased();

        item.getDepth();
        verify(hsmi).getDepth();

        item.isSelected();
        verify(hsmi).isSelected();
    }
}