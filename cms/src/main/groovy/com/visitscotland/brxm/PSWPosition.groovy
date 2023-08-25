package com.visitscotland.brxm

import org.hippoecm.repository.api.HippoWorkspace
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.query.Query
import javax.jcr.query.QueryResult
import java.util.regex.Pattern

/**
 *
 *
 * @author jcalcines
 * @since August 2023
 * @version 2.0.1
 */
class PSWPosition extends BaseNodeUpdateVisitor {


    def STANDARD_REGEX = "/sandbox(/.*)?"
    def TOP_LEVEL_REGEX = "/sandbox(/.*)?"
    def ROOT_NODE = "/content/documents/visitscotland/"

    @Override
    boolean doUpdate(Node node) {
        applyRule(node.session, "Top-Level", TOP_LEVEL_REGEX)
        applyRule(node.session, "Standard", STANDARD_REGEX)
        return true
    }

    void applyRule(session, pageType, regex){
        NodeIterator it = query(session,"//content/documents//element(*, visitscotland:General)[visitscotland:theme = \"${pageType}\"]")
        def pattern = Pattern.compile(regex)
        int counter = 0

        while (it.hasNext()){
            Node n = it.next()
            if (!n.hasProperty("visitscotland:pswPosition") || n.getProperty("visitscotland:pswPosition") == "Default") {
                String path = n.path.substring(ROOT_NODE.length()-1, n.path.indexOf("/content/content"))
                def matcher = path =~ pattern
                n.setProperty("visitscotland:pswPosition", matcher.find()?"Top":"Bottom")
                log.info "Path = ${path}, Position = ${matcher.find()?"Top":"Bottom"}"
            } else{

            }
        }

        if (counter > 0) {
            log.info "A total of ${counter} ${pageType} pages have been skipped"
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
            log.info "Matches = ${results.getNodes().size()}"
        }
        return results.getNodes()
    }

    @Override
    boolean undoUpdate(Node node) throws RepositoryException, UnsupportedOperationException {
        return false
    }
}
