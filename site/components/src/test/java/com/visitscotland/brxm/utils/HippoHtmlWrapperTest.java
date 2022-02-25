package com.visitscotland.brxm.utils;

import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HippoHtmlWrapperTest {

    @DisplayName("VS-2654 - Content takes precedence over default text.")
    @ParameterizedTest
    @ValueSource(strings = {"Hello", "<p>Hello</p>", "<p>."})
    void invalidValues(String html){
        HippoHtml base = mock(HippoHtml.class);
        when(base.getContent()).thenReturn(html);

        Assertions.assertEquals(html, new HippoHtmlWrapper(base, "default").getContent());
    }

    @DisplayName("VS-2654 - Default text is displayed if the content doesn't contain any text")
    @ParameterizedTest
    @ValueSource(strings = {"", " \n\t ", "</br>","<p></p>","&nbsp;", "<p>&nbsp;</p>"})
    void validValues(String html){
        HippoHtml base = mock(HippoHtml.class);
        when(base.getContent()).thenReturn(html);

        Assertions.assertEquals("default", new HippoHtmlWrapper(base, "default").getContent());
    }

    @DisplayName("Create a new instance of HippoHtml based on labels")
    @Test
    void createHtml(){
        Assertions.assertEquals("label", new HippoHtmlWrapper("label").getContent());
    }



}