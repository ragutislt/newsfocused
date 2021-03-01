package eu.adainius.newsfocused;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class Site {
	private String siteUrl;
	private HttpClient httpClient;

	public Site(String siteUrl, HttpClient httpClient) {
		this.httpClient = httpClient;
		if (!siteUrl.startsWith("http")) {
			this.siteUrl = "https://" + siteUrl;
		} else {
			this.siteUrl = siteUrl;
		}
	}

	public String read() throws Exception {
		var response = retrieveSiteContent();

		String htmlResponse = response.body();

		return htmlResponse;
	}

	private HttpResponse<String> retrieveSiteContent() throws IOException, InterruptedException {
		// create a request
		var request = HttpRequest.newBuilder(URI.create(siteUrl)).header("accept", "text/html").build();

		// use the client to send the request
		var response = httpClient.send(request, BodyHandlers.ofString());
		return response;
	}
}
