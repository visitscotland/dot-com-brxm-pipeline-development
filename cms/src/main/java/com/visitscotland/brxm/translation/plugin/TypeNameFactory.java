package com.visitscotland.brxm.translation.plugin;

import org.hippoecm.frontend.i18n.types.TypeTranslator;
import org.hippoecm.frontend.model.nodetypes.JcrNodeTypeModel;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import static org.hippoecm.repository.api.HippoNodeType.*;

public class TypeNameFactory {
    protected String lookupTypeName(Node node) {
        // Code originally from org.hippoecm.frontend.plugins.standards.list.resolvers.TypeRenderer
        try {
            if (node.isNodeType(NT_HANDLE) && node.hasNode(node.getName())) {
                node = node.getNode(node.getName());
            }
            String type = null;
            if (node.isNodeType(NT_DOCUMENT)) {
                type = node.getPrimaryNodeType().getName();
            }
            if (node.isNodeType(NT_TEMPLATETYPE)) {
                type = node.getParent().getName() + ":" + node.getName();
            }
            if (type != null) {
                return new TypeTranslator(new JcrNodeTypeModel(type)).getTypeName().getObject();
            }
        } catch (RepositoryException ex) {
            // consume the exception and make the type unknown
        }
        return "unknown";
    }
}
