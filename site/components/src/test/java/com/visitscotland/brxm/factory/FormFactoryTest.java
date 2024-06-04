package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.MarketoForm;
import com.visitscotland.brxm.mock.MarketoFormMockBuilder;
import com.visitscotland.brxm.model.FormModule;
import com.visitscotland.brxm.model.form.MarketoConfiguration;
import com.visitscotland.brxm.utils.SiteProperties;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FormFactoryTest {

    @Mock
    SiteProperties properties;

    @InjectMocks
    FormFactory marketoFormFactory;

    @Test
    @DisplayName("VS-3358 - Marketo form")
    void marketoForm() {
        MarketoForm form = new MarketoFormMockBuilder().title("title")
                .configuration("config").noJavascriptMessage("nojs").copy("copy").build();

        when(properties.getFormsRecaptcha()).thenReturn("recaptcha");
        when(properties.getFormsMarketoUrl()).thenReturn("url");
        when(properties.getFormsMarketoMunchkin()).thenReturn("munchkin");
        when(properties.getFormsMarketoScript()).thenReturn("script.js");
        when(properties.getFormsMarketoIsProduction()).thenReturn(true);

        FormModule module = marketoFormFactory.getModule(form);

        assertEquals("title", module.getTitle());
        assertEquals("config", module.getJsonUrl());
        assertEquals("nojs", module.getNoJavaScriptMessage());
        assertEquals("copy", module.getCopy().getContent());
        assertInstanceOf(MarketoConfiguration.class, module.getConfig());
        assertEquals("recaptcha", ((MarketoConfiguration) module.getConfig()).getRecaptcha());
        assertEquals("url", ((MarketoConfiguration) module.getConfig()).getMarketoInstance());
        assertEquals("script.js", ((MarketoConfiguration) module.getConfig()).getScript());
        assertEquals("munchkin", ((MarketoConfiguration) module.getConfig()).getMunchkinId());
    }

    @Test
    @Disabled("To be completed")
    @DisplayName("BE - #93 - Fepl Form Configuration")
    void formMarketo() {

    }

    @Test
    @Disabled("To be completed")
    @DisplayName("BE - #93 - Marketo Form Configuration")
    void formFepl() {

    }

    @Test
    @Disabled("To be completed")
    @DisplayName("BE - #93 - No configuration logs a warning")
    void noConfiguration() {

    }

}
