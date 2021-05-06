package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.IknowCommunity;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.IKnowCommunityModule;
import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.utils.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class IKnowCommunityFactory {

    private final HippoUtilsService utils;
    private final ResourceBundleService bundle;
    private final Properties properties;
    private static final String BUNDLE_ID = "iknow-community";
    private static final String TAG_VALUE_LIST_IDENTIFIER = "iknow-community-tags";
    private static final Logger logger = LoggerFactory.getLogger(IKnowCommunityFactory.class);


    public IKnowCommunityFactory(HippoUtilsService utils, ResourceBundleService bundle, Properties properties){
        this.utils = utils;
        this.bundle = bundle;
        this.properties = properties;
    }

    public IKnowCommunityModule getIKnowCommunityModule(IknowCommunity iknowCommunity, Locale locale) {
        logger.info("Creating iKnowCommunityModule for {}", iknowCommunity.getPath());

        IKnowCommunityModule iKnowCommunityModule = new IKnowCommunityModule();
        if (Contract.isEmpty(iknowCommunity.getTitle())){
            iKnowCommunityModule.setTitle(bundle.getResourceBundle(BUNDLE_ID,"iknow-community.title.default", locale));
        } else {
            iKnowCommunityModule.setTitle(iknowCommunity.getTitle());
        }

        if (Contract.isEmpty(iknowCommunity.getCopy())){
            iKnowCommunityModule.setCopy(bundle.getResourceBundle(BUNDLE_ID, "iknow-community.copy.default", locale));
        } else {
            iKnowCommunityModule.setCopy(iknowCommunity.getCopy());
        }

        iKnowCommunityModule.setLink(new FlatLink(bundle.getResourceBundle(BUNDLE_ID, "iknow-community.link.label", locale),
                this.properties.getIknowCommunityUrl(), LinkType.INTERNAL));

        Map<String, String> tagValueMap = utils.getValueMap(TAG_VALUE_LIST_IDENTIFIER);
        List<FlatLink> tagLinks = Arrays.stream(iknowCommunity.getTags()).map(tagKey -> {
            if (!tagValueMap.containsKey(tagKey)) {
                logger.error("Failed to find key {} in value list iknow-community-tags", tagKey);
            }
            return new FlatLink(tagValueMap.getOrDefault(tagKey, tagKey), getTaggedDiscussionUrl(tagKey), LinkType.INTERNAL);
        }).collect(Collectors.toList());

        iKnowCommunityModule.setTags(tagLinks);
        return iKnowCommunityModule;
    }

    private String getTaggedDiscussionUrl(String tag) {
        return String.format("%s%s%s", this.properties.getIknowCommunityUrl(), this.properties.getIknowCommunityTaggedDiscussion(), tag);
    }

}
