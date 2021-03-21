package eu.adainius.newsfocused.site;

import static org.junit.jupiter.api.Assertions.fail;

import java.net.http.HttpClient;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

public class SiteReadTest {
    @Test
    public void reads_site_data() throws Exception {
        Site site = new Site("www.bbc.com", HttpClient.newHttpClient());

        String siteHtml = site.read();

        try {
            Jsoup.parse(siteHtml);
        } catch (Exception e) {
            fail("Parsing a valid page should succeed");
        }
    }
}
