package com.visitscotland.brxm.menu;

import com.visitscotland.brxm.config.VsComponentManager;
import org.hippoecm.repository.api.WorkflowContext;
import org.hippoecm.repository.standardworkflow.FolderWorkflowImpl;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;

/**
 * This custom workflow is enabled in /hippo:configuration/hippo:workflows/threepane/folder-permissions
 * Used to dynamically specify the new document/folder menus and the types of documents that can be created from them for admin users
 *
 * Note: /hippo:configuration/hippo:workflows/threepane/folder-extended and /hippo:configuration/hippo:workflows/threepane/folder nodes
 * allow the documents to be created by other roles and no just admin users
 */
public class CustomFolderWorkflowImpl extends FolderWorkflowImpl {

    private final Node subjectNode;
    private final MenuItemProvider menuItemProvider;
    private final WorkflowContext context;

    public CustomFolderWorkflowImpl(WorkflowContext context, Session userSession, Session rootSession, Node subject) throws RepositoryException {
        super(context, userSession, rootSession, subject);
        subjectNode = subject;
        menuItemProvider = VsComponentManager.get(MenuItemProvider.class);
        this.context = context;
    }

    /**
     * This method defines the new document options in the menu, and for each new document what document types should
     * be shown in the selector
     *
     * @return A map where the key corresponds to the name of a node under /hippo:configuration/hippo:queries/hippo:templates
     * (e.g. new-module). The value is a set of document types that can be created from the given menu (e.g.
     * visitscotland:Destination).
     */
    @Override
    protected Map<String, Set<String>> prototypes() {
        Map<String, Set<String>> prototypes = super.prototypes();
        menuItemProvider.constructPageAndModuleMenus(this.subjectNode, prototypes, this.context.getWorkflowConfiguration());
        return prototypes;
    }

}
