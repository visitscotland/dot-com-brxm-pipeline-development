package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.hippobeans.General;
import com.visitscotland.brxm.utils.HippoUtilsService;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;
import org.onehippo.taxonomy.api.Taxonomy;
import org.onehippo.taxonomy.api.TaxonomyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.util.Optional;

/**
 * jcr:nane = visitscotland:map-page-validator
 */
public class MapPageValidator implements Validator<Node> {

    private static final String CMS_LINK_TYPE = "visitscotland:CMSLink";
    private static final Logger logger = LoggerFactory.getLogger(MapPageValidator.class);

    private HippoUtilsService utilsService;

    public MapPageValidator() {
        this.utilsService = new HippoUtilsService();
    }

    @Override
    public Optional<Violation> validate(ValidationContext validationContext, Node node) {
        try {
            Value[] taxonomyKeys = node.getProperty("hippotaxonomy:keys").getValues();
            boolean isGeneralPage = utilsService.getDocumentFromNode(node.getParent().getParent().getNode("content")) instanceof General;
            if (isGeneralPage){
                if (taxonomyKeys.length > 1) {
                    return Optional.of(validationContext.createViolation("generalPage"));
                }else{
                    if (taxonomyKeys.length > 0) {
                        TaxonomyManager taxonomyManager = HstServices.getComponentManager().getComponent("TaxonomyManager", "org.onehippo.taxonomy.contentbean");
                        Taxonomy vsTaxonomyTree = taxonomyManager.getTaxonomies().getTaxonomy("Visitscotland-categories");
                        if (vsTaxonomyTree.getCategoryByKey(taxonomyKeys[0].getString()).getChildren().isEmpty()){
                            return Optional.of(validationContext.createViolation("generalPage"));
                        }
                    }
                }
            }
            else{
                if (taxonomyKeys.length > 0) {
                    return Optional.of(validationContext.createViolation("destionatioPage"));
                }
            }
        } catch (RepositoryException ex) {
            logger.error("Repository error during validation", ex);
        } catch (QueryException e) {
            e.printStackTrace();
        } catch (ObjectBeanManagerException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
