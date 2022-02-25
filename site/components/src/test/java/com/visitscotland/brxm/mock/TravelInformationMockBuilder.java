package com.visitscotland.brxm.mock;


import com.visitscotland.brxm.hippobeans.TravelInformation;
import com.visitscotland.brxm.hippobeans.TravelInformationTab;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class TravelInformationMockBuilder {

    private TravelInformation mock;

    public TravelInformationMockBuilder() {
        mock = Mockito.mock(TravelInformation.class);
    }

    public TravelInformation build() {
        return mock;
    }

    public TravelInformationMockBuilder copy(String copy) {
        HippoHtml copyHtml = Mockito.mock(HippoHtml.class);
        when(copyHtml.getContent()).thenReturn(copy);
        when(mock.getCopy()).thenReturn(copyHtml);
        return this;
    }

    public TravelInformationMockBuilder title(String title) {
        when(mock.getTitle()).thenReturn(title);
        return this;
    }

    public TravelInformationMockBuilder gettingTo(TravelInformationTab gettingTo) {
        when(mock.getGettingTo()).thenReturn(gettingTo);
        return this;
    }

    public TravelInformationMockBuilder gettingAround(TravelInformationTab gettingAround) {
        when(mock.getGettingAround()).thenReturn(gettingAround);
        return this;
    }


}
