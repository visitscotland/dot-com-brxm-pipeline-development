package com.visitscotland.brxm.mock;

import com.visitscotland.brxm.hippobeans.IknowCommunity;
import com.visitscotland.brxm.utils.HippoHtmlWrapper;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
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
        HippoHtml hippoHtml = mock(HippoHtml.class);
        when(hippoHtml.getContent()).thenReturn(copy);
        when(mock.getCopy()).thenReturn(hippoHtml);
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
