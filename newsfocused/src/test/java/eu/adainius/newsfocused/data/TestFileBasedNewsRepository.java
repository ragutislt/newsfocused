package eu.adainius.newsfocused.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Stream;

import com.google.gson.Gson;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import eu.adainius.newsfocused.headline.Headline;
import eu.adainius.newsfocused.headline.Headlines;
import eu.adainius.newsfocused.util.Today;

public class TestFileBasedNewsRepository {
    @TempDir
    Path tempRepoFile;

    @Test
    void returnsRunningWeekFor() throws Exception {

        // GIVEN
        Path repoFolder = Files.createDirectory(tempRepoFile.resolve("repo"));

        String userEmail = "returnsRunningWeekFor@test.com";
        String storageFileName = FileBasedNewsRepository.emailToFilename(userEmail);
        Path repoFilePath = Files.createFile(repoFolder.resolve(storageFileName));

        NewsRepository repository = new FileBasedNewsRepository(repoFolder.toString());

        Headline headline1 = Headline.builder().date(LocalDate.parse("2018-05-05")).htmlLink("www.bbc.com")
                .title("title").urlLink("www.bbc.com").website("www.bbc.com").build();
        Headline headline2 = Headline.builder().date(LocalDate.parse("2020-09-17")).htmlLink("www.bbc.com")
                .title("title").urlLink("www.bbc.com").website("www.bbc.com").build();
        Headlines headlines = Headlines.of(headline1, headline2);
        writeToFile(headlines, repoFilePath);

        // WHEN
        Headlines runningWeek = repository.getRunningWeekFor(userEmail);

        // THEN
        assertEquals(headlines, runningWeek);
    }

    @Test
    void returnsEmptyRunningWeekIfNoDataInFile() throws Exception {
        // GIVEN
        Path repoFolder = Files.createDirectory(tempRepoFile.resolve("repo"));

        String userEmail = "returnsEmptyRunningWeekIfNoDataInFile@test.com";
        String storageFileName = FileBasedNewsRepository.emailToFilename(userEmail);
        Path repoFilePath = Files.createFile(repoFolder.resolve(storageFileName));

        NewsRepository repository = new FileBasedNewsRepository(repoFolder.toString());
        writeToFile(null, repoFilePath);

        // WHEN
        Headlines runningWeekHeadlines = repository.getRunningWeekFor(userEmail);

        // THEN
        assertTrue(runningWeekHeadlines.areEmpty());
    }

    @Test
    void returnsEmptyRunningWeekIfFileDoesNotExist() throws Exception {
        // GIVEN
        Path repoFolder = Files.createDirectory(tempRepoFile.resolve("repo"));

        String userEmail = "returnsEmptyRunningWeekIfFileDoesNotExist@test.com";
        String storageFileName = FileBasedNewsRepository.emailToFilename(userEmail);

        Path repoFilePath = Files.createFile(repoFolder.resolve(storageFileName));
        File repoFile = new File(repoFilePath.toAbsolutePath().toString());

        if (repoFile.exists()) {
            repoFile.delete();
        }

        NewsRepository repository = new FileBasedNewsRepository(repoFolder.toString());

        // WHEN
        Headlines runningWeekHeadlines = repository.getRunningWeekFor(userEmail);

        // THEN
        assertTrue(runningWeekHeadlines.areEmpty());
    }

    private void writeToFile(Headlines headlines, Path repoFile) throws Exception {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(repoFile.toString())) {
            gson.toJson(headlines, writer);
        }
    }

    @Test
    void savesRunningWeek() throws Exception {
        // GIVEN
        Path repoFolder = Files.createDirectory(tempRepoFile.resolve("repo"));

        String userEmail = "savesRunningWeek@test.com";
        String storageFileName = FileBasedNewsRepository.emailToFilename(userEmail);

        File repoFile = createRepoFile(repoFolder, storageFileName);
        NewsRepository repository = new FileBasedNewsRepository(repoFolder.toString());

        Headline headline1 = Headline.builder().date(LocalDate.parse("2018-05-05")).htmlLink("www.bbc.com")
                .title("title").urlLink("www.bbc.com").website("www.bbc.com").build();
        Headline headline2 = Headline.builder().date(LocalDate.parse("2020-09-17")).htmlLink("www.bbc.com")
                .title("title").urlLink("www.bbc.com").website("www.bbc.com").build();
        Headlines headlines = Headlines.of(headline1, headline2);

        // WHEN
        repository.saveRunningWeekFor(headlines, userEmail);

        // THEN
        Headlines headlinesFromFile = readFromFile(repoFile);
        assertEquals(headlines, headlinesFromFile);

        repoFile.delete();
    }

    @Test
    void creates_repo_user_file_if_it_does_not_exist() throws Exception {
        // GIVEN
        Path repoFolder = Files.createDirectory(tempRepoFile.resolve("repo"));
        String userEmail = "savesRunningWeek@test.com";
        String storageFileName = FileBasedNewsRepository.emailToFilename(userEmail);
        File expectedRepoFile = Paths.get(repoFolder.toString(), storageFileName).toFile();

        if (expectedRepoFile.exists()) {
            expectedRepoFile.delete();
        }

        NewsRepository repository = new FileBasedNewsRepository(repoFolder.toString());
        Headlines headlines = new Headlines();

        // WHEN
        repository.saveRunningWeekFor(headlines, userEmail);

        // THEN
        assertTrue(expectedRepoFile.exists());
    }

    private Headlines readFromFile(File repoFile) throws Exception {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(repoFile)) {
            return gson.fromJson(reader, Headlines.class);
        }
    }

    @Test
    void resetsRunningWeek() throws Exception {
        // GIVEN
        Path repoFolder = Files.createDirectory(tempRepoFile.resolve("repo"));

        String userEmail = "resetsRunningWeek@test.com";
        String storageFileName = FileBasedNewsRepository.emailToFilename(userEmail);

        File repoFile = createRepoFile(repoFolder, storageFileName);

        NewsRepository repository = new FileBasedNewsRepository(repoFolder.toString());

        Headline headline = Headline.builder().date(LocalDate.parse("2020-09-17")).htmlLink("www.bbc.com")
                .title("title").urlLink("www.bbc.com").website("www.bbc.com").build();
        Headlines headlines = Headlines.of(headline);

        repository.saveRunningWeekFor(headlines, userEmail);

        // WHEN
        repository.resetRunningWeekFor(userEmail);

        // THEN
        String oldRepoFileName = String.format("%s_%s.json", storageFileName.replace(".json", ""),
                Today.getDateString());

        File oldRepoFile = new File(repoFolder.resolve(oldRepoFileName).toString());
        assertTrue(oldRepoFile.exists());

        Headlines oldHeadlinesFromFile = readFromFile(oldRepoFile);
        assertEquals(headlines, oldHeadlinesFromFile);

        repoFile.delete();
        oldRepoFile.delete();
    }

    private File createRepoFile(Path repoFolder, String storageFileName) throws IOException {
        Path repoFilePath = Files.createFile(repoFolder.resolve(storageFileName));
        File repoFile = new File(repoFilePath.toAbsolutePath().toString());

        if (repoFile.exists()) {
            repoFile.delete();
        }

        repoFile.createNewFile();
        return repoFile;
    }

    @ParameterizedTest
    @MethodSource("provideEmailsAndExpectedFilenames")
    @DisplayName("email is converted to a correct filename")
    public void emailToFilename(String email, String expectedFilename) {
        assertEquals(expectedFilename, FileBasedNewsRepository.emailToFilename(email));
    }

    private static Stream<Arguments> provideEmailsAndExpectedFilenames() {
        return Stream.of(Arguments.of("some@email.com", "headlines_running_week___some__at__email.com.json"),
                Arguments.of("some@email_email.com", "headlines_running_week___some__at__email_email.com.json"),
                Arguments.of("some_some@email.com", "headlines_running_week___some_some__at__email.com.json"),
                Arguments.of("some@email.com.eu", "headlines_running_week___some__at__email.com.eu.json"),
                Arguments.of("some@email-email.com", "headlines_running_week___some__at__email-email.com.json"),
                Arguments.of("some.some@email.com", "headlines_running_week___some.some__at__email.com.json"));
    }
}
