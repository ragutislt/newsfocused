package eu.adainius.newsfocused.headline;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import eu.adainius.newsfocused.util.Today;

public class BBCParser implements HeadlineParser {
    private static final String BBC_URL = "www.bbc.com";

    private static final String HEADLINE_TAG = "h3";

    @Override
    public List<Headline> parseFrom(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);

        return doc.getElementsByTag(HEADLINE_TAG).stream().map(h -> h.html()).filter(h -> isHeadline(h))
                .map(h -> htmlToHeadline(h)).collect(Collectors.toList());
    }

    private boolean isHeadline(String headlineHtml) {
        Document doc = Jsoup.parse(headlineHtml);
        Element linkElement = doc.selectFirst("a");
        if (linkElement == null) {
            return false;
        }
        return true;
    }

    private Headline htmlToHeadline(String headlineHtml) {
        String urlLink = htmlToUrl(headlineHtml);
        String title = htmlToTitle(headlineHtml);
        String htmlLink = htmlToHtmlLink(headlineHtml);
        if (htmlLink == null || urlLink == null || title == null) {
            return null;
        }
        return Headline.builder().date(Today.getToday()).website(BBC_URL).title(title).urlLink(urlLink)
                .htmlLink(htmlLink).build();
    }

    private String htmlToTitle(String headlineHtml) {
        Document doc = Jsoup.parse(headlineHtml);
        Element linkElement = doc.selectFirst("a");
        String title = linkElement.text();
        return title;
    }

    private String htmlToHtmlLink(String headlineHtml) {
        Document doc = Jsoup.parse(headlineHtml);
        Element linkElement = doc.selectFirst("a");
        if (linkElement == null) {
            return null;
        }
        replaceRelativeHrefWithAbsoluteInHtmlHeadline(linkElement);
        return linkElement.outerHtml();
    }

    private String htmlToUrl(String headlineHtml) {
        Document doc = Jsoup.parse(headlineHtml);
        Element linkElement = doc.selectFirst("a");
        String url = getHrefFromHtmlHeadline(linkElement);
        return url;
    }

    private String getHrefFromHtmlHeadline(Element linkElement) {
        return linkElement.attr("href").startsWith("http") ? linkElement.attr("href")
                : "http://" + BBC_URL + linkElement.attr("href");
    }

    private void replaceRelativeHrefWithAbsoluteInHtmlHeadline(Element linkElement) {
        String url = getHrefFromHtmlHeadline(linkElement);
        linkElement.attr("href", url);
    }
}
