package com.visitscotland.brxm.mock;

import com.visitscotland.brxm.hippobeans.IknowCommunity;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.*;

public class IKnowCommunityMockBuilder {

    private final IknowCommunity mock;

    public IKnowCommunityMockBuilder() {
        this.mock = Mockito.mock(IknowCommunity.class);
    }

    public IKnowCommunityMockBuilder title(String title) {
        when(mock.getTitle()).thenReturn(title);
        return this;
    }

    public IKnowCommunityMockBuilder copy(String copy) {
        when(mock.getCopy()).thenReturn(copy);
        return this;
    }

    public IKnowCommunityMockBuilder tags(List<String> tags) {
        when(mock.getTags()).thenReturn(tags.toArray(new String[0]));
        return this;
    }

    public IknowCommunity build() {
        return mock;
    }
}
