package com.visitscotland.brxm.dms;

import com.visitscotland.brxm.dms.model.LocationObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LocationLoaderTest {

    @InjectMocks
    LocationLoader locationService;

    @Mock
    DMSProxy dmsProxy;

    @Test
    @Disabled ("Location Loader Methods need to be created")
    void test(){
        assertFalse(true, "This method has been created due to static analysis");
    }

    /**
     * Streamline way of creating a LocationObject. It populate non-essential fields with null
     */
    private LocationObject fakeLocation(String id, String key, String name, String type, String parentId){
        return new LocationObject(id, key,name, type, null,null,null,new HashSet<>(Arrays.asList(type)), parentId);
    }

    private void initiatizeLocations(){
        final String DMS_RESPONSE = "{\"data\": [\n" +
                "{\"id\":\"1\",\"key\":\"1\",\"name\":\"Scotland\",\"type\":\"COUNTRY\", \"types\":[\"COUNTRY\"]},\n" +
                "{\"id\":\"10\",\"key\":\"10\",\"name\":\"Region\",\"type\":\"REGION\",\"parentId\":\"1\", \"types\":[\"REGION\"]},\n" +
                "{\"id\":\"100\",\"key\":\"100\",\"name\":\"Subregion\",\"type\":\"SUBREGION\",\"parentId\":\"10\", \"types\":[\"SUBREGION\"]},\n" +
                "{\"id\":\"1000\",\"key\":\"1000\",\"name\":\"District \",\"type\":\"DISTRICT\",\"parentId\":\"100\", \"types\":[\"DISTRICT\"]},\n" +
                "{\"id\":\"polygon\",\"key\":\"10\",\"name\":\"Region\",\"type\":\"POLYGON\", \"types\":[\"POLYGON\", \"REGION\"]}\n" +
                "]}";

        Mockito.when(dmsProxy.canMakeRequest()).thenReturn(true);
        Mockito.when(dmsProxy.request(DMSConstants.META_LOCATIONS, Locale.UK)).thenReturn(DMS_RESPONSE);
        locationService.validateMaps();
    }

    
    @Test
    @DisplayName("getRegion - returns itself if the location is a region")
    void getRegion_Region(){
        LocationObject location = fakeLocation("555", "555", "The Region", "REGION", "555-2");

        LocationObject output = locationService.getRegion(location, Locale.UK);

        Assertions.assertEquals(location, output);
    }

    @Test
    @DisplayName("getRegion - returns the region if the location is inside a region")
    void getRegion_Destination(){
        LocationObject level5 = fakeLocation("55555", "55555", "Something below than a district", "LOCATION", "1000");
        LocationObject level4 = fakeLocation("5555", "5555", "Something below than a district", "LOCATION", "100");

        initiatizeLocations();
        Assertions.assertTrue(locationService.getRegion(level4, Locale.UK).isRegion());
        Assertions.assertTrue(locationService.getRegion(level5, Locale.UK).isRegion());
    }

    @Test
    @DisplayName("getRegion - returns null if the location is Scotland (country level)")
    void getRegion_Country(){
        LocationObject location = fakeLocation("1", "1", "Scotland", "COUNTRY", "");

        initiatizeLocations();
        LocationObject output = locationService.getRegion(location, Locale.UK);

        Assertions.assertNull(output);
    }

    @Test
    @DisplayName("getRegion - returns null if the location is not contained in a region")
    void getRegion_InvalidLocation(){
        LocationObject location = fakeLocation("555", "555", "Secret Island", "DISTRICT", "42");

        initiatizeLocations();
        LocationObject output = locationService.getRegion(location, Locale.UK);

        Assertions.assertNull(output);
    }

}