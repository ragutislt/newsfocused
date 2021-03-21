package eu.adainius.newsfocused.headline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Headlines {
    List<Headline> headlinesList = new ArrayList<>();

    private Headlines(List<Headline> headlinesList) {
        this.headlinesList.addAll(headlinesList);
    }

    public Headlines() {
    }

    public static Headlines of(Headline... headlines) {
        return new Headlines(List.of(headlines));
    }

    public static Headlines of(List<Headline> headlinesList) {
        return new Headlines(headlinesList);
    }

    public List<Headline> getList() {
        return Collections.unmodifiableList(headlinesList);
    }

    public void add(List<Headline> headlinesToAdd) {
        this.headlinesList.addAll(headlinesToAdd);
    }

    public List<Headline> from(String headlineDate) {
        var headlinesFromDate = new ArrayList<Headline>();
        for (Headline headline : headlinesList) {
            if(headlineDate.equals(headline.date().toString())) {
                headlinesFromDate.add(headline);
            }
        }
        return Collections.unmodifiableList(headlinesFromDate);
    }

    public int count() {
        return headlinesList.size();
    }

}
