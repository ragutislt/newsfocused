package eu.adainius.newsfocused;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class AppTest {

    @Test
    public void parses_headlines_and_saves_them_to_file() throws Exception {
        runWithMockedHttpResponses(() -> {
            String fileResults = "target/results.txt";
            String fileSites = "src/test/resources/sites.txt";

            App.main(new String[] { fileSites, fileResults });

            String fileContent = Files.readString(Paths.get(fileResults));

            assertTrue(fileContent.contains(
                    "Profesorius Janeliūnas: vertybiniai klausimai ateityje gali sukelti didelius nesutarimus koalicijoje"));
            assertTrue(fileContent.contains("Bloody day in Myanmar's main city sees 14 killed"));
        });
    }

    @Test
    public void saves_headlines_to_html_file() throws IOException {
        String fileResults = "target/results.txt";
        String fileSites = "src/test/resources/sites.txt";

        App.main(new String[] { fileSites, fileResults });

        String fileContent = Files.readString(Paths.get(fileResults));

        Document doc = Jsoup.parse(fileContent);
        assertNotNull(doc);
    }

    @Test
    public void main_accepts_email() {

    }

    @Test
    public void main_accepts_sites_file() {

    }

    private interface RunnableWithExceptions {
        void run() throws Exception;
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
        ;
    }
}
