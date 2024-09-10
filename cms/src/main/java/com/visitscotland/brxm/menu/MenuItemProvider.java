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

    private static final String NEW_BE_PAGE_MENU = "new-be-page";
    private static final String NEW_BE_MODULE_MENU = "new-be-module";

    private static final String NEW_BSH_PAGE_MENU = "new-bsh-page";
    private static final String NEW_BSH_MODULE_MENU = "new-bsh-module";

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
                prototypes.clear();
            }
            simplifyMenu(subjectNode, prototypes, NEW_PAGE_MENU, NEW_MODULE_MENU);
            simplifyMenu(subjectNode, prototypes, NEW_BE_PAGE_MENU, NEW_BE_MODULE_MENU);
            simplifyMenu(subjectNode, prototypes, NEW_BSH_PAGE_MENU, NEW_BSH_MODULE_MENU);
        } catch (RepositoryException | ObjectBeanManagerException | QueryException ex) {
            logger.warn("Failed to obtain child JCR types for menu selection", ex);
        }
    }

    private void simplifyMenu(Node subjectNode, Map<String, Set<String>> prototypes, String pageMenu, String moduleMenu) throws ObjectBeanManagerException, QueryException, RepositoryException {
        if (prototypes.containsKey(pageMenu) && prototypes.containsKey(moduleMenu)) {
            Optional<Page> optionalPage = getPageContentBean(subjectNode);
            if (optionalPage.isPresent()) {
                prototypes.remove(pageMenu);
                if (!NEW_BE_PAGE_MENU.equals(pageMenu)) {
                    // Business Events currently get the list of items from the hippo:template defined at:
                    // /hippo:configuration/hippo:queries/hippo:templates/new-be-module
                    prototypes.put(moduleMenu, Set.of((optionalPage.get()).getChildJcrTypes()));
                }
            } else {
                prototypes.remove(moduleMenu);
            }
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
