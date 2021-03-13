package eu.adainius.newsfocused;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

public class SitesTest {
    @Test
    public void lists_sites() throws IOException {
        String siteFile = "src/main/resources/sites.txt";
        Sites sites = new Sites(siteFile);

        assertEquals(List.of("www.bbc.com", "www.lrt.lt"), sites.list());
    }

    @Test
    public void retrieves_headlines() {
        String siteFile = "src/main/resources/sites.txt";
        Sites sites = new Sites(siteFile);

        Headline bbcHeadline = new Headline();
        Headline lrtHeadline = new Headline();

        Headlines headlines = sites.headlines();

        assertEquals(Headlines.of(bbcHeadline, lrtHeadline), headlines);
    }
}
