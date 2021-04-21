package eu.adainius.newsfocused;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import eu.adainius.newsfocused.email.Email;
import eu.adainius.newsfocused.email.EmailProvider;

public class AppTest {

    @Test
    public void parses_headlines_and_sends_them_in_an_email() throws Exception {
        runWithMockedHttpResponses(() -> {
            runWithMockedMailProvider(mailProviderMock -> {
                String email = "sample@email.com";
                String sitesFile = "src/test/resources/sites.txt";
                String fileResults = "src/test/resources/emailSample.html";

                App.main(new String[] { sitesFile, email });

                ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
                mailProviderMock.verify(() -> EmailProvider.sendEmail(emailCaptor.capture()), times(1));
                assertEquals(Files.readString(Paths.get(fileResults)), emailCaptor.getValue().body());
            });
        });
    }

    @Test
    public void parses_headlines_and_saves_them_to_file() throws Exception {
        runWithMockedHttpResponses(() -> {
            String fileResults = "target/results.html";
            String fileSites = "src/test/resources/sites.txt";

            App.main(new String[] { fileSites, fileResults });

            String fileContent = Files.readString(Paths.get(fileResults));

            assertTrue(fileContent.contains(
                    "Profesorius Janeliūnas: vertybiniai klausimai ateityje gali sukelti didelius nesutarimus koalicijoje"));
            assertTrue(fileContent.contains("Bloody day in Myanmar's main city sees 14 killed"));
        });
    }

    @Test
    public void saves_headlines_to_html_file() throws Exception {
        runWithMockedHttpResponses(() -> {
            String fileResults = "target/results.html";
            String fileSites = "src/test/resources/sites.txt";

            App.main(new String[] { fileSites, fileResults });

            String fileContent = Files.readString(Paths.get(fileResults));

            assertTrue(fileContent.contains("<html"));
            assertTrue(fileContent.contains("</html>"));
        });
    }

    private interface RunnableWithExceptions {
        void run() throws Exception;
    }

    private interface RunnableStaticMockWithExceptions {
        void run(MockedStatic mock) throws Exception;
    }

    private void runWithMockedMailProvider(RunnableStaticMockWithExceptions testToExecute) throws Exception {
        String bbcContentLocation = "src/test/resources/bbc.html";
        String bbcContent = Files.readString(Paths.get(bbcContentLocation));

        String lrtContentLocation = "src/test/resources/lrt.html";
        String lrtContent = Files.readString(Paths.get(lrtContentLocation));

        HttpClient mockClient = Mockito.mock(HttpClient.class);
        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.body()).thenReturn(bbcContent).thenReturn(lrtContent);
        Mockito.when(mockClient.send(any(HttpRequest.class), any(BodyHandler.class))).thenReturn(mockResponse);

        try (MockedStatic<EmailProvider> mockedEmailProvider = mockStatic(EmailProvider.class)) {
            testToExecute.run(mockedEmailProvider);
        }
    }

    private void runWithMockedHttpResponses(RunnableWithExceptions testToExecute) throws Exception {
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
