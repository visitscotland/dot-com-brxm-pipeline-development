package com.visitscotland.brxm.utils;

import com.visitscotland.brxm.components.content.GeneralContentComponent;
import com.visitscotland.brxm.factory.*;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.model.*;
import com.visitscotland.brxm.model.megalinks.LinksModule;
import com.visitscotland.brxm.model.megalinks.MultiImageLinksModule;
import com.visitscotland.brxm.model.megalinks.SingleImageLinksModule;
import com.visitscotland.brxm.services.DocumentUtilsService;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.core.component.HstRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

@Component
public class PageTemplateBuilder {

    private static final Logger logger = LoggerFactory.getLogger(PageTemplateBuilder.class);

    //Static Constant
    static final String INTRO_THEME = "introTheme";
    static final String PAGE_ITEMS = "pageItems";
    static final String SEARCH_RESULTS = "searchResultsPage";


    static final String[] alignment = {"right", "left"};

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
    private final StacklaFactory stacklaFactory;
    private final TravelInformationFactory travelInformationFactory;
    private final CannedSearchFactory cannedSearchFactory;
    private final PreviewModeFactory previewFactory;
    private final MarketoFormFactory marketoFormFactory;
    private final MapFactory mapFactory;
    private final Logger contentLogger;


    @Autowired
    public PageTemplateBuilder(DocumentUtilsService documentUtils, MegalinkFactory linksFactory, ICentreFactory iCentre,
                               IKnowFactory iKnow, ArticleFactory article, LongCopyFactory longcopy,
                               StacklaFactory stacklaFactory, TravelInformationFactory travelInformationFactory, CannedSearchFactory cannedSearchFactory,
                               PreviewModeFactory previewFactory, MarketoFormFactory marketoFormFactory, MapFactory mapFactory,
                               ContentLogger contentLogger) {
        this.linksFactory = linksFactory;
        this.iCentreFactory = iCentre;
        this.iKnowFactory = iKnow;
        this.documentUtils = documentUtils;
        this.articleFactory = article;
        this.longCopyFactory = longcopy;
        this.stacklaFactory = stacklaFactory;
        this.travelInformationFactory = travelInformationFactory;
        this.cannedSearchFactory = cannedSearchFactory;
        this.previewFactory = previewFactory;
        this.marketoFormFactory = marketoFormFactory;
        this.mapFactory = mapFactory;
        this.contentLogger = contentLogger;
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
                    page.modules.add(stacklaFactory.getStacklaModule((Stackla) item, request.getLocale()));
                }  else if (item instanceof TravelInformation) {
                    page.modules.add(travelInformationFactory.getTravelInformation((TravelInformation) item, request.getLocale()));
                }else if (item instanceof CannedSearch) {
                    page.modules.add(cannedSearchFactory.getCannedSearchModule((CannedSearch) item, request.getLocale()));
                } else if (item instanceof CannedSearchTours) {
                    page.modules.add(cannedSearchFactory.getCannedSearchToursModule((CannedSearchTours) item, request.getLocale()));
                } else if (item instanceof MarketoForm) {
                    page.modules.add(marketoFormFactory.getModule((MarketoForm) item));
                }
            } catch (MissingResourceException e){
                logger.error("The module for {} couldn't be built because some labels do not exist", item.getPath(), e);
            } catch (RuntimeException e){
                logger.error("An unexpected exception happened while building the module for {}", item.getPath(), e);
            }
        }

        setIntroTheme(request, page.modules);
        //TODO try to move this to GeneralContentComponent
        if (getDocument(request).getPath().contains("/site-search-results")){
            request.setAttribute(SEARCH_RESULTS, true);
        }

        request.setAttribute(PAGE_ITEMS, page.modules);
    }

    /**
     * Convert a LongCopy into a LongCopy module and adds it to the list of modules
     *
     * Note: Consider to create a factory if the creation of the Module requires more logic.
     */
    private void processLongCopy(HstRequest request, PageConfiguration config, LongCopy document){
        Page page = getDocument(request);
        if (page instanceof General && ((General) page).getTheme().equals(GeneralContentComponent.SIMPLE)){
            if (config.modules.stream().anyMatch(LongCopyModule.class::isInstance)){
                logger.error("Only one instance of this module is allowed");
            } else {
                config.modules.add(longCopyFactory.getModule(document));
            }
        } else {
            logger.error("The document type LongCopy is only allowed in Simple Pages");
            contentLogger.error("The document type LongCopy is not allowed in this page. Path {}", page.getPath());
        }
    }

    /**
     * Creates a LinkModule from a Megalinks document
     */
    private void processMegalinks(HstRequest request, PageConfiguration page, Megalinks item){
        LinksModule<?> al = linksFactory.getMegalinkModule(item, request.getLocale());
        int numLinks = al.getLinks().size();
        if (al instanceof MultiImageLinksModule) {
            numLinks += ((MultiImageLinksModule) al).getFeaturedLinks().size();
        }
        if (numLinks == 0) {
            contentLogger.error("Megalinks module at {} contains no valid items", item.getPath());
            page.modules.add(previewFactory.createErrorModule(al));
            return;
        }

        if (al.getType().equalsIgnoreCase(SingleImageLinksModule.class.getSimpleName())) {
            al.setAlignment(alignment[page.alignment++ % alignment.length]);
        }
        if (Contract.isEmpty(al.getTitle()) && page.style > 0) {
            page.style--;
        }

        al.setThemeIndex(page.style++ % THEMES);
        al.setHippoBean(item);

        page.modules.add(al);
    }

    /**
     * Creates a LinkModule from a TouristInformation document
     */
    private void processTouristInformation(HstRequest request, PageConfiguration page, TourismInformation touristInfo, String location){

        ICentreModule iCentreModule = iCentreFactory.getModule(touristInfo.getICentre(), request.getLocale(), location);
        if (iCentreModule != null) {
            iCentreModule.setHippoBean(touristInfo);
            page.modules.add(iCentreModule);
        }

        IKnowModule iKnowModule = iKnowFactory.getIKnowModule(touristInfo.getIKnow(), location, request.getLocale());
        iKnowModule.setHippoBean(touristInfo);

        page.modules.add(iKnowModule);
    }

    /**
     * Sets the theme for the intro of the page based on the list of modules.
     * @param request HstRequest request
     * @param modules List Modules
     */
    private void setIntroTheme(HstRequest request, List<Module<?>> modules){
        if(!modules.isEmpty() && modules.get(0) instanceof LinksModule){
            request.setAttribute(INTRO_THEME, ((LinksModule<?>) modules.get(0)).getThemeIndex());
        }
    }

    /**
     * Controls the configuration of the page.
     *
     * It handles the list of modules as well as the memory for style and the alignment
     */
    class PageConfiguration {
        List<Module<?>> modules = new ArrayList<>();

        int style = 0;
        int alignment = 0;
    }
}
