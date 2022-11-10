package com.visitscotland.brxm.utils;

import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContentLoggerTest {

    @Test
    @DisplayName("Log an error")
    void log_error(){
        Logger mock = mock(Logger.class);
        ContentLogger contentLogger = new ContentLogger(mock);
        contentLogger.error("ERROR");
        verify(mock).error("ERROR");
    }

    @Test
    @DisplayName("Log an error with Arguments")
    void log_errorWithArguments(){
        Logger mock = mock(Logger.class);
        ContentLogger contentLogger = new ContentLogger(mock);
        contentLogger.error("ERROR {}", "with arguments");
        verify(mock).error("ERROR {}", "with arguments");
    }

    @Test
    @DisplayName("Log the similar errors")
    void log_errorSimilarErrors(){
        Logger mock = mock(Logger.class);
        ContentLogger contentLogger = new ContentLogger(mock);
//        contentLogger.error("ERROR");
        contentLogger.error("ERROR", "with arguments");
        contentLogger.error("ERROR", "with different arguments");
//        contentLogger.error("ERROR", "with this argument", "and this argument");
//        contentLogger.error("ERROR", "with this argument", "and this other argument");
//        verify(mock, times(1)).error("ERROR");
        verify(mock, times(2)).error(eq("ERROR"), any(Object.class));
//        verify(mock, times(2)).error(eq("ERROR"), any(Object.class), any(Object.class));
    }

    @Test
    @DisplayName("Log the same error several times")
    void log_sameError(){
        Logger mock = mock(Logger.class);
        ContentLogger contentLogger = new ContentLogger(mock);
        for (int i = 0; i <10; i++) {
            contentLogger.error("ERROR {}", "with arguments");
        }
        for (int i = 0; i <5; i++) {
            contentLogger.error("ERROR");
        }
        verify(mock, atMostOnce()).error("ERROR");
        verify(mock, atMostOnce()).error("ERROR {}", "with arguments");
    }

}