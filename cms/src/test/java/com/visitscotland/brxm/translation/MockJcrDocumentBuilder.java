package com.visitscotland.brxm.translation;

import com.visitscotland.brxm.translation.plugin.JcrDocument;
import com.visitscotland.brxm.translation.plugin.JcrDocumentFactory;

import javax.jcr.Node;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class MockJcrDocumentBuilder {
    Map<String, Node> documentVariants = new HashMap<>();
    JcrDocumentFactory factory;
    Node rootNode;
    Node handle;
    Set<JcrDocument> translationSet;
    Boolean isDraftBeingEdited;
    List<String> typeList = new ArrayList<>();
    String localeName;

    public MockJcrDocumentBuilder withJcrDocumentFactory(JcrDocumentFactory factory) {
        this.factory = factory;
        return this;
    }

    public MockJcrDocumentBuilder createdFromNode(Node rootNode) {
        this.rootNode = rootNode;
        return this;
    }

    public MockJcrDocumentBuilder withVariantNode(String state, Node variantNode) {
        documentVariants.put(state, variantNode);
        return this;
    }

    public MockJcrDocumentBuilder withTranslations(JcrDocument... translations) {
        if (null == translationSet) {
            translationSet = new HashSet<>();
        }
        translationSet.addAll(Arrays.asList(translations));
        return this;
    }

    public MockJcrDocumentBuilder isDraftBeingEdited(boolean value) {
        isDraftBeingEdited = value;
        return this;
    }

    public MockJcrDocumentBuilder withHandle(Node handle) {
        this.handle = handle;
        return this;
    }

    public MockJcrDocumentBuilder isNodeType(String type) {
        typeList.add(type);
        return this;
    }

    public MockJcrDocumentBuilder withLocaleName(String localeName) {
        this.localeName = localeName;
        return this;
    }

    public JcrDocument build() throws Exception {
        JcrDocument mockJcrDocument = mock(JcrDocument.class);
        lenient().when(mockJcrDocument.isNodeType(anyString())).thenReturn(false);

        if ( null != factory && null != rootNode ) {
            when(factory.createFromNode(same(rootNode))).thenReturn(mockJcrDocument);
        }

        for (Map.Entry<String, Node> variantEntry : documentVariants.entrySet()) {
            when(mockJcrDocument.getVariantNode(eq(variantEntry.getKey()))).thenReturn(variantEntry.getValue());
        }

        if (null != translationSet) {
            when(mockJcrDocument.getTranslations()).thenReturn(translationSet);
        }

        if (null != isDraftBeingEdited) {
            when(mockJcrDocument.isDraftBeingEdited()).thenReturn(isDraftBeingEdited);
        }

        if (null != handle) {
            when(mockJcrDocument.getHandle()).thenReturn(handle);
        }

        for(String type : typeList) {
            when(mockJcrDocument.isNodeType(eq(type))).thenReturn(true);
        }

        if (null != localeName) {
            when(mockJcrDocument.getLocaleName()).thenReturn(localeName);
        }

        return mockJcrDocument;
    }
}
