package eu.adainius.newsfocused;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import eu.adainius.newsfocused.data.NewsRepository;
import eu.adainius.newsfocused.email.Email;
import eu.adainius.newsfocused.email.EmailProvider;
import eu.adainius.newsfocused.headline.Headlines;

public class AppTest {
    @Test
    public void loadsHeadlinesFromRunningWeek() throws Exception {
        runWithMockedHttpResponses(() -> {
            runWithMockedMailProvider(mailProviderMock -> {
                String email = "sample@email.com";
                String sitesFile = "src/test/resources/sites.txt";

                NewsRepository mockRepository = Mockito.mock(NewsRepository.class);
                when(mockRepository.getRunningWeek()).thenReturn(Headlines.of(List.of()));
                App.setNewsRepository(mockRepository);
                App.main(new String[] { sitesFile, email });

                ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
                mailProviderMock.verify(() -> EmailProvider.sendEmail(emailCaptor.capture()), times(1));

                Mockito.verify(mockRepository).getRunningWeek();
            });
        });
    }

    @Test
    public void savesHeadlinesToRunningWeek() throws Exception {
        runWithMockedHttpResponses(() -> {
            runWithMockedMailProvider(mailProviderMock -> {
                String email = "sample@email.com";
                String sitesFile = "src/test/resources/sites.txt";

                NewsRepository mockRepository = Mockito.mock(NewsRepository.class);
                when(mockRepository.getRunningWeek()).thenReturn(Headlines.of(List.of()));
                App.setNewsRepository(mockRepository);
                App.main(new String[] { sitesFile, email });

                ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
                mailProviderMock.verify(() -> EmailProvider.sendEmail(emailCaptor.capture()), times(1));

                Mockito.verify(mockRepository).saveRunningWeek(Mockito.any(Headlines.class));
            });
        });
    }

    @Test
    public void parses_headlines_and_sends_them_in_an_email() throws Exception {
        runWithMockedHttpResponses(() -> {
            runWithMockedMailProvider(mailProviderMock -> {
                String email = "sample@email.com";
                String sitesFile = "src/test/resources/sites.txt";
                String fileResults = "src/test/resources/emailSample.html";

                NewsRepository mockRepository = Mockito.mock(NewsRepository.class);
                when(mockRepository.getRunningWeek()).thenReturn(Headlines.of(List.of()));
                App.setNewsRepository(mockRepository);

                App.main(new String[] { sitesFile, email });

                ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
                mailProviderMock.verify(() -> EmailProvider.sendEmail(emailCaptor.capture()), times(1));
                assertEquals(Files.readString(Paths.get(fileResults)), emailCaptor.getValue().body());
            });
        });
    }
    private interface RunnableWithExceptions {
        void run() throws Exception;
    }

    private interface RunnableStaticMockWithExceptions {
        void run(MockedStatic mock) throws Exception;
    }

    private void runWithMockedMailProvider(RunnableStaticMockWithExceptions testToExecute) throws Exception {
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
