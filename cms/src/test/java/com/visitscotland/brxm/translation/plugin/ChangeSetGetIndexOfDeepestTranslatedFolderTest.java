package com.visitscotland.brxm.translation.plugin;

import org.hippoecm.frontend.translation.ILocaleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ChangeSetGetIndexOfDeepestTranslatedFolderTest {
    private ChangeSet changeSet;
    @Mock
    private ILocaleProvider.HippoLocale mockLocale;

    @BeforeEach
    public void beforeEach() {
        changeSet = new ChangeSet(mockLocale);
    }

    @Test
    public void getIndexOfDeepestTranslatedFolder_empty() {
        // When there are no folders in the change set, should throw an error
        assertThrows(IllegalStateException.class, () -> changeSet.getIndexOfDeepestTranslatedFolder());
    }

    @Test
    public void getIndexOfDeepestTranslatedFolder_none_translated() {
        // Should never have the scenario where none are translated, the root must always be translated
        changeSet.getFolders().add(createMockFolderTranslation("root", true));

        assertThrows(IllegalStateException.class, () -> changeSet.getIndexOfDeepestTranslatedFolder());
    }

    @Test
    public void getIndexOfDeepestTranslatedFolder_allTranslated() {
        // When all the folders are translated the index should be the last element in the list
        changeSet.getFolders().add(createMockFolderTranslation("root", false));
        changeSet.getFolders().add(createMockFolderTranslation("level1", false));
        changeSet.getFolders().add(createMockFolderTranslation("level2", false));

        int deepestTranslatedFolderIndex = changeSet.getIndexOfDeepestTranslatedFolder();

        assertEquals(changeSet.getFolders().size() - 1, deepestTranslatedFolderIndex);
    }

    @Test
    public void getIndexOfDeepestTranslatedFolder_with_untranslated() {
        changeSet.getFolders().add(createMockFolderTranslation("root", false));
        changeSet.getFolders().add(createMockFolderTranslation("level1", false));
        changeSet.getFolders().add(createMockFolderTranslation("level2", true));

        int deepestTranslatedFolderIndex = changeSet.getIndexOfDeepestTranslatedFolder();

        assertEquals(1, deepestTranslatedFolderIndex);
    }

    private FolderTranslation createMockFolderTranslation(String nodeId, boolean isEditable) {
        FolderTranslation folder = new FolderTranslation(nodeId);
        folder.setEditable(isEditable);
        return folder;
    }
}
