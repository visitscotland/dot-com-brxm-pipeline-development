package com.visitscotland.brxm.mock;

import com.visitscotland.brxm.hippobeans.CannedSearchTours;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CannedSearchToursMockBuilder {

    private final CannedSearchTours mock;

    public CannedSearchToursMockBuilder() {
        this.mock = Mockito.mock(CannedSearchTours.class);
    }

    public CannedSearchToursMockBuilder title(String title) {
        when(mock.getTitle()).thenReturn(title);
        return this;
    }

    public CannedSearchToursMockBuilder copy(String copy) {
        HippoHtml hippoHtml = mock(HippoHtml.class);
        when(hippoHtml.getContent()).thenReturn(copy);
        when(mock.getCopy()).thenReturn(hippoHtml);
        return this;
    }

    public CannedSearchToursMockBuilder viewAllCta(String viewAllCta) {
        when(mock.getViewAll()).thenReturn(viewAllCta);
        return this;
    }

    public CannedSearchToursMockBuilder toursSearch(String toursSearchUrl) {
        when(mock.getToursSearch()).thenReturn(toursSearchUrl);
        return this;
    }

    public CannedSearchTours build() {
        return mock;
    }

}
