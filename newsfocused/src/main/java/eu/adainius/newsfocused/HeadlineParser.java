package eu.adainius.newsfocused;

import java.util.List;

public interface HeadlineParser {
    public List<Headline> parseFrom(String htmlContent);
}
