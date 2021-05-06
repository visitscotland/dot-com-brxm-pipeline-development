package com.visitscotland.brxm.dms;

import com.visitscotland.brxm.services.CommonUtilsService;
import com.visitscotland.brxm.utils.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DMSProxyTest {

    DMSProxy proxy;

    @Mock
    CommonUtilsService utils;

    @Mock
    HttpURLConnection huc;

    @Mock
    Properties properties;

    @BeforeEach
    void init() {
        //Resets the last Failure.
        DMSProxy.lastRegisteredFailure = null;

        //Overrides the method that defines that instances the connection
        proxy = new DMSProxy(properties) {
            @Override
            protected HttpURLConnection openConnection(String url) {
                return huc;
            }
        };
        proxy.properties = this.properties;
    }

    @Test
    @DisplayName("VS-2386 - Serves the content of an endpoint that returns a 200 status code")
    void requestContent() throws IOException {
        final String CONTENT = "This is it";
        when(properties.getDmsEncoding()).thenReturn(Charset.defaultCharset());
        when(huc.getResponseCode()).thenReturn(200);
        when(huc.getInputStream()).thenReturn(new ByteArrayInputStream(CONTENT.getBytes()));

        Assertions.assertEquals(CONTENT, proxy.request("/happy-path"));

        verify(huc).setRequestProperty(eq(DMSProxy.HEADER), any());
    }

    @Test
    @DisplayName("VS-2386 - Serves the content redirected from a 300 status code")
    void requestWithRedirects() throws IOException {
        final String CONTENT = "This is it";
        when(properties.getDmsEncoding()).thenReturn(Charset.defaultCharset());
        when(huc.getResponseCode()).thenReturn(300);
        when(huc.getInputStream()).thenReturn(new ByteArrayInputStream(CONTENT.getBytes()));

        HttpURLConnection.setFollowRedirects(false);

        Assertions.assertEquals(CONTENT, proxy.request("/300-the-film"));
        Assertions.assertEquals(true, HttpURLConnection.getFollowRedirects());

        verify(huc).setRequestProperty(eq(DMSProxy.HEADER), any());
    }

    @Test
    @DisplayName("VS-2386 - There were several attempts of retrieving the data")
    void exceptionPropagation() throws IOException {
        final int DMS_TRIES = 5;
        when(properties.getDmsTries()).thenReturn(DMS_TRIES);
        // Verifies that the exception is propagated and a number of tries is attempted
        when(huc.getResponseCode()).thenThrow(new SocketTimeoutException());

        Assertions.assertNull(proxy.request("/timeout"));

        verify(huc, times(DMS_TRIES)).getResponseCode();
    }

    @Test
    @DisplayName("VS-2386 - Simultaneous connections are only executed once")
    void simultaneousConnection() throws IOException, InterruptedException {
        //Creates a Pool of 10 simultaneous connections
        ExecutorService service = Executors.newFixedThreadPool(20);
        final int DMS_TRIES = 1;

        when(properties.getDmsTries()).thenReturn(DMS_TRIES);
        when(properties.getDmsWaitTime()).thenReturn(60_000);

        //Apart from throwing the exceptions it halts the test to ensure concurrent calls to the method
        when(huc.getResponseCode()).then(invocationOnMock -> {
            TimeUnit.SECONDS.sleep(1);
            throw new SocketTimeoutException();
        });

        //Request 50 chuncks of data.
        for (int i = 0; i < 50; i++) {
            service.submit(() -> proxy.request("/gimme-data"));
        }
        //Send the request and wait 10 seconds maximum
        service.shutdown();
        service.awaitTermination(10, TimeUnit.SECONDS);
        if (!service.isTerminated()) {
            Assertions.fail("Concurrency hasn't been managed properly");
        }

        Assertions.assertNotNull(DMSProxy.lastRegisteredFailure);

        verify(huc, times(DMS_TRIES)).getResponseCode();
    }

    @Test
    @DisplayName("VS-2386 - Test that the lock is release after a while")
    void lockRelease() throws IOException {
        when(huc.getResponseCode()).thenThrow(new SocketTimeoutException());
        when(properties.getDmsWaitTime()).thenReturn(60_000);

        for (int i = 0; i < 3; i++) {
            Assertions.assertNull(proxy.request("/fails-and-lock"));
        }

        // Reset the Failure as if it were done an hour ago
        DMSProxy.lastRegisteredFailure = new Date().getTime() - TimeUnit.HOURS.toMillis(1);

        for (int i = 0; i < 3; i++) {
            Assertions.assertNull(proxy.request("/fails-and-lock"));
        }

        verify(huc, times(2)).getResponseCode();
    }

    @Test
    @DisplayName("The locale parameter is added to the requests, when the language is not english")
    void language() throws IOException {
        when(huc.getResponseCode()).thenThrow(new SocketTimeoutException());

        //Note: That some assertions are defined here in order to simplify the code
        DMSProxy methodProxy = new DMSProxy(properties) {
            @Override
            protected HttpURLConnection openConnection(String url) {
                Assertions.assertTrue(url.contains("?locale=fr") || url.contains("&locale=fr"));
                return huc;
            }
        };

        Assertions.assertNull(methodProxy.request("/french-locale", Locale.FRANCE));
        Assertions.assertNull(methodProxy.request("/french-locale?page=1", Locale.FRANCE));
    }


}
