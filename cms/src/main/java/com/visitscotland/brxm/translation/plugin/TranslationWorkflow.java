package com.visitscotland.brxm.translation.plugin;

import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.repository.api.Document;
import org.hippoecm.repository.api.Workflow;
import org.hippoecm.repository.api.WorkflowException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.rmi.RemoteException;

public interface TranslationWorkflow extends Workflow {
    String VS_TRANSLATABLE = "visitscotland:translatable";

    Document addTranslation(String var1, String var2) throws WorkflowException, RepositoryException, RemoteException, ObjectBeanManagerException;

    Document addTranslation(String language, String newDocumentName, Node sourceNode) throws WorkflowException, RepositoryException, RemoteException, ObjectBeanManagerException;

    void saveSession() throws RepositoryException;

}
