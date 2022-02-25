package com.visitscotland.brxm

import org.hippoecm.repository.api.HippoWorkspace
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.query.Query
import javax.jcr.query.QueryResult

/**
 * Groovy Script created for altering the structure of Megalinks due to the changes related to video.
 *
 * This Scripts acted also as a practice exercise for future Groovy Scripts
 *
 * Use the following XPATH /jcr:root
 *
 * @author jcalcines
 * @since November 2021
 * @version 0.0.1
 */
class VideoMegalinks extends BaseNodeUpdateVisitor {

    void fixArticle(Session session){
        renameField(session, "Article", "image", "media");
    }

    void fixStop(Session session){
        renameField(session, "Stop", "image", "media");
    }

    void fixMegalinks(Session session){
        renameField(session, "MegalinkItem", "link", "links");
    }

    void renameField(Session session, String targetDocument, String fromField, String toField) {
        NodeIterator it = query(session,"//content/documents//element(*, visitscotland:${targetDocument})")

        while (it.hasNext()){
            Node n = it.next()
            log.debug n.getPath()
            if (n.hasNode("visitscotland:${fromField}")) {
                if (n.hasNode("visitscotland:${toField}")) {
                    session.removeItem(n.getNode("visitscotland:${fromField}").getPath())
                } else {
                    session.move(n.getNode("visitscotland:${fromField}").getPath(), n.getPath() + "/visitscotland:${toField}")
                }
            }
        }
    }

    void fixOTYML(Session session) {
        NodeIterator it = query(session,"//content/documents//element(*, visitscotland:OTYML)")

        while (it.hasNext()){
            Node n = it.next()
            log.debug n.getPath()
            NodeIterator links = n.getNodes("visitscotland:links")
            while (links.hasNext()){
                Node link = links.next()
                if (link.hasNode("visitscotland:link")) {
                    log.info "Converting ${link.getPath()}"
                    session.move(link.getNode("visitscotland:link").getPath(), n.getPath() + "/visitscotland:links")
                    session.removeItem(link.getPath())
                }
            }
        }
    }

    /**
     * Executes a query and log when no results are returned
     * @param session
     * @param query
     * @return
     */
    NodeIterator query(def session, def query){
        QueryResult results = ((HippoWorkspace) session.getWorkspace()).getQueryManager().createQuery(query, Query.XPATH).execute();
        if (!results.getNodes().hasNext()){
            log.warn "No query results for ${query}"
        } else {
            // Note that the method size. Moves the pointer for the iterator at the end. That is why we need to
            // invoke .getNodes() for getting the size
            log.info "Matches = ${results.getNodes().size()}";
        }
        return results.getNodes();
    }

    @Override
    boolean doUpdate(Node node) {
        fixOTYML(node.session);

        fixArticle(node.session);
        fixStop(node.session)
        fixMegalinks(node.session);
        return true;
    }

    @Override
    boolean undoUpdate(Node node) throws RepositoryException, UnsupportedOperationException {
        return false
    }
}
