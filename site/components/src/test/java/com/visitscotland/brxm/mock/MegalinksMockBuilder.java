package com.visitscotland.brxm.mock;

import com.visitscotland.brxm.factory.MegalinkFactory;
import com.visitscotland.brxm.factory.MegalinkFactoryTest;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.hippobeans.capabilities.Linkable;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MegalinksMockBuilder {

    public static final String DMS_ID = "0123456798";
    public static final String EXTERNAL_URL = "http://www.fake.site";
    public static final String MOCK_JSON = "{" +
            " \"productLink\": {\"link\": \"/info/fake-product-p0123456798\"}," +
            " \"name\":\"Fake Product\", " +
            " \"images\":[{" +
            "    \"mediaUrl\":\"https://img.visitscotland.com/fake-product.jpg\"" +
            "}]}";

    public enum LinkType {CMS, DMS, EXTERNAL, PRODUCT_SEARCH}

    private Megalinks megalinks;
    private HippoBean linkable;
    private List<MegalinkItem> megalinkItems;

    public MegalinksMockBuilder() {
        megalinks = Mockito.mock(Megalinks.class);
        megalinkItems = new ArrayList<>();
    }

    public Megalinks build() {
        if (megalinkItems.size() > 0) {
            when(megalinks.getMegalinkItems()).thenReturn(megalinkItems);
        }
        return megalinks;
    }

    public MegalinksMockBuilder horizontalLayout(int numberLinks) {
        megalinks = mock(Megalinks.class, RETURNS_DEEP_STUBS);
        when (megalinks.getLayout()).thenReturn(MegalinkFactory.HORIZONTAL_LAYOUT);
        when (megalinks.getMegalinkItems().size()).thenReturn(numberLinks);

        return this;
    }

    public MegalinksMockBuilder singleImageLayout() {
        when(megalinks.getSingleImageModule()).thenReturn(mock(SingleImageModule.class));

        return this;
    }

    public MegalinksMockBuilder featured(boolean featured){
        MegalinkItem item = megalinkItems.get(megalinkItems.size() - 1);
        when(item.getFeature()).thenReturn(featured);

        return this;
    }

    public MegalinksMockBuilder addPageLink(){
        MegalinkItem item = mock(MegalinkItem.class);
        when(item.getLinkItem()).thenReturn(mock(Page.class));
        megalinkItems.add(item);

        return this;
    }

    public MegalinksMockBuilder addSharedLink() {
        MegalinkItem item = mock(MegalinkItem.class);
        when(item.getFeature()).thenReturn(false);
        when(item.getLinkItem()).thenReturn(mock(SharedLink.class));

        megalinkItems.add(item);

        return this;
    }

    public MegalinksMockBuilder addLink(HippoBean link) {
        MegalinkItem item = mock(MegalinkItem.class);
        when(item.getLinkItem()).thenReturn(link);

        megalinkItems.add(item);

        return this;
    }

    public MegalinksMockBuilder title(String title) {
        if (linkable instanceof SharedLink) {
            when(((SharedLink)linkable).getTitle()).thenReturn(title);
        } else {
            new UnsupportedOperationException();
        }
        return this;
    }

    public Itinerary getItinerary(String mainTransport){
        Itinerary itinerary = mock(Itinerary.class, RETURNS_DEEP_STUBS);

        when (itinerary.getTransports()).thenReturn(new String[]{mainTransport});

        return itinerary;
    }
    public Linkable getPage(){
       return mock(Page.class);
    }

    public MegalinkItem createMockItem(boolean featured, MegalinkFactoryTest.LinkType type, String title) {
        MegalinkItem item = mock(MegalinkItem.class, withSettings().lenient());

        when(item.getFeature()).thenReturn(featured);
        if (type == MegalinkFactoryTest.LinkType.CMS) {
            when(item.getLinkItem()).thenReturn(mock(Page.class));
        } else {
            SharedLink link = mockSharedLink(type);
            when(link.getLinkType()).thenReturn(mock(ExternalDocument.class));
            when(item.getLinkItem()).thenReturn(link);
            if (title != null) {
                when(link.getTitle()).thenReturn(title);
            }
        }

        return item;
    }

    private SharedLink mockSharedLink(MegalinkFactoryTest.LinkType linkType) {
        SharedLink link = mock(SharedLink.class, withSettings().lenient());
        when(link.getImage()).thenReturn(mock(Image.class, withSettings().lenient()));

        switch (linkType) {
            case DMS:
                DMSLink type = mock(DMSLink.class, withSettings().lenient());
                when(type.getProduct()).thenReturn(DMS_ID);
                when(link.getLinkType()).thenReturn(type);
                break;
            case PRODUCT_SEARCH:
                ProductsSearch ps = mock(ProductsSearch.class, withSettings().lenient());
                when(link.getLinkType()).thenReturn(ps);
                break;
            case EXTERNAL:
                ExternalLink external = mock(ExternalLink.class, withSettings().lenient());
                when(external.getLink()).thenReturn(EXTERNAL_URL);
                when(link.getLinkType()).thenReturn(external);
                break;
            default:

        }
        return link;
    }
}
