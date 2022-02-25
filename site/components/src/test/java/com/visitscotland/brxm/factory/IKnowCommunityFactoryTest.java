package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.IknowCommunity;
import com.visitscotland.brxm.mock.IKnowCommunityMockBuilder;
import com.visitscotland.brxm.model.IKnowCommunityModule;
import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.Properties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IKnowCommunityFactoryTest {

    @Mock
    ResourceBundleService bundle;

    @Mock
    Properties properties;

    @InjectMocks
    IKnowCommunityFactory factory;

    private static final String BUNDLE_ID = "iknow-community";
    private static final String TAG_BUNDLE_KEY = "iknow-community-tags";

    @DisplayName("When no title or copy provided then default title and copy are used")
    @Test
    void whenNoTitleOrCopyProvided_thenDefaultTitleAndCopyUsed() {
        when(bundle.getResourceBundle(BUNDLE_ID, "iknow-community.title.default", Locale.UK))
                .thenReturn("default title");
        when(bundle.getResourceBundle(BUNDLE_ID, "iknow-community.copy.default", Locale.UK))
                .thenReturn("default copy");
        IknowCommunity iknowCommunity = new IKnowCommunityMockBuilder().copy("").tags(Collections.emptyList()).build();

        IKnowCommunityModule module = factory.getIKnowCommunityModule(iknowCommunity, Locale.UK);

        Assertions.assertEquals("default title", module.getTitle());
        Assertions.assertEquals("default copy", module.getCopy().getContent());
        Assertions.assertEquals(0, module.getTags().size());
    }

    @DisplayName("When title or copy provided then they are used in module")
    @Test
    void whenTitleOrCopyProvided_thenUsedInModule() {
        IknowCommunity iknowCommunity = new IKnowCommunityMockBuilder()
                .title("title").copy("copy").tags(Collections.emptyList()).build();

        IKnowCommunityModule module = factory.getIKnowCommunityModule(iknowCommunity, Locale.UK);

        Assertions.assertEquals("title", module.getTitle());
        Assertions.assertEquals("copy", module.getCopy().getContent());
        Assertions.assertEquals(0, module.getTags().size());
    }

    @DisplayName("When tags exists, they are converted into links with correct labels and maintain order")
    @Test
    void tagsConvertedIntoLinks() {
        when(properties.getIknowCommunityUrl())
                .thenReturn("domain/");
        when(properties.getIknowCommunityTaggedDiscussion())
                .thenReturn("subdomain/");

        doReturn("title").when(bundle).getResourceBundle(BUNDLE_ID, "iknow-community.title.default", Locale.UK);
        doReturn("copy").when(bundle).getResourceBundle(BUNDLE_ID, "iknow-community.copy.default", Locale.UK);
        doReturn("link").when(bundle).getResourceBundle(BUNDLE_ID, "iknow-community.link.label", Locale.UK);
        doReturn("First Label").when(bundle).getResourceBundle(TAG_BUNDLE_KEY, "first", Locale.UK);
        doReturn("Second Label").when(bundle).getResourceBundle(TAG_BUNDLE_KEY, "second", Locale.UK);
        IknowCommunity iknowCommunity = new IKnowCommunityMockBuilder().tags(Arrays.asList("first", "second")).build();

        IKnowCommunityModule module = factory.getIKnowCommunityModule(iknowCommunity, Locale.UK);

        Assertions.assertEquals(2, module.getTags().size());
        // These also assert that ordering is maintained
        Assertions.assertEquals("domain/subdomain/first", module.getTags().get(0).getLink());
        Assertions.assertEquals("domain/subdomain/second", module.getTags().get(1).getLink());
        Assertions.assertEquals("First Label", module.getTags().get(0).getLabel());
        Assertions.assertEquals("Second Label", module.getTags().get(1).getLabel());
        Assertions.assertEquals(LinkType.INTERNAL, module.getTags().get(0).getType());
        Assertions.assertEquals(LinkType.INTERNAL, module.getTags().get(1).getType());
    }

    @DisplayName("When tag doesn't exist in value map, then use tag key as link label")
    @Test
    void tagValueDoesNotExist() {
        when(properties.getIknowCommunityUrl())
                .thenReturn("domain/");
        when(properties.getIknowCommunityTaggedDiscussion())
                .thenReturn("subdomain/");
        doReturn("title").when(bundle).getResourceBundle(BUNDLE_ID, "iknow-community.title.default", Locale.UK);
        doReturn("copy").when(bundle).getResourceBundle(BUNDLE_ID, "iknow-community.copy.default", Locale.UK);
        doReturn("link").when(bundle).getResourceBundle(BUNDLE_ID, "iknow-community.link.label", Locale.UK);
        doReturn(null).when(bundle).getResourceBundle(TAG_BUNDLE_KEY, "third", Locale.UK);
        IknowCommunity iknowCommunity = new IKnowCommunityMockBuilder().tags(Collections.singletonList("third")).build();

        IKnowCommunityModule module = factory.getIKnowCommunityModule(iknowCommunity, Locale.UK);

        Assertions.assertEquals(1, module.getTags().size());
        Assertions.assertEquals("domain/subdomain/third", module.getTags().get(0).getLink());
        Assertions.assertEquals("third", module.getTags().get(0).getLabel());
        Assertions.assertEquals(LinkType.INTERNAL, module.getTags().get(0).getType());
    }

    @DisplayName("Community link added to module")
    @Test
    void communityLink() {
        when(properties.getIknowCommunityUrl())
                .thenReturn("url");
        when(bundle.getResourceBundle(eq(BUNDLE_ID), any(), eq(Locale.UK))).thenReturn("");
        when(bundle.getResourceBundle(BUNDLE_ID, "iknow-community.link.label", Locale.UK)).thenReturn("label");

        IknowCommunity iknowCommunity = new IKnowCommunityMockBuilder().tags(Collections.emptyList()).build();
        IKnowCommunityModule module = factory.getIKnowCommunityModule(iknowCommunity, Locale.UK);

        Assertions.assertEquals("url", module.getLink().getLink());
        Assertions.assertEquals("label", module.getLink().getLabel());
        Assertions.assertEquals(LinkType.INTERNAL, module.getLink().getType());
    }

}
