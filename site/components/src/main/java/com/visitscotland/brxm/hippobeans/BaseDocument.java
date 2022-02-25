package com.visitscotland.brxm.hippobeans;

import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.services.DocumentUtilsService;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.util.List;
import java.util.stream.Collectors;

@Node(jcrType = "visitscotland:basedocument")
public class BaseDocument extends HippoDocument {

    private static final Logger logger = LoggerFactory.getLogger(BaseDocument.class.getName());
    private final DocumentUtilsService documentUtils = VsComponentManager.get(DocumentUtilsService.class);

    public String getPrimaryType() {
        try {
            return String.valueOf(node.getProperty("jcr:primaryType"));
        } catch (RepositoryException e) {
            logger.error(String.format("jcr:primaryType has not been defined by the node %s ({%s})", getName(), getClass()));
            return null;
        }
    }

    protected static <T extends HippoBean> T getOnlyChild(List<T> children) {
        if (children.isEmpty()) {
            return null;
        } else if (children.size() == 1) {
            return children.get(0);
        } else {
            logger.warn("This list in supposed to have only one child");
            return children.get(0);
        }
    }

    /**
     * There is an existing issue in BloomReach affecting only images where they are not correctly mapped. This method
     * works as a workaround to that issue
     *
     * @return
     */
    protected <T extends HippoBean> List<T> getMedia(String childNodeName) {
        return (List<T>) getChildBeansByName(childNodeName, HippoBean.class).stream().map(hippoBean -> {
                    if (hippoBean instanceof HippoMirror) {
                        return ((HippoMirror) hippoBean).getReferencedBean();
                    }
                    return hippoBean;
                }
        ).collect(Collectors.toList());
    }

}
