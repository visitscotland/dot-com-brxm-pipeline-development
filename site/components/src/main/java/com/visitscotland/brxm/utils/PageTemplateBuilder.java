package com.visitscotland.brxm.utils;

import com.visitscotland.brxm.components.content.GeneralContentComponent;
import com.visitscotland.brxm.factory.*;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.model.*;
import com.visitscotland.brxm.model.Module;
import com.visitscotland.brxm.model.megalinks.LinksModule;
import com.visitscotland.brxm.model.megalinks.MultiImageLinksModule;
import com.visitscotland.brxm.model.megalinks.SingleImageLinksModule;
import com.visitscotland.brxm.services.DocumentUtilsService;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.core.component.HstRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.visitscotland.brxm.components.content.PageContentComponent.LABELS;
import static com.visitscotland.brxm.services.ResourceBundleService.GLOBAL_BUNDLE_FILE;

@Component
public class PageTemplateBuilder {

    private static final Logger logger = LoggerFactory.getLogger(PageTemplateBuilder.class);

    //Static Constant
    static final String INTRO_THEME = "introTheme";
    static final String PAGE_ITEMS = "pageItems";

    static final String DEFAULT = "default";

    static final String[] ALIGNMENT = {"right", "left"};

    /**
     * TODO: Convert into property?
     */
    static final Integer THEMES = 3;

    //Utils
    private final DocumentUtilsService documentUtils;

    //Factories
    private final MegalinkFactory linksFactory;
    private final ICentreFactory iCentreFactory;
    private final IKnowFactory iKnowFactory;
    private final ArticleFactory articleFactory;
    private final LongCopyFactory longCopyFactory;
    private final UserGeneratedContentFactory userGeneratedContentFactory;
    private final TravelInformationFactory travelInformationFactory;
    private final CannedSearchFactory cannedSearchFactory;
    private final PreviewModeFactory previewFactory;
    private final FormFactory formFactory;
    private final MapFactory mapFactory;
    private final SignpostFactory signPostFactory;
    private final SkiFactory skiFactory;
    private final DevModuleFactory devModuleFactory;
    private final SiteProperties properties;

    private final ResourceBundleService bundle;
    private final Logger contentLogger;


    @Autowired
    public PageTemplateBuilder(DocumentUtilsService documentUtils, MegalinkFactory linksFactory, ICentreFactory iCentreFactory,
                               IKnowFactory iKnowFactory, ArticleFactory articleFactory, LongCopyFactory longCopyFactory,
                               UserGeneratedContentFactory userGeneratedContentFactory, TravelInformationFactory travelInformationFactory,
                               CannedSearchFactory cannedSearchFactory, PreviewModeFactory previewFactory, FormFactory marketoFormFactory,
                               MapFactory mapFactory, SkiFactory skiFactory, SiteProperties properties,
                               DevModuleFactory devModuleFactory, ResourceBundleService bundle, Logger contentLogger, SignpostFactory signPostFactory) {
        this.documentUtils = documentUtils;
        this.linksFactory = linksFactory;
        this.iCentreFactory = iCentreFactory;
        this.iKnowFactory = iKnowFactory;
        this.articleFactory = articleFactory;
        this.longCopyFactory = longCopyFactory;
        this.userGeneratedContentFactory = userGeneratedContentFactory;
        this.travelInformationFactory = travelInformationFactory;
        this.cannedSearchFactory = cannedSearchFactory;
        this.previewFactory = previewFactory;
        this.formFactory = marketoFormFactory;
        this.mapFactory = mapFactory;
        this.devModuleFactory = devModuleFactory;
        this.skiFactory = skiFactory;
        this.properties = properties;
        this.bundle = bundle;
        this.contentLogger = contentLogger;
        this.signPostFactory = signPostFactory;
    }

    private Page getDocument(HstRequest request) {
        return (Page) request.getAttribute("document");
    }

    public void addModules(HstRequest request) {
        addModules(request, null);
    }

    public void addModules(HstRequest request, String location) {
        PageConfiguration page = new PageConfiguration();

        for (BaseDocument item : documentUtils.getAllowedDocuments(getDocument(request))) {
            try {
                logger.debug("A {} module was found. Type {}", item.getClass(), item.getPath());
                addModule(request, page, item, location);
            } catch (MissingResourceException e){
                logger.error("The module for {} couldn't be built because some labels do not exist", item.getPath(), e);
            } catch (RuntimeException e){
                logger.error("An unexpected exception happened while building the module for {}", item.getPath(), e);
            }
        }

        setIntroTheme(request, page.modules);

        if (page.modules.isEmpty() && Boolean.FALSE.equals(getDocument(request).getSeoNoIndex())){
            logger.warn("The page {} does not have any modules published", request.getRequestURI());
        }

        request.setModel(PAGE_ITEMS, page.modules);
    }

    private void addModule(HstRequest request, PageConfiguration page, BaseDocument item, String location){
        if (item instanceof Megalinks) {
            processMegalinks(request, page, (Megalinks) item);
        } else if (item instanceof TourismInformation) {
            processTouristInformation(request,page, (TourismInformation) item, location);
        } else if (item instanceof Article){
            page.modules.add(articleFactory.getModule(request, (Article) item));
        } else if (item instanceof LongCopy){
            processLongCopy(request, page, (LongCopy) item);
        } else if (item instanceof MapModule) {
            page.modules.add(mapFactory.getModule(request, (MapModule) item, getDocument(request)));
        } else if (item instanceof Stackla) {
            page.modules.add(userGeneratedContentFactory.getUGCModule((Stackla) item, request.getLocale()));
        } else if (item instanceof TravelInformation) {
            page.modules.add(travelInformationFactory.getTravelInformation((TravelInformation) item, request.getLocale()));
        } else if (item instanceof CannedSearch) {
            page.modules.add(cannedSearchFactory.getCannedSearchModule((CannedSearch) item, request.getLocale()));
        } else if (item instanceof CannedSearchTours) {
            page.modules.add(cannedSearchFactory.getCannedSearchToursModule((CannedSearchTours) item, request.getLocale()));
        } else if (item instanceof MarketoForm || item instanceof Form) {
            page.modules.add(getForm(request, item));
        } else if (item instanceof SkiCentre){
            page.modules.add(skiFactory.createSkyModule((SkiCentre) item, request.getLocale()));
        } else if (item instanceof SkiCentreList){
            page.modules.add(skiFactory.createSkyListModule((SkiCentreList) item, request.getLocale()));
        } else if (item instanceof DevModule){
            page.modules.add(devModuleFactory.getModule((DevModule) item));
        } else if (item instanceof CTABanner){
            page.modules.add(signPostFactory.createModule((CTABanner) item));
        } else {
            logger.warn("Unrecognized Module Type: {}", item.getClass());
        }
    }
    private FormModule getForm(HstRequest request, BaseDocument form){
        addAllLabels(request, "forms");
        Map<String, String> formLabels = labels(request).get("forms");

        //The following files are required independent of the Form Framework
        formLabels.put("cfg.form.json.countries", properties.getProperty("form.json.countries"));
        formLabels.put("cfg.form.json.messages", properties.getProperty("form.json.messages"));

        if (form instanceof MarketoForm) {
            return formFactory.getModule((MarketoForm) form);
        } else if (form instanceof Form) {
            return formFactory.getModule((Form) form);
        } else if (form != null) {
            logger.error("Form Class not recognized {}, path = {}", form.getClass(), form.getPath());
        }
        return null;
    }

    /**
     * Convert a LongCopy into a LongCopy module and adds it to the list of modules
     * Note: Consider to create a factory if the creation of the Module requires more logic.
     */
    private void processLongCopy(HstRequest request, PageConfiguration config, LongCopy document) {
        Page page = getDocument(request);
        if (page instanceof General && ((General) page).getTheme().equals(GeneralContentComponent.SIMPLE)){
            if (config.modules.stream().anyMatch(LongCopyModule.class::isInstance)){
                logger.error("Only one instance of Long Module is allowed");
                config.modules.add(new ErrorModule(document, "Only one instance of Long Module module is allowed"));
            } else {
                config.modules.add(longCopyFactory.getModule(document));
            }
        } else {
            logger.error("The document type LongCopy is only allowed in Simple Pages");
            contentLogger.error("The document type LongCopy is not allowed in this page. Path {}", page.getPath());
            config.modules.add(new ErrorModule(document, "The document type Long Copy is only allowed in Simple Pages"));
        }
    }

    /**
     * Creates a LinkModule from a Megalinks document
     */
    private void processMegalinks(HstRequest request, PageConfiguration page, Megalinks item) {
        LinksModule<?> al = linksFactory.getMegalinkModule(item, request.getLocale());
        int numLinks = al.getLinks().size();
        if (al instanceof MultiImageLinksModule) {
            numLinks += ((MultiImageLinksModule) al).getFeaturedLinks().size();
        }
        if (numLinks == 0) {
            contentLogger.warn("Megalinks module at {} contains no valid items", item.getPath());
            page.modules.add(previewFactory.createErrorModule(al));
            return;
        }

        if (al.getType().equalsIgnoreCase(SingleImageLinksModule.class.getSimpleName())) {
            al.setAlignment(ALIGNMENT[page.alignment++ % ALIGNMENT.length]);
            if (Contract.isEmpty(al.getAlignment())) {
                logger.warn("The Single Image Megalink module for {} does not have the alignment field defined", item.getPath());
            }
        }

        if (Contract.isEmpty(al.getTitle()) && page.style > 0) {
            page.style--;
        }
        al.setThemeIndex(page.style++ % THEMES);
        al.setHippoBean(item);

        if (!item.getPersonalization().isEmpty()) {
            PersonalisationModule personalisationModule = new PersonalisationModule();
            List<Module> personalisationList = new ArrayList<>();
            al.setMarketoId(DEFAULT);
            personalisationList.add(al);
            for (Personalization personalisationMegalink : item.getPersonalization()) {
                personalisationList.add(processPersonalisation(request, (Megalinks)personalisationMegalink.getModule(), personalisationMegalink.getId(), al));
            }
            personalisationModule.setModules(personalisationList);

            page.modules.add(personalisationModule);
        } else {
            page.modules.add(al);
        }

        addGlobalLabel(request, "third-party-error");
    }
    private Module<Megalinks> processPersonalisation(HstRequest request, Megalinks item, String marketoId, LinksModule<?> parent) {
        LinksModule<?> al = linksFactory.getMegalinkModule(item, request.getLocale());

        al.setThemeIndex(parent.getThemeIndex());
        al.setHippoBean(item);

        if (!Contract.isEmpty(marketoId)) {
            al.setMarketoId(marketoId);
        }

        if (al.getType().equalsIgnoreCase(SingleImageLinksModule.class.getSimpleName())) {
            al.setAlignment(parent.getAlignment());
            if (Contract.isEmpty(al.getAlignment())) {
                logger.warn("The Single Image Megalink module for {} does not have the alignment field defined", item.getPath());
            }
        }

        return al;

    }

    private boolean isICentreLanding(HstRequest request) {
        return request.getPathInfo().equals(properties.getSiteICentre().substring(0, properties.getSiteICentre().length() - 8));
    }
    /**
     * Creates a LinkModule from a TouristInformation document
     */
    private void processTouristInformation(HstRequest request, PageConfiguration page, TourismInformation touristInfo, String location) {
        if (!isICentreLanding(request)) {
            ICentreModule iCentreModule = iCentreFactory.getModule(touristInfo.getICentre(), request.getLocale(), location);

            if (iCentreModule != null) {
                iCentreModule.setHippoBean(touristInfo);
                page.modules.add(iCentreModule);
            }
        }
        if (properties.isIknowEnabled()) {
            IKnowModule iKnowModule = iKnowFactory.getIKnowModule(touristInfo.getIKnow(), location, request.getLocale());
            iKnowModule.setHippoBean(touristInfo);

            page.modules.add(iKnowModule);
        }
    }

    /**
     * Sets the theme for the intro of the page based on the list of modules.
     * @param request HstRequest request
     * @param modules List Modules
     */
    private void setIntroTheme(HstRequest request, List<Module<?>> modules) {
        if(!modules.isEmpty() && modules.get(0) instanceof LinksModule){
            request.setModel(INTRO_THEME, ((LinksModule<?>) modules.get(0)).getThemeIndex());
        }
    }

    private Map<String, Map<String, String>> labels(HstRequest request) {
        if (request.getModel(LABELS) == null) {
            Map<String, Map<String, String>> labels = new HashMap<>();
            request.setModel(LABELS, labels);
            return labels;
        }

        return request.getModel(LABELS);
    }

    private void addGlobalLabel(HstRequest request, String key) {
        labels(request).get(GLOBAL_BUNDLE_FILE).put(key, bundle.getSiteResourceBundle(GLOBAL_BUNDLE_FILE, key, request.getLocale()));
    }

    private void addAllLabels(HstRequest request, String bundleName) {
        labels(request).put(bundleName, bundle.getAllLabels(bundleName, request.getLocale()));
    }

    /**
     * Controls the configuration of the page.
     * It handles the list of modules as well as the memory for style and the alignment
     */
    static class PageConfiguration {
        List<Module<?>> modules = new ArrayList<>();

        int style = 0;
        int alignment = 0;
    }
}
