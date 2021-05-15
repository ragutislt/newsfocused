package eu.adainius.newsfocused;

import java.io.IOException;
import java.util.List;

import eu.adainius.newsfocused.data.FileBasedNewsRepository;
import eu.adainius.newsfocused.data.NewsRepository;
import eu.adainius.newsfocused.email.Email;
import eu.adainius.newsfocused.email.EmailProvider;
import eu.adainius.newsfocused.headline.Headlines;
import eu.adainius.newsfocused.site.Site;
import eu.adainius.newsfocused.site.Sites;
import eu.adainius.newsfocused.util.Today;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static Configuration templateEngineConfiguration;
    private static NewsRepository newsRepository;

    public static void main(String[] args) throws IOException {
        // TODO add validation of args

        String sitesFile = args[0];
        String emailAddress = args[1];
        List<String> daysToSendOn = (args.length > 2 && !args[2].isEmpty()) ? List.of(args[2].split(","))
                : List.of("Saturday");

        if (App.newsRepository == null) {
            String repositoryFile = args[3];
            log.info("Initializing news repository to FileBasedNewsRepository with file at: {}", repositoryFile);
            App.setNewsRepository(new FileBasedNewsRepository(repositoryFile));
        }

        log.info("Sites will be read from: {}", sitesFile);
        log.info("News will be sent to: {}", emailAddress);
        log.info("News will be sent on days: {}", daysToSendOn);

        Sites sites = new Sites(sitesFile);
        log.info("Sites read from {} are: {}", sitesFile, sites.list());

        Headlines headlinesOfTheWeek = newsRepository.getRunningWeek();

        List<Site> siteList = sites.getSites();
        for (Site site : siteList) {
            Headlines headlines = site.headlines();
            headlinesOfTheWeek.add(headlines.getList());
        }

        newsRepository.saveRunningWeek(headlinesOfTheWeek);

        if (Today.isOneOf(daysToSendOn)) {
            Email email = new Email("email_template.ftl", headlinesOfTheWeek, emailAddress);
            EmailProvider.sendEmail(email);
            newsRepository.resetRunningWeek();
        }
    }

    public static void setNewsRepository(NewsRepository newsRepository) {
        App.newsRepository = newsRepository;
    }
}
