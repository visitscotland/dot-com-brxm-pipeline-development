package com.visitscotland.brxm.translation.plugin;

import org.hippoecm.frontend.translation.ILocaleProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
public class ChangeSetFactoryTest {
    @Mock
    ILocaleProvider.HippoLocale mockLocale;

    @Test
    public void constructor() {
        ChangeSet change = new ChangeSetFactory().createChangeSet(mockLocale);
        assertSame(mockLocale, change.getTargetLocale());
    }
}
