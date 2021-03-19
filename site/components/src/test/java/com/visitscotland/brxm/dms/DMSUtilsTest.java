package com.visitscotland.brxm.dms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visitscotland.dataobjects.DataType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Resource;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class DMSUtilsTest {

    @Resource @InjectMocks
    DMSUtils utils;


    @Test
    @DisplayName("Parse Key facilities into an array of entries (DataType)")
    void getFacilities() throws JsonProcessingException {
        final String PRODUCT = "{\"keyFacilities\": [" +
                "{\"name\" : \"facility1\", \"id\" : \"F1\"}," +
                "{\"name\" : \"facility2\", \"id\" : \"F2\"}" +
                "]}";

        List<DataType> facilities = utils.getKeyFacilities(new ObjectMapper().readTree(PRODUCT));

        Assertions.assertEquals(2, facilities.size());
        Assertions.assertEquals("facility1", facilities.get(0).getName());
        Assertions.assertEquals("F2", facilities.get(1).getId());
    }
}
