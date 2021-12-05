package eu.adainius.newsfocused.data;

import eu.adainius.newsfocused.headline.Headlines;

public interface NewsRepository {
    Headlines getRunningWeekFor(String email);
    void saveRunningWeekFor(Headlines headlines, String email);
    void resetRunningWeekFor(String email);
}
