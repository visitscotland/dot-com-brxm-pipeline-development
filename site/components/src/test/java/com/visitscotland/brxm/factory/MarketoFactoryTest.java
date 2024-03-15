package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.MarketoForm;
import com.visitscotland.brxm.mock.MarketoFormMockBuilder;
import com.visitscotland.brxm.model.MarketoFormModule;
import com.visitscotland.brxm.utils.Properties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class MarketoFactoryTest {

    @Mock
    Properties properties;

    @InjectMocks
    MarketoFormFactory marketoFormFactory;

    @Test
    @DisplayName("VS-3358 - Marketo form")
    void marketoForm() {
        MarketoForm form = new MarketoFormMockBuilder().title("title")
                .configuration("config").noJavascriptMessage("nojs").copy("copy").build();

        when(properties.getProperty("form.recaptcha-key")).thenReturn("recaptcha");
        when(properties.getProperty("form.marketo.instance-url")).thenReturn("url");
        when(properties.getProperty("form.marketo.munchkin")).thenReturn("munchkin");
        when(properties.getProperty("form.marketo.script")).thenReturn("script.js");

        MarketoFormModule module = marketoFormFactory.getModule(form);

        assertEquals("title", module.getTitle());
        assertEquals("config", module.getJsonUrl());
        assertEquals("nojs", module.getNoJavaScriptMessage());
        assertEquals("copy", module.getCopy().getContent());
        assertEquals("recaptcha", module.getConfig().getRecaptcha());
        assertEquals("url", module.getConfig().getMarketoInstance());
        assertEquals("script.js", module.getConfig().getScript());
        assertEquals("munchkin", module.getConfig().getMunchkinId());
    }

}
