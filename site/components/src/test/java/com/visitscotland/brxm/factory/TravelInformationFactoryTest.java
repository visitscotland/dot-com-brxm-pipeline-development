package com.visitscotland.brxm.factory;


import com.visitscotland.brxm.hippobeans.TravelInformation;
import com.visitscotland.brxm.hippobeans.TravelInformationTab;
import com.visitscotland.brxm.hippobeans.TravelInformationTransportRow;
import com.visitscotland.brxm.mock.TravelInformationMockBuilder;
import com.visitscotland.brxm.mock.TravelInformationTabMockBuilder;
import com.visitscotland.brxm.mock.TravelInformationTransportRowMockBuilder;
import com.visitscotland.brxm.model.TravelInformationModule;
import com.visitscotland.brxm.services.ResourceBundleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TravelInformationFactoryTest {
    private final String TRAVEL_INFO_TRANSPORTS_OPTIONS = "travel-information-transports";

    @Mock
    ResourceBundleService bundle;

    @InjectMocks
    TravelInformationFactory factory;

    @DisplayName("Travel information module created correctly")
    @Test
    void travelInformationModuleCreated() {
        doReturn("Train").when(bundle).getResourceBundle(TRAVEL_INFO_TRANSPORTS_OPTIONS, "train", Locale.UK);
        doReturn("Cycling").when(bundle).getResourceBundle(TRAVEL_INFO_TRANSPORTS_OPTIONS, "cycling", Locale.UK);

        TravelInformationTransportRow trainRow = new TravelInformationTransportRowMockBuilder().copy("train row copy").transport("train").build();
        TravelInformationTransportRow cyclingRow = new TravelInformationTransportRowMockBuilder().copy("cycling row copy").transport("cycling").build();
        TravelInformationTab gettingAround = new TravelInformationTabMockBuilder().title("getting around tab title").addTransportRow(trainRow).build();
        TravelInformationTab gettingTo = new TravelInformationTabMockBuilder().title("getting to tab title").addTransportRow(trainRow).addTransportRow(cyclingRow).build();
        TravelInformation travelInformation = new TravelInformationMockBuilder().title("travel information title").copy("travel information copy").gettingAround(gettingAround).gettingTo(gettingTo).build();

        TravelInformationModule module = factory.getTravelInformation(travelInformation, Locale.UK);

        assertEquals("travel information title", module.getTitle());
        assertEquals("travel information copy", module.getCopy().getContent());
        assertEquals("getting around tab title", module.getGettingAround().getTitle());
        assertEquals("getting to tab title", module.getGettingTo().getTitle());
        assertEquals(2, module.getGettingTo().getTravelInformationTransportRows().size());
        assertEquals(1, module.getGettingAround().getTravelInformationTransportRows().size());
        assertEquals("train row copy", module.getGettingTo().getTravelInformationTransportRows().get(0).getCopy().getContent());
        assertEquals("train row copy", module.getGettingAround().getTravelInformationTransportRows().get(0).getCopy().getContent());
        assertEquals("cycling row copy", module.getGettingTo().getTravelInformationTransportRows().get(1).getCopy().getContent());
        assertEquals("train", module.getGettingAround().getTravelInformationTransportRows().get(0).getTransport().getKey());
        assertEquals("Train", module.getGettingAround().getTravelInformationTransportRows().get(0).getTransport().getLabel());
        assertEquals("cycling", module.getGettingTo().getTravelInformationTransportRows().get(1).getTransport().getKey());
        assertEquals("Cycling", module.getGettingTo().getTravelInformationTransportRows().get(1).getTransport().getLabel());
    }

    @DisplayName("When travel item doesn't exist in options, then key is used as label")
    @Test
    void travelItemKeyDoesNotExistInOptions() {
        TravelInformationTransportRow trainRow = new TravelInformationTransportRowMockBuilder().transport("key does not exist").build();
        TravelInformationTab gettingAround = new TravelInformationTabMockBuilder().addTransportRow(trainRow).build();
        TravelInformationTab gettingTo = new TravelInformationTabMockBuilder().build();
        TravelInformation travelInformation = new TravelInformationMockBuilder().gettingAround(gettingAround).gettingTo(gettingTo).build();
        TravelInformationModule module = factory.getTravelInformation(travelInformation, Locale.UK);

        assertEquals("key does not exist", module.getGettingAround().getTravelInformationTransportRows().get(0).getTransport().getKey());
        assertEquals("key does not exist", module.getGettingAround().getTravelInformationTransportRows().get(0).getTransport().getLabel());
    }
}
