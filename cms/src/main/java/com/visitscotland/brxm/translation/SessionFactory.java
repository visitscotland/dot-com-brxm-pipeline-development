package com.visitscotland.brxm.translation;

import org.hippoecm.frontend.session.UserSession;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class SessionFactory {
    public Session getJcrSession() {
        return UserSession.get().getJcrSession();
    }
    public UserSession getUserSession() { return UserSession.get(); }

    public Node getHippoNodeByIdentifier(String id) throws RepositoryException {
        Node galleryNode = getJcrSession().getNodeByIdentifier(id);
        if (galleryNode != null){
            return galleryNode.getNode(galleryNode.getName());
        }
        return null;
    }
}
