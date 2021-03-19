package com.visitscotland.brxm.translation.plugin;

import org.hippoecm.repository.translation.HippoTranslatedNode;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class HippoTranslatedNodeFactory {
    public HippoTranslatedNode createFromNode(Node node) throws RepositoryException {
        return new HippoTranslatedNode(node);
    }
}
