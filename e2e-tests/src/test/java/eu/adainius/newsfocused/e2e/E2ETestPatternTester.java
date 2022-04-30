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
public class E2ETestPatternTester {
    private static String buildMailcatcherDocker = "docker build --rm -t news_focused/mailcatcher ./src/test/resources/mailServer";
    private static String runMailcatcherDocker = "docker run --rm --name=news_focused-mailcatcher -d --publish=1080:1080 --publish=10025:10025 news_focused/mailcatcher";
    private static String stopMailcatcherDocker = "docker stop news_focused-mailcatcher";

    @BeforeAll
    public static void runMailServer() throws IOException, InterruptedException {

    }

    private static void logProcess(Process p) throws IOException {
        log.info("Process logs: {}", new String(p.getInputStream().readAllBytes()));
        log.info("Process error logs: {}", new String(p.getErrorStream().readAllBytes()));
    }

    @AfterAll
    public static void stopMailServer() throws IOException, InterruptedException {

    }

    @Test
    public void parses_headlines_from_BBC_and_sends_email(@TempDir Path tempDir) throws Exception {
        // GIVEN


        // WHEN
        


        // THEN


    }

}
