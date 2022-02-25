package com.visitscotland.brxm.translation.plugin;

import org.hippoecm.frontend.translation.ILocaleProvider;
import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.repository.translation.HippoTranslationNodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Node;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChangeSetPopulateFoldersTest {
    // The JCR has a pair of Nodes to represent a document. The highest node has a Mixin of
    // hippo:translated, but its parent does not. It is the hippo:named Node parent that
    // is passed to the populateFolders method.
    @Mock
    JcrDocument document;
    HippoNode translatedFolder5Node;
    TranslatedFolder translatedFolder5;
    HippoNode translatedFolder4Node;
    TranslatedFolder translatedFolder4;
    HippoNode translatedFolder3Node;
    TranslatedFolder translatedFolder3;
    HippoNode translatedFolder2Node;
    TranslatedFolder translatedFolder2;
    HippoNode translatedFolder1Node;
    TranslatedFolder translatedFolder1;
    HippoNode translatedFolder0Node;
    TranslatedFolder translatedFolder0;

    @Mock
    ILocaleProvider.HippoLocale mockLocale;
    ChangeSet changeSet;

    @BeforeEach
    public void beforeEach() throws Exception {
        when(mockLocale.getName()).thenReturn("fr");
        changeSet = spy(new ChangeSet(mockLocale));

        createSourceTranslatedFolders();
        Node documentHandle = createDocumentHandle(translatedFolder5Node);
        when(document.getHandle()).thenReturn(documentHandle);

    }

    // Each test covers a scenario that has a source chain of folders from the document to be translated to the root
    // translation folder.
    // The target chain of folders differs in each case by the number of translated folders that already exist.
    //
    // Once the path of a TranslatedFolder is /content/documents it returns null, so the root folder's parent
    // will always be null.

    @Test
    public void populateFolders_only_root_translated() throws Exception {
        // only the root folder exists in the target, all the folders should be added as mutable folder translations
        addTranslatedFolder(translatedFolder0, null, "fr_name0", "fr_url0");

        changeSet.populateFolders(document);

        // Should have a list of FolderTranslations
        // at index 0 should be the root folder, immutable with the english and french names
        // All the rest should be mutable with a clone of the english names.
        assertEquals(6, changeSet.getFolders().size());

        assertFalse(changeSet.getFolders().get(0).isEditable());
        assertEquals("name0", changeSet.getFolders().get(0).getName());
        assertEquals("fr_name0", changeSet.getFolders().get(0).getNamefr());
        assertEquals("url0", changeSet.getFolders().get(0).getUrl());
        assertEquals("fr_url0", changeSet.getFolders().get(0).getUrlfr());

        for (int index = 1; index <= 5; index++) {
            assertNewTranslation(changeSet.getFolders(), index);
        }
    }


    @Test
    public void populateFolders_partial_tree_translation() throws Exception {
        // only half of the tree exists in the target tree. Half of the folders should be mutable, the other half
        // immutable because they already exist.
        TranslatedFolder frenchTranslatedFolder0 = addTranslatedFolder(translatedFolder0, null, "fr_name0", "fr_url0");
        TranslatedFolder frenchTranslatedFolder1 = addTranslatedFolder(translatedFolder1, frenchTranslatedFolder0, "fr_name1", "fr_url1");
        addTranslatedFolder(translatedFolder2, frenchTranslatedFolder1, "fr_name2", "fr_url2");

        changeSet.populateFolders(document);

        // this time the already translated folders should be immutable,
        // this should be the folder at index 0, 1, 2
        assertEquals(6, changeSet.getFolders().size());

        for (int index = 0; index <= 2; index++) {
            assertPreviouslyTranslated(changeSet.getFolders(), index);
        }

        for (int index = 3; index <= 5; index++) {
            assertNewTranslation(changeSet.getFolders(), index);
        }
    }

    @Test
    public void populateFolders_whole_tree_translated() throws Exception {
        // the whole of the target tree already exists, only immutable folders should be added to the list
        TranslatedFolder frenchTranslatedFolder0 = addTranslatedFolder(translatedFolder0, null, "fr_name0", "fr_url0");
        TranslatedFolder frenchTranslatedFolder1 = addTranslatedFolder(translatedFolder1, frenchTranslatedFolder0, "fr_name1", "fr_url1");
        TranslatedFolder frenchTranslatedFolder2 = addTranslatedFolder(translatedFolder2, frenchTranslatedFolder1, "fr_name2", "fr_url2");
        TranslatedFolder frenchTranslatedFolder3 = addTranslatedFolder(translatedFolder3, frenchTranslatedFolder2, "fr_name3", "fr_url3");
        TranslatedFolder frenchTranslatedFolder4 = addTranslatedFolder(translatedFolder4, frenchTranslatedFolder3, "fr_name4", "fr_url4");
        addTranslatedFolder(translatedFolder5, frenchTranslatedFolder4, "fr_name5", "fr_url5");

        changeSet.populateFolders(document);

        // this time they are all translated folders and should be immutable,
        assertEquals(6, changeSet.getFolders().size());

        for (int index = 0; index <= 5; index++) {
            assertPreviouslyTranslated(changeSet.getFolders(), index);
        }
    }

    private void assertNewTranslation(List<FolderTranslation> folders, int index) {
        assertTrue(folders.get(index).isEditable());

        assertTrue(folders.get(index).getName().startsWith("name"));
        assertTrue(folders.get(index).getName().endsWith("" + index));
        assertTrue(folders.get(index).getUrl().startsWith("url"));
        assertTrue(folders.get(index).getUrl().endsWith("" + index));
        assertTrue(folders.get(index).getNamefr().startsWith("name"));
        assertTrue(folders.get(index).getNamefr().endsWith("" + index));
        assertTrue(folders.get(index).getUrlfr().startsWith("url"));
        assertTrue(folders.get(index).getUrlfr().endsWith("" + index));
    }

    private void assertPreviouslyTranslated(List<FolderTranslation> folders, int index) {
        String s = "" + index;
        assertFalse(folders.get(index).isEditable());

        assertTrue(folders.get(index).getName().startsWith("name"));
        assertTrue(folders.get(index).getName().endsWith(s));
        assertTrue(folders.get(index).getUrl().startsWith("url"));
        assertTrue(folders.get(index).getUrl().endsWith(s));
        assertTrue(folders.get(index).getNamefr().startsWith("fr_name"));
        assertTrue(folders.get(index).getNamefr().endsWith(s));
        assertTrue(folders.get(index).getUrlfr().startsWith("fr_url"));
        assertTrue(folders.get(index).getUrlfr().endsWith(s));
    }

    private TranslatedFolder addTranslatedFolder(TranslatedFolder sourceFolder, TranslatedFolder translatedParentFolder, String name, String url) throws Exception {
        TranslatedFolder translatedFolder = createMockTranslatedFolder(translatedParentFolder);
        createMockTranslatedFolderNode(translatedFolder, name, url);
        lenient().when(sourceFolder.getSibling(eq("fr"))).thenReturn(translatedFolder);
        return translatedFolder;
    }

    private void createSourceTranslatedFolders() throws Exception {
        translatedFolder0 = createMockTranslatedFolder(null);
        translatedFolder0Node = createMockTranslatedFolderNode(translatedFolder0, "name0", "url0");

        translatedFolder1 = createMockTranslatedFolder(translatedFolder0);
        translatedFolder1Node = createMockTranslatedFolderNode(translatedFolder1, "name1", "url1");

        translatedFolder2 = createMockTranslatedFolder(translatedFolder1);
        translatedFolder2Node = createMockTranslatedFolderNode(translatedFolder2, "name2", "url2");

        translatedFolder3 = createMockTranslatedFolder(translatedFolder2);
        translatedFolder3Node = createMockTranslatedFolderNode(translatedFolder3, "name3", "url3");

        translatedFolder4 = createMockTranslatedFolder(translatedFolder3);
        translatedFolder4Node = createMockTranslatedFolderNode(translatedFolder4, "name4", "url4");

        translatedFolder5 = createMockTranslatedFolder(translatedFolder4);
        translatedFolder5Node = createMockTranslatedFolderNode(translatedFolder5, "name5", "url5");
        when(translatedFolder5Node.isNodeType(eq(HippoTranslationNodeType.NT_TRANSLATED))).thenReturn(true);
        doReturn(translatedFolder5).when(changeSet).createTranslatedFolder(same(translatedFolder5Node));
    }

    private HippoNode createMockTranslatedFolderNode(TranslatedFolder translatedFolder, String name, String url) throws Exception {
        HippoNode mockNode = mock(HippoNode.class);
        when(translatedFolder.getNode()).thenReturn(mockNode);
        when(mockNode.getName()).thenReturn(url);
        when(mockNode.getDisplayName()).thenReturn(name);
        return mockNode;
    }

    private TranslatedFolder createMockTranslatedFolder(TranslatedFolder parent) throws Exception {
        TranslatedFolder mockFolder = mock(TranslatedFolder.class);
        lenient().when(mockFolder.getParent()).thenReturn(parent);
        return mockFolder;
    }

    private HippoNode createDocumentHandle(HippoNode parentFolder) throws Exception {
        HippoNode documentNode = mock(HippoNode.class);
        when(documentNode.isNodeType(eq(HippoTranslationNodeType.NT_TRANSLATED))).thenReturn(false);
        when(documentNode.getParent()).thenReturn(parentFolder);
        return documentNode;
    }
}
