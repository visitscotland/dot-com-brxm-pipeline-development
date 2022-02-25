package com.visitscotland.brxm.translation.plugin;

import org.hippoecm.frontend.translation.ILocaleProvider;
import org.hippoecm.repository.api.HippoNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.RepositoryException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ChangeSetAddAllUntranslatedFoldersTest {
    private ChangeSet changeSet;
    @Mock
    private TranslatedFolder mockSourceFolder;
    @Mock
    private ILocaleProvider.HippoLocale mockLocale;
    @Mock
    private HippoTranslatedNodeFactory mockHippoTranslatedNodeFactory;
    @Mock
    private JcrFolderTranslationFactory mockJcrFolderTranslationFactory;

    @BeforeEach
    public void beforeEach() {
        changeSet = new ChangeSet(mockLocale);
    }

    @Test
    public void addAllUntranslatedFolders_highestAlreadyTranslated() throws Exception {
        // The first folder is already translated, should return that folder
        when(mockSourceFolder.getSibling(eq("no"))).thenReturn(mock(TranslatedFolder.class));

        changeSet.addAllUntranslatedFolders("no", changeSet.getFolders(), mockSourceFolder);
        assertTrue(changeSet.getFolders().isEmpty());
    }

    @Test
    public void addAllUntranslatedFolders_parentIsNull() throws Exception {
        // No translated sibling before parent is eventually null
        int numberInChain = 10;
        createUntranslatedFolderChain(mockSourceFolder, numberInChain, "no", false);
        assertThrows(RepositoryException.class, () -> changeSet.addAllUntranslatedFolders("no", changeSet.getFolders(), mockSourceFolder));

        // The folders should also be populated, not sure if this intended functionality
        // but I'll test that the values are what they should be
        // Source folder + numberInChain, note the folders are in reverse index order
        assertEquals(numberInChain + 1, changeSet.getFolders().size());
        for (int index = 0; index < numberInChain + 1; index++) {
            assertFolderTranslation(index, changeSet.getFolders().get(index));
        }
    }

    @Test
    public void addAllUntranslatedFolders_parentIsTranslated() throws Exception {
        // Valid path, walks the untranslated folders adding them to the folders list
        // the names of the folders and their URLs should match the originals
        int numberInChain = 10;
        createUntranslatedFolderChain(mockSourceFolder, numberInChain, "no", true);
        changeSet.addAllUntranslatedFolders("no", changeSet.getFolders(), mockSourceFolder);

        // The folders should be populated
        assertEquals(numberInChain, changeSet.getFolders().size());
        for (int index = 0; index < numberInChain; index++) {
            assertFolderTranslation(index, changeSet.getFolders().get(index));
        }
    }

    private void assertFolderTranslation(int index, FolderTranslation translation) {
        assertTrue(translation.getName().endsWith(Integer.toString(index)));
        assertEquals(translation.getName(), translation.getNamefr());
        assertTrue(translation.getUrl().endsWith(Integer.toString(index)));
        assertEquals(translation.getUrl(), translation.getUrlfr());
    }

    private HippoNode createHippoNode(String url, String displayName) throws Exception {
        HippoNode mockNode = mock(HippoNode.class);
        when(mockNode.getName()).thenReturn(url);
        when(mockNode.getDisplayName()).thenReturn(displayName);
        return mockNode;
    }

    private void createUntranslatedFolderChain(TranslatedFolder sourceFolder, int numberInChain, String targetLanguage, boolean lastFolderTranslated) throws Exception {
        // We have numberInChain + 1 for the source folder. The source folder is number 0
        int nodeNumber = numberInChain;
        TranslatedFolder lastCreated = mock(TranslatedFolder.class);
        if (lastFolderTranslated) {
            when(lastCreated.getSibling(eq(targetLanguage))).thenReturn(mock(TranslatedFolder.class));
        } else {
            HippoNode mockNode = createHippoNode("url" + nodeNumber, "name" + nodeNumber);
            when(lastCreated.getNode()).thenReturn(mockNode);
            when(lastCreated.getSibling(eq(targetLanguage))).thenReturn(null);
            when(lastCreated.getParent()).thenReturn(null);
        }
        nodeNumber--;

        for (int index = 1; index < numberInChain; index++) {
            TranslatedFolder nextFolder = mock(TranslatedFolder.class);
            HippoNode mockNode = createHippoNode("url" + nodeNumber, "name" + nodeNumber);
            nodeNumber--;
            when(nextFolder.getNode()).thenReturn(mockNode);
            when(nextFolder.getSibling(eq(targetLanguage))).thenReturn(null);
            when(nextFolder.getParent()).thenReturn(lastCreated);
            lastCreated = nextFolder;
        }
        HippoNode mockSourceNode = createHippoNode("url" + nodeNumber, "name" + nodeNumber);
        when(sourceFolder.getNode()).thenReturn(mockSourceNode);
        when(sourceFolder.getSibling(eq(targetLanguage))).thenReturn(null);
        when(sourceFolder.getParent()).thenReturn(lastCreated);
    }
}
