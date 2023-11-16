package com.visitscotland.brxm.mock;

import com.visitscotland.brxm.hippobeans.Stackla;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import static org.mockito.Mockito.*;

public class UserGeneratedContentMockBuilder {

    private final Stackla mock;

    public UserGeneratedContentMockBuilder() {
        this.mock = mock(Stackla.class);
    }

    public UserGeneratedContentMockBuilder title(String title) {
        when(mock.getTitle()).thenReturn(title);
        return this;
    }

    public UserGeneratedContentMockBuilder dataId(String dataId) {
        when(mock.getStorystreamId()).thenReturn(dataId);
        return this;
    }

    public UserGeneratedContentMockBuilder copy(String copy) {
        HippoHtml mockCopy = mock(HippoHtml.class);
        when(mockCopy.getContent()).thenReturn(copy);
        when(mock.getCopy()).thenReturn(mockCopy);
        return this;
    }

    public Stackla build() {
        return mock;
    }


}
