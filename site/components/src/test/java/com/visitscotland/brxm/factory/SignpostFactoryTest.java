package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.model.SignpostModule;
import com.visitscotland.brxm.services.ResourceBundleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Locale;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignpostFactoryTest {

    private static final String BUNDLE_ID = "newsletter-signpost";

    @Mock
    ResourceBundleService bundle;

    @InjectMocks
    SignpostFactory signpostFactory;

    @DisplayName("Newsletter signpost module includes newsletter information from properties")
    @Test
    void newsletterSignpost() {
        when(bundle.getResourceBundle(BUNDLE_ID, "newsletter.cta.text", Locale.UK)).thenReturn("cta.text");
        when(bundle.getResourceBundle(BUNDLE_ID, "newsletter.cta.link", Locale.UK)).thenReturn("cta.link");
        when(bundle.getResourceBundle(BUNDLE_ID, "newsletter.title", Locale.UK)).thenReturn("title");
        when(bundle.getResourceBundle(BUNDLE_ID, "newsletter.copy", Locale.UK)).thenReturn("copy");

        SignpostModule module = signpostFactory.createNewsletterSignpostModule(Locale.UK);

        Assertions.assertEquals("cta.text", module.getCta().getLabel());
        Assertions.assertEquals("cta.link", module.getCta().getLink());
        Assertions.assertEquals(LinkType.INTERNAL, module.getCta().getType());
        Assertions.assertEquals("title", module.getTitle());
        Assertions.assertEquals("copy", module.getCopy().getContent());
    }

}
