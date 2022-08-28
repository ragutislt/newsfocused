package eu.adainius.newsfocused.admin.site.back.repositories;

import eu.adainius.newsfocused.admin.site.back.domain.User;

public interface UserRepository {

    void save(User registeredUser);

}
