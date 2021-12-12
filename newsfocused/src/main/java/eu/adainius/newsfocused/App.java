package eu.adainius.newsfocused;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.google.gson.Gson;

import eu.adainius.newsfocused.config.EmailConfiguration;
import eu.adainius.newsfocused.data.FileBasedNewsRepository;
import eu.adainius.newsfocused.data.NewsRepository;
import eu.adainius.newsfocused.email.Email;
import eu.adainius.newsfocused.email.EmailProvider;
import eu.adainius.newsfocused.headline.Headlines;
import eu.adainius.newsfocused.site.Site;
import eu.adainius.newsfocused.site.Sites;
import eu.adainius.newsfocused.user.User;
import eu.adainius.newsfocused.util.Today;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static Configuration templateEngineConfiguration;
    private static NewsRepository newsRepository;

    public static void main(String[] args) throws IOException {
        // TODO add validation of args

        String usersLocation = args[0];

        List<User> users = User.parseFromUsing(usersLocation, new Gson());
        log.info("All users' properties have been read from {}", usersLocation);

        initializeRepository(args);

        setEmailProperties(args);

        runFor(users);
    }

    private static void runFor(List<User> users) {
        log.info("Going to process {} users", users.size());

        for (User user : users) {
            Sites sites = new Sites(user.sites()); // TODO adapt sites
            log.info("Sites for user {} are: {}", user.email(), user.sites());

            Headlines headlinesOfTheWeek = newsRepository.getRunningWeekFor(user.email());

            List<Site> siteList = sites.getSites();
            for (Site site : siteList) {
                Headlines headlines = site.headlines();
                headlinesOfTheWeek.add(headlines.getList());
            }

            newsRepository.saveRunningWeekFor(headlinesOfTheWeek, user.email());

            if (Today.isOneOf(user.daysToSendOn())) {
                Email email = new Email("email_template.ftl", headlinesOfTheWeek, user.email());
                EmailProvider.sendEmail(email);
                newsRepository.resetRunningWeekFor(user.email());
            } else {
                log.info("Today is not one of {}, email will not be sent to {}", user.daysToSendOn(), user.email());
            }
        }
    }

    private static void setEmailProperties(String[] args) throws IOException, FileNotFoundException {
        if (args.length > 2 && args[2] != null) {
            String emailProtocolPropertiesFile = args[2];
            Properties emailProtocolProperties = new Properties();
            emailProtocolProperties.load(new FileReader(emailProtocolPropertiesFile));

            EmailConfiguration.setEmailProtocolProperties(emailProtocolProperties);
            log.info("Email protocol properties will be used from file {}, they are: {}", emailProtocolPropertiesFile,
                    emailProtocolProperties);
        }
    }

    private static void initializeRepository(String[] args) {
        if (App.newsRepository == null) {
            String repositoryFolder = args[1];
            log.info("Initializing news repository to FileBasedNewsRepository with folder at: {}", repositoryFolder);
            App.setNewsRepository(new FileBasedNewsRepository(repositoryFolder));
        }
    }

    public static void setNewsRepository(NewsRepository newsRepository) {
        App.newsRepository = newsRepository;
    }
}
