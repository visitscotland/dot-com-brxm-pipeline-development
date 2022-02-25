package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.Stackla;
import com.visitscotland.brxm.mock.StacklaMockBuilder;
import com.visitscotland.brxm.model.StacklaModule;
import com.visitscotland.brxm.services.ResourceBundleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import java.util.Locale;

@ExtendWith(MockitoExtension.class)
class StacklaFactoryTest {

    @InjectMocks
    StacklaFactory stacklaFactory;

    @Mock
    ResourceBundleService bundle;

    private static final String BUNDLE_ID = "stackla";

    @DisplayName("All information from stackla bean is passed into stackla module")
    @Test
    void stacklaModule() {
        Stackla stackla = new StacklaMockBuilder().title("title").copy("copy").dataHash("hash").dataId("id").build();
        when(bundle.getResourceBundle(BUNDLE_ID, "stackla.no-cookies-message", Locale.UK)).thenReturn("no cookies");
        when(bundle.getResourceBundle(BUNDLE_ID, "stackla.no-js-message", Locale.UK)).thenReturn("no js");
        when(bundle.getResourceBundle(BUNDLE_ID, "stackla.update-cookies-link.label", Locale.UK)).thenReturn("no cookies link");
        StacklaModule module = stacklaFactory.getStacklaModule(stackla, Locale.UK);
        Assertions.assertEquals("title", module.getTitle());
        Assertions.assertEquals("copy", module.getCopy().getContent());
        Assertions.assertEquals("hash", module.getDataHash());
        Assertions.assertEquals("id", module.getDataId());
        Assertions.assertEquals("no cookies", module.getNoCookiesMessage());
        Assertions.assertEquals("no js", module.getNoJsMessage());
        Assertions.assertEquals("no cookies link", module.getNoCookiesLinkLabel());
    }


}
