package com.visitscotland.brxm.translation.plugin;

import org.hippoecm.repository.api.HippoNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JcrFolderTranslationFactoryTest {
    private JcrFolderTranslationFactory factory;

    @BeforeEach
    public void beforeEach() {
        factory = new JcrFolderTranslationFactory();
    }

    @Test
    public void createFolderTranslation_bothNull() throws Exception {
        // When both Nodes are null it should throw an Exception
        assertThrows(IllegalArgumentException.class, () -> factory.createFolderTranslation(null, null));
    }

    @Test
    public void createFolderTranslation_originalOnly() throws Exception {
        // Should only set the name, url
        HippoNode mockOriginalNode = mock(HippoNode.class);
        when(mockOriginalNode.getIdentifier()).thenReturn("originalId");
        when(mockOriginalNode.getName()).thenReturn("originalUrl");
        when(mockOriginalNode.getDisplayName()).thenReturn("originalName");

        FolderTranslation ft = factory.createFolderTranslation(mockOriginalNode, null);

        assertNotNull(ft);
        assertEquals("originalId", ft.getId());
        assertEquals("originalUrl", ft.getUrl());
        assertEquals("originalName", ft.getName());
        assertEquals("", ft.getUrlfr());
        assertEquals("", ft.getNamefr());
    }

    @Test
    public void createFolderTranslation_targetOnly() throws Exception {
        // Should only set the Namefr and the Urlfr values
        HippoNode mockTargetNode = mock(HippoNode.class);
        when(mockTargetNode.getIdentifier()).thenReturn("targetId");
        when(mockTargetNode.getName()).thenReturn("targetUrl");
        when(mockTargetNode.getDisplayName()).thenReturn("targetName");

        FolderTranslation ft = factory.createFolderTranslation(null, mockTargetNode);

        assertNotNull(ft);
        assertEquals("targetId", ft.getId());
        assertNull(ft.getUrl());
        assertNull(ft.getName());
        assertEquals("targetUrl", ft.getUrlfr());
        assertEquals("targetName", ft.getNamefr());
    }

    @Test
    public void createFolderTranslation_withBoth() throws Exception {
        // Should set all name and url values, and have the if of the original node
        HippoNode mockOriginalNode = mock(HippoNode.class);
        when(mockOriginalNode.getIdentifier()).thenReturn("originalId");
        when(mockOriginalNode.getName()).thenReturn("originalUrl");
        when(mockOriginalNode.getDisplayName()).thenReturn("originalName");

        HippoNode mockTargetNode = mock(HippoNode.class);
        when(mockTargetNode.getName()).thenReturn("targetUrl");
        when(mockTargetNode.getDisplayName()).thenReturn("targetName");

        FolderTranslation ft = factory.createFolderTranslation(mockOriginalNode, mockTargetNode);

        assertNotNull(ft);
        assertEquals("originalId", ft.getId());
        assertEquals("originalUrl", ft.getUrl());
        assertEquals("originalName", ft.getName());
        assertEquals("targetUrl", ft.getUrlfr());
        assertEquals("targetName", ft.getNamefr());
    }
}
