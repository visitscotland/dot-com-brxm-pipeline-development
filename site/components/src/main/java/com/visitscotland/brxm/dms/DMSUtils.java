package com.visitscotland.brxm.dms;

import com.fasterxml.jackson.databind.JsonNode;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.dataobjects.DataType;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.visitscotland.brxm.dms.DMSConstants.DMSProduct.*;

@Component
public class DMSUtils {

    public static final String ITINERARY_BUNDLE = "itinerary";

    private ResourceBundleService bundle;

    public DMSUtils(ResourceBundleService bundle) {
        this.bundle = bundle;
    }

    public List<DataType> getKeyFacilities(JsonNode product){
        List<DataType> facilities = new ArrayList<>();
        if (product.has(FACILITIES) && product.get(FACILITIES).isArray()){
            for (JsonNode facility : product.get(FACILITIES)) {
                DataType dataType = new DataType(facility.get(ID).asText(),facility.get(NAME).asText());
                facilities.add(dataType);
            }
        }
        return facilities;
    }

}
