package com.visitscotland.brxm.mock;

import com.visitscotland.brxm.hippobeans.*;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class ListicleItemMockBuilder {

    ListicleItem listicle;

    public ListicleItemMockBuilder() {
        listicle = mock(ListicleItem.class);
    }

    public ListicleItemMockBuilder title (String title){
        when(listicle.getTitle()).thenReturn(title);
        return this;
    }

    public ListicleItemMockBuilder subtitle(String subtitle){
        when(listicle.getSubtitle()).thenReturn(subtitle);
        return this;
    }

    public ListicleItemMockBuilder addDescription(){
        when(listicle.getDescription()).thenReturn(mock(HippoHtml.class));
        return this;
    }

    public ListicleItemMockBuilder extraLink(){
        when(listicle.getExtraLinks()).thenReturn(Collections.singletonList(mock(HippoCompound.class)));
        return this;
    }
    public ListicleItemMockBuilder extraSharedLink(String label){
        CMSLink cmsLink = mock(CMSLink.class, withSettings().lenient());
        when(cmsLink.getLabel()).thenReturn(label);
        when(listicle.getExtraLinks()).thenReturn(Collections.singletonList(cmsLink));
        return this;
    }

    public ListicleItemMockBuilder addImage(){
        when(listicle.getListicleItemImage()).thenReturn(mock(Image.class));
        return this;
    }

    public ListicleItemMockBuilder cmsLink(Page linkTo){
        CMSLink cmsLink = mock(CMSLink.class);
        when(cmsLink.getLink()).thenReturn(linkTo);
        when(listicle.getListicleItem()).thenReturn(cmsLink);

        return this;
    }
    public ListicleItemMockBuilder cmsLinkOverrideLabel(){
        CMSLink cmsLink = mock(CMSLink.class);
        Page page = mock(Page.class);
        when(cmsLink.getLink()).thenReturn(page);
        when( cmsLink.getLabel()).thenReturn("override label");
        when(listicle.getListicleItem()).thenReturn(cmsLink);

        return this;
    }

    public ListicleItemMockBuilder cmsLink(){
        return cmsLink(mock(Page.class));
    }

    public ListicleItemMockBuilder dmsLink(String id){
        DMSLink dmsLink = mock(DMSLink.class);
        when(dmsLink.getProduct()).thenReturn(id);
        when(listicle.getListicleItem()).thenReturn(dmsLink);
        return this;
    }

    public ListicleItemMockBuilder externalLink(){
        ExternalLink externalLink = mock(ExternalLink.class);
        when(listicle.getListicleItem()).thenReturn(externalLink);
        return this;
    }
    public ListicleItemMockBuilder productSearchLink(){
        ProductSearchLink psrLink = mock(ProductSearchLink.class);
        when(listicle.getListicleItem()).thenReturn(psrLink);
        return this;
    }

    public ListicleItem build() {
        return listicle;
    }
}
