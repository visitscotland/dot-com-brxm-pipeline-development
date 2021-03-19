package com.visitscotland.brxm.translation.plugin.menu;

import com.visitscotland.brxm.translation.plugin.TranslationWorkflow;
import com.visitscotland.brxm.translation.plugin.TranslationWorkflowPlugin;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.WicketTester;
import org.hippoecm.addon.workflow.WorkflowDescriptorModel;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.service.IBrowseService;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.repository.translation.HippoTranslatedNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ViewTranslationActionTest {
    private WicketTester wicket;
    private ViewTranslationAction action;
    @Mock
    private TranslationWorkflowPlugin mockWorkflowPlugin;
    @Mock
    private IModel<String> mockNameModel;
    @Mock
    private IModel<ILocaleProvider.HippoLocale> mockLocaleModel;
    @Mock
    private IModel<String> mockLanguageModel;
    @Mock
    private TranslationWorkflow mockWorkflow;

    @BeforeEach
    public void beforeEach() {
        wicket = new WicketTester();
        action = spy(new ViewTranslationAction(
                mockWorkflowPlugin,
                "translation",
                mockNameModel,
                mockLocaleModel,
                "es",
                mockLanguageModel));
    }

    @Test
    public void execute_browser_is_null() {
        // Just to ensure that an exception is thrown
        when(mockWorkflowPlugin.getBrowserService()).thenReturn(null);
        IPluginConfig mockConfig = mock(IPluginConfig.class);
        when(mockConfig.getString(eq("browser.id"))).thenReturn("config");
        when(mockWorkflowPlugin.getPluginConfig()).thenReturn(mockConfig);

        action.execute(mockWorkflow);
    }

    @Test
    public void execute_WorkflowDescriptorModel_is_null() {
        // Just to ensure that an exception is thrown
        IBrowseService<IModel<Node>> mockBrowser = mock(IBrowseService.class);
        when(mockWorkflowPlugin.getBrowserService()).thenReturn(mockBrowser);
        when(mockWorkflowPlugin.getDefaultModel()).thenReturn(null);

        action.execute(mockWorkflow);
    }

    @Test
    public void execute_document_node_is_null() {
        // Just to ensure that an exception is thrown
        IBrowseService<IModel<Node>> mockBrowser = mock(IBrowseService.class);
        when(mockWorkflowPlugin.getBrowserService()).thenReturn(mockBrowser);
        WorkflowDescriptorModel mockWdm = mock(WorkflowDescriptorModel.class);
        when(mockWorkflowPlugin.getDefaultModel()).thenReturn((IModel) mockWdm);

        action.execute(mockWorkflow);
    }

    @Test
    public void execute_translation_not_found() throws Exception {
        // verify when the getTranslation method throws a RepositoryException it is caught
        IBrowseService<IModel<Node>> mockBrowser = mock(IBrowseService.class);
        when(mockWorkflowPlugin.getBrowserService()).thenReturn(mockBrowser);
        WorkflowDescriptorModel mockWdm = mock(WorkflowDescriptorModel.class);
        when(mockWorkflowPlugin.getDefaultModel()).thenReturn((IModel) mockWdm);
        HippoNode mockDocNode = mock(HippoNode.class);
        when(mockWdm.getNode()).thenReturn(mockDocNode);
        HippoTranslatedNode mockTranslatedNode = mock(HippoTranslatedNode.class);
        doReturn(mockTranslatedNode).when(action).createFromNode(same(mockDocNode));
        when(mockTranslatedNode.getTranslation(eq("es"))).thenThrow(new RepositoryException());

        action.execute(mockWorkflow);
    }

    @Test
    public void execute_success() throws Exception {
        IBrowseService<IModel<Node>> mockBrowser = mock(IBrowseService.class);
        when(mockWorkflowPlugin.getBrowserService()).thenReturn(mockBrowser);
        WorkflowDescriptorModel mockWdm = mock(WorkflowDescriptorModel.class);
        when(mockWorkflowPlugin.getDefaultModel()).thenReturn((IModel) mockWdm);
        HippoNode mockDocNode = mock(HippoNode.class);
        when(mockWdm.getNode()).thenReturn(mockDocNode);
        HippoTranslatedNode mockTranslatedHippoNode = mock(HippoTranslatedNode.class);
        doReturn(mockTranslatedHippoNode).when(action).createFromNode(same(mockDocNode));
        Node mockTranslatedNode = mock(Node.class);
        Node mockTranslatedNodeParent = mock(Node.class);
        when(mockTranslatedNode.getParent()).thenReturn(mockTranslatedNodeParent);
        when(mockTranslatedHippoNode.getTranslation(eq("es"))).thenReturn(mockTranslatedNode);

        doReturn(mock(JcrNodeModel.class)).when(action).createJcrNodeModel(any(Node.class));

        action.execute(mockWorkflow);

        verify(mockBrowser).browse(any(JcrNodeModel.class));
    }
}
