package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.Stackla;
import com.visitscotland.brxm.mock.UserGeneratedContentMockBuilder;
import com.visitscotland.brxm.model.UserGeneratedContentModule;
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
class UserGeneratedContentFactoryTest {

    @InjectMocks
    UserGeneratedContentFactory userGeneratedContentFactory;

    @Mock
    ResourceBundleService bundle;

    private static final String BUNDLE_ID = "ugc";

    @DisplayName("All information from stackla bean is passed into User Generated Content module")
    @Test
    void UserGeneratedContentModule() {
        Stackla stackla = new UserGeneratedContentMockBuilder().title("title").copy("copy").dataId("id").build();
        when(bundle.getResourceBundle(BUNDLE_ID, "ugc.no-cookies-message", Locale.UK)).thenReturn("no cookies");
        when(bundle.getResourceBundle(BUNDLE_ID, "ugc.no-js-message", Locale.UK)).thenReturn("no js");
        UserGeneratedContentModule module = userGeneratedContentFactory.getUGCModule(stackla, Locale.UK);
        Assertions.assertEquals("title", module.getTitle());
        Assertions.assertEquals("copy", module.getCopy().getContent());
        Assertions.assertEquals("id", module.getStorystreamId());
        Assertions.assertEquals("no cookies", module.getNoCookiesMessage());
        Assertions.assertEquals("no js", module.getNoJsMessage());
    }


}
