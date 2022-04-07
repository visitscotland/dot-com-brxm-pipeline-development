package com.visitscotland.brxm.translation.plugin.menu;

import com.visitscotland.brxm.translation.plugin.*;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MenuLocaleProvider implements IDataProvider<ILocaleProvider.HippoLocale> {
    private static final Logger logger = LoggerFactory.getLogger(MenuLocaleProvider.class);
    private final ILocaleProvider localeProvider;
    private TranslationWorkflowPlugin workflowPlugin;
    private transient List<ILocaleProvider.HippoLocale> availableLocales;
    private DocumentTranslator documentTranslator;

    public MenuLocaleProvider(TranslationWorkflowPlugin workflowPlugin) {
        this(workflowPlugin, new DocumentTranslator());
    }

    protected MenuLocaleProvider(TranslationWorkflowPlugin workflowPlugin, DocumentTranslator documentTranslator) {
        this.workflowPlugin = workflowPlugin;
        this.localeProvider = workflowPlugin.getLocaleProvider();
        this.documentTranslator = documentTranslator;
    }

    protected List<ILocaleProvider.HippoLocale> getAvailableLocales() {
        return availableLocales;
    }

    void load() {
        availableLocales = new LinkedList<>();
        String documentLocale = workflowPlugin.getCurrentlySelectedDocumentLocale();
        // Only want to add languages we have a translation for
        boolean hasMissingTranslation = false;
        for (String language : workflowPlugin.getAvailableLanguages()) {
            if (workflowPlugin.hasLocaleTranslation(language)) {
                // Don't add the current language to the menu
                if (!documentLocale.equals(language)) {
                    availableLocales.add(localeProvider.getLocale(language));
                }
            } else {
                hasMissingTranslation = true;
            }
        }
        availableLocales.sort(Comparator.comparing(o -> o.getDisplayName(workflowPlugin.getLocale())));
        if (hasMissingTranslation) {
            availableLocales.add(0, new UntranslatedLocale());
        } else {
            try {
                // Build a change set to check if there are any untranslated children
                // A change set is only added to the list if it or one of it's children is missing a translation
                List<ChangeSet> changeSetList = documentTranslator.buildChangeSetList(workflowPlugin.getSourceDocumentNode(),
                        workflowPlugin.getAvailableLocales());

                if (!changeSetList.isEmpty()) {
                    availableLocales.add(0, new UntranslatedLocale());
                }
            } catch (ObjectBeanManagerException | RepositoryException | TranslationException ex) {
                logger.error("Unable to build change set for document children");
            }
        }
    }

    @Override
    public Iterator<? extends ILocaleProvider.HippoLocale> iterator(long first, long count) {
        if (availableLocales == null) {
            load();
        }
        return availableLocales.subList((int) first, (int) (first + count)).iterator();
    }

    @Override
    public IModel<ILocaleProvider.HippoLocale> model(ILocaleProvider.HippoLocale object) {
        return new LoadableDetachableModel<ILocaleProvider.HippoLocale>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected ILocaleProvider.HippoLocale load() {
                return object;
            }

        };
    }

    @Override
    public long size() {
        if (availableLocales == null) {
            load();
        }
        return availableLocales.size();
    }

    public void detach() {
        availableLocales = null;
    }
}
