package com.visitscotland.brxm.hippobeans;

import com.visitscotland.brxm.services.DocumentUtilsService;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.util.List;

@Node(jcrType="visitscotland:basedocument")
public class BaseDocument extends HippoDocument {

    private static final Logger logger = LoggerFactory.getLogger(BaseDocument.class.getName());

    public String getPrimaryType() {
        try {
            return String.valueOf(node.getProperty("jcr:primaryType"));
        } catch (RepositoryException e) {
            logger.error(String.format("jcr:primaryType has not been defined by the node %s ({%s})", getName(), getClass()));
            return null;
        }
    }


    /**
     * TODO: Remove this method after the refactoring of itineraries
     *
     * @deprecated Instead of this, you should invoke {@code new DocumentUtils().getSiblingDocuments()}     *
     */
    @Deprecated
    public <T extends HippoBean> List<T> getPageChildrenByType(Class<T> type) {
        return DocumentUtilsService.getInstance().getSiblingDocuments(this, type, type.getAnnotation(Node.class).jcrType());
    }

    protected <T> T getOnlyChild(List<T> children) {
        if (children.size() == 0) {
            return null;
        } else if (children.size() == 1) {
            return children.get(0);
        } else {
            logger.warn("This list in supposed to have only one child");
            return children.get(0);
        }
    }
}
