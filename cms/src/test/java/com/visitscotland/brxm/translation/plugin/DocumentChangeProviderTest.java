package com.visitscotland.brxm.translation.plugin;

import org.apache.wicket.model.IModel;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Node;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocumentChangeProviderTest {
    @Mock
    private TypeNameFactory mockTypeNameFactory;

    @Test
    public void buildDocumentChangeMap_emptyChangeSet() {
        // Given an empty ChangeSet, and empty list should be returned
        DocumentChangeProvider provider = new DocumentChangeProvider(new ArrayList<>(), mockTypeNameFactory);
        List<DocumentChangeProvider.Entry> entryList = provider.documentChangeList;
        assertNotNull(entryList);
        assertTrue(entryList.isEmpty());
    }

    private Node createSourceNode(String name) throws Exception {
        Node sourceNode = mock(Node.class);
        when(mockTypeNameFactory.lookupTypeName(same(sourceNode))).thenReturn(name);
        return sourceNode;
    }

    @Test
    public void buildDocumentChangeMap_validChangeSet() throws Exception {
        // Given a valid ChangeSet List there should be an entry for every document with all the locales of the
        // ChangeSets the document is in

        ILocaleProvider.HippoLocale german = mock(ILocaleProvider.HippoLocale.class);
        ILocaleProvider.HippoLocale french = mock(ILocaleProvider.HippoLocale.class);
        ILocaleProvider.HippoLocale spanish = mock(ILocaleProvider.HippoLocale.class);

        ChangeSet change1 = mock(ChangeSet.class);
        when(change1.getTargetLocale()).thenReturn(german);
        List<FolderTranslation> change1Documents = new ArrayList<>();
        when(change1.getDocuments()).thenReturn(change1Documents);

        ChangeSet change2 = mock(ChangeSet.class);
        when(change2.getTargetLocale()).thenReturn(french);
        List<FolderTranslation> change2Documents = new ArrayList<>();
        when(change2.getDocuments()).thenReturn(change2Documents);

        ChangeSet change3 = mock(ChangeSet.class);
        when(change3.getTargetLocale()).thenReturn(spanish);
        List<FolderTranslation> change3Documents = new ArrayList<>();
        when(change3.getDocuments()).thenReturn(change3Documents);

        FolderTranslation document1 = new FolderTranslation("doc1id");
        document1.setName("doc1name");
        document1.setSourceNode(createSourceNode("doc1type"));
        FolderTranslation document2 = new FolderTranslation("doc2id");
        document2.setName("doc2name");
        document2.setSourceNode(createSourceNode("doc2type"));
        FolderTranslation document3 = new FolderTranslation("doc3id");
        document3.setName("doc3name");
        document3.setSourceNode(createSourceNode("doc3type"));

        change1Documents.add(document1);

        change2Documents.add(document1);
        change2Documents.add(document2);

        change3Documents.add(document1);
        change3Documents.add(document2);
        change3Documents.add(document3);

        List<ChangeSet> changeSetList = new ArrayList<>();
        changeSetList.add(change1);
        changeSetList.add(change2);
        changeSetList.add(change3);

        DocumentChangeProvider provider = new DocumentChangeProvider(changeSetList, mockTypeNameFactory);
        List<DocumentChangeProvider.Entry> entryList = provider.documentChangeList;

        assertNotNull(entryList);
        assertEquals(3, entryList.size());

        for (DocumentChangeProvider.Entry entry : entryList) {
            switch (entry.getDocumentName()) {
                case "doc1name":
                    assertEquals("doc1type", entry.getDocumentType());
                    assertEquals(3, entry.getLocaleList().size());
                    assertTrue(entry.getLocaleList().contains(german));
                    assertTrue(entry.getLocaleList().contains(french));
                    assertTrue(entry.getLocaleList().contains(spanish));
                    break;
                case "doc2name":
                    assertEquals("doc2type", entry.getDocumentType());
                    assertEquals(2, entry.getLocaleList().size());
                    assertTrue(entry.getLocaleList().contains(french));
                    assertTrue(entry.getLocaleList().contains(spanish));
                    break;
                case "doc3name":
                    assertEquals("doc3type", entry.getDocumentType());
                    assertEquals(1, entry.getLocaleList().size());
                    assertTrue(entry.getLocaleList().contains(spanish));
                    break;
                default:
                    fail("unexpected document name");
            }
        }
    }

    @Test
    public void model() {
        DocumentChangeProvider provider = new DocumentChangeProvider(new ArrayList<>());

        DocumentChangeProvider.Entry mockEntry = mock(DocumentChangeProvider.Entry.class);
        IModel<DocumentChangeProvider.Entry> model = provider.model(mockEntry);

        assertNotNull(model);
        assertSame(mockEntry, model.getObject());
    }
}
