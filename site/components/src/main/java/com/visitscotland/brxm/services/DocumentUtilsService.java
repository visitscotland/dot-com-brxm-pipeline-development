package com.visitscotland.brxm.services;

import com.visitscotland.brxm.hippobeans.BaseDocument;
import com.visitscotland.brxm.hippobeans.Page;
import com.visitscotland.brxm.model.LocalizedURL;
import com.visitscotland.brxm.utils.ContentLogger;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.Language;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.*;

/**
 * Singleton
 */
@Component
public class DocumentUtilsService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentUtilsService.class.getName());


    public static final String DOCUMENT_TYPE = "jcr:primaryType";

    private final HippoUtilsService utils;
    private final ResourceBundleService bundle;
    private final Properties properties;
    private final Logger contentLog;

    public DocumentUtilsService(HippoUtilsService utils, ResourceBundleService bundle, Properties properties,
                                ContentLogger contentLogger) {
        this.utils = utils;
        this.bundle = bundle;
        this.properties = properties;
        this.contentLog = contentLogger;
    }

    /**
     * Returns a list of allowed documents defined by its main document (@{code Page})
     *
     * @param page Main document that represents a document and has some children associated
     */
    public List<BaseDocument> getAllowedDocuments(Page page) {
        return getSiblingDocuments(page, BaseDocument.class, page.getChildJcrTypes());
    }

    public <T extends BaseDocument> List<T> getAllowedDocuments(Page page, Class<T> expectedClass) {
        return getSiblingDocuments(page, expectedClass, page.getChildJcrTypes());
    }

    /**
     * Returns a list of allowed documents
     *
     * @param document      Main document that link all children
     * @param expectedClass A List of this type will be returned
     * @param allowedTypes  jcr:primaryTypes that are allowed to be discovered
     * @param <T>           Type that will be returned
     */
    public <T> List<T> getSiblingDocuments(HippoBean document, Class<T> expectedClass, String... allowedTypes) {
        //Get the list of sibling nodes
        final NodeIterator it;
        final List<T> documents = new ArrayList<>();

        try {
            it = document.getNode().getParent().getParent().getNodes();
        } catch (RepositoryException e) {
            logError("Error trying to connect to the repository", document.getNode(), e);
            return documents;
        }

        while (it.hasNext()) {
            javax.jcr.Node jcrNode = it.nextNode();
            try {
                if (jcrNode.getNodes().getSize() > 0) {
                    String primaryType = jcrNode.getNodes().nextNode().getProperty(DOCUMENT_TYPE).getString();
                    for (String type : allowedTypes) {
                        if (type.equals(primaryType)) {
                            Object hippoBean = utils.getDocumentFromNode(jcrNode);

                            //The document is added if the type matches
                            if (hippoBean != null) {
                                documents.add((T) hippoBean);
                            }
                            break;
                        }
                    }
                }
            } catch (QueryException | RepositoryException | NullPointerException | ObjectBeanManagerException e) {
                logError("The following node is corrupted", document.getNode(), e);
            }
        }
        return documents;
    }

    private void logError(String message, Node node, Exception e) {
        String logMessage;
        try {
            logMessage = message + " : " + node.getPath();
        } catch (RepositoryException e1) {
            logMessage = message + ". A nested exception happened while trying to access to the node ";
        }
        logger.error(logMessage, e);
    }



    public List<LocalizedURL> getLocalizedURLs(HstRequest request) {
        List<LocalizedURL> translatedURL = new ArrayList<>(Language.values().length);

        Optional<HippoBean> contentBean = utils.getContentBeanWithTranslationFallback(request);

        if (contentBean.isPresent()) {
            HippoBean document = contentBean.get();
            for (Language language : Language.values()) {
                LocalizedURL lan = new LocalizedURL();
                lan.setLocale(language.getLocale());
                lan.setLanguage(language.getLocale().getLanguage());
                lan.setDisplayName(bundle.getResourceBundle("universal", lan.getLanguage(), request.getLocale()));

                HippoBean translation = document.getAvailableTranslations().getTranslation(lan.getLanguage());

                if (Locale.UK.equals(language.getLocale())) {
                    if (translation == null) {
                        logger.error("The requested page does not exist in English: {}", document.getPath());
                    }
                }

                if (translation instanceof Page) {
                    lan.setUrl(utils.createUrl((Page) translation, false));
                    lan.setExists(true);
                } else {
                    //TODO: Define if the URL is made up, or we use the englishSite link instead
                    //lan.setUrl(utils.createUrl(englishSite));
                    lan.setUrl(composeNonExistingLanguageURL(language.getLocale(), request));
                    lan.setExists(false);
                }
                translatedURL.add(lan);
            }
        } else {
            logger.error("Menu functionality is not supported for Channel Manager Pages at the moment");
        }
        return translatedURL;
    }

    /**
     * Reorganize the document translations depending on an order predefined by SEO so hreflang
     *
     * @param <B>
     * @return
     */
    public <B extends BaseDocument> List<B> sortTranslationsForSeo(List<BaseDocument> availableTranslations){
        List<B> sortedTranslations = new ArrayList<>();
        // The ordering of translations for SEO purposes is defined in VS-1416 (see issue comments)
        // This is stored as a comma separated list in the channel properties file
        String seoSortOrderProperty = Contract.defaultIfNull(properties.getChannelOrder(), "");
        for (String locale: seoSortOrderProperty.split(",")) {
            for (BaseDocument bean : availableTranslations){
                if (locale != null && bean.getLocale().getLanguage().equals(locale)){
                    sortedTranslations.add((B) bean);
                }
            }
        }
        // If, for example, channel.seo.alternate-link-locale-orders is missing the fr language, then all french documents
        // in availableTranslations will not be placed in the sortedTranslations list
        if (sortedTranslations.size() != availableTranslations.size()) {
            contentLog.warn("Failed to order translations as property channel.seo.alternate-link-locale-orders is set incorrectly");
            return (List<B>)availableTranslations;
        }
        return sortedTranslations;
    }

    /**
     * Composes the URL from the current request for a non-existing URL.
     */
    private String composeNonExistingLanguageURL(Locale locale, HstRequest request){
        String languagePath = "";

        if (locale != null) {
            languagePath += Language.getLanguageForLocale(locale).getPathVariable();
        }

        return properties.getCmsBasePath() +
                languagePath + request.getRequestContext().getBaseURL().getPathInfo();
    }


}
