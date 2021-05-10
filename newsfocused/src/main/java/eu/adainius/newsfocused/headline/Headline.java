package eu.adainius.newsfocused.headline;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import eu.adainius.newsfocused.ApplicationException;
import eu.adainius.newsfocused.Validation;
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
        if (!website.startsWith("http")) {
            this.website = "https://" + website;
        } else {
            this.website = website;
        }
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

    @Override
    public String toString() {
        return String.format("Headline [title=%s, date=%s, site=%s]", title,
                date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), website);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((htmlLink == null) ? 0 : htmlLink.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((urlLink == null) ? 0 : urlLink.hashCode());
        result = prime * result + ((website == null) ? 0 : website.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Headline other = (Headline) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (htmlLink == null) {
            if (other.htmlLink != null)
                return false;
        } else if (!htmlLink.equals(other.htmlLink))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (urlLink == null) {
            if (other.urlLink != null)
                return false;
        } else if (!urlLink.equals(other.urlLink))
            return false;
        if (website == null) {
            if (other.website != null)
                return false;
        } else if (!website.equals(other.website))
            return false;
        return true;
    }
}
