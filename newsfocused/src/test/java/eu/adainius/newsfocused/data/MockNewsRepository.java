package eu.adainius.newsfocused.data;

import eu.adainius.newsfocused.headline.Headlines;

public class MockNewsRepository implements NewsRepository {

    @Override
    public Headlines getRunningWeek() {
        return null;
    }

    @Override
    public void saveRunningWeek(Headlines headlines) {
    }
}
