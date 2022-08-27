package eu.adainius.newsfocused.admin.site.back.domain;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Admin {
    private String username;

    public User registerNewUser(String email, List<String> daysToSendOn, String[] sites, int headlineCount) {
        // build user from params
        // raiseEvent new user

        return null;
    }

    public void updateUser(User modifiedUser) {
        // build user from params
        // raiseEvent modified user
    }

    public List<User> searchForUser(String email, List<User> allUsers) {
        // performs search against allUsers
        
        // then returns the n results based on the pagination settings
        // also returns the total count
        return null;
    }

    public User openUserDetails(String email, List<User> allUsers) {
        // just find the right user and return it
        return null;
    }
}
