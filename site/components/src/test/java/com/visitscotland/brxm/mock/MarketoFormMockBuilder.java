package com.visitscotland.brxm.mock;

import com.visitscotland.brxm.hippobeans.MarketoForm;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import static org.mockito.Mockito.*;

public class MarketoFormMockBuilder {

    private MarketoForm mock;

    public MarketoFormMockBuilder() {
        mock = mock(MarketoForm.class);
    }

    public MarketoForm build() {
        return mock;
    }

    public MarketoFormMockBuilder title(String title) {
        when(mock.getTitle()).thenReturn(title);
        return this;
    }

    public MarketoFormMockBuilder noJavascriptMessage(String noJsMessage) {
        when(mock.getNonJavaScriptMessage()).thenReturn(noJsMessage);
        return this;
    }

    public MarketoFormMockBuilder configuration(String config) {
        when(mock.getJsonUrl()).thenReturn(config);
        return this;
    }

    public MarketoFormMockBuilder copy(String copy) {
        HippoHtml mockCopy = mock(HippoHtml.class);
        when(mockCopy.getContent()).thenReturn(copy);
        when(mock.getCopy()).thenReturn(mockCopy);
        return this;
    }

}
