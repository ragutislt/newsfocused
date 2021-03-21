package eu.adainius.newsfocused;

import java.net.http.HttpClient;

public class HttpClientFactory {

    public static HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }

}
