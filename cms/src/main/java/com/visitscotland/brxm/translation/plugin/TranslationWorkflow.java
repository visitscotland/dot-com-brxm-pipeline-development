package com.visitscotland.brxm.translation.plugin;

import com.visitscotland.brxm.report.translation.TranslationPriority;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.repository.api.Document;
import org.hippoecm.repository.api.Workflow;
import org.hippoecm.repository.api.WorkflowException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.rmi.RemoteException;
import java.util.Calendar;

public interface TranslationWorkflow extends Workflow {
    String VS_TRANSLATABLE = "visitscotland:translatable";

    Document addTranslation(String var1, String var2) throws WorkflowException, RepositoryException, RemoteException, ObjectBeanManagerException;

    Document addTranslation(String language, String newDocumentName, Node sourceNode) throws WorkflowException, RepositoryException, RemoteException, ObjectBeanManagerException;

    void setTranslationPriority(TranslationPriority priority) throws RepositoryException, RemoteException;

    void clearTranslationFlag() throws RepositoryException, RemoteException;

    void setTranslationDeadline(Calendar deadline) throws RepositoryException, RemoteException;

    void setTranslationFlag(boolean flag) throws RepositoryException, RemoteException;

    void setTranslationDiff(String diff) throws RepositoryException, RemoteException;

    void saveSession() throws RepositoryException;

}
