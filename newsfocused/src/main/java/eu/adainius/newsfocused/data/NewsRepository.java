package eu.adainius.newsfocused.data;

import eu.adainius.newsfocused.headline.Headlines;

public interface NewsRepository {
    // TODO make the repo have one instance per application, not one instance per user

    Headlines getRunningWeek();
    void saveRunningWeek(Headlines headlines);
    void resetRunningWeek();
}
