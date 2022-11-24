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
 * Groovy Script created for altering foreign language channels.
 *
 * This script seek for host nodes with languages (es,fr,de,it,nl) and
 *
 * Use the following XPATH /jcr:root
 *
 * @author jcalcines
 * @since October 2022
 * @version 1.0.13
 */
class ChangeLanguageURLSegment extends BaseNodeUpdateVisitor {

    void renameNodes(Session session) {
        NodeIterator it = query(session,"//hst:visitscotland/hst:hosts//element(*, hst:mount)")

        while (it.hasNext()){
            Node dataNode = it.next()

            if (dataNode.name == "es" || dataNode.name == "fr" ||
                    dataNode.name == "de" || dataNode.name == "it" ||
                    dataNode.name == "nl"){
                log.debug "Renaming node ${dataNode.name} (${dataNode.path})"
                session.move(dataNode.path, dataNode.path + "-" + dataNode.name)
            } else {
                log.debug "Skipping node ${dataNode.name} (${dataNode.path})"
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
        renameNodes(node.session);
        return true;
    }

    @Override
    boolean undoUpdate(Node node) throws RepositoryException, UnsupportedOperationException {
        return false
    }
}