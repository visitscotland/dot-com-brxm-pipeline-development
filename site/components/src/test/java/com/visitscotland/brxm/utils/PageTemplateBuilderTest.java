package com.visitscotland.brxm.utils;

import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.model.ICentreModule;
import com.visitscotland.brxm.model.IKnowModule;
import com.visitscotland.brxm.model.LongCopyModule;
import com.visitscotland.brxm.model.Module;
import com.visitscotland.brxm.model.megalinks.HorizontalListLinksModule;
import com.visitscotland.brxm.model.megalinks.LinksModule;
import com.visitscotland.brxm.model.megalinks.MultiImageLinksModule;
import com.visitscotland.brxm.model.megalinks.SingleImageLinksModule;
import com.visitscotland.brxm.factory.*;
import com.visitscotland.brxm.mock.MegalinksMockBuilder;
import com.visitscotland.brxm.mock.TouristInformationMockBuilder;
import com.visitscotland.brxm.services.DocumentUtilsService;
import org.hippoecm.hst.mock.core.component.MockHstRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PageTemplateBuilderTest {


    MockHstRequest request;

    @Mock
    Page page;

    @Mock
    ICentreFactory iCentreFactory;

    @Mock
    IKnowFactory iKnowFactory;

    @Mock
    LinkModulesFactory linksFactory;

    @Mock
    ArticleFactory articleFactory;

    @Mock
    LongCopyFactory longCopyFactory;

    @Mock
    DocumentUtilsService utils;

    @Resource
    @InjectMocks
    PageTemplateBuilder builder;

    @BeforeEach
    void init() {
        request = new MockHstRequest();
        request.setLocale(Locale.UK);

        //Adds a mock document to the Request
        request.setAttribute("document", page);
    }

    /**
     * Builds a Page with no documents associated
     */
    @Test
    void pageWithoutElements() {
        when(utils.getAllowedDocuments(page)).thenReturn(Collections.emptyList());

        builder.addModules(request);

        List items = (List) request.getAttribute(PageTemplateBuilder.PAGE_ITEMS);

        assertEquals(0, items.size());
    }

    /**
     * Build a page with one Megalinks document associated
     */
    @Test
    void addMegalinksModule_basic() {
        Megalinks megalinks = new MegalinksMockBuilder().build();
        LinksModule<?> module = new MultiImageLinksModule();

        when(utils.getAllowedDocuments(page)).thenReturn(Collections.singletonList(megalinks));
        doReturn(module).when(linksFactory).getMegalinkModule(megalinks, Locale.UK);

        builder.addModules(request);
        List items = (List) request.getAttribute(PageTemplateBuilder.PAGE_ITEMS);

        assertEquals(1, items.size());
    }

    /**
     * Styles alternate, and the last repeats the first colour
     */
    @Test
    void addMegalinksModule_alternateStyles() {
        List<BaseDocument> list = Arrays.asList(
                new MegalinksMockBuilder().build(),
                new MegalinksMockBuilder().build(),
                new MegalinksMockBuilder().build(),
                new MegalinksMockBuilder().build());

        when(utils.getAllowedDocuments(page)).thenReturn(list);
        doReturn(new MultiImageLinksModule("h2")).when(linksFactory).getMegalinkModule((Megalinks) list.get(0), Locale.UK);
        doReturn(new MultiImageLinksModule("h2")).when(linksFactory).getMegalinkModule((Megalinks) list.get(1), Locale.UK);
        doReturn(new MultiImageLinksModule("h2")).when(linksFactory).getMegalinkModule((Megalinks) list.get(2), Locale.UK);
        doReturn(new MultiImageLinksModule("h2")).when(linksFactory).getMegalinkModule((Megalinks) list.get(3), Locale.UK);

        builder.addModules(request);
        List<LinksModule> items = (List<LinksModule>) request.getAttribute(PageTemplateBuilder.PAGE_ITEMS);

        assertEquals(4, items.size());

        for (int i = 0; i < 4; i++) {
            assertEquals(i % PageTemplateBuilder.THEMES, items.get(i).getThemeIndex());
        }
    }

    /**
     * 3 first items share colour because their title is null, 4th is different
     */
    @Test
    void addMegalinksModule_skipAlternateStyles_whenNoH2() {
        List<BaseDocument> list = Arrays.asList(
                new MegalinksMockBuilder().build(),
                new MegalinksMockBuilder().build(),
                new MegalinksMockBuilder().build(),
                new MegalinksMockBuilder().build());

        when(utils.getAllowedDocuments(page)).thenReturn(list);
        doReturn(new MultiImageLinksModule("h2")).when(linksFactory).getMegalinkModule((Megalinks) list.get(0), Locale.UK);
        doReturn(new MultiImageLinksModule()).when(linksFactory).getMegalinkModule((Megalinks) list.get(1), Locale.UK);
        doReturn(new MultiImageLinksModule()).when(linksFactory).getMegalinkModule((Megalinks) list.get(2), Locale.UK);
        doReturn(new MultiImageLinksModule("h2")).when(linksFactory).getMegalinkModule((Megalinks) list.get(3), Locale.UK);

        builder.addModules(request);
        List<LinksModule> items = (List<LinksModule>) request.getAttribute(PageTemplateBuilder.PAGE_ITEMS);

        assertEquals(4, items.size());

        for (int i = 0; i < 4; i++) {
            assertEquals(i != 3 ? 0 : 1, items.get(i).getThemeIndex());
        }
    }

    /**
     * First item always have the same style independently of if the section title is defined
     */
    @Test
    void addMegalinksModule_firstItemColourIsStyle3_whenNoH2() {
        Megalinks mega = new MegalinksMockBuilder().build();
        when(utils.getAllowedDocuments(page)).thenReturn(Collections.singletonList(mega));

        // Build the first case where the first element has no title
        doReturn(new MultiImageLinksModule()).when(linksFactory).getMegalinkModule(mega, Locale.UK);
        builder.addModules(request);
        LinksModule firstModuleWithoutTitle = ((List<LinksModule>) request.getAttribute(PageTemplateBuilder.PAGE_ITEMS)).get(0);

        // Build the second case where the first element has a title
        doReturn(new MultiImageLinksModule("h2")).when(linksFactory).getMegalinkModule(mega, Locale.UK);
        builder.addModules(request);
        LinksModule firstModuleWithTitle = ((List<LinksModule>) request.getAttribute(PageTemplateBuilder.PAGE_ITEMS)).get(0);

        //Compare that the result is identical
        assertEquals(firstModuleWithoutTitle.getThemeIndex(), firstModuleWithTitle.getThemeIndex());
    }

    /**
     * Verifies that the alignment for Single Image modules alternates
     */
    @Test
    void addMegalinksModule_alternateAlignment() {
        List<BaseDocument> list = Arrays.asList(
                new MegalinksMockBuilder().build(),
                new MegalinksMockBuilder().build(),
                new MegalinksMockBuilder().build(),
                new MegalinksMockBuilder().build());

        when(utils.getAllowedDocuments(page)).thenReturn(list);
        when(linksFactory.getMegalinkModule((Megalinks) list.get(0), Locale.UK)).thenReturn(new SingleImageLinksModule());
        when(linksFactory.getMegalinkModule((Megalinks) list.get(1), Locale.UK)).thenReturn(new SingleImageLinksModule());
        when(linksFactory.getMegalinkModule((Megalinks) list.get(2), Locale.UK)).thenReturn(new SingleImageLinksModule());
        when(linksFactory.getMegalinkModule((Megalinks) list.get(3), Locale.UK)).thenReturn(new SingleImageLinksModule());

        builder.addModules(request);
        List<LinksModule> items = (List<LinksModule>) request.getAttribute(PageTemplateBuilder.PAGE_ITEMS);

        assertEquals(4, items.size());

        for (int i = 0; i < 4; i++) {
            assertEquals(PageTemplateBuilder.alignment[i % 2], items.get(i).getAlignment());
        }
    }

    /**
     * Verifies that is able to add an iKnowModule when the minimum amount of information has been provided
     * Verifies that is able to set the Hippo bean for only Iknow configuration
     */
    @Test
    void addTouristInformation_iKnowModule() {
        TourismInformation ti = new TouristInformationMockBuilder().build();

        when(utils.getAllowedDocuments(page)).thenReturn(Collections.singletonList(ti));
        when(iKnowFactory.getIKnowModule(any(), eq(null), eq(request.getLocale()))).thenReturn(new IKnowModule());

        builder.addModules(request);

        List<Module> items = (List<Module>) request.getAttribute(PageTemplateBuilder.PAGE_ITEMS);
        assertEquals(1, items.size());
        assertEquals(ti, items.get(0).getHippoBean());
    }

    /**
     * Verifies that is able to add an iKnowModule when the minimum amount of information has been provided
     * Verifies that is able to set the Hippo Bean when 2 items are returned.
     * Verifies that only one Hippo Bean is set edit module is enabled.
     */
    @Test
    @Disabled("This requirement is being reviewed")
    @DisplayName("Verifies that only one edit button appears in the preview mode")
    void addTouristInformation_iCentreModule() {

        TourismInformation ti = new TouristInformationMockBuilder().build();

        when(utils.getAllowedDocuments(page)).thenReturn(Collections.singletonList(ti));

        when(iCentreFactory.getModule(any(), eq(request.getLocale()), eq(null))).thenReturn(new ICentreModule());
        when(iKnowFactory.getIKnowModule(any(), eq(null), eq(request.getLocale()))).thenReturn(new IKnowModule());

        builder.addModules(request);

        List<Module> items = (List<Module>) request.getAttribute(PageTemplateBuilder.PAGE_ITEMS);
        assertEquals(2, items.size());
        assertEquals(ti, items.get(0).getHippoBean());
        assertNull(items.get(1).getHippoBean());
    }

    /**
     * Build a page with one OTYML section associated.
     *
     * TODO: Do we want to move this test to PageContentComponent Test?
     */
    @Test
    @Disabled("OTYML is no longer controlled by PageTemplateBuilder but PageContentComponent")
    void addOTYMLModule() {
        HorizontalListLinksModule module = new HorizontalListLinksModule();

        when(page.getOtherThings()).thenReturn(new OTYML());
        when(linksFactory.horizontalListLayout(page.getOtherThings(), Locale.UK)).thenReturn(module);

        builder.addModules(request);
        List<LinksModule> items = (List<LinksModule>) request.getAttribute(PageTemplateBuilder.PAGE_ITEMS);

        assertEquals(1, items.size());
        assertEquals(module.getType(), items.get(0).getType());
    }

    @Test
    @DisplayName("VS-2015 - Match the initial background colour with the megalinks")
    void setIntroTheme(){
        Megalinks mega = new MegalinksMockBuilder().build();
        when(utils.getAllowedDocuments(page)).thenReturn(Collections.singletonList(mega));

        doReturn(new MultiImageLinksModule()).when(linksFactory).getMegalinkModule(mega, Locale.UK);
        builder.addModules(request);
        LinksModule module = (LinksModule) ((List<Module>) request.getAttribute(PageTemplateBuilder.PAGE_ITEMS)).get(0);

        assertNotNull(request.getAttribute(PageTemplateBuilder.INTRO_THEME));
        assertEquals(request.getAttribute(PageTemplateBuilder.INTRO_THEME), module.getThemeIndex());
    }

    @Test
    @DisplayName("VS-2015 - introTheme is populated with a neutral theme when the theme cannot be inferred")
    void setIntroTheme_forNonMegalinks(){
        when(utils.getAllowedDocuments(page)).thenReturn(Collections.emptyList());
        builder.addModules(request);

        assertNull(request.getAttribute(PageTemplateBuilder.INTRO_THEME));
    }

    @Test
    @DisplayName("VS-2132 - Happy Path crete a module that contains the basic information")
    void createLongCopy_basic(){
        General page = mock(General.class);
        LongCopy longCopy = mock(LongCopy.class);

        //The module is only allowed got general pages.
        when(page.getTheme()).thenReturn("Simple");
        request.setAttribute("document", page);

        when(utils.getAllowedDocuments(page)).thenReturn(Collections.singletonList(longCopy));
        when(longCopyFactory.getModule(any(LongCopy.class))).thenReturn(new LongCopyModule());

        builder.addModules(request);

        //List<Module> items = (List<Module>) request.getAttribute(PageTemplateBuilder.PAGE_ITEMS);
        LongCopyModule module = (LongCopyModule) ((List<Module>) request.getAttribute(PageTemplateBuilder.PAGE_ITEMS)).get(0);
        assertNotNull(module);
    }

    @Test
    @DisplayName("VS-2132 - This item allowed on general page type - simple theme pages only (Document types)")
    void createLongCopy_forbidden_destinations(){
        Destination page = mock(Destination.class);
        LongCopy longCopy = mock(LongCopy.class);

        //The module is only allowed got general pages.
        request.setAttribute("document", page);

        when(utils.getAllowedDocuments(page)).thenReturn(Collections.singletonList(longCopy));
        builder.addModules(request);

        assertEquals(0, ((List<Module>) request.getAttribute(PageTemplateBuilder.PAGE_ITEMS)).size());
    }

    @Test
    @DisplayName("VS-2132 - This item allowed on general page type - simple theme pages only (Themes)")
    void createLongCopy_forbidden_generalStandard(){
        General page = mock(General.class);
        LongCopy longCopy = mock(LongCopy.class);

        //The module is only allowed got general pages.
        when(page.getTheme()).thenReturn("Standard");
        request.setAttribute("document", page);

        when(utils.getAllowedDocuments(page)).thenReturn(Collections.singletonList(longCopy));
        builder.addModules(request);

        assertEquals(0, ((List<Module>) request.getAttribute(PageTemplateBuilder.PAGE_ITEMS)).size());
    }

    @Test
    @DisplayName("VS-2132 - This item could be used only ... as a single instance")
    void createLongCopy_forbidden_multiple(){
        General page = mock(General.class);

        //The module is only allowed got general pages.
        when(page.getTheme()).thenReturn("Simple");
        request.setAttribute("document", page);

        when(utils.getAllowedDocuments(page)).thenReturn(Arrays.asList(mock(LongCopy.class), mock(LongCopy.class), mock(LongCopy.class)));
        when(longCopyFactory.getModule(any(LongCopy.class))).thenReturn(new LongCopyModule());

        builder.addModules(request);

        assertEquals(1, ((List<Module>) request.getAttribute(PageTemplateBuilder.PAGE_ITEMS)).size());
    }

}
