package eu.adainius.newsfocused.site;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import eu.adainius.newsfocused.HttpClientFactory;
import eu.adainius.newsfocused.headline.Headlines;

public class SitesTest {
    @Test
    public void lists_sites() throws IOException {
        String siteFile = "src/main/resources/sites.txt";
        Sites sites = new Sites(siteFile);

        assertEquals(List.of("www.bbc.com", "www.lrt.lt"), sites.list());
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

        try(MockedStatic<HttpClientFactory> mockedHttpClientBuilder = mockStatic(HttpClientFactory.class)) {
            mockedHttpClientBuilder.when(HttpClientFactory::httpClient).thenReturn(mockClient);

            String siteFile = "src/main/resources/sites.txt";
            Sites sites = new Sites(siteFile);

            Headlines bbcHeadlines = sites.getSites().get(0).headlines();
            Headlines lrtHeadlines = sites.getSites().get(1).headlines();
    
            assertEquals(5, bbcHeadlines.count());
            assertEquals(22, lrtHeadlines.count());
        };
    }
}
