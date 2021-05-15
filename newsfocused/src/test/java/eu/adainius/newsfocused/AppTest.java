package eu.adainius.newsfocused;

import static eu.adainius.newsfocused.test.util.TestUtils.runWithMockedHttpResponses;
import static eu.adainius.newsfocused.test.util.TestUtils.runWithMockedMailProvider;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
}
