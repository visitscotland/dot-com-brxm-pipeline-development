package com.visitscotland.brxm.report.translation;

import com.visitscotland.brxm.translation.SessionFactory;
import com.visitscotland.brxm.translation.plugin.JcrDocument;
import org.hippoecm.repository.api.Workflow;
import org.hippoecm.repository.api.WorkflowException;
import org.hippoecm.repository.impl.NodeDecorator;
import org.hippoecm.repository.standardworkflow.EditableWorkflow;
import org.springframework.stereotype.Component;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.RowIterator;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@FunctionalInterface
interface CheckedExceptionConsumer<T, E extends Throwable> {
    void accept(T arg) throws E;
}

@Component
public class JcrUtilService {

    private final SessionFactory sessionFactory;

    private final String EDIT_WORKFLOW_NAME = "editing";
    private final String VS_TRANSLATION_PRIORITY = "visitscotland:translationPriority";

    public JcrUtilService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setTranslationPriority(JcrDocument document, TranslationPriority priority) throws RepositoryException, RemoteException, WorkflowException {
        if (!document.getLocaleName().equals("en")) {
            throw new IllegalArgumentException("Can not set " + VS_TRANSLATION_PRIORITY + " on Node with locale" + document.getLocaleName());
        }

        doInEditWorkflow(document, jcrSession -> {
            Node node = document.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED);
            node.setProperty(VS_TRANSLATION_PRIORITY, priority.toString());
        });

    }

    /**
     * Allows for a document to be safely edited
     *
     * Manages JCR session & edit workflow. Also ensures that the document is not being edited by another user
     * @param document  The document to be edited inside of the action lambda
     * @param action
     * @throws RepositoryException
     * @throws RemoteException
     * @throws WorkflowException
     */
    public void doInEditWorkflow(JcrDocument document, CheckedExceptionConsumer<Session, RepositoryException> action) throws RepositoryException, RemoteException, WorkflowException {
        if (document.isDraftBeingEdited()) {
            throw new RepositoryException("Document is locked by another user");
        }
        Workflow editing = sessionFactory.getUserSession().getWorkflowManager().getWorkflow(EDIT_WORKFLOW_NAME, document.getHandle());
        Session jcrSession = sessionFactory.getJcrSession();


        if (editing instanceof EditableWorkflow) {
            EditableWorkflow editableWorkflow = (EditableWorkflow) editing;
            editableWorkflow.obtainEditableInstance();
            try {
                // Actually run the function in the session
                action.accept(jcrSession);
            } finally {
                editableWorkflow.disposeEditableInstance();
            }

            jcrSession.save();
            jcrSession.refresh(true);
        } else {
            throw new IllegalStateException("Unable to get EditableWorkflow");
        }
    }

    /**
     * Determine all types that are derived from a given base type
     * @param supertype
     * @return
     * @throws RepositoryException
     */
    public Set<String> getTypesDeriving(String supertype) throws RepositoryException {
        String query = String.format("//element(*, hipposysedit:nodetype)[jcr:contains(@hipposysedit:supertype, \"%s\")]/../..", supertype);
        Set<String> types = new HashSet<>();

        for (Node node : queryNodes(query, "xpath")) {
            types.add(node.getName());
        }

        return types;
    }

    public List<JcrDocument> getAllUnpublishedDocuments() throws RepositoryException {
        List<NodeDecorator> nodes = queryNodes("select * from visitscotland:basedocument where hippostd:state = 'unpublished' and hippotranslation:locale = 'en'", "sql");
        List<JcrDocument> jcrDocuments = new ArrayList<>();
        for (NodeDecorator node : nodes) {
            jcrDocuments.add(new JcrDocument(node));
        }
        return jcrDocuments;
    }

    public List<NodeDecorator> queryNodes(String query, String type) throws RepositoryException {
        RowIterator allPagesRows = sessionFactory.getJcrSession().getWorkspace().getQueryManager()
                .createQuery(query, type).execute().getRows();

        List<NodeDecorator> nodes = new ArrayList<>();
        while (allPagesRows.hasNext()) {
            nodes.add((NodeDecorator) allPagesRows.nextRow().getNode());
        }

        return nodes;
    }
}
