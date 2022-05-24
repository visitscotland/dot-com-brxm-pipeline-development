package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.MarketoForm;
import com.visitscotland.brxm.mock.MarketoFormMockBuilder;
import com.visitscotland.brxm.model.MarketoFormModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


@ExtendWith(MockitoExtension.class)
public class MarketoFactoryTest {

    @InjectMocks
    MarketoFormFactory marketoFormFactory;

    @Test
    @DisplayName("VS-3358 - Marketo form")
    void marketoForm() {
        MarketoForm form = new MarketoFormMockBuilder().title("title")
                .configuration("config").noJavascriptMessage("nojs").copy("copy").build();
        MarketoFormModule module = marketoFormFactory.getModule(form);

        assertEquals("title", module.getTitle());
        assertEquals("config", module.getJsonUrl());
        assertEquals("nojs", module.getNoJavaScriptMessage());
        assertEquals("copy", module.getCopy().getContent());
    }

}
