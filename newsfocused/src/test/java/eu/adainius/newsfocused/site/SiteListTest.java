package eu.adainius.newsfocused.site;

import static eu.adainius.newsfocused.test.util.TestUtils.runWithMockedHttpResponses;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

public class SiteListTest {
    @Test
    public void should_get_list_from_file() throws IOException {
        String sitesFile = "src/test/resources/sites.txt";
        Sites sites = new Sites(sitesFile);
        assertEquals(List.of("www.bbc.com", "www.lrt.lt"), sites.list());
    }

    @Test
    public void should_make_each_site_parse_headlines() throws Exception {
        runWithMockedHttpResponses(() -> {
            String sitesFile = "src/test/resources/sites.txt";
            Sites sites = new Sites(sitesFile);

            for (Site site : sites.getSites()) {
                assertTrue(site.headlines().count() > 0);
            }
        });
    }
}
