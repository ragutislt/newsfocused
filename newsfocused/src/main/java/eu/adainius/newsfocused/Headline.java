package eu.adainius.newsfocused;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public class Headline {
    private LocalDate date;
    private String title;
    private String website;
    private String htmlLink;
    private String urlLink;

    public Headline() {
    }

    private Headline(LocalDate date, String title, String website, String htmlLink, String urlLink) {
        String nullOrEmptyField = null;

        if (date == null) {
            nullOrEmptyField = "date";
        }
        if (Validation.empty(title)) {
            nullOrEmptyField = "title";
        }
        if (Validation.empty(website)) {
            nullOrEmptyField = "website";
        }
        if (Validation.empty(htmlLink)) {
            nullOrEmptyField = "htmlLink";
        }
        if (Validation.empty(urlLink)) {
            nullOrEmptyField = "urlLink";
        }

        if (nullOrEmptyField != null) {
            throw new ApplicationException("Field " + nullOrEmptyField + " was null or empty");
        }

        this.date = date;
        this.title = title;
        this.website = website;
        this.htmlLink = htmlLink;
        this.urlLink = urlLink;
    }

    public LocalDate date() {
        return date;
    }

    public String title() {
        return title;
    }

    public String website() {
        return website;
    }

    public String htmlLink() {
        return htmlLink;
    }

    public String urlLink() {
        return urlLink;
    }
}
