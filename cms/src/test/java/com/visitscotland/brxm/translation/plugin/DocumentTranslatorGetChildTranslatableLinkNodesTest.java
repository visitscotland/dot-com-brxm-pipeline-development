package com.visitscotland.brxm.translation.plugin;

import com.visitscotland.brxm.translation.MockNodeBuilder;
import com.visitscotland.brxm.translation.SessionFactory;
import org.hippoecm.repository.HippoStdNodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Node;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class DocumentTranslatorGetChildTranslatableLinkNodesTest {
    private DocumentTranslator documentTranslator;
    @Mock
    private SessionFactory mockSessionFactory;
    @Mock
    private HippoTranslatedNodeFactory mockHippoTranslatedNodeFactory;
    @Mock
    private JcrDocumentFactory mockJcrDocumentFactory;
    @Mock
    private ChangeSetFactory mockChangeSetFactory;

    @BeforeEach
    public void beforeEach() {
        documentTranslator = new DocumentTranslator(mockHippoTranslatedNodeFactory,
                                                    mockSessionFactory,
                                                    mockJcrDocumentFactory,
                                                    mockChangeSetFactory);
    }

    @Test
    public void getChildTranslatableLinkNodes_noChildren() throws Exception {
        // When the TranslationLinkContainer contains a type, but none of the children are hippo:mirror.
        Node mockSourceNode = new MockNodeBuilder()
                .build();
        List<Node> nodeList = documentTranslator.getChildTranslatableLinkNodes(mockSourceNode);

        assertNotNull(nodeList);
        assertTrue(nodeList.isEmpty());
    }

    @Test
    public void getChildTranslatableLinkNodes_withMatchingChildren() throws Exception {
        // When the node has hippo:mirror children
        Node mockChildNode = new MockNodeBuilder()
                .withNodeType("hippo:mirror")
                .withProperty("hippo:docbase", "LINK_ID")
                .build();

        Node mockNestedMirror = new MockNodeBuilder()
                .withNodeType("hippo:mirror")
                .withProperty("hippo:docbase", "LINK_ID")
                .build();
        Node mockNestedParentNode = new MockNodeBuilder()
                .withNodeType(HippoStdNodeType.NT_CONTAINER)
                .withChildNode("child", mockNestedMirror)
                .build();

        Node mockSourceNode = new MockNodeBuilder()
                .withChildNode("child1", mockChildNode)
                .withChildNode("child2", mockNestedParentNode)
                .build();

        List<Node> nodeList = documentTranslator.getChildTranslatableLinkNodes(mockSourceNode);

        assertNotNull(nodeList);
        assertFalse(nodeList.isEmpty());
        assertTrue(nodeList.contains(mockChildNode));
        assertTrue(nodeList.contains(mockNestedMirror));
    }
}
