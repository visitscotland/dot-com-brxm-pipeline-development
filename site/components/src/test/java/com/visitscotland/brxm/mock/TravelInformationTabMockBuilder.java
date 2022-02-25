package com.visitscotland.brxm.mock;

import com.visitscotland.brxm.hippobeans.TravelInformationTab;
import com.visitscotland.brxm.hippobeans.TravelInformationTransportRow;
import com.visitscotland.utils.Contract;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class TravelInformationTabMockBuilder {

    private TravelInformationTab mock;
    private List<TravelInformationTransportRow> transports;

    public TravelInformationTabMockBuilder() {
        mock = Mockito.mock(TravelInformationTab.class);
        transports = new ArrayList<>();
    }

    public TravelInformationTab build() {
        if (!Contract.isEmpty(transports)) {
            when(mock.getAccordion()).thenReturn(transports);
        }
        return mock;
    }

    public TravelInformationTabMockBuilder title(String title) {
        when(mock.getTitle()).thenReturn(title);
        return this;
    }

    public TravelInformationTabMockBuilder addTransportRow(TravelInformationTransportRow transport) {
        transports.add(transport);
        return this;
    }



}
