package com.visitscotland.brxm.utils;

import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.utils.Contract;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class SiteProperties extends Properties {

    static final String DEFAULT_CONFIG = "default.site.config";
    static final String OVERRIDE_PROPERTY = "visitscotland:siteProperties";

    static final String SITE_ID = "site-id";
    static final String IKNOW_COMMUNITY_URL = "iknow-community.url";
    static final String IKNOW_COMMUNITY_TAGGED_DISCUSSION = "iknow-community.tagged-discussion";

    static final String CHANNEL_ORDER = "seo.alternate-link-locale-order";
    static final String GLOBAL_SEARCH_PATH = "global-search.path";
    static final String ENGINE_ID = "global-search.engine-id";


    //Environment

    static final String INTERNAL_SITES = "links.internal-sites";
    static final String CONVERT_TO_RELATIVE = "links.convert-to-relative";
    static final String DOWNLOAD_EXTENSIONS = "links.download.extensions";


    //Page References
    private static final String PATH_GLOBAL_SEARCH = "site.path.global-search";
    private static final String PATH_SKI_SECTION = "site.path.ski-landing";
    private static final String PATH_CAMPAIGN_SECTION = "site.path.campaigns";
    private static final String PATH_ABOUT_US = "site.path.about-us";
    private static final String PATH_NEWSLETTER = "site.path.newsletter";
    private static final String PATH_ICENTRE = "site.path.icentre-landing";

    //Modules References
    static final String ENABLE_IKNOW_MODULE = "iknow-module.enabled";

    //GTM Properties
    public static final String GTM_CONTAINER_ID = "gtm.container-id";
    public static final String GTM_IS_PRODUCTION = "gtm.is-production";
    public static final String GTM_PREVIEW_QUERY_STRING = "gtm.preview-query-string";

    //Form Properties
    static final String FORMS_RECAPTCHA = "form.recaptcha-key";
    static final String FORMS_MARKETO_URL = "form.maketo.instance-url";
    static final String FORMS_MARKETO_MUNCHKIN = "form.marketo.munchkin";
    static final String FORMS_MARKETO_SCRIPT = "form.marketo.script";
    static final String FORMS_MARKETO_IS_PRODUCTION = "form.is-production";
    static final String FORM_BREG_LEGAL_BASIS = "form.breg.legal-basis";
    static final String FORM_BREG_LEGAL_BASIS_ENABLE = "form.breg.legal-basis.enable";

    private final CMSProperties cmsProperties;
    public SiteProperties(ResourceBundleService bundle, HippoUtilsService utils, CMSProperties cmsProperties){
        super(bundle, utils);
        this.cmsProperties = cmsProperties;
    }

    @Override
    public String getDefaultConfig() {
        return DEFAULT_CONFIG;
    }

    @Override
    public String getOverrideProperty() {
        return OVERRIDE_PROPERTY;
    }

    public String getConvertToRelative() {
        if (cmsProperties.isRelativeURLs()) {
            return readString(CONVERT_TO_RELATIVE);
        } else {
            return "";
        }
    }
    public String getGlobalSearchURL() {
        return readString(GLOBAL_SEARCH_PATH);
    }
    public String getChannelOrder(){
        return readString(CHANNEL_ORDER);
    }
    public String getIknowCommunityUrl() {
        return readString(IKNOW_COMMUNITY_URL);
    }
    public String getIknowCommunityTaggedDiscussion() {
        return readString(IKNOW_COMMUNITY_TAGGED_DISCUSSION);
    }
    public String getSiteSkiSection() {
        return readString(PATH_SKI_SECTION);
    }
    public String getCampaignSection() {
        return readString(PATH_CAMPAIGN_SECTION);
    }
    public String getSiteAboutUs() {
        return readString(PATH_ABOUT_US);
    }
    public String getSiteGlobalSearch() {
        return readString(PATH_GLOBAL_SEARCH);
    }
    public String getSiteNewsletter() {
        return readString(PATH_NEWSLETTER);
    }
    public String getSiteICentre() {
        return readString(PATH_ICENTRE);
    }
    public String getGtmContainerId (){
        return readString(GTM_CONTAINER_ID);
    }
    public String getGtmIsProduction() {
        return readString(GTM_IS_PRODUCTION);
    }
    public String getGtmPreviewQueryString() {
        return readString(GTM_PREVIEW_QUERY_STRING);
    }
    public boolean isIknowEnabled() {
        return readBoolean(ENABLE_IKNOW_MODULE);
    }
    public List<String> getInternalSites() {
        String sites = readString(INTERNAL_SITES);
        if (!Contract.isEmpty(sites)){
            // TODO Java 11: Replace & Test: Arrays.stream(sites.trim().split("\\s*,\\s*")).filter(Predicate.not(String::isEmpty)).collect(Collectors.toUnmodifiableList());
            return Arrays.stream(sites.trim().split("\\s*,\\s*")).filter(((Predicate<String>) String::isEmpty).negate()).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public String getFormsMarketoUrl() {
        return readString(FORMS_MARKETO_URL);
    }

    public String getFormsRecaptcha() {
        return readString(FORMS_RECAPTCHA);
    }

    public String getFormsMarketoMunchkin() {
        return readString(FORMS_MARKETO_MUNCHKIN);
    }

    public String getFormsMarketoScript() {
        return readString(FORMS_MARKETO_SCRIPT);
    }

    public Boolean getFormsMarketoIsProduction() {
        return readBoolean(FORMS_MARKETO_IS_PRODUCTION);
    }

    public String getSiteId() {
        return readString(SITE_ID);
    }

    public String getFormBregLegalBasis() {
        return readString(FORM_BREG_LEGAL_BASIS);
    }
    public Boolean isFormBregLegalBasisEnabled() {
        return readBoolean(FORM_BREG_LEGAL_BASIS_ENABLE);
    }

    public String getDownloadExtensions() {
        return readString(DOWNLOAD_EXTENSIONS);
    }

}
