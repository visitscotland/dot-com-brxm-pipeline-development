package com.visitscotland.brxm.translation.plugin;

import org.hippoecm.repository.api.HippoNode;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public final class JcrFolderTranslationFactory {

    public FolderTranslation createFolderTranslation(Node original, Node node) throws RepositoryException {
        if (original == null && node == null) {
            throw new IllegalArgumentException("Both source and target folders are null");
        }

        Node reference;
        if (original == null) {
            reference = node;
        } else {
            reference = original;
        }
        FolderTranslation ft = new FolderTranslation(reference.getIdentifier());

        if (original != null) {
            ft.setUrl(original.getName());
            ft.setName(((HippoNode) original).getDisplayName());
            ft.setSourceNode(original);
        }

        if (node != null) {
            ft.setUrlfr(node.getName());
            ft.setNamefr(((HippoNode) node).getDisplayName());
        }

        return ft;
    }

}
