package com.visitscotland.brxm.mock;

import com.visitscotland.brxm.hippobeans.CannedSearch;
import com.visitscotland.brxm.hippobeans.ProductSearchLink;
import com.visitscotland.brxm.hippobeans.ProductsSearch;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class CannedSearchMockBuilder {

    private final CannedSearch mock;

    public CannedSearchMockBuilder() {
        this.mock = Mockito.mock(CannedSearch.class);
    }

    public CannedSearchMockBuilder title(String title) {
        when(mock.getTitle()).thenReturn(title);
        return this;
    }

    public CannedSearchMockBuilder copy(String copy) {
        HippoHtml hippoHtml = mock(HippoHtml.class);
        when(hippoHtml.getContent()).thenReturn(copy);
        when(mock.getCopy()).thenReturn(hippoHtml);
        return this;
    }

   public CannedSearchMockBuilder criteria(String productType) {
        ProductSearchLink psrLink = mock(ProductSearchLink.class, RETURNS_DEEP_STUBS);
        when(psrLink.getSearch().getProductType()).thenReturn(productType);

        when(mock.getCriteria()).thenReturn(psrLink);

        return this;
    }

    public CannedSearch build() {
        return mock;
    }
}
