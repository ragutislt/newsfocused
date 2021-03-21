package eu.adainius.newsfocused.site;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.adainius.newsfocused.ApplicationException;
import eu.adainius.newsfocused.headline.BBCParser;
import eu.adainius.newsfocused.headline.HeadlineParser;
import eu.adainius.newsfocused.headline.Headlines;

public class Sites {
    private List<String> siteNames;
    private List<Site> sites;

    private static final String BBC = "www.bbc.com";
    private static final String LRT = "www.lrt.lt";

    public Sites(String sitesFile) {
        sites = new ArrayList<>();

        Path path = Paths.get(sitesFile);
        try {
            siteNames = Files.readAllLines(path);
        } catch (IOException e) {
            throw new ApplicationException("Could not read sites file", e);
        }

        buildSites();
    }

    public List<Site> getSites() {
        return Collections.unmodifiableList(sites);
    }

    public List<String> list() {
        return Collections.unmodifiableList(siteNames);
    }

    private void buildSites() {
        for (String siteName : siteNames) {
            HeadlineParser parser;
            String siteUrl;

            switch (siteName) {
            case BBC:
                parser = new BBCParser();
                siteUrl = BBC;
                break;
            case LRT:
                parser = new BBCParser();
                siteUrl = LRT;
                break;
            default:
                throw new ApplicationException("Site " + siteName + " does not have a parser. Implement one");
            }

            Site site = new Site(parser, siteUrl);
            site.addHeadlines(site.parseHeadlines());
            sites.add(site);
        }
    }
}
