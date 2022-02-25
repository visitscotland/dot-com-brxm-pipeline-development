package com.visitscotland.brxm.translation.plugin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Node;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JcrDocumentFactoryTest {
    @Test
    public void constructor() throws Exception {
        Node sourceNode = mock(Node.class);
        when(sourceNode.isNodeType(JcrDocument.HIPPO_HANDLE)).thenReturn(true);
        JcrDocument document = new JcrDocumentFactory().createFromNode(sourceNode);
        assertNotNull(document);
        assertSame(sourceNode, document.getHandle());
    }
}
