package com.visitscotland.brxm.translation.plugin;

import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.repository.translation.HippoTranslatedNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.RepositoryException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HippoTranslatedNodeFactoryTest {
    @Test
    public void createFromNode_throwsException() throws Exception{
        HippoNode mockNode = mock(HippoNode.class);
        when(mockNode.isNodeType(eq("hippotranslation:translated"))).thenReturn(false);
        assertThrows(RepositoryException.class, () -> new HippoTranslatedNodeFactory().createFromNode(mockNode));
    }

    @Test
    public void createFromNode_passesThroughNode() throws Exception {
        HippoNode mockNode = mock(HippoNode.class);
        when(mockNode.isNodeType(eq("hippotranslation:translated"))).thenReturn(true);
        HippoTranslatedNode translatedNode = new HippoTranslatedNodeFactory().createFromNode(mockNode);
        assertNotNull(translatedNode);
    }
}
