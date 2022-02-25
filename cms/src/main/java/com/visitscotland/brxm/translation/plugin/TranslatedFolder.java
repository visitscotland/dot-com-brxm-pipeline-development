package com.visitscotland.brxm.translation.plugin;

import org.hippoecm.repository.translation.HippoTranslatedNode;
import org.hippoecm.repository.translation.HippoTranslationNodeType;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class TranslatedFolder {

    private final Node node;

    public TranslatedFolder(Node node) {
        if (null == node) {
            throw new IllegalArgumentException("unable to intialise to null");
        }
        this.node = node;
    }

    /**
     * Return the first translated parent of a node, or null if the root
     * path is reached before finding a translated parent.
     *
     * @return The first translated parent or null if they are all untranslated.
     * @throws RepositoryException
     */
    public TranslatedFolder getParent() throws RepositoryException {
        Node ancestor = node;
        do {
            ancestor = ancestor.getParent();
            if ("/content/documents".equals(ancestor.getPath())) {
                return null;
            }
        } while (!ancestor.isNodeType(HippoTranslationNodeType.NT_TRANSLATED));
        return new TranslatedFolder(ancestor);
    }

    public TranslatedFolder getSibling(String locale) throws RepositoryException {
        HippoTranslatedNode translatedNode = toHippoTranslatedNode();
        try {
            return new TranslatedFolder(translatedNode.getTranslation(locale));
        } catch (ItemNotFoundException e) {
            return null;
        }
    }

    protected HippoTranslatedNode toHippoTranslatedNode() throws RepositoryException {
        return new HippoTranslatedNode(getNode());
    }

    public Node getNode() {
        return node;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TranslatedFolder)) {
            return false;
        }
        try {
            return ((TranslatedFolder) obj).node.isSame(node);
        } catch (RepositoryException e) {
            throw new IllegalStateException("could not determine whether nodes are equivalent", e);
        }
    }

    @Override
    public int hashCode() {
        try {
            return node.getPath().hashCode();
        } catch (RepositoryException e) {
            throw new IllegalStateException("could not determine path of node", e);
        }
    }
}
