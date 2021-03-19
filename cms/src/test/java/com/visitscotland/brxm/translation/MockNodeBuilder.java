package com.visitscotland.brxm.translation;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class MockNodeBuilder {
    Map<String, Property> properties = new HashMap<>();
    String primaryNodeType;
    List<String> nodeTypes = new ArrayList<>();
    Map<String, List<Node>> childNodes = new HashMap<>();
    String nodeId;
    Session jcrSession;

    public MockNodeBuilder inJcrSession(Session jcrSession) {
        this.jcrSession = jcrSession;
        return this;
    }

    public MockNodeBuilder withNodeId(String nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public MockNodeBuilder withPrimaryNodeType(String primaryNodeType) {
        this.primaryNodeType = primaryNodeType;
        return this;
    }

    public MockNodeBuilder withNodeType(String nodeType) {
        nodeTypes.add(nodeType);
        return this;
    }

    public MockNodeBuilder withProperty(String propertyPath, Value[] valueArray) throws Exception {
        Property mockProperty = mock(Property.class);
        lenient().when(mockProperty.getValues()).thenReturn(valueArray);
        properties.put(propertyPath, mockProperty);
        return this;
    }

    public MockNodeBuilder withProperty(String propertyPath, String propertyValue) throws Exception {
        Property mockProperty = mock(Property.class);
        lenient().when(mockProperty.getString()).thenReturn(propertyValue);
        properties.put(propertyPath, mockProperty);
        return this;
    }

    public MockNodeBuilder withProperty(String propertyPath, boolean propertyValue) throws Exception {
        Property mockProperty = mock(Property.class);
        lenient().when(mockProperty.getBoolean()).thenReturn(propertyValue);
        properties.put(propertyPath, mockProperty);
        return this;
    }

    public MockNodeBuilder withProperty(String propertyPath, Property property) throws Exception {
        properties.put(propertyPath, property);
        return this;
    }

    public MockNodeBuilder withPropertyThowsValueFormatException(String propertyPath) throws Exception {
        Property mockProperty = mock(Property.class);
        lenient().when(mockProperty.getString()).thenThrow(new ValueFormatException());
        properties.put(propertyPath, mockProperty);
        return this;
    }

    public MockNodeBuilder withChildNode(String propertyPath, Node childNode) {
        List<Node> nodes = childNodes.get(propertyPath);
        if (null == nodes) {
            nodes = new ArrayList<>();
            childNodes.put(propertyPath, nodes);
        }
        nodes.add(childNode);
        return this;
    }

    public MockNodeBuilder withEmptyChildNode(String propertyPath) {
        List<Node> nodes = childNodes.get(propertyPath);
        if (null == nodes) {
            nodes = new ArrayList<>();
            childNodes.put(propertyPath, nodes);
        }
        nodes.clear();
        return this;
    }

    public Node build() throws Exception {
        Node mockNode = mock(Node.class);
        lenient().when(mockNode.hasProperty(any())).thenReturn(false);
        lenient().when(mockNode.isNodeType(any())).thenReturn(false);

        if ( null != jcrSession && null != nodeId) {
            when(jcrSession.getNodeByIdentifier(eq(nodeId))).thenReturn(mockNode);
        }

        if ( null != primaryNodeType) {
            NodeType mockNodeType = mock(NodeType.class);
            when(mockNode.getPrimaryNodeType()).thenReturn(mockNodeType);
            when(mockNodeType.getName()).thenReturn(primaryNodeType);
            lenient().when(mockNode.isNodeType(eq(primaryNodeType))).thenReturn(true);
        }

        for (String type : nodeTypes) {
            when(mockNode.isNodeType(type)).thenReturn(true);
        }

        for (Map.Entry<String, Property> propertyEntry : properties.entrySet()) {
            lenient().when(mockNode.hasProperty(eq(propertyEntry.getKey()))).thenReturn(true);
            when(mockNode.getProperty(eq(propertyEntry.getKey()))).thenReturn(propertyEntry.getValue());
        }

        for (Map.Entry<String, List<Node>> childNodeEntry : childNodes.entrySet()) {
            final NodeIterator mockNodeIterator = createNodeIterator(childNodeEntry.getValue());
            lenient().when(mockNode.getNodes(eq(childNodeEntry.getKey()))).thenReturn(mockNodeIterator);
        }

        List<Node> allChildNodes = new ArrayList<>();
        for (Map.Entry<String, List<Node>> childNodeEntry : childNodes.entrySet()) {
            allChildNodes.addAll(childNodeEntry.getValue());
        }
        final NodeIterator mockAllNodeIterator = createNodeIterator(allChildNodes);
        lenient().when(mockNode.getNodes()).thenReturn(mockAllNodeIterator);

        return mockNode;
    }

    private NodeIterator createNodeIterator(List<Node> nodeList) {
        final Iterator<Node> nodeIterator = nodeList.iterator();
        NodeIterator mockNodeIterator = mock(NodeIterator.class);
        lenient().when(mockNodeIterator.hasNext()).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return nodeIterator.hasNext();
            }
        });
        lenient().when(mockNodeIterator.nextNode()).thenAnswer(new Answer<Node>() {
            @Override
            public Node answer(InvocationOnMock invocation) throws Throwable {
                return nodeIterator.next();
            }
        });
        return mockNodeIterator;
    }
}
