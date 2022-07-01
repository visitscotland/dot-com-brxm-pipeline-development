package com.visitscotland.brxm.mock;

import com.visitscotland.brxm.hippobeans.Page;
import com.visitscotland.brxm.hippobeans.TourismInformation;
import com.visitscotland.brxm.hippobeans.capabilities.Linkable;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.HippoUtilsService;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NavigationMockBuilder {

    HstSiteMenuItem mock;
    List<HstSiteMenuItem> children;
    HippoBean linkable;

    HippoUtilsService utils;
    ResourceBundleService bundle;
    HstRequest request;

    public NavigationMockBuilder(HstRequest request, HippoUtilsService utils, ResourceBundleService bundle) {
        this.utils = utils;
        this.bundle = bundle;
        this.request = request;
        mock = Mockito.mock(HstSiteMenuItem.class);
    }

    public NavigationMockBuilder name (String name) {
        when(mock.getName()).thenReturn(name);

        return this;
    }

    public NavigationMockBuilder breadcrumb(String breadcrumb){
        if (linkable == null){
            linkable = mock(Page.class);
        }
        when(((Page) linkable).getBreadcrumb()).thenReturn(breadcrumb);

        return this;
    }

    public NavigationMockBuilder title(String title){
        if (linkable == null){
            linkable = mock(Page.class);
        }
        when(((Linkable)linkable).getTitle()).thenReturn(title);

        return this;
    }

    public NavigationMockBuilder addMockLink(){
        return addLink("mockUrl", mock(Page.class));
    }

    public NavigationMockBuilder addLink(String url, HippoBean linkable){
        this.linkable = linkable;
        HstLink link = mock(HstLink.class);
        when(link.getPath()).thenReturn(url);
        ResolvedSiteMapItem rsi = mock(ResolvedSiteMapItem.class);

        when(mock.getHstLink()).thenReturn(link);
        when(mock.resolveToSiteMapItem()).thenReturn(rsi);

        when(utils.getBeanForResolvedSiteMapItem(request, rsi)).thenReturn(linkable);

        return this;
    }

    public NavigationMockBuilder addChildren(HstSiteMenuItem child){
        if (children == null){
            children = new ArrayList<>();
            when(mock.getChildMenuItems()).thenReturn(children);
        }
        children.add(child);

        return this;
    }

    public HstSiteMenuItem build (){
        return mock;
    }
}