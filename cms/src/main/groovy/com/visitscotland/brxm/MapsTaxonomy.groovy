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
 * Groovy Script created for altering the existing Destinations and Stops documents in order to add the taxonomy field.
 *
 * This Scripts acted also as a practice exercise for future Groovy Scripts
 *
 * Use the following XPATH /jcr:root
 *
 * @author jhurtado
 * @since June 2022
 * @version 1.0.6
 */
class MapsTaxonomy extends BaseNodeUpdateVisitor {

    void fixDestination(Session session){
        renameField(session, "Destination");
    }

    void fixStop(Session session){
        renameField(session, "Stop");
    }


    void renameField(Session session, String targetDocument) {
        NodeIterator it = query(session,"//content/documents//element(*, visitscotland:${targetDocument})")

        while (it.hasNext()){
            Node n = it.next()
            log.debug n.getPath()

            n.addMixin("hippotaxonomy:classifiable")

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

        fixDestination(node.session);
        fixStop(node.session)
        return true;
    }

    @Override
    boolean undoUpdate(Node node) throws RepositoryException, UnsupportedOperationException {
        return false
    }
}
