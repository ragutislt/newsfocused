package eu.adainius.newsfocused;

import java.net.http.HttpClient;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class BBCSite {
    private static final String BBC_URL = "www.bbc.com";
    private static final String HEADLINE_TAG = "h3";
    private String siteHtmlContent;
    private final Site site;

    public BBCSite(HttpClient httpClient) {
        this.site = new Site(BBC_URL, httpClient);
    }

    public List<String> headlines() {
        if (siteHtmlContent == null) {
            try {
                siteHtmlContent = site.read();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        Document document = Jsoup.parse(siteHtmlContent);
        return document.getElementsByTag(HEADLINE_TAG).stream().map(h -> h.html()).collect(Collectors.toList());
    }

}
