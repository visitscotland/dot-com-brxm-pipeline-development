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

    public ListicleItemMockBuilder sutitle (String subtitle){
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

    public ListicleItemMockBuilder addImage(){
        when(listicle.getListicleItemImage()).thenReturn(mock(Image.class));
        return this;
    }

    public ListicleItemMockBuilder cmsLink(){
        return cmsLink(false);
    }

    public ListicleItemMockBuilder cmsLink(boolean addImage){
        CMSLink cmsLink = mock(CMSLink.class);
        Page page = mock(Page.class);
        when(cmsLink.getLink()).thenReturn(page);
        when(listicle.getListicleItem()).thenReturn(cmsLink);

        if (addImage) {
            when(page.getHeroImage()).thenReturn(mock(Image.class));
        }

        return this;
    }

    public ListicleItemMockBuilder dmsLink(String id){
        DMSLink dmsLink = mock(DMSLink.class);
        when(dmsLink.getProduct()).thenReturn(id);
        when(listicle.getListicleItem()).thenReturn(dmsLink);
        return this;
    }

    public ListicleItem build() {
        return listicle;
    }
}
