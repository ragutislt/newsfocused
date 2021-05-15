package eu.adainius.newsfocused;

import static eu.adainius.newsfocused.test.util.TestUtils.runWithMockedHttpResponses;
import static eu.adainius.newsfocused.test.util.TestUtils.runWithMockedMailProvider;
import static eu.adainius.newsfocused.test.util.TestUtils.runWithMockedTodaysDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
import eu.adainius.newsfocused.util.Today;

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
                runWithMockedTodaysDate(mockedToday -> {
                    String email = "sample@email.com";
                    String sitesFile = "src/test/resources/sites.txt";
                    String expectedEmail = "src/test/resources/emailSample.html";

                    mockedToday.when(() -> Today.isOneOf(any(List.class))).thenReturn(true);

                    NewsRepository mockRepository = Mockito.mock(NewsRepository.class);
                    when(mockRepository.getRunningWeek()).thenReturn(Headlines.of(List.of()));
                    App.setNewsRepository(mockRepository);

                    App.main(new String[] { sitesFile, email });

                    ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
                    mailProviderMock.verify(() -> EmailProvider.sendEmail(emailCaptor.capture()), times(1));
                    assertEquals(Files.readString(Paths.get(expectedEmail)), emailCaptor.getValue().body());
                });
            });
        });
    }

    @Test
    public void sends_emails_on_defined_days() throws Exception {
        runWithMockedHttpResponses(() -> {
            runWithMockedMailProvider(mailProviderMock -> {
                runWithMockedTodaysDate(mockedToday -> {
                    String email = "sample@email.com";
                    String sitesFile = "src/test/resources/sites.txt";
                    List<String> daysToSendOn = List.of("Saturday", "Wednesday");

                    NewsRepository mockRepository = Mockito.mock(NewsRepository.class);
                    when(mockRepository.getRunningWeek()).thenReturn(Headlines.of(List.of()));
                    App.setNewsRepository(mockRepository);

                    mockedToday.when(() -> Today.isOneOf(daysToSendOn)).thenReturn(true);

                    App.main(new String[] { sitesFile, email, String.join(",", daysToSendOn) });

                    mailProviderMock.verify(() -> EmailProvider.sendEmail(any(Email.class)), times(1));
                });
            });
        });
    }

    @Test
    public void does_not_send_emails_on_undefined_days() throws Exception {
        runWithMockedHttpResponses(() -> {
            runWithMockedMailProvider(mailProviderMock -> {
                runWithMockedTodaysDate(mockedToday -> {
                    String email = "sample@email.com";
                    String sitesFile = "src/test/resources/sites.txt";
                    List<String> daysToSendOn = List.of("Saturday", "Wednesday");

                    NewsRepository mockRepository = Mockito.mock(NewsRepository.class);
                    when(mockRepository.getRunningWeek()).thenReturn(Headlines.of(List.of()));
                    App.setNewsRepository(mockRepository);

                    mockedToday.when(() -> Today.isOneOf(daysToSendOn)).thenReturn(false);

                    App.main(new String[] { sitesFile, email, String.join(",", daysToSendOn) });

                    mailProviderMock.verify(() -> EmailProvider.sendEmail(any(Email.class)), times(0));
                });
            });
        });
    }
}
