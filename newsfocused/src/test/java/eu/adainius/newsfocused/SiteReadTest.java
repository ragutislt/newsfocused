package eu.adainius.newsfocused;

import static org.junit.jupiter.api.Assertions.fail;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

public class SiteReadTest {
    @Test
    public void reads_site_data() throws Exception {
        Site site = new Site("www.bbc.com");

        String siteHtml = site.read();

        try {
            Jsoup.parse(siteHtml);
        } catch (Exception e) {
            fail("Parsing a valid page should succeed");
        }
    }
}
