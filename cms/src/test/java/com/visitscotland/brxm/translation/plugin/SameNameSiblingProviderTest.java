package com.visitscotland.brxm.translation.plugin;

import org.apache.wicket.model.IModel;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.repository.api.HippoSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SameNameSiblingProviderTest {
    @Mock
    HippoSession mockSession;

    @Test
    public void constructor_emptyChangeSetList() throws Exception {
        SameNameSiblingProvider provider = new SameNameSiblingProvider(new ArrayList<>(), mockSession);
        assertNotNull(provider.getEntryList());
        assertTrue(provider.getEntryList().isEmpty());
    }

    @Test
    public void constructor_withFolderAndDocuments() throws Exception {
        List<ChangeSet> changeSetList = new ArrayList<>();

        ChangeSet mockChangeSet1 = mock(ChangeSet.class);
        ILocaleProvider.HippoLocale mockLocale1 = mock(ILocaleProvider.HippoLocale.class);
        when(mockChangeSet1.getTargetLocale()).thenReturn(mockLocale1);
        List<FolderTranslation> changeSet1Folders = new ArrayList<>();
        when(mockChangeSet1.getFolders()).thenReturn(changeSet1Folders);
        List<FolderTranslation> changeSet1Documents = new ArrayList<>();
        when(mockChangeSet1.getDocuments()).thenReturn(changeSet1Documents);
        changeSetList.add(mockChangeSet1);

        ChangeSet mockChangeSet2 = mock(ChangeSet.class);
        ILocaleProvider.HippoLocale mockLocale2 = mock(ILocaleProvider.HippoLocale.class);
        when(mockChangeSet2.getTargetLocale()).thenReturn(mockLocale2);
        List<FolderTranslation> changeSet2Folders = new ArrayList<>();
        when(mockChangeSet2.getFolders()).thenReturn(changeSet2Folders);
        List<FolderTranslation> changeSet2Documents = new ArrayList<>();
        when(mockChangeSet2.getDocuments()).thenReturn(changeSet2Documents);
        changeSetList.add(mockChangeSet2);

        FolderTranslation folder1 = new FolderTranslation("folder1");
        FolderTranslation folder2 = new FolderTranslation("folder2");
        folder2.setHasSameNameSibling(true);
        folder2.setSameNameSiblingId("folder2sibling");
        HippoNode folder2sibling = mock(HippoNode.class);
        when(mockSession.getNodeByIdentifier(eq("folder2sibling"))).thenReturn(folder2sibling);
        FolderTranslation folder3 = new FolderTranslation("folder3");
        folder3.setHasSameUrlSibling(true);
        folder3.setSameUrlSiblingId("folder3sibling");
        HippoNode folder3sibling = mock(HippoNode.class);
        when(mockSession.getNodeByIdentifier(eq("folder3sibling"))).thenReturn(folder3sibling);
        FolderTranslation folder4 = new FolderTranslation("folder4");
        folder4.setHasSameNameSibling(true);
        folder4.setSameNameSiblingId("folder4sibling");
        HippoNode folder4sibling = mock(HippoNode.class);
        when(mockSession.getNodeByIdentifier(eq("folder4sibling"))).thenReturn(folder4sibling);
        folder4.setHasSameUrlSibling(true);
        folder4.setSameUrlSiblingId("folder4sibling");
        FolderTranslation folder5 = new FolderTranslation("folder5");
        folder5.setHasSameNameSibling(true);
        folder5.setSameNameSiblingId("folder5name");
        HippoNode folder5name = mock(HippoNode.class);
        when(mockSession.getNodeByIdentifier(eq("folder5name"))).thenReturn(folder5name);
        folder5.setHasSameUrlSibling(true);
        folder5.setSameUrlSiblingId("folder5url");
        HippoNode folder5url = mock(HippoNode.class);
        when(mockSession.getNodeByIdentifier(eq("folder5url"))).thenReturn(folder5url);

        changeSet2Folders.add(folder1);
        changeSet2Folders.add(folder2);
        changeSet2Folders.add(folder3);
        changeSet2Folders.add(folder4);
        changeSet2Folders.add(folder5);

        ChangeSet mockChangeSet3 = mock(ChangeSet.class);
        ILocaleProvider.HippoLocale mockLocale3 = mock(ILocaleProvider.HippoLocale.class);
        when(mockChangeSet3.getTargetLocale()).thenReturn(mockLocale3);
        List<FolderTranslation> changeSet3Folders = new ArrayList<>();
        when(mockChangeSet3.getFolders()).thenReturn(changeSet3Folders);
        List<FolderTranslation> changeSet3Documents = new ArrayList<>();
        when(mockChangeSet3.getDocuments()).thenReturn(changeSet3Documents);
        changeSetList.add(mockChangeSet3);

        FolderTranslation document1 = new FolderTranslation("doc1");
        FolderTranslation document2 = new FolderTranslation("doc2");
        document2.setHasSameNameSibling(true);
        document2.setSameNameSiblingId("doc2sibling");
        HippoNode doc2sibling = mock(HippoNode.class);
        when(mockSession.getNodeByIdentifier(eq("doc2sibling"))).thenReturn(doc2sibling);
        FolderTranslation document3 = new FolderTranslation("doc3");
        document3.setHasSameUrlSibling(true);
        document3.setSameUrlSiblingId("doc3sibling");
        HippoNode doc3sibling = mock(HippoNode.class);
        when(mockSession.getNodeByIdentifier(eq("doc3sibling"))).thenReturn(doc3sibling);
        FolderTranslation document4 = new FolderTranslation("doc4");
        document4.setHasSameUrlSibling(true);
        document4.setSameUrlSiblingId("doc4sibling");
        HippoNode doc4sibling = mock(HippoNode.class);
        when(mockSession.getNodeByIdentifier(eq("doc4sibling"))).thenReturn(doc4sibling);
        document4.setHasSameNameSibling(true);
        document4.setSameNameSiblingId("doc4sibling");
        FolderTranslation document5 = new FolderTranslation("doc5");
        document5.setHasSameUrlSibling(true);
        document5.setSameUrlSiblingId("doc5url");
        HippoNode doc5url = mock(HippoNode.class);
        when(mockSession.getNodeByIdentifier(eq("doc5url"))).thenReturn(doc5url);
        document5.setHasSameNameSibling(true);
        document5.setSameNameSiblingId("doc5name");
        HippoNode doc5name = mock(HippoNode.class);
        when(mockSession.getNodeByIdentifier(eq("doc5name"))).thenReturn(doc5name);

        changeSet3Documents.add(document1);
        changeSet3Documents.add(document2);
        changeSet3Documents.add(document3);
        changeSet3Documents.add(document4);
        changeSet3Documents.add(document5);

        SameNameSiblingProvider provider = new SameNameSiblingProvider(changeSetList, mockSession);

        assertNotNull(provider.getEntryList());
        assertEquals(10, provider.getEntryList().size());

    }

    @Test
    public void model() throws Exception {
        SameNameSiblingProvider provider = new SameNameSiblingProvider(new ArrayList<>(), mockSession);
        SameNameSiblingProvider.Entry entry = provider.new Entry(mock(ILocaleProvider.HippoLocale.class),
                "url", "name", true, false);

        IModel<SameNameSiblingProvider.Entry> model = provider.model(entry);

        assertSame(entry, model.getObject());
    }
}
