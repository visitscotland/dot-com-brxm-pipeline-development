package com.visitscotland.brxm.report.translation;

import com.visitscotland.brxm.translation.SessionFactory;
import com.visitscotland.brxm.translation.plugin.JcrDocument;
import org.hippoecm.repository.impl.NodeDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.query.RowIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class JcrUtilService {

    private static final Logger logger = LoggerFactory.getLogger(JcrUtilService.class);

    private final SessionFactory sessionFactory;

    public JcrUtilService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
    /**
     * Determine all types that are derived from a given base type
     * @param supertype
     * @return
     * @throws RepositoryException
     */
    public Set<String> getTypesDeriving(String supertype) throws RepositoryException {
        String query = String.format("//element(*, hipposysedit:nodetype)[jcr:contains(@hipposysedit:supertype, \"%s\")]/../..", supertype);
        Set<String> types = new HashSet<>();

        for (Node node : queryNodes(query, "xpath")) {
            types.add(node.getName());
        }

        return types;
    }

    public List<JcrDocument> getAllUnpublishedDocuments() throws RepositoryException {
        List<NodeDecorator> nodes = queryNodes("select * from visitscotland:basedocument where hippostd:state = 'unpublished' and hippotranslation:locale = 'en'", "sql");
        List<JcrDocument> jcrDocuments = new ArrayList<>();
        for (NodeDecorator node : nodes) {
            jcrDocuments.add(new JcrDocument(node));
        }
        return jcrDocuments;
    }

    public List<NodeDecorator> queryNodes(String query, String type) throws RepositoryException {
        RowIterator allPagesRows = sessionFactory.getJcrSession().getWorkspace().getQueryManager()
                .createQuery(query, type).execute().getRows();

        List<NodeDecorator> nodes = new ArrayList<>();
        while (allPagesRows.hasNext()) {
            nodes.add((NodeDecorator) allPagesRows.nextRow().getNode());
        }
        logger.debug("The query has returned {} results", nodes.size());

        return nodes;
    }
}
