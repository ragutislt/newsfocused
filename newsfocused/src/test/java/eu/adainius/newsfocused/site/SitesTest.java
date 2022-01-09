package eu.adainius.newsfocused.site;

import static eu.adainius.newsfocused.test.util.TestUtils.runWithMockedHttpResponses;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import eu.adainius.newsfocused.headline.Headlines;

public class SitesTest {
    @Test
    public void lists_sites() throws IOException {
        List<String> sitesList = List.of("www.bbc.com", "www.lrt.lt");
        Sites sites = new Sites(sitesList);

        assertEquals(List.of("www.bbc.com", "www.lrt.lt"), sites.list());
    }

    @Test
    public void should_make_each_site_parse_headlines() throws Exception {
        runWithMockedHttpResponses(() -> {
            List<String> sitesList = List.of("www.bbc.com", "www.lrt.lt");
            Sites sites = new Sites(sitesList);

            for (Site site : sites.getSites()) {
                assertTrue(site.headlines().count() > 0);
            }
        });
    }

    @Test
    public void retrieves_headlines() throws Exception {
        runWithMockedHttpResponses(() -> {
            List<String> sitesList = List.of("www.bbc.com", "www.lrt.lt");
            Sites sites = new Sites(sitesList);

            Headlines bbcHeadlines = sites.getSites().get(0).headlines();
            Headlines lrtHeadlines = sites.getSites().get(1).headlines();

            assertEquals(5, bbcHeadlines.count());
            assertEquals(16, lrtHeadlines.count());
        });
    }
}
