package com.visitscotland.brxm.translation.plugin;

import org.apache.wicket.model.IModel;
import org.hippoecm.addon.workflow.WorkflowDescriptorModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LanguageModelTest {
    private static final String TEST_PROPERTY = "test value";
    private LanguageModel model;
    @Mock
    private TranslationWorkflowPlugin mockWorkflow;
    @Mock
    private WorkflowDescriptorModel mockWorkflowModel;
    @Mock
    private Node mockNode;
    @Mock
    private Property mockProperty;

    @BeforeEach
    public void beforeEach() throws Exception {
        lenient().when(mockWorkflow.getDefaultModel()).thenReturn((IModel) mockWorkflowModel);
        lenient().when(mockWorkflowModel.getNode()).thenReturn(mockNode);
        lenient().when(mockNode.getProperty(anyString())).thenReturn(mockProperty);

        model = new LanguageModel(mockWorkflow);
    }

    @Test
    public void valid() throws Exception {
        // A valid test case, expect the result to returned with expected value
        when(mockProperty.getString()).thenReturn(TEST_PROPERTY);

        String result = model.load();
        assertEquals(TEST_PROPERTY, result);
    }

    @Test
    public void node_getProperty_throws_RepositoryException() throws Exception {
        // General repository exception thrown when getting the locale property from the node,
        // all exceptions should be caught and unknown value returned
        when(mockNode.getProperty(anyString())).thenThrow(new RepositoryException());

        String result = model.load();
        assertEquals(LanguageModel.UNKNOWN_VALUE, result);
    }

    @Test
    public void property_getString_throws_RepositoryException() throws Exception {
        // General repository exception thrown when getting the property value,
        // all exceptions should be caught and unknown value returned
        when(mockProperty.getString()).thenThrow(new RepositoryException());

        String result = model.load();
        assertEquals(LanguageModel.UNKNOWN_VALUE, result);
    }

    @Test
    public void workflow_getNode_throws_RepositoryException() throws Exception {
        // A general repository exception is thrown when getting the node from the workflow,
        // all exceptions should be caught and unknown value returned
        when(mockWorkflowModel.getNode()).thenThrow(new RepositoryException());

        String result = model.load();
        assertEquals(LanguageModel.UNKNOWN_VALUE, result);
    }

    @Test
    public void workflowModel_isNull() {
        when(mockWorkflow.getDefaultModel()).thenReturn(null);

        String result = model.load();
        assertEquals(LanguageModel.UNKNOWN_VALUE, result);
    }
}
