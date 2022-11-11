package com.visitscotland.brxm.utils;

import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageCacheTest {

    @InjectMocks
    MessageCache cache;

    @Test
    @DisplayName("Log an issue")
    void issue(){
//        Assertions.assertTrue(cache.canLog("Error"));
    }

    @Test
    @DisplayName("Log an issue with Arguments")
    void errorWithArguments(){
//        Assertions.assertTrue(cache.canLog("Error", "with arguments"));
    }

    @Test
    @DisplayName("Log the similar issues")
    void similarIssues(){
//        Assertions.assertTrue(cache.canLog("ERROR"));
//        Assertions.assertTrue(cache.canLog("ERROR", "with arguments"));
//        Assertions.assertTrue(cache.canLog("ERROR", "with different arguments"));
//        Assertions.assertTrue(cache.canLog("ERROR", "with this argument", "and this argument"));
//        Assertions.assertTrue(cache.canLog("ERROR", "with this argument", "and this other argument"));
    }

    @Test
    @DisplayName("Log the same issue several times")
    void log_sameIssue(){
//        Logger mock = mock(Logger.class);
//        ContentLogger contentLogger = new ContentLogger(mock);
//        for (int i = 0; i <10; i++) {
//            contentLogger.error("ERROR {}", "with arguments");
//        }
//        for (int i = 0; i <5; i++) {
//            contentLogger.error("ERROR");
//        }
//        verify(mock, atMostOnce()).error("ERROR");
//        verify(mock, atMostOnce()).error("ERROR {}", "with arguments");
    }

}