package com.visitscotland.brxm.translation.plugin;

import org.hippoecm.frontend.translation.ILocaleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChangeSetTest {
    private ChangeSet changeSet;
    @Mock
    private ILocaleProvider.HippoLocale mockLocale;

    @BeforeEach
    public void beforeEach() {
        changeSet = new ChangeSet(mockLocale);
    }

    @Test
    public void getTargetPath_singleFolder() {
        // There must always be a single folder, can never be no folders
        // Make sure the path starts with a / with no trailing /
        FolderTranslation rootFolder = mock(FolderTranslation.class);
        when(rootFolder.getUrlfr()).thenReturn("root");
        changeSet.getFolders().add(rootFolder);

        String targetPath = changeSet.getTargetPath();

        assertEquals("/root", targetPath);
    }

    @Test
    public void getTargetPath_multipleFolders() {
        // Make sure the path starts with a / with no trailing / and has the folders in the correct order
        FolderTranslation rootFolder = mock(FolderTranslation.class);
        when(rootFolder.getUrlfr()).thenReturn("root");
        FolderTranslation folder1 = mock(FolderTranslation.class);
        when(folder1.getUrlfr()).thenReturn("folder1");
        FolderTranslation folder2 = mock(FolderTranslation.class);
        when(folder2.getUrlfr()).thenReturn("folder2");
        changeSet.getFolders().add(rootFolder);
        changeSet.getFolders().add(folder1);
        changeSet.getFolders().add(folder2);

        String targetPath = changeSet.getTargetPath();

        assertEquals("/root/folder1/folder2", targetPath);
    }

    @Test
    public void containsMatchingUrl_emptyDocuments() {
        assertFalse(changeSet.containsDocumentMatchingUrl("/root"));
    }

    @Test
    public void containsMatchingUrl_noMatchingDocument() {
        FolderTranslation notMatching = mock(FolderTranslation.class);
        when(notMatching.getUrlfr()).thenReturn("/root/folder_fr");
        when(notMatching.getUrl()).thenReturn("/root/folder");
        changeSet.getDocuments().add(notMatching);

        assertFalse(changeSet.containsDocumentMatchingUrl("/root"));
    }

    @Test
    public void containsMatchingUrl_withMatchingDocument_UrlFr() {
        FolderTranslation notMatching = mock(FolderTranslation.class);
        when(notMatching.getUrlfr()).thenReturn("/root/folder_fr");

        changeSet.getDocuments().add(notMatching);

        assertTrue(changeSet.containsDocumentMatchingUrl("/root/folder_fr"));
    }

    @Test
    public void containsMatchingUrl_withMatchingDocument_Url() {
        FolderTranslation notMatching = mock(FolderTranslation.class);
        when(notMatching.getUrlfr()).thenReturn("/root/folder_fr");
        when(notMatching.getUrl()).thenReturn("/root/folder");
        changeSet.getDocuments().add(notMatching);

        assertTrue(changeSet.containsDocumentMatchingUrl("/root/folder"));
    }
}

