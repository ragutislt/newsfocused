package eu.adainius.newsfocused.site;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import eu.adainius.newsfocused.config.HttpClientFactory;
import eu.adainius.newsfocused.headline.Headlines;

import static eu.adainius.newsfocused.test.util.TestUtils.runWithMockedHttpResponses;

public class SitesTest {
    @Test
    public void lists_sites() throws IOException {
        List<String> sitesList = List.of("www.bbc.com", "www.lrt.lt");
        Sites sites = new Sites(sitesList);

        assertEquals(List.of("www.bbc.com", "www.lrt.lt"), sites.list());
    }

    @Test
    public void should_make_each_site_parse_headlines() throws Exception {
        runWithMockedHttpResponses(() -> {
            List<String> sitesList = List.of("www.bbc.com", "www.lrt.lt");
            Sites sites = new Sites(sitesList);

            for (Site site : sites.getSites()) {
                assertTrue(site.headlines().count() > 0);
            }
        });
    }

    @Test
    public void retrieves_headlines() throws Exception {
        String bbcContentLocation = "src/test/resources/bbc.html";
        String bbcContent = Files.readString(Paths.get(bbcContentLocation));

        String lrtContentLocation = "src/test/resources/lrt.html";
        String lrtContent = Files.readString(Paths.get(lrtContentLocation));

        HttpClient mockClient = Mockito.mock(HttpClient.class);
        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.body()).thenReturn(bbcContent).thenReturn(lrtContent);
        Mockito.when(mockClient.send(any(HttpRequest.class), any(BodyHandler.class))).thenReturn(mockResponse);

        // TODO replace with runWithMockedHttpResponses
        try (MockedStatic<HttpClientFactory> mockedHttpClientBuilder = mockStatic(HttpClientFactory.class)) {
            mockedHttpClientBuilder.when(HttpClientFactory::httpClient).thenReturn(mockClient);

            List<String> sitesList = List.of("www.bbc.com", "www.lrt.lt");
            Sites sites = new Sites(sitesList);

            Headlines bbcHeadlines = sites.getSites().get(0).headlines();
            Headlines lrtHeadlines = sites.getSites().get(1).headlines();

            assertEquals(5, bbcHeadlines.count());
            assertEquals(22, lrtHeadlines.count());
        }
        ;
    }
}
