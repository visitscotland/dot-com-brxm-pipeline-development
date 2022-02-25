package com.visitscotland.brxm.mock;

import com.visitscotland.brxm.hippobeans.TravelInformationTransportRow;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class TravelInformationTransportRowMockBuilder {

    private TravelInformationTransportRow mock;

    public TravelInformationTransportRowMockBuilder() {
        mock = Mockito.mock(TravelInformationTransportRow.class);
    }

    public TravelInformationTransportRow build() {
        return mock;
    }

    public TravelInformationTransportRowMockBuilder transport(String transport) {
        when(mock.getTransport()).thenReturn(transport);
        return this;
    }

    public TravelInformationTransportRowMockBuilder copy(String copy) {
        HippoHtml copyHtml = Mockito.mock(HippoHtml.class);
        when(copyHtml.getContent()).thenReturn(copy);
        when(mock.getCopy()).thenReturn(copyHtml);
        return this;
    }

}
