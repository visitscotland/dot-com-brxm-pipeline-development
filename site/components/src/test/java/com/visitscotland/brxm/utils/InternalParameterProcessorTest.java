package com.visitscotland.brxm.utils;

import com.visitscotland.brxm.model.LocalizedURL;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.InternalParameterProcessor;
import com.visitscotland.brxm.utils.Language;
import org.hippoecm.hst.core.component.HstRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class InternalParameterProcessorTest {

    InternalParameterProcessor component;

    @Mock
    HippoUtilsService utils;

    @Mock
    ResourceBundleService bundle;

    /**
     * Note: We usually prefer to use the out-of-the-box class MockRequest, however, in this class we need to mock
     * the request context with is not possible to do with MockRequest
     */
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    HstRequest request;

    @BeforeEach
    void init() {
        component = new InternalParameterProcessor(bundle, utils);
    }

    @Test
    @DisplayName("VS-443 - processParameters - No query parameters defined")
    void processParameters() {
        ArgumentCaptor<String> ssoUrl = ArgumentCaptor.forClass(String.class);

        when(request.setModel(eq(InternalParameterProcessor.LOGINREDIRECT_PARAMETERS), ssoUrl.capture())).thenReturn(null);

        component.processParameters(request);

        Assertions.assertEquals("returnurl=/" + InternalParameterProcessor.PATH_PLACEHOLDER, ssoUrl.getValue());
    }

    @Test
    @DisplayName("VS-2357 - processParameters - external query parameter specification")
    void processParameters_external() {
        //Verify tha populates the fullQualifiedUrls attribute on the request CONTEXT
        //When there is no parameters the SSO_URL is host + /[PATH-PLACEHOLDER]
        ArgumentCaptor<String> fullyQualified = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> ssoUrl = ArgumentCaptor.forClass(String.class);

        when(utils.getParameterFromUrl(request, InternalParameterProcessor.PARAM_EXTERNAL)).thenReturn("true");
        when(request.getRequestContext().setModel(eq(InternalParameterProcessor.FULLY_QUALIFIED_URLS), fullyQualified.capture())).thenReturn(null);
        when(request.getLocale()).thenReturn(Locale.UK);
        when(request.setModel(eq(InternalParameterProcessor.LOGINREDIRECT_PARAMETERS), ssoUrl.capture())).thenReturn(null);
        when(request.getScheme()).thenReturn("https");
        when(request.getServerName()).thenReturn("www.visitscotland.com");
        when(request.getServerPort()).thenReturn(-1);
        when(request.getRequestContext().getBaseURL().getContextPath()).thenReturn("/site");

        component.processParameters(request);

        Assertions.assertEquals("true", fullyQualified.getValue());
        Assertions.assertEquals("returnurl=https://www.visitscotland.com/site/[PATH-PLACEHOLDER]", ssoUrl.getValue());
    }


    @Test
    @DisplayName("VS-2357 - processParameters - root-path query parameter specification")
    void processParameters_rootPath() {
        //Verify tha populates the fullQualifiedUrls attribute on the request CONTEXT
        //When there is no parameters the SSO_URL is rootpath + /[PATH-PLACEHOLDER]
        ArgumentCaptor<String> fullyQualified = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> ssoUrl = ArgumentCaptor.forClass(String.class);

        when(utils.getParameterFromUrl(eq(request), any())).thenReturn(null);
        when(utils.getParameterFromUrl(request, InternalParameterProcessor.PARAM_ROOT_PATH)).thenReturn("http://visitscotlan.com/");
        when(request.getRequestContext().setModel(eq(InternalParameterProcessor.FULLY_QUALIFIED_URLS), fullyQualified.capture())).thenReturn(null);
        when(request.setModel(eq(InternalParameterProcessor.LOGINREDIRECT_PARAMETERS), ssoUrl.capture())).thenReturn(null);

        component.processParameters(request);

        Assertions.assertEquals("true", fullyQualified.getValue());
        Assertions.assertEquals("returnurl=http://visitscotlan.com/[PATH-PLACEHOLDER]", ssoUrl.getValue());
    }

    @Test
    @DisplayName("VS-2358 - processParameters - sso query parameter specification")
    void processParameters_sso() {
        //Verify that DOES NOT populate the fullQualifiedUrls attribute on the request CONTEXT
        //When there is no parameters the SSO_URL is /[PATH-PLACEHOLDER]?id={sso}
        ArgumentCaptor<String> ssoUrl = ArgumentCaptor.forClass(String.class);

        when(utils.getParameterFromUrl(eq(request), any())).thenReturn(null);
        when(utils.getParameterFromUrl(request, InternalParameterProcessor.PARAM_SSO)).thenReturn("jcalcines");

        when(request.setModel(eq(InternalParameterProcessor.LOGINREDIRECT_PARAMETERS), ssoUrl.capture())).thenReturn(null);

        component.processParameters(request);

        verify(request.getRequestContext(), never()).setModel(eq(InternalParameterProcessor.FULLY_QUALIFIED_URLS), any());
        Assertions.assertEquals("returnurl=/[PATH-PLACEHOLDER]&id=jcalcines", ssoUrl.getValue());
    }

    @Test
    @DisplayName("VS-2360 - addlocalizedUrls - verify that localized URLs have the placeholders")
    void addLocalizedUrls() {
        ArgumentCaptor<List<LocalizedURL>> urls = ArgumentCaptor.forClass(List.class);

        when(request.getRequestContext().setModel(eq(InternalParameterProcessor.GLOBAL_MENU_URLS), urls.capture())).thenReturn(null);

        component.addLocalizedURLs(request);

        Assertions.assertEquals(Language.values().length, urls.getValue().size());
        Assertions.assertTrue(urls.getValue().get(0).getUrl().contains(InternalParameterProcessor.PATH_PLACEHOLDER));
    }

    @Test
    @DisplayName("VS-2357 - processParameters - root-path behaves as the legacy project does")
    void getDomain_rootPath_mimicLegacyProject() {
        Assertions.assertEquals("returnurl=http://demo.visitscotland.com/[PATH-PLACEHOLDER]",
                testRootPath("http://demo.visitscotland.com"));
        Assertions.assertEquals("returnurl=http://demo.visitscotland.com/[PATH-PLACEHOLDER]",
                testRootPath("http://demo.visitscotland.com/"));
        Assertions.assertEquals("returnurl=http://demo.visitscotland.com/[PATH-PLACEHOLDER]",
                testRootPath("http://demo.visitscotland.com/tours"));

        Assertions.assertEquals("returnurl=/[PATH-PLACEHOLDER]", testRootPath("demo.visitscotland.com"));
        Assertions.assertEquals("returnurl=/[PATH-PLACEHOLDER]", testRootPath("visitscotland"));
    }

    @Test
    @DisplayName("VS-2357 - processParameters - root-path fixes issues from the legacy project")
    void getDomain_rootPath_fixIssuesInLegacyProject() {
        Assertions.assertEquals("returnurl=http://demo.visitscotland.com:8080/[PATH-PLACEHOLDER]",
                testRootPath("http://demo.visitscotland.com:8080"));
        Assertions.assertEquals("returnurl=http://user@demo.visitscotland.com:8080/[PATH-PLACEHOLDER]",
                testRootPath("http://user@demo.visitscotland.com:8080"));
        Assertions.assertEquals("returnurl=//demo.visitscotland.com/[PATH-PLACEHOLDER]",
                testRootPath("//demo.visitscotland.com"));
        Assertions.assertEquals("returnurl=scheme://demo.visitscotland.com/[PATH-PLACEHOLDER]",
                testRootPath("scheme://demo.visitscotland.com"));
    }

    /**
     * Mocks a request and process the parameter rootPath.
     *
     * @return The resultant returnURL for that parameter
     */
    String testRootPath(String rootPath) {
        HstRequest request = mock(HstRequest.class, Answers.RETURNS_DEEP_STUBS);
        ArgumentCaptor<String> ssoUrl = ArgumentCaptor.forClass(String.class);

        when(utils.getParameterFromUrl(eq(request), any())).thenReturn(null);
        when(utils.getParameterFromUrl(request, InternalParameterProcessor.PARAM_ROOT_PATH)).thenReturn(rootPath);
        when(request.setModel(eq(InternalParameterProcessor.LOGINREDIRECT_PARAMETERS), ssoUrl.capture())).thenReturn(null);

        component.processParameters(request);

        return ssoUrl.getValue();
    }
}
