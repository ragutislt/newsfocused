package eu.adainius.newsfocused.e2e;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import eu.adainius.newsfocused.App;
import eu.adainius.newsfocused.user.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class E2ETestBatch {
    private static String buildMailcatcherDocker = "docker build --rm -t news_focused/mailcatcher ./src/test/resources/mailServer";
    private static String runMailcatcherDocker = "docker run --rm --name=news_focused-mailcatcher -d --publish=1080:1080 --publish=10025:10025 news_focused/mailcatcher";
    private static String stopMailcatcherDocker = "docker stop news_focused-mailcatcher";

    @BeforeAll
    public static void runMailServer() throws IOException, InterruptedException {
        stopMailServer();
        Process p;
        Runtime rt = Runtime.getRuntime();
        p = rt.exec(buildMailcatcherDocker);
        p.waitFor();
        logProcess(p);
        p = Runtime.getRuntime().exec(runMailcatcherDocker);
        p.waitFor();
        logProcess(p);
        log.info("Started mail server");
    }

    private static void logProcess(Process p) throws IOException {
        log.info("Process logs: {}", new String(p.getInputStream().readAllBytes()));
        log.info("Process error logs: {}", new String(p.getErrorStream().readAllBytes()));
    }

    @AfterAll
    public static void stopMailServer() throws IOException, InterruptedException {
        log.info("Stopping mail server");
        Process p = Runtime.getRuntime().exec(stopMailcatcherDocker);
        p.waitFor();
        logProcess(p);
    }

    @Test
    public void parses_headlines_from_BBC_and_sends_email(@TempDir Path tempDir) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        String mailServerUrl = "http://127.0.0.1:1080/";

        String usersFile = "src/test/resources/users.json";
        String repoLocation = createRepoLocation(tempDir);

        App.main(new String[] { usersFile, repoLocation });

        assertEmailWasSent(httpClient, mailServerUrl);
    }

    private void assertEmailWasSent(HttpClient httpClient, String mailServerUrl)
            throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(URI.create(mailServerUrl + "messages"))
                .header("accept", "application/json").build();

        // use the client to send the request
        var response = httpClient.send(request, BodyHandlers.ofString());

        JsonArray messageArray = new Gson().fromJson(response.body(), JsonArray.class);

        assertTrue(messageArray.size() > 0);
    }

    private String createRepoLocation(Path tempDir) throws IOException {
        return Files.createDirectories(tempDir.resolve("repo")).toString();
    }

}
