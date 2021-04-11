package eu.adainius.newsfocused;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import eu.adainius.newsfocused.headline.Headline;
import eu.adainius.newsfocused.headline.Headlines;
import eu.adainius.newsfocused.site.Site;
import eu.adainius.newsfocused.site.Sites;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(String[] args) throws IOException {
        String sitesFile = args[0];
        String email = args[1];
        log.info("Sites will be read from: {}", sitesFile);
        log.info("News will be sent to: {}", email);

        Sites sites = new Sites(sitesFile);
        log.info("Sites read from {} are: {}", sitesFile, sites.list());
        
        File file = new File(email);
        file.createNewFile();

        List<Site> siteList = sites.getSites();
        for(Site site : siteList) {
            Headlines headlines = site.headlines();
            for(Headline headline: headlines.getList()) {
                Files.write(
                    Paths.get(email), 
                    headline.htmlLink().getBytes(), 
                    StandardOpenOption.APPEND);
            }
        }
    }
}
