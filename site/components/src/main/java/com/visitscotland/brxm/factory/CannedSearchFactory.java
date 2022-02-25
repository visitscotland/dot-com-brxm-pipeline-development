package com.visitscotland.brxm.factory;


import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.dms.DMSConstants;
import com.visitscotland.brxm.dms.ProductSearchBuilder;
import com.visitscotland.brxm.hippobeans.CannedSearch;
import com.visitscotland.brxm.hippobeans.CannedSearchTours;
import com.visitscotland.brxm.model.CannedSearchModule;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.utils.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import vs.ase.dms.ProductTypes;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

@Component
public class CannedSearchFactory {

    private static final Logger logger = LoggerFactory.getLogger(CannedSearchFactory.class);
    private static final Logger contentLog = LoggerFactory.getLogger("content");

    static final String BUNDLE_ID = "canned-search";

    private final ResourceBundleService bundle;
    private final LinkService linkService;
    private final Properties properties;

    public CannedSearchFactory(ResourceBundleService bundle, LinkService linkService, Properties properties){
        this.bundle = bundle;
        this.linkService = linkService;
        this.properties = properties;
    }

    public CannedSearchModule getCannedSearchModule(CannedSearch document, Locale locale){
        logger.info("Creating CannedSearchModule for {}", document.getPath());

        CannedSearchModule module = new CannedSearchModule ();
        module.setHippoBean(document);
        module.setTitle(document.getTitle());
        module.setProductType(document.getCriteria().getSearch().getProductType());
        module.setDescription(document.getCopy());

        FlatLink viewAllCta = linkService.createFindOutMoreLink(module, locale, document.getCriteria());

        if (Contract.isEmpty(viewAllCta.getLabel()) || viewAllCta.getLabel().equals(bundle.getResourceBundle("essentials.global","button.find-out-more",  locale))) {
            viewAllCta.setLabel(bundle.getResourceBundle(BUNDLE_ID, "canned-search.listview", locale));
        }
        module.setViewAllLink(viewAllCta);

        module.setCannedSearchEndpoint(productSearch().fromHippoBean(document.getCriteria().getSearch()).locale(locale).buildCannedSearch());

        if (document.getCriteria().getSearch().getProductType().equals(ProductTypes.EVENT.getId())){
            module.setCredit(bundle.getResourceBundle(BUNDLE_ID,"canned-search.credit-events", locale));
        }


        return module;
    }

    public CannedSearchModule getCannedSearchToursModule(CannedSearchTours document, Locale locale) {
        logger.info("Creating CannedSearchToursModule for {}", document.getPath());
        CannedSearchModule module = new CannedSearchModule();
        module.setHippoBean(document);
        module.setTitle(document.getTitle());
        module.setDescription(document.getCopy());
        module.setProductType("tour");

        URL documentToursSearchUrl;
        try {
            documentToursSearchUrl = new URL(document.getToursSearch());
        } catch (MalformedURLException e) {
            // This should be prevented by the url validator
            contentLog.error("Invalid tours search URL {} on CannedSearchToursModule {}", document.getToursSearch(), document.getPath());
            return null;
        }

        String toursSearchQueryString = Contract.defaultIfNull(documentToursSearchUrl.getQuery(), "");
        String tmsCannedSearchUrl = UriComponentsBuilder.fromUriString(properties.getDmsDataPublicHost())
                .path(DMSConstants.VS_DMS_CANNED_SEARCH_TOURS)
                .query(toursSearchQueryString)
                .queryParam("locale", locale.toLanguageTag())
                .queryParam("limit", 24)
                .build().toString();

        module.setCannedSearchEndpoint(tmsCannedSearchUrl);

        FlatLink viewAllCta = linkService.createExternalLink(document.getToursSearch());
        if (!Contract.isEmpty(document.getViewAll())) {
            viewAllCta.setLabel(document.getViewAll());
        } else {
            viewAllCta.setLabel(bundle.getResourceBundle(BUNDLE_ID, "canned-search.listview", locale));
        }
        module.setViewAllLink(viewAllCta);
        return module;
    }

    /**
     * Fetches a new Product Search Object
     */
    private ProductSearchBuilder productSearch(){
        return VsComponentManager.get(ProductSearchBuilder.class);
    }

}
