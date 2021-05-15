package eu.adainius.newsfocused.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import com.google.gson.Gson;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import eu.adainius.newsfocused.headline.Headline;
import eu.adainius.newsfocused.headline.Headlines;
import eu.adainius.newsfocused.util.Today;

public class TestFileBasedNewsRepository {
    @TempDir
    Path tempRepoFile;

    @Test
    void returnsRunningWeek() throws Exception {

        Path repoFile = Files.createFile(tempRepoFile.resolve("headlines_return_running_week.json"));

        NewsRepository repository = new FileBasedNewsRepository(repoFile.toString());

        Headline headline1 = Headline.builder().date(LocalDate.parse("2018-05-05")).htmlLink("www.bbc.com")
                .title("title").urlLink("www.bbc.com").website("www.bbc.com").build();
        Headline headline2 = Headline.builder().date(LocalDate.parse("2020-09-17")).htmlLink("www.bbc.com")
                .title("title").urlLink("www.bbc.com").website("www.bbc.com").build();
        Headlines headlines = Headlines.of(headline1, headline2);
        writeToFile(headlines, repoFile);

        Headlines runningWeek = repository.getRunningWeek();
        assertEquals(headlines, runningWeek);
    }

    private void writeToFile(Headlines headlines, Path repoFile) throws Exception {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(repoFile.toString())) {
            gson.toJson(headlines, writer);
        }
    }

    @Test
    void savesRunningWeek() throws Exception {
        File repoFile = new File("C:\\Users\\senel\\AppData\\Local\\Temp\\tests", "headlines_save_running_week.json");

        if (repoFile.exists()) {
            repoFile.delete();
        }
        repoFile.createNewFile();
        NewsRepository repository = new FileBasedNewsRepository(repoFile.getAbsolutePath());

        Headline headline1 = Headline.builder().date(LocalDate.parse("2018-05-05")).htmlLink("www.bbc.com")
                .title("title").urlLink("www.bbc.com").website("www.bbc.com").build();
        Headline headline2 = Headline.builder().date(LocalDate.parse("2020-09-17")).htmlLink("www.bbc.com")
                .title("title").urlLink("www.bbc.com").website("www.bbc.com").build();
        Headlines headlines = Headlines.of(headline1, headline2);

        repository.saveRunningWeek(headlines);
        Headlines headlinesFromFile = readFromFile(repoFile);
        assertEquals(headlines, headlinesFromFile);

        repoFile.delete();
    }

    private Headlines readFromFile(File repoFile) throws Exception {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(repoFile)) {
            return gson.fromJson(reader, Headlines.class);
        }
    }

    @Test
    void resetsRunningWeek() throws Exception {
        File repoFile = new File("C:\\Users\\senel\\AppData\\Local\\Temp\\tests", "headlines_save_running_week.json");

        if (repoFile.exists()) {
            repoFile.delete();
        }
        repoFile.createNewFile();
        NewsRepository repository = new FileBasedNewsRepository(repoFile.getAbsolutePath());

        Headline headline = Headline.builder().date(LocalDate.parse("2020-09-17")).htmlLink("www.bbc.com")
                .title("title").urlLink("www.bbc.com").website("www.bbc.com").build();
        Headlines headlines = Headlines.of(headline);

        repository.saveRunningWeek(headlines);
        repository.resetRunningWeek();

        String oldRepoFileName = String.format("headlines_save_running_week_%s.json", Today.getDateString());
        File oldRepoFile = new File("C:\\Users\\senel\\AppData\\Local\\Temp\\tests", oldRepoFileName);
        //assertTrue(repoFile.exists());
        assertTrue(oldRepoFile.exists());

        /* Headlines newHeadlines = readFromFile(repoFile);
        assertTrue(newHeadlines.areEmpty()); */

        Headlines oldHeadlinesFromFile = readFromFile(oldRepoFile);
        assertEquals(headlines, oldHeadlinesFromFile);

        repoFile.delete();
        oldRepoFile.delete();
    }
}
