package com.visitscotland.brxm.translation.plugin;

import org.hippoecm.frontend.translation.ILocaleProvider;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.repository.HippoStdNodeType;
import org.hippoecm.repository.translation.HippoTranslatedNode;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Provides helper methods for working with the hippo documents.
 * Each document is represented by a handle Node with multiple Variant documents,
 * this class encapsulates that functionality.
 */
public class JcrDocument {
    public static final String HIPPO_HANDLE = "hippo:handle";
    public static final String HIPPO_TRANSLATED = HippoStdNodeType.NT_TRANSLATED;
    public static final String HIPPOSTD_STATE = HippoStdNodeType.HIPPOSTD_STATE;
    public static final String HIPPOSTD_PUBLISHABLE = HippoStdNodeType.NT_PUBLISHABLE;

    public static final String VARIANT_PUBLISHED = HippoStdNodeType.PUBLISHED;
    public static final String VARIANT_UNPUBLISHED = HippoStdNodeType.UNPUBLISHED;
    public static final String VARIANT_DRAFT = HippoStdNodeType.DRAFT;

    public static final String VS_TRANSLATION_DIFF = "visitscotland:diff";
    public static final String VS_TRANSLATION_FLAG = "visitscotland:translationFlag";
    public static final String VS_TRANSLATABLE_TYPE = "visitscotland:translatable";
    public static final String VS_TRANSLATION_DEADLINE = "visitscotland:translationDeadline";
    public static final String VS_TRANSLATION_PRIORITY = "visitscotland:translationPriority";

    private Node handle;
    // Do not access directly, will be lazy loaded, use getter
    protected Map<String, Node> variantMap;
    // Do not access directly, will be lazy loaded, use getter
    protected HippoBean hippoBean;

    protected Set<String> translationLocales;

    /**
     * Will create an instance from the hippostd:handle instance or one of the document variants.
     *
     * @param handle
     */
    public JcrDocument(Node handle) throws RepositoryException {
        if (null == handle) {
            throw new IllegalArgumentException("the Node supplied must be a handle or a document variant, not null");
        }

        if (handle.isNodeType(HIPPO_HANDLE)) {
            this.handle = handle;
        } else {
            Node parent = handle.getParent();
            if (null != parent && parent.isNodeType(HIPPO_HANDLE)) {
                this.handle = parent;
            } else {
                throw new IllegalArgumentException("the Node supplied must be a handle or a document variant");
            }
        }
    }

    /**
     * Gets the variants of the document, or if it has not been initialised will populate the variants.
     *
     * @return A Map of the variant hippostd:state to the variant Node
     */
    public Map<String, Node> getVariants() throws RepositoryException {
        if (null == variantMap) {
            variantMap = new HashMap<>();
            NodeIterator varIterator = handle.getNodes();
            while (varIterator.hasNext()) {
                Node variant = varIterator.nextNode();
                if (variant.isNodeType(HippoStdNodeType.NT_PUBLISHABLE)) {
                    Property state = variant.getProperty(HIPPOSTD_STATE);
                    variantMap.put(state.getString(), variant);
                }
            }
        }
        return variantMap;
    }

    /**
     * Returns the variant with the matching state, or returns null if not present.
     *
     * @param state the variant state to match, draft, unpublished, or published
     * @return the variant Node matching the given state
     * @throws RepositoryException
     */
    public Node getVariantNode(String state) throws RepositoryException {
        return getVariants().get(state);
    }

    /**
     * Will return true if the unpublished variant of a document matches one of the jcr types.
     *
     * @param jcrType
     * @return
     * @throws RepositoryException
     */
    public boolean isNodeType(String... jcrType) throws RepositoryException {
        Node unpublished = getVariants().get(VARIANT_UNPUBLISHED);
        if (null != unpublished) {
            for (String type : jcrType) {
                if (unpublished.isNodeType(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the folder Node containing this document.
     *
     * @return
     * @throws RepositoryException
     */
    public Node getContainingFolder() throws RepositoryException {
        Node parent = handle.getParent();
        if (null == parent || !parent.isNodeType(HippoStdNodeType.NT_FOLDER)) {
            throw new IllegalStateException("No folder found for Node");
        }
        return parent;
    }

    /**
     * Gets the handle Node for the document.
     *
     * @return hippostd:handle Node
     */
    public Node getHandle() {
        return handle;
    }

    /**
     * Converts the document to a HippoBean. Will be lazy loaded on first access.
     *
     * @return
     */
    public HippoBean asHippoBean() throws ObjectBeanManagerException {
        if (null == hippoBean) {
            hippoBean = (HippoBean) RequestContextProvider.get().getContentBeansTool()
                    .getObjectConverter().getObject(handle);
        }
        return hippoBean;
    }

    public <T> T asHippoBean(Class<T> clazz) throws ObjectBeanManagerException {
        return clazz.cast(asHippoBean());
    }

    public boolean hasTranslation(ILocaleProvider.HippoLocale targetlocale) throws RepositoryException {
        return hasTranslation(targetlocale.getName());
    }

    public boolean hasTranslation(String localeName) throws RepositoryException {
        HippoTranslatedNode translatedNode = new HippoTranslatedNode(getVariantNode(VARIANT_UNPUBLISHED));
        return translatedNode.hasTranslation(localeName);
    }

    public Node getTranslation(String localName) throws RepositoryException {
        HippoTranslatedNode translatedNode = new HippoTranslatedNode(getVariantNode(VARIANT_UNPUBLISHED));
        return translatedNode.getTranslation(localName);
    }

    public String getLocaleName() throws RepositoryException {
        HippoTranslatedNode translatedNode = new HippoTranslatedNode(getVariantNode(VARIANT_UNPUBLISHED));
        return translatedNode.getLocale();
    }

    public Set<JcrDocument> getTranslations() throws RepositoryException {
        HippoTranslatedNode translatedNode = new HippoTranslatedNode(getVariantNode(VARIANT_UNPUBLISHED));
        Set<String> translations = translatedNode.getTranslations();
        Set<JcrDocument> translationDocuments = new HashSet<>();
        for (String language : translations) {
            if (!language.equals("en")) {
                Node translation = translatedNode.getTranslation(language);
                translationDocuments.add(new JcrDocument(translation));
            }
        }
        return translationDocuments;
    }

    public boolean isDeleted() throws RepositoryException {
        return handle.getPath() == null || handle.getPath().startsWith("/content/attic/");
    }

    public Set<String> getTranslationLocaleNames() throws RepositoryException {
        // Cache translation locales as HippoTranslatedNode#getTranslations requires the JCR to be queried
        if (translationLocales != null) return translationLocales;
        translationLocales =  new HippoTranslatedNode(getVariantNode(VARIANT_UNPUBLISHED)).getTranslations();
        return translationLocales;
    }

    public boolean isDraftBeingEdited() throws RepositoryException {
        Node draftNode = getVariantNode(JcrDocument.VARIANT_DRAFT);
        if (null == draftNode) {
            return false;
        }
        return draftNode.hasProperty(HippoStdNodeType.HIPPOSTD_HOLDER);
    }
}
