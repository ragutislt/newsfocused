package eu.adainius.newsfocused.admin.site.back.domain;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.vavr.control.Either;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Admin {
    private String username;

    public Either<String, User> registerNewUser(String email, Set<String> daysToSendOn, Set<String> sites,
            int headlineCount) {
        try {
            return Either.right(
                    new User(email, User.Preferences.builder().daysToSendOn(daysToSendOn).sites(sites)
                            .headlineCount(headlineCount).build()));
        } catch (IllegalArgumentException e) {
            return Either.left(e.getMessage());
        }
    }

    public Either<String, User> updateUser(String email, Set<String> daysToSendOn, Set<String> sites,
    int headlineCount) {
        return registerNewUser(email, daysToSendOn, sites, headlineCount);
    }

    public List<User> searchForUser(String email, Set<User> allUsers) {
        // performs search against allUsers

        // then returns the n results based on the pagination settings
        // also returns the total count
        return null;
    }

    public Optional<User> openUserDetails(String email, Set<User> allUsers) {
        return allUsers.stream().filter(user -> email.equals(user.email())).findFirst();
    }
}
