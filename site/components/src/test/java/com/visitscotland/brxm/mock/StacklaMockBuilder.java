package com.visitscotland.brxm.mock;

import com.visitscotland.brxm.hippobeans.Stackla;
import com.visitscotland.brxm.model.StacklaModule;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import static org.mockito.Mockito.*;

public class StacklaMockBuilder {

    private final Stackla mock;

    public StacklaMockBuilder() {
        this.mock = mock(Stackla.class);
    }

    public StacklaMockBuilder title(String title) {
        when(mock.getTitle()).thenReturn(title);
        return this;
    }

    public StacklaMockBuilder dataHash(String dataHash) {
        when(mock.getStacklaHash()).thenReturn(dataHash);
        return this;
    }

    public StacklaMockBuilder dataId(String dataId) {
        when(mock.getStacklaId()).thenReturn(dataId);
        return this;
    }

    public StacklaMockBuilder copy(String copy) {
        HippoHtml mockCopy = mock(HippoHtml.class);
        when(mockCopy.getContent()).thenReturn(copy);
        when(mock.getCopy()).thenReturn(mockCopy);
        return this;
    }

    public Stackla build() {
        return mock;
    }


}
