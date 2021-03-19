package com.visitscotland.brxm.translation.plugin;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class JcrDocumentFactory {
    public JcrDocument createFromNode(Node sourceNode) throws RepositoryException {
        return new JcrDocument(sourceNode);
    }
}
