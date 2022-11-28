package com.visitscotland.brxm.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageSuppressorTest {

    @InjectMocks
    MessageSuppressor cache;

    @Mock
    Properties properties;

    @BeforeEach
    void init(){
        when(properties.getContentCacheMaxElements()).thenReturn(10);
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
        when(properties.getContentCacheRetention()).thenReturn(Integer.MAX_VALUE);

        Assertions.assertTrue(cache.canLog("ERROR", "with arguments"));
        for (int i=0; i < 10; i++){
            Assertions.assertFalse(cache.canLog("ERROR", "with arguments"));
        }
    }

    @Test
    @DisplayName("Clean up job removes messages not logged recently")
    void cleanUp() {
        when(properties.getContentCacheRetention()).thenReturn(60_000);

        for (int i =0; i<9;i++){
            cache.messages.add(createOldMessage(i%2==0));
        }

        Assertions.assertTrue(cache.canLog("ERROR 10"));
        Assertions.assertEquals(10, cache.messages.size());

        // Half of the previous messages should have disappeared after the cleanup
        Assertions.assertTrue(cache.canLog("ERROR 11"));
        Assertions.assertEquals(6, cache.messages.size());

        Assertions.assertTrue(cache.canLog("ERROR 12"));
        Assertions.assertEquals(7, cache.messages.size());
    }

 /*   @Test
    @DisplayName("Messages are logged again after the retention time is exceeded")
    void retention() throws InterruptedException {
        when(properties.getContentCacheRetention()).thenReturn(1);

        Assertions.assertTrue(cache.canLog("SHORT RETENTION"));

        // We wait 1ms before trying to log it again
        Thread.sleep(1);
        // TODO Replace previous line with "await().atMost(1, Duration.MILLISECONDS)". Sonar is complaining
        Assertions.assertTrue(cache.canLog("SHORT RETENTION"));
    }*/

    private MessageSuppressor.Message createOldMessage(boolean lastOld) {
        return createMessage(new Date(0), lastOld?new Date(0):new Date());
    }
    private MessageSuppressor.Message createMessage(Date first, Date last){
        MessageSuppressor.Message message = mock(MessageSuppressor.Message.class);
        lenient().when(message.getFirstOccurrence()).thenReturn(first);
        lenient().when(message.getLastOccurrence()).thenReturn(last);

        return  message;
    }

}