package eu.adainius.newsfocused;

import static eu.adainius.newsfocused.test.util.TestUtils.runWithMockedEmailConfiguration;
import static eu.adainius.newsfocused.test.util.TestUtils.runWithMockedHttpResponses;
import static eu.adainius.newsfocused.test.util.TestUtils.runWithMockedMailProvider;
import static eu.adainius.newsfocused.test.util.TestUtils.runWithMockedTodaysDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import eu.adainius.newsfocused.config.EmailConfiguration;
import eu.adainius.newsfocused.data.NewsRepository;
import eu.adainius.newsfocused.email.Email;
import eu.adainius.newsfocused.email.EmailProvider;
import eu.adainius.newsfocused.headline.Headlines;
import eu.adainius.newsfocused.util.Today;

public class AppTest {
    @TempDir
    Path tempRepoFile;
    
    @Test
    public void loads_headlines_from_running_week() throws Exception {
        runWithMockedHttpResponses(() -> {
            runWithMockedMailProvider(mailProviderMock -> {
                String usersLocation = "src/test/resources/users.json";

                NewsRepository mockRepository = Mockito.mock(NewsRepository.class);
                when(mockRepository.getRunningWeekFor(anyString())).thenReturn(Headlines.of(List.of()));
                App.setNewsRepository(mockRepository);
                App.main(new String[] { usersLocation });

                mailProviderMock.verify(() -> EmailProvider.sendEmail(any(Email.class)), times(2));

                Mockito.verify(mockRepository).getRunningWeekFor("some@email.com");
                Mockito.verify(mockRepository).getRunningWeekFor("some@email222.com");
            });
        });
    }

    @Test
    public void saves_headlines_to_running_week() throws Exception {
        runWithMockedHttpResponses(() -> {
            runWithMockedMailProvider(mailProviderMock -> {
                String usersLocation = "src/test/resources/users.json";

                NewsRepository mockRepository = Mockito.mock(NewsRepository.class);
                when(mockRepository.getRunningWeekFor(anyString())).thenReturn(Headlines.of(List.of()));
                App.setNewsRepository(mockRepository);
                App.main(new String[] { usersLocation });

                mailProviderMock.verify(() -> EmailProvider.sendEmail(any(Email.class)), times(2));

                Mockito.verify(mockRepository).saveRunningWeekFor(Mockito.any(Headlines.class), eq("some@email.com"));
                Mockito.verify(mockRepository).saveRunningWeekFor(Mockito.any(Headlines.class), eq("some@email222.com"));
            });
        });
    }

    @Test
    public void resets_running_week_headlines_upon_sending_email() throws Exception {
        runWithMockedHttpResponses(() -> {
            runWithMockedMailProvider(mailProviderMock -> {
                runWithMockedTodaysDate(mockedToday -> {
                    String usersLocation = "src/test/resources/users.json";

                    mockedToday.when(() -> Today.isOneOf(any(List.class))).thenReturn(true);
                    mockedToday.when(() -> Today.getToday()).thenReturn(LocalDate.of(2021,05,15));

                    NewsRepository mockRepository = Mockito.mock(NewsRepository.class);
                    when(mockRepository.getRunningWeekFor(anyString())).thenReturn(Headlines.of(List.of()));
                    App.setNewsRepository(mockRepository);
                    App.main(new String[] { usersLocation });

                    mailProviderMock.verify(() -> EmailProvider.sendEmail(any(Email.class)), times(2));
                    Mockito.verify(mockRepository).resetRunningWeekFor("some@email.com");
                    Mockito.verify(mockRepository).resetRunningWeekFor("some@email222.com");
                });
            });
        });
    }

    @Test
    public void parses_headlines_and_sends_them_in_an_email() throws Exception {
        runWithMockedHttpResponses(() -> {
            runWithMockedMailProvider(mailProviderMock -> {
                runWithMockedTodaysDate(mockedToday -> {
                    String usersLocation = "src/test/resources/users_bbc.json";
                    String expectedEmail = "src/test/resources/emailSample.html";

                    mockedToday.when(() -> Today.isOneOf(any(List.class))).thenReturn(true);
                    mockedToday.when(() -> Today.getToday()).thenReturn(LocalDate.of(2021,05,15));

                    NewsRepository mockRepository = Mockito.mock(NewsRepository.class);
                    when(mockRepository.getRunningWeekFor(anyString())).thenReturn(Headlines.of(List.of()));
                    App.setNewsRepository(mockRepository);

                    App.main(new String[] { usersLocation });

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
                    String usersLocation = "src/test/resources/users_bbc_daysToSendOn.json";
                    List<String> daysToSendOn = List.of("Wednesday", "Saturday");

                    NewsRepository mockRepository = Mockito.mock(NewsRepository.class);
                    when(mockRepository.getRunningWeekFor(anyString())).thenReturn(Headlines.of(List.of()));
                    App.setNewsRepository(mockRepository);

                    mockedToday.when(() -> Today.isOneOf(daysToSendOn)).thenReturn(true);
                    mockedToday.when(() -> Today.getToday()).thenReturn(LocalDate.now());

                    App.main(new String[] { usersLocation });

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
                    String usersLocation = "src/test/resources/users.json";
                    List<String> daysToSendOn = List.of("Saturday", "Wednesday");

                    NewsRepository mockRepository = Mockito.mock(NewsRepository.class);
                    when(mockRepository.getRunningWeekFor(anyString())).thenReturn(Headlines.of(List.of()));
                    App.setNewsRepository(mockRepository);

                    mockedToday.when(() -> Today.isOneOf(daysToSendOn)).thenReturn(false);
                    mockedToday.when(() -> Today.getToday()).thenReturn(LocalDate.of(2021,05,15));

                    App.main(new String[] { usersLocation });

                    mailProviderMock.verify(() -> EmailProvider.sendEmail(any(Email.class)), times(0));
                });
            });
        });
    }

    @Test
    void uses_default_email_configuration_if_no_email_properties_file_supplied() throws Exception {
        runWithMockedHttpResponses(() -> {
            runWithMockedMailProvider(mailProviderMock -> {
                runWithMockedEmailConfiguration(mockedEmailConfiguration -> {
                    String usersLocation = "src/test/resources/users.json";

                    NewsRepository mockRepository = Mockito.mock(NewsRepository.class);
                    when(mockRepository.getRunningWeekFor(anyString())).thenReturn(Headlines.of(List.of()));
                    App.setNewsRepository(mockRepository);

                    App.main(new String[] { usersLocation });

                    mockedEmailConfiguration.verify(
                            () -> EmailConfiguration.setEmailProtocolProperties(any(Properties.class)), times(0));
                });
            });
        });
    }

    @Test
    void uses_email_configuration_from_email_properties() throws Exception {
        runWithMockedHttpResponses(() -> {
            runWithMockedMailProvider(mailProviderMock -> {
                runWithMockedEmailConfiguration(mockedEmailConfiguration -> {
                    String usersLocation = "src/test/resources/users.json";
                    Path repoFolder = Files.createDirectory(tempRepoFile.resolve("repo"));
                    String repoLocation = repoFolder.toString();
                    String emailPropertiesFile = "src/test/resources/emailProtocol.properties";

                    NewsRepository mockRepository = Mockito.mock(NewsRepository.class);
                    when(mockRepository.getRunningWeekFor(anyString())).thenReturn(Headlines.of(List.of()));
                    App.setNewsRepository(mockRepository);

                    App.main(new String[] { usersLocation, repoLocation, emailPropertiesFile });

                    ArgumentCaptor<Properties> emailPropertiesCaptor = ArgumentCaptor.forClass(Properties.class);
                    mockedEmailConfiguration.verify(
                            () -> EmailConfiguration.setEmailProtocolProperties(emailPropertiesCaptor.capture()),
                            times(1));

                    assertEquals("some_host_name.com", emailPropertiesCaptor.getValue().getProperty("mail.smtp.host"));
                    assertEquals("666", emailPropertiesCaptor.getValue().getProperty("mail.smtp.port"));
                    assertEquals("true", emailPropertiesCaptor.getValue().getProperty("mail.smtp.starttls.enable"));
                    assertEquals("false", emailPropertiesCaptor.getValue().getProperty("mail.smtp.auth"));
                });
            });
        });
    }
}
