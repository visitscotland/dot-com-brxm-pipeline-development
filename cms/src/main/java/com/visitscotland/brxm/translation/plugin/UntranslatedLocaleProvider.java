package com.visitscotland.brxm.translation.plugin;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hippoecm.frontend.translation.ILocaleProvider;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class UntranslatedLocaleProvider implements IDataProvider<ILocaleProvider.HippoLocale> {
    private final ILocaleProvider localeProvider;
    private TranslationWorkflowPlugin workflow;
    private transient List<ILocaleProvider.HippoLocale> missingLocales;

    public UntranslatedLocaleProvider(TranslationWorkflowPlugin workflow, ILocaleProvider localeProvider) {
        this.workflow = workflow;
        this.localeProvider = localeProvider;
    }

    protected List<ILocaleProvider.HippoLocale> getMisingLocales() {
        return missingLocales;
    }

    protected void load() {
        missingLocales = new LinkedList<>();
        for (String language : workflow.getAvailableLanguages()) {
            if (!workflow.hasLocaleTranslation(language)) {
                missingLocales.add(localeProvider.getLocale(language));
            }
        }
    }

    @Override
    public Iterator<? extends ILocaleProvider.HippoLocale> iterator(long first, long count) {
        if (missingLocales == null) {
            load();
        }
        return missingLocales.subList((int) first, (int) (first + count)).iterator();
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
        if (missingLocales == null) {
            load();
        }
        return missingLocales.size();
    }

    public void detach() {
        missingLocales = null;
    }
}
