package com.visitscotland.brxm.translation.plugin;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hippoecm.frontend.translation.ILocaleProvider;

import java.util.Iterator;
import java.util.List;

public class LocaleListProvider implements IDataProvider<ILocaleProvider.HippoLocale> {
    private List<ILocaleProvider.HippoLocale> localeList;

    public LocaleListProvider(List<ILocaleProvider.HippoLocale> localeList) {
        this.localeList = localeList;
    }

    @Override
    public Iterator<? extends ILocaleProvider.HippoLocale> iterator(long first, long count) {
        return localeList.subList((int) first, (int) (first + count)).iterator();
    }

    @Override
    public long size() {
        return localeList.size();
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
    public void detach() {

    }
}
