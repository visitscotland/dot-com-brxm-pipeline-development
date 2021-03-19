package com.visitscotland.brxm.utils;

import com.visitscotland.brxm.components.content.GeneralContentComponent;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.model.ICentreModule;
import com.visitscotland.brxm.model.IKnowModule;
import com.visitscotland.brxm.model.LongCopyModule;
import com.visitscotland.brxm.model.Module;
import com.visitscotland.brxm.model.megalinks.HorizontalListLinksModule;
import com.visitscotland.brxm.model.megalinks.LinksModule;
import com.visitscotland.brxm.model.megalinks.SingleImageLinksModule;
import com.visitscotland.brxm.factory.*;
import com.visitscotland.brxm.services.DocumentUtilsService;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.core.component.HstRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    static final String[] themes = {"theme1", "theme2", "theme3"};
    static final String[] alignment = {"right", "left"};

    /**
     * @deprecated to be removed by after VS-2441 is completed
     */
    @Deprecated
    public static final String NEUTRAL_THEME = themes[1];

    //Utils
    private final DocumentUtilsService documentUtils;

    //Factories
    private final LinkModulesFactory linksFactory;
    private final ICentreFactory iCentreFactory;
    private final IKnowFactory iKnowFactory;
    private final ArticleFactory articleFactory;
    private final LongCopyFactory longCopyFactory;

    public PageTemplateBuilder(DocumentUtilsService documentUtils, LinkModulesFactory linksFactory, ICentreFactory iCentre, IKnowFactory iKnow, ArticleFactory article, LongCopyFactory longcopy) {
        this.linksFactory = linksFactory;
        this.iCentreFactory = iCentre;
        this.iKnowFactory = iKnow;
        this.documentUtils = documentUtils;
        this.articleFactory = article;
        this.longCopyFactory = longcopy;
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
                logger.info("A {} module was found. Type {}", item.getClass(), item.getPath());
                if (item instanceof Megalinks) {
                    processMegalinks(request, page, (Megalinks) item);
                } else if (item instanceof TourismInformation) {
                    processTouristInformation(request,page, (TourismInformation) item, location);
                } else if (item instanceof Article){
                    page.modules.add(articleFactory.getModule(request, (Article) item));
                } else if (item instanceof LongCopy){
                    processLongCopy(request, page, (LongCopy) item);
                }
            } catch (MissingResourceException e){
                logger.error("The module for {} couldn't be built because some labels do not exist", item.getPath(), e);
            } catch (RuntimeException e){
                logger.error("An unexpected exception happened while building the module for {}", item.getPath(), e);
            }
        }

        addOTYMLModule(request, page.modules);
        setIntroTheme(request, page.modules);

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
            if (config.modules.stream().anyMatch(module -> module instanceof LongCopyModule)){
                logger.error("Only one instance of this module is allowed");
            } else {
                config.modules.add(longCopyFactory.getModule(document));
            }
        } else {
            //TODO Content Issue;
            logger.error("The document type LongCopy is only allowed in Simple Pages");
        }
    }
    /**
     * Creates a LinkModule from a Megalinks document
     */
    private void processMegalinks(HstRequest request, PageConfiguration page, Megalinks item){
        LinksModule<?> al = linksFactory.getMegalinkModule(item, request.getLocale());

        if (al.getType().equalsIgnoreCase(SingleImageLinksModule.class.getSimpleName())) {
            al.setAlignment(alignment[page.alignment++ % alignment.length]);
        }
        if (Contract.isEmpty(al.getTitle()) && page.style > 0) {
            page.style--;
        }

        al.setTheme(themes[page.style++ % themes.length]);
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
     * Adds the a OTYML (Other Things You Might Like) module at the end of the list
     * @param request
     * @param modules
     */
    private void addOTYMLModule(HstRequest request, List<Module> modules){
        OTYML otyml = getDocument(request).getOtherThings();
        if(otyml!=null) {
            HorizontalListLinksModule al = linksFactory.horizontalListLayout(otyml, request.getLocale());
            al.setTheme(NEUTRAL_THEME);
            modules.add(al);
        }
    }

    /**
     * Sets the theme for the intro of the page based on the list of modules.
     * @param request
     * @param modules
     */
    private void setIntroTheme(HstRequest request, List<Module> modules){
        if(!modules.isEmpty() && modules.get(0) instanceof LinksModule){
            request.setAttribute(INTRO_THEME, ((LinksModule) modules.get(0)).getTheme());
        }else{
            request.setAttribute(INTRO_THEME, NEUTRAL_THEME);
        }
    }

    /**
     * Controls the configuration of the page.
     *
     * It handles the list of modules as well as the memory for style and the alignment
     */
    class PageConfiguration {
        List<Module> modules = new ArrayList<>();

        int style = 0;
        int alignment = 0;
    }
}
