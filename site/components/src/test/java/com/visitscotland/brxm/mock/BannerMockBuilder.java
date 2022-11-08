package com.visitscotland.brxm.mock;

import com.visitscotland.brxm.hippobeans.Banner;
import com.visitscotland.brxm.hippobeans.CMSLink;
import com.visitscotland.brxm.hippobeans.ExternalLink;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import static org.mockito.Mockito.*;

public class BannerMockBuilder {

    private final Banner banner;

    public BannerMockBuilder() {
        banner = mock(Banner.class);
    }

    public Banner build() {
        return banner;
    }

    public BannerMockBuilder copy(String copy) {
        HippoHtml hippoHtml = mock(HippoHtml.class);
        when(hippoHtml.getContent()).thenReturn(copy);
        when(banner.getCopy()).thenReturn(hippoHtml);
        return this;
    }

    public BannerMockBuilder cmsCtaLink(String label, HippoBean linkTo) {
        CMSLink mockLink = mock(CMSLink.class);
        when(mockLink.getLink()).thenReturn(linkTo);
        when(mockLink.getLabel()).thenReturn(label);
        return this;
    }

    public BannerMockBuilder externalCtaLink(String label, String url) {
        ExternalLink mockLink = mock(ExternalLink.class);
        when(mockLink.getLink()).thenReturn(url);
        when(mockLink.getLabel()).thenReturn(label);
        return this;
    }

}
