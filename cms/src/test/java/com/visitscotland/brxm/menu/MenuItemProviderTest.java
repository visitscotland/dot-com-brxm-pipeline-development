package com.visitscotland.brxm.menu;


import com.visitscotland.brxm.hippobeans.BaseDocument;
import com.visitscotland.brxm.hippobeans.General;
import com.visitscotland.brxm.translation.MockNodeBuilder;
import com.visitscotland.brxm.translation.plugin.JcrDocument;
import com.visitscotland.brxm.utils.HippoUtilsService;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.repository.api.RepositoryMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemProviderTest {

    @Mock
    HippoUtilsService hippoUtilsService;

    @InjectMocks
    MenuItemProvider menuItemProvider;


    @DisplayName("Prototypes are only modified when both new-page and new-module exist on menu")
    @ParameterizedTest
    @ValueSource(strings = {"new-page", "new-module", "new-document"})
    void prototypesNotChanged(String prototypeKey) {
        Map<String, Set<String>> prototypes = new HashMap<>();
        prototypes.put(prototypeKey, new TreeSet<>(Arrays.asList("a", "b")));

        menuItemProvider.constructPageAndModuleMenus(mock(Node.class), prototypes, mock(RepositoryMap.class));

        Assertions.assertEquals(1, prototypes.size());
        Assertions.assertEquals(new TreeSet<>(Arrays.asList("a", "b")), prototypes.get(prototypeKey));
    }


    @DisplayName("When no content document exists in folder, remove new-module option")
    @Test
    void noContentDocument() throws RepositoryException {
        Map<String, Set<String>> prototypes = new HashMap<>();
        prototypes.put("new-page", new TreeSet<>(Arrays.asList("a", "b")));
        prototypes.put("new-module", new TreeSet<>(Arrays.asList("c", "d")));

        Node subjectNode = mock(Node.class);
        when(subjectNode.hasNode("content")).thenReturn(false);

        menuItemProvider.constructPageAndModuleMenus(subjectNode, prototypes,  mock(RepositoryMap.class));

        Assertions.assertEquals(1, prototypes.size());
        Assertions.assertEquals(new TreeSet<>(Arrays.asList("a", "b")), prototypes.get("new-page"));
    }

    @DisplayName("When content item is not a page, remove new-module option")
    @Test
    void contentDocumentNotAPage() throws RepositoryException, ObjectBeanManagerException, QueryException {
        Map<String, Set<String>> prototypes = new HashMap<>();
        prototypes.put("new-page", new TreeSet<>(Arrays.asList("a", "b")));
        prototypes.put("new-module", new TreeSet<>(Arrays.asList("c", "d")));

        Node contentNode = mock(Node.class);
        when(contentNode.isNodeType(JcrDocument.HIPPO_HANDLE)).thenReturn(true);

        Node subjectNode = mock(Node.class);
        when(subjectNode.hasNode("content")).thenReturn(true);
        when(subjectNode.getNode("content")).thenReturn(contentNode);
        BaseDocument baseDoc = mock(BaseDocument.class);
        when(hippoUtilsService.getDocumentFromNode(contentNode, true)).thenReturn(baseDoc);

        menuItemProvider.constructPageAndModuleMenus(subjectNode, prototypes,  mock(RepositoryMap.class));

        Assertions.assertEquals(1, prototypes.size());
        Assertions.assertEquals(new TreeSet<>(Arrays.asList("a", "b")), prototypes.get("new-page"));
    }

    @DisplayName("When content document is a page, only show new-module list with filtered child jcr types")
    @Test
    void noContentDocumentPage() throws RepositoryException, ObjectBeanManagerException, QueryException {
        Map<String, Set<String>> prototypes = new HashMap<>();
        prototypes.put("new-page", new TreeSet<>(Arrays.asList("a", "b")));
        prototypes.put("new-module", new TreeSet<>(Arrays.asList("c", "d")));

        Node contentNode = mock(Node.class);
        when(contentNode.isNodeType(JcrDocument.HIPPO_HANDLE)).thenReturn(true);

        Node subjectNode = mock(Node.class);
        when(subjectNode.hasNode("content")).thenReturn(true);
        when(subjectNode.getNode("content")).thenReturn(contentNode);
        General generalPage = mock(General.class);
        when(generalPage.getChildJcrTypes()).thenReturn(new String[]{"TypeA", "TypeB"});
        when(hippoUtilsService.getDocumentFromNode(contentNode, true)).thenReturn(generalPage);

        menuItemProvider.constructPageAndModuleMenus(subjectNode, prototypes,  mock(RepositoryMap.class));

        Assertions.assertEquals(1, prototypes.size());
        Assertions.assertEquals(new TreeSet<>(Arrays.asList("TypeA", "TypeB")), prototypes.get("new-module"));
    }


    @DisplayName("When Repository Exception is thrown, prototypes are not changed")
    @Test
    void repoException() throws RepositoryException {
        Map<String, Set<String>> prototypes = new HashMap<>();
        prototypes.put("new-page", new TreeSet<>(Arrays.asList("a", "b")));
        prototypes.put("new-module", new TreeSet<>(Arrays.asList("c", "d")));

        Node subjectNode = mock(Node.class);
        when(subjectNode.hasNode("content")).thenReturn(true);
        when(subjectNode.getNode("content")).thenThrow(RepositoryException.class);

        menuItemProvider.constructPageAndModuleMenus(subjectNode, prototypes, mock(RepositoryMap.class));

        Assertions.assertEquals(2, prototypes.size());
        Assertions.assertEquals(new TreeSet<>(Arrays.asList("a", "b")), prototypes.get("new-page"));
        Assertions.assertEquals(new TreeSet<>(Arrays.asList("c", "d")), prototypes.get("new-module"));
    }

    @DisplayName("When new document disabled on translation, new-page, new-module & new-translated-folder removed on menu")
    @Test
    void newTranslationDocumentDisabled() throws Exception {
        Map<String, Set<String>> prototypes = new HashMap<>();
        prototypes.put("new-page", new TreeSet<>(Arrays.asList("a", "b")));
        prototypes.put("new-module", new TreeSet<>(Arrays.asList("c", "d")));
        prototypes.put("new-translated-folder", new TreeSet<>(Arrays.asList("e", "f")));
        prototypes.put("other", new TreeSet<>(Arrays.asList("g", "h")));

        Node subjectNode = new MockNodeBuilder().withProperty("hippotranslation:locale", "de").build();
        RepositoryMap config = mock(RepositoryMap.class);
        when(config.get("visitscotland:create-documents-on-translations")).thenReturn(false);

        menuItemProvider.constructPageAndModuleMenus(subjectNode, prototypes, config);

        Assertions.assertEquals(1, prototypes.size());
        Assertions.assertTrue(prototypes.containsKey("other"));
    }

    @DisplayName("When new document enabled on translation, then new-module added to page")
    @Test
    void newTranslationDocumentEnabled() throws Exception {
        Map<String, Set<String>> prototypes = new HashMap<>();
        prototypes.put("new-page", new TreeSet<>(Arrays.asList("a", "b")));
        prototypes.put("new-module", new TreeSet<>(Arrays.asList("c", "d")));

        Node subjectNode = new MockNodeBuilder().withProperty("hippotranslation:locale", "de").build();
        RepositoryMap config = mock(RepositoryMap.class);
        when(config.get("visitscotland:create-documents-on-translations")).thenReturn(true);

        menuItemProvider.constructPageAndModuleMenus(subjectNode, prototypes, config);

        Assertions.assertEquals(1, prototypes.size());
    }


}
