package eu.adainius.newsfocused.headline;

import java.util.List;

public interface HeadlineParser {
    public List<Headline> parseFrom(String htmlContent);
}
