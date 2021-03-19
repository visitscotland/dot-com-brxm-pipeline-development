package com.visitscotland.brxm.translation.plugin.menu;

import com.visitscotland.brxm.translation.plugin.TranslationWorkflow;
import com.visitscotland.brxm.translation.plugin.TranslationWorkflowPlugin;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.hippoecm.addon.workflow.StdWorkflow;
import org.hippoecm.addon.workflow.WorkflowDescriptorModel;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.service.IBrowseService;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.hippoecm.repository.translation.HippoTranslatedNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class ViewTranslationAction extends StdWorkflow<TranslationWorkflow> {
    private static final Logger logger = LoggerFactory.getLogger(ViewTranslationAction.class);
    private final String language;
    private final IModel<ILocaleProvider.HippoLocale> localeModel;
    private TranslationWorkflowPlugin workflow;

    public ViewTranslationAction(
            TranslationWorkflowPlugin workflowPlugin,
            String id,
            IModel<String> name,
            IModel<ILocaleProvider.HippoLocale> localeModel,
            String language,
            IModel<String> languageModel) {
        super(id, name, workflowPlugin.getPluginContext(), (WorkflowDescriptorModel) workflowPlugin.getModel());
        this.workflow = workflowPlugin;
        this.language = language;
        this.localeModel = localeModel;
    }

    @Override
    public boolean isVisible() {
        if (super.isVisible() && findPage() != null) {
            return workflow.canTranslateModel();
        }
        return false;
    }

    @Override
    protected Component getIcon(final String id) {
        return workflow.getActionIcon(id, localeModel);
    }

    @Override
    protected String execute(TranslationWorkflow wf) {
        final IBrowseService<IModel<Node>> browser = workflow.getBrowserService();
        if (browser == null) {
            logger.warn("Cannot navigate to translation - configured browser.id '" + workflow.getPluginConfig().getString(
                    "browser.id") + "' is invalid.");
            return null;
        }

        final WorkflowDescriptorModel wdm = (WorkflowDescriptorModel) workflow.getDefaultModel();
        if (wdm == null) {
            logger.error("No workflow descriptor model for document");
            return null;
        }

        Node node;
        try {
            node = wdm.getNode();
            if (node == null) {
                logger.error("No node found for document");
                return null;
            }

            HippoTranslatedNode translatedNode = createFromNode(node);
            Node translation = translatedNode.getTranslation(language);
            browser.browse(createJcrNodeModel(translation.getParent()));
        } catch (RepositoryException e) {
            logger.error("Error retrieving translation node", e);
        }

        return null;
    }

    protected HippoTranslatedNode createFromNode(Node node) throws RepositoryException {
        return new HippoTranslatedNode(node);
    }

    protected JcrNodeModel createJcrNodeModel(Node node) throws RepositoryException {
        return new JcrNodeModel(node);
    }
}
