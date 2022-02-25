package com.visitscotland.brxm.menu;

import com.visitscotland.brxm.components.content.ContentComponent;
import com.visitscotland.brxm.hippobeans.Page;
import com.visitscotland.brxm.translation.plugin.JcrDocument;
import com.visitscotland.brxm.utils.HippoUtilsService;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.repository.api.RepositoryMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.*;

@Component
public class MenuItemProvider {

    private static final String NEW_PAGE_MENU = "new-page";
    private static final String NEW_MODULE_MENU = "new-module";
    private static final String NEW_FOLDER_MENU = "new-translated-folder";
    private static final String LOCALE_PROPERTY_PATH = "hippotranslation:locale";

    private static final Logger logger = LoggerFactory.getLogger(MenuItemProvider.class);
    private final HippoUtilsService hippoUtilsService;

    public MenuItemProvider(HippoUtilsService hippoUtilsService) {
        this.hippoUtilsService = hippoUtilsService;
    }

    public void constructPageAndModuleMenus(Node subjectNode, Map<String, Set<String>> prototypes, RepositoryMap workflowConfiguration) {
        try {
            Object createDocumentOnTranslationObject = workflowConfiguration.get("visitscotland:create-documents-on-translations");
            boolean createDocumentOnTranslation = createDocumentOnTranslationObject instanceof Boolean ? (Boolean) createDocumentOnTranslationObject : true;
            if (!isEnglishFolder(subjectNode) && !createDocumentOnTranslation) {
                prototypes.remove(NEW_PAGE_MENU);
                prototypes.remove(NEW_MODULE_MENU);
                prototypes.remove(NEW_FOLDER_MENU);
            } else if (prototypes.containsKey(NEW_PAGE_MENU) && prototypes.containsKey(NEW_MODULE_MENU)) {
                Optional<Page> optionalPage = getPageContentBean(subjectNode);
                if (optionalPage.isPresent()) {
                    prototypes.remove(NEW_PAGE_MENU);
                    prototypes.put(NEW_MODULE_MENU, new TreeSet<>(Arrays.asList((optionalPage.get()).getChildJcrTypes())));
                } else {
                    prototypes.remove(NEW_MODULE_MENU);
                }
            }
        } catch (RepositoryException | ObjectBeanManagerException | QueryException ex) {
            logger.warn("Failed to obtain child JCR types for menu selection", ex);
        }
    }

    private boolean isEnglishFolder(Node node) throws RepositoryException {
        if (node.hasProperty(LOCALE_PROPERTY_PATH)) {
            return node.getProperty(LOCALE_PROPERTY_PATH).getString().equals("en");
        }
        return true;
    }

    private Optional<Page> getPageContentBean(Node subjectNode) throws RepositoryException, ObjectBeanManagerException, QueryException {
        if (!subjectNode.hasNode(ContentComponent.PAGE_PATH) || !subjectNode.getNode(ContentComponent.PAGE_PATH).isNodeType(JcrDocument.HIPPO_HANDLE)) {
            return Optional.empty();
        }

        HippoBean subjectBean = hippoUtilsService.getDocumentFromNode(subjectNode.getNode(ContentComponent.PAGE_PATH), true);
        if (!(subjectBean instanceof Page)) {
            return Optional.empty();
        }
        return Optional.of((Page)subjectBean);
    }

}
