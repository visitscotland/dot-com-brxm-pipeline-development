package com.visitscotland.brxm.translation.plugin;

import org.apache.wicket.model.IModel;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocaleListProviderTest {
    @Test
    public void size() {
        List<ILocaleProvider.HippoLocale> localeList = mock(List.class);
        LocaleListProvider provider = new LocaleListProvider(localeList);

        when(localeList.size()).thenReturn(5);
        assertEquals(5, provider.size());
    }

    @Test
    public void iterator() {
        List<ILocaleProvider.HippoLocale> localeList = mock(List.class);
        LocaleListProvider provider = new LocaleListProvider(localeList);

        Iterator mockIterator = mock(Iterator.class);
        List subList = mock(List.class);
        when(subList.iterator()).thenReturn(mockIterator);
        when(localeList.subList(anyInt(), anyInt())).thenReturn(subList);

        Iterator sublistIterator = provider.iterator(1, 5);
        assertSame(mockIterator, sublistIterator);
        verify(localeList).subList(eq(1), eq(6));
    }

    @Test
    public void model() {
        ILocaleProvider.HippoLocale mockLocale = mock(ILocaleProvider.HippoLocale.class);
        List<ILocaleProvider.HippoLocale> localeList = mock(List.class);
        LocaleListProvider provider = new LocaleListProvider(localeList);

        IModel<ILocaleProvider.HippoLocale> model = provider.model(mockLocale);
        assertNotNull(model);
        assertSame(mockLocale, model.getObject());
    }
}
