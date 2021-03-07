package eu.adainius.newsfocused.e2e;

import org.junit.jupiter.api.Test;
import eu.adainius.newsfocused.App;

public class BBCe2eTest {

    @Test
    public void parses_headlines_from_BBC_and_sends_email() throws Exception {
        String siteFile = "/sites.txt";
        App.main(new String[] {siteFile});
    }
}
