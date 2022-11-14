package com.visitscotland.brxm.utils;

import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.slf4j.Logger;
import org.springframework.context.annotation.Lazy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageCacheTest {

    /**
     * If retention were too low, don't increase the value, add the following line
     * to each test case {@code when(properties.getContentCacheRetention()).thenReturn(5000)}
     */
    private final int RETENTION = 500000;

    @InjectMocks
    MessageCache cache;

    @Mock
    Properties properties;

    @BeforeEach
    void init(){
        lenient().when(properties.getContentCacheMaxElements()).thenReturn(10);
        lenient().when(properties.getContentCacheRetention()).thenReturn(RETENTION);
    }

    @Test
    @DisplayName("Log an issue")
    void issue(){
        Assertions.assertTrue(cache.canLog("Error"));
    }

    @Test
    @DisplayName("Log an issue with Arguments")
    void errorWithArguments(){
        Assertions.assertTrue(cache.canLog("Error", "with arguments"));
    }

    @Test
    @DisplayName("Log the similar issues")
    void similarIssues(){
        Assertions.assertTrue(cache.canLog("ERROR"));
        Assertions.assertTrue(cache.canLog("ERROR", "with arguments"));
        Assertions.assertTrue(cache.canLog("ERROR", "with different arguments"));
        Assertions.assertTrue(cache.canLog("ERROR", "with this argument", "and this argument"));
        Assertions.assertTrue(cache.canLog("ERROR", "with this argument", "and this other argument"));
    }

    @Test
    @DisplayName("Log the same issue several times")
    void log_sameIssue(){
        Assertions.assertTrue(cache.canLog("ERROR", "with arguments"));
        for (int i=0; i < 10; i++){
            Assertions.assertFalse(cache.canLog("ERROR", "with arguments"));
        }
    }

    @Test
    @DisplayName("Messages are logged again after the retention time is exceeded")
    void retention() throws InterruptedException {
        //TODO
//        Assertions.assertTrue(cache.canLog("ERROR"));
//        Assertions.assertFalse(cache.canLog("ERROR"));
//        reset(properties);
//        when(properties.getContentCacheMaxElements()).thenReturn(10);
//        when(properties.getContentCacheRetention()).thenReturn(1);
//        Assertions.assertTrue(cache.canLog("ERROR"));
//        Assertions.assertFalse(cache.canLog("ERROR"));
    }

}