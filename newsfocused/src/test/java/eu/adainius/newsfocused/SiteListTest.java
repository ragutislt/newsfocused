package eu.adainius.newsfocused;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
