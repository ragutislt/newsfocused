package eu.adainius.newsfocused.site;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import eu.adainius.newsfocused.ApplicationException;
import eu.adainius.newsfocused.config.HttpClientFactory;
import eu.adainius.newsfocused.headline.Headline;
import eu.adainius.newsfocused.headline.HeadlineParser;
import eu.adainius.newsfocused.headline.Headlines;

public class Site {
	private String siteUrl;
	private HttpClient httpClient;
	private HeadlineParser parser;
	private Headlines headlines;

	public Site(HeadlineParser parser, String siteUrl) {
		this.parser = parser;
		this.httpClient = HttpClientFactory.httpClient();
		if (!siteUrl.startsWith("http")) {
			this.siteUrl = "https://" + siteUrl;
		} else {
			this.siteUrl = siteUrl;
		}
		this.headlines = new Headlines();
	}

	public List<Headline> parseHeadlines() {
		String htmlContent = "";
		try {
			htmlContent = read();
		} catch (Exception e) {
			throw new ApplicationException("Error when reading site: " + siteUrl, e);
		}

		List<Headline> headlines = parser.parseFrom(htmlContent);
		return headlines;
	}

	private String read() throws Exception {
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

	public void addHeadlines(List<Headline> headlinesToAdd) {
		headlines.add(headlinesToAdd);
	}

    public Headlines headlines() {
        return headlines;
    }
}
