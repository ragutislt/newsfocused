package eu.adainius.newsfocused.test.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

import eu.adainius.newsfocused.config.EmailConfiguration;
import eu.adainius.newsfocused.config.HttpClientFactory;
import eu.adainius.newsfocused.email.EmailProvider;
import eu.adainius.newsfocused.util.Today;

public class TestUtils {
    public static void runWithMockedMailProvider(RunnableStaticMockWithExceptions testToExecute) throws Exception {
        try (MockedStatic<EmailProvider> mockedEmailProvider = mockStatic(EmailProvider.class)) {
            testToExecute.run(mockedEmailProvider);
        }
    }

    public static void runWithMockedEmailConfiguration(RunnableStaticMockWithExceptions testToExecute) throws Exception {
        try (MockedStatic<EmailConfiguration> mockedEmailConfiguration = mockStatic(EmailConfiguration.class)) {
            testToExecute.run(mockedEmailConfiguration);
        }
    }

    public static void runWithMockedTodaysDate(RunnableStaticMockWithExceptions testToExecute) throws Exception {
        try (MockedStatic<Today> mockedToday = mockStatic(Today.class)) {
            testToExecute.run(mockedToday);
        }
    }

    public static void runWithMockedHttpResponses(RunnableWithExceptions testToExecute) throws Exception {
        String bbcContentLocation = "src/test/resources/bbc.html";
        String bbcContent = Files.readString(Paths.get(bbcContentLocation));

        String lrtContentLocation = "src/test/resources/lrt.html";
        String lrtContent = Files.readString(Paths.get(lrtContentLocation));

        HttpClient mockClient = Mockito.mock(HttpClient.class);
        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.body()).thenReturn(bbcContent).thenReturn(lrtContent);
        Mockito.when(mockClient.send(any(HttpRequest.class), any(BodyHandler.class))).thenReturn(mockResponse);

        try (MockedStatic<HttpClientFactory> mockedHttpClientBuilder = mockStatic(HttpClientFactory.class)) {
            mockedHttpClientBuilder.when(HttpClientFactory::httpClient).thenReturn(mockClient);
            testToExecute.run();
        }
    }
}
