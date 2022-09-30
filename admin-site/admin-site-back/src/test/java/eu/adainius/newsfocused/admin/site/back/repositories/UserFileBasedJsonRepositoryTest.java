package eu.adainius.newsfocused.admin.site.back.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.Test;

import eu.adainius.newsfocused.admin.site.back.domain.User;
import eu.adainius.newsfocused.admin.site.back.domain.User.Preferences;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserFileBasedJsonRepositoryTest {

    @Test
    void testRetrieveAll() {
        // GIVEN
        String testEmail1 = "some@email.com";
        String testEmail2 = "some@email222.com";
        User testUser1 = User.builder().email(testEmail1).preferences(
                Preferences.builder().daysToSendOn(Set.of("Sunday")).headlineCount(4).sites(Set.of("BBC")).build())
                .build();
        User testUser2 = User.builder().email(testEmail2).preferences(
                Preferences.builder().daysToSendOn(Set.of("Wednesday")).headlineCount(48).sites(Set.of("BBC")).build())
                .build();

        File repoFilePath = new File("src/test/java/eu/adainius/resources/userRepository.json");

        UserFileBasedJsonRepository userRepository = new UserFileBasedJsonRepository(repoFilePath.toString());

        // WHEN
        Set<User> existingUsers = userRepository.retrieveAll();

        // THEN
        assertThat(existingUsers).isNotEmpty();
        assertThat(existingUsers.stream().filter(u -> u.email().equals(testEmail1)).findFirst().get())
                .isEqualTo(testUser1);
        assertThat(existingUsers.stream().filter(u -> u.email().equals(testEmail2)).findFirst().get())
                .isEqualTo(testUser2);
    }

    @Test
    void testSave() {

    }

    @Test
    void repo_fails_construction_if_repo_file_does_not_exist() throws IOException {
        // GIVEN
        // WHEN
        // THEN
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> {
                    new UserFileBasedJsonRepository("non-existing-path.json");
                });
    }
}
