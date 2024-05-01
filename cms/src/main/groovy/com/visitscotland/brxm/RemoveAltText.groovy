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
 * Groovy Script created for removing the value of alt-Text from image as all current images have been tagged as decorative.
 *
 * See VS-3830 For more information
 *
 * Use the following XPATH /jcr:root
 *
 * @author jcalcines
 * @since August 2022 (Revised on March 2023)
 * @version 1.0.9
 */
class RemoveAltText extends BaseNodeUpdateVisitor {

    void removeAltText(Session session){
        NodeIterator it = query(session,"//content/gallery/visitscotland//element(*)[visitscotland:altText != \"\"]")

        while (it.hasNext()){
            Node n = it.next()
            log.debug "Removing alternative text from ${n.getPath()}"
            n.getProperty("visitscotland:altText").remove()
        }
    }

    @Override
    boolean doUpdate(Node node) {
        removeAltText(node.session)
        return true
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
            log.info "Matches = ${results.getNodes().size()}"
        }
        return results.getNodes()
    }

    @Override
    boolean undoUpdate(Node node) throws RepositoryException, UnsupportedOperationException {
        return false
    }
}