package eu.adainius.newsfocused.headline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Headlines {
    private List<Headline> headlinesList = new ArrayList<>();

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

    public boolean areEmpty() {
        return headlinesList.size() == 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((headlinesList == null) ? 0 : headlinesList.hashCode());
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
        Headlines other = (Headlines) obj;
        if (headlinesList == null) {
            if (other.headlinesList != null)
                return false;
        } else if (!headlinesList.equals(other.headlinesList))
            return false;
        return true;
    }

}
