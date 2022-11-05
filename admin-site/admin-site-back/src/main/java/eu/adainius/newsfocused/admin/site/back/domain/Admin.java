package eu.adainius.newsfocused.admin.site.back.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.vavr.control.Either;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@Builder
// TODO implement a delete
public class Admin {
    private String username;

    public Either<String, User> registerNewUser(String email, Set<String> daysToSendOn, Set<String> sites,
            int headlineCount) {
        try {
            return Either.right(
                    User.builder().email(email)
                            .preferences(
                                    User.Preferences.builder().daysToSendOn(daysToSendOn).sites(sites)
                                            .headlineCount(headlineCount).build())
                            .build());
        } catch (IllegalArgumentException e) {
            return Either.left(e.getMessage());
        }
    }

    public Either<String, User> updateUser(String email, Set<String> daysToSendOn, Set<String> sites,
            int headlineCount) {
        return registerNewUser(email, daysToSendOn, sites, headlineCount);
    }

    public UserSearchResults searchForUser(String email, Set<User> allUsers, int pageSize, int pageRequested) {
        Set<User> searchResults = allUsers.stream()
                .filter(user -> user.email().contains(email)).collect(Collectors.toSet());

        int index = 1;
        int indexOfPageStart = pageSize * pageRequested - pageSize + 1;
        int indexOfPageEnd = indexOfPageStart + pageSize - 1;

        Set<User> pageToReturn = new HashSet<>(pageSize);

        Iterator<User> resultsIterator = searchResults.iterator();
        while (resultsIterator.hasNext()) {
            User currentUser = resultsIterator.next();
            if (index < indexOfPageStart) {
                index++;
                continue;
            }
            if (index > indexOfPageEnd) {
                index++;
                break;
            }
            index++;
            pageToReturn.add(currentUser);
        }

        return UserSearchResults.of(Collections.unmodifiableSet(pageToReturn), pageRequested, searchResults.size());
    }

    public Optional<User> openUserDetails(String email, Set<User> allUsers) {
        return allUsers.stream().filter(user -> email.equals(user.email())).findFirst();
    }
}
