package eu.adainius.newsfocused;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import eu.adainius.newsfocused.email.Email;
import eu.adainius.newsfocused.email.EmailProvider;
import eu.adainius.newsfocused.headline.Headlines;
import eu.adainius.newsfocused.site.Site;
import eu.adainius.newsfocused.site.Sites;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static Configuration templateEngineConfiguration;
    // TODO limit headlines of each site

    public static void main(String[] args) throws IOException {
        String sitesFile = args[0];
        String emailAddress = args[1];
        log.info("Sites will be read from: {}", sitesFile);
        log.info("News will be sent to: {}", emailAddress);

        Sites sites = new Sites(sitesFile);
        log.info("Sites read from {} are: {}", sitesFile, sites.list());

        File file = new File(emailAddress);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        Headlines aggregatedHeadlines = new Headlines();

        List<Site> siteList = sites.getSites();
        for (Site site : siteList) {
            Headlines headlines = site.headlines();
            aggregatedHeadlines.add(headlines.getList());
        }

        Email email = new Email("email_template.ftl", aggregatedHeadlines, emailAddress);

        EmailProvider.sendEmail(email);
    }
}
