package eu.adainius.newsfocused;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BBCSiteTest {
    @Mock
    HttpClient mockHttpClient;

    @ParameterizedTest
    @ValueSource(strings = { "<h3>This is a headline</h3>", "<h3><span>This is also a headline</span></h3>",
            "<h3><a href=\"\">This is a headline</a></h3>" })
    public void parses_news_headlines(String headline) throws IOException, InterruptedException {

        var mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.body()).thenReturn(headline);
        Mockito.when(mockHttpClient.send(any(), any())).thenReturn(mockResponse);

        var site = new BBCSite(mockHttpClient);
        List<String> headlines = site.headlines();

        assertEquals(List.of(headline.replace("<h3>","").replace("</h3>", "")), headlines);
    }
}
