package eu.adainius.newsfocused.headline;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import eu.adainius.newsfocused.util.Today;

public class LRTParser implements HeadlineParser {
    private static final String LRT_URL = "https://www.lrt.lt";

    private static final String HEADLINE_TAG = "h3";
    private static final String IGNORE_SECTION_NAME = ".news-feed-horizontal";

    @Override
    public List<Headline> parseFrom(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);

        return doc.getElementsByTag(HEADLINE_TAG).stream().filter(h -> h.closest(IGNORE_SECTION_NAME) == null). map(h -> h.html()).filter(h -> isHeadline(h))
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
        String htmlLink = buildHtmlLink(title, urlLink);
        if (htmlLink == null || urlLink == null || title == null) {
            return null;
        }
        return Headline.builder().date(Today.getToday()).website(LRT_URL).title(title).urlLink(urlLink)
                .htmlLink(htmlLink).build();
    }

    private String htmlToTitle(String headlineHtml) {
        Document doc = Jsoup.parse(headlineHtml);
        Element linkElement = doc.selectFirst("a");
        String title = linkElement.text();
        return title;
    }

    private String buildHtmlLink(String title, String urlLink) {
        Element linkElement = new Element("a");
        linkElement.text(title);
        linkElement.attr("href", urlLink);
        linkElement.attr("title", title);
        return linkElement.outerHtml();
    }

    private String htmlToUrl(String headlineHtml) {
        Document doc = Jsoup.parse(headlineHtml);
        Element linkElement = doc.selectFirst("a");
        String url = linkElement.attr("href").startsWith("http") ? linkElement.attr("href")
                : LRT_URL + linkElement.attr("href");
        return url;
    }

}
