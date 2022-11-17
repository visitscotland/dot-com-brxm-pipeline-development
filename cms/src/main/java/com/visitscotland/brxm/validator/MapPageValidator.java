package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.hippobeans.General;
import com.visitscotland.brxm.utils.HippoUtilsService;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;
import org.onehippo.taxonomy.api.Taxonomy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.util.Optional;

/**
 * jcr:name = visitscotland:map-page-validator
 */
public class MapPageValidator implements Validator<Node> {

    private static final Logger logger = LoggerFactory.getLogger(MapPageValidator.class);

    public HippoUtilsService getUtilsService() {
        return VsComponentManager.get(HippoUtilsService.class);
    }

    @Override
    public Optional<Violation> validate(ValidationContext validationContext, Node node) {
        try {
            Value[] taxonomyKeys = node.getProperty("hippotaxonomy:keys").getValues();
            boolean isGeneralPage = getUtilsService().getDocumentFromNode(node.getParent().getParent().getNode("content")) instanceof General;
            if (isGeneralPage){
                if (taxonomyKeys.length > 1) {
                    return Optional.of(validationContext.createViolation("generalPage"));
                }else{
                    if (taxonomyKeys.length > 0) {
                        Taxonomy vsTaxonomyTree = getUtilsService().getTaxonomy();
                        if (vsTaxonomyTree.getCategoryByKey(taxonomyKeys[0].getString()).getChildren().isEmpty()){
                            return Optional.of(validationContext.createViolation("generalPage"));
                        }
                    }
                }
            }
            else{
                if (taxonomyKeys.length > 0) {
                    return Optional.of(validationContext.createViolation("destinationPage"));
                }
            }
        } catch (RepositoryException | QueryException | ObjectBeanManagerException ex) {
            logger.error("Repository error during validation", ex);
        }
        return Optional.empty();
    }
}
