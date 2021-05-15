package eu.adainius.newsfocused.config;

import java.net.http.HttpClient;

public class HttpClientFactory {

    public static HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }

}
