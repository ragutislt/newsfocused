package eu.adainius.newsfocused.data;

import eu.adainius.newsfocused.headline.Headlines;

public interface NewsRepository {
    Headlines getRunningWeek();
    void saveRunningWeek(Headlines headlines);
}
