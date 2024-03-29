package eu.adainius.newsfocused.admin.site.back.domain;

import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@ToString
@Accessors(fluent = true)
@EqualsAndHashCode
public final class UserSearchResults {
    private Set<User> usersFound;
    private int pageNumber;
    private int totalCount;

    public UserSearchResults() {
    }

    public static UserSearchResults of(Set<User> usersFound, int pageNumber, int totalCount) {
        UserSearchResults results = new UserSearchResults();
        results.usersFound = usersFound;
        results.pageNumber = pageNumber;
        results.totalCount = totalCount;
        return results;
    }
}
