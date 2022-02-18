package com.visitscotland.brxm.mock;

import com.fasterxml.jackson.databind.JsonNode;
import com.visitscotland.brxm.dms.DMSConstants;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.hippobeans.*;
import org.mockito.Mockito;

import java.util.Locale;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

public class SharedLinkMockBuilder {

    private SharedLink sharedLink;

    public SharedLinkMockBuilder(){
        sharedLink = Mockito.mock(SharedLink.class);
    }

    public SharedLinkMockBuilder dmsLink(DMSDataService dmsData, JsonNode node) {
        sharedLink = Mockito.mock(SharedLink.class);
        DMSLink link = mock(DMSLink.class);

        when(link.getProduct()).thenReturn("id");
        when(sharedLink.getLinkType()).thenReturn(link);
        when(dmsData.productCard("id", Locale.UK)).thenReturn(node);


        return this;
    }

    public SharedLinkMockBuilder productSearchLink() {
        sharedLink = Mockito.mock(SharedLink.class);
        ProductsSearch ps = mock(ProductsSearch.class);
        when(sharedLink.getLinkType()).thenReturn(mock(ProductsSearch.class));

        return this;
    }

    public SharedLinkMockBuilder externalDocument(String title, String url, String category){
        sharedLink = mock(SharedLink.class, RETURNS_DEEP_STUBS);
        ExternalDocument externalDocument = mock(ExternalDocument.class, RETURNS_DEEP_STUBS);

        when (sharedLink.getLinkType()).thenReturn(externalDocument);
        when (sharedLink.getTitle()).thenReturn(title);
        when (externalDocument.getLink()).thenReturn(url);
        if (category!=null) {
            when(externalDocument.getCategory()).thenReturn(category);
        }
        return this;
    }

    public SharedLink build(){
        return sharedLink;
    }
}
