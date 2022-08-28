package eu.adainius.newsfocused.site;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.adainius.newsfocused.ApplicationException;
import eu.adainius.newsfocused.headline.BBCParser;
import eu.adainius.newsfocused.headline.HeadlineParser;
import eu.adainius.newsfocused.headline.LRTParser;

public class Sites {
    private List<String> siteNames;
    private List<Site> sites;

    public static final String BBC = "www.bbc.com";
    public static final String LRT = "www.lrt.lt";

    public Sites(List<String> sitesList) {
        sites = new ArrayList<>(sitesList.size());
        siteNames = List.copyOf(sitesList);
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
                parser = new LRTParser();
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
