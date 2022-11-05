package eu.adainius.newsfocused.admin.site.back.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import eu.adainius.newsfocused.admin.site.back.infrastructure.controller.UserDeserializer;
import eu.adainius.newsfocused.admin.site.back.infrastructure.controller.UserSerializer;
import eu.adainius.newsfocused.admin.site.back.domain.User;
import eu.adainius.newsfocused.admin.site.back.domain.User.Preferences;

public class UserFileBasedJsonRepositoryTest {

        @TempDir
        Path tempRepoFile;

        private static ObjectMapper objectMapper;

        @BeforeAll
        static void initialize() {
                objectMapper = new ObjectMapper();
                objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
                objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

                SimpleModule module = new SimpleModule();
                module.addSerializer(User.class, new UserSerializer());
                module.addDeserializer(User.class, new UserDeserializer());
                objectMapper.registerModule(module);
        }

        private String copyTestDataToTempFile() throws IOException {
                String testData = Files
                                .readString(Paths.get("src/test/java/eu/adainius/resources/userRepository.json"));
                // File tempFile = new File(tempRepoFile.toAbsolutePath() +
                // "tempUserRepo.json");
                File tempFile = tempRepoFile.toFile().createTempFile("temp", null);
                Files.writeString(tempFile.toPath(), testData, Charset.forName("UTF-8"),
                                StandardOpenOption.TRUNCATE_EXISTING);
                return tempFile.toPath().toAbsolutePath().toString();
        }

        @Test
        void testRetrieveAll() {
                // GIVEN
                String testEmail1 = "some@email.com";
                String testEmail2 = "some@email222.com";
                User testUser1 = User.builder().email(testEmail1).preferences(
                                Preferences.builder().daysToSendOn(Set.of("Sunday")).headlineCount(4)
                                                .sites(Set.of("www.bbc.com")).build())
                                .build();
                User testUser2 = User.builder().email(testEmail2).preferences(
                                Preferences.builder().daysToSendOn(Set.of("Wednesday")).headlineCount(48)
                                                .sites(Set.of("www.bbc.com")).build())
                                .build();

                File repoFilePath = new File("src/test/java/eu/adainius/resources/userRepository.json");

                UserFileBasedJsonRepository userRepository = new UserFileBasedJsonRepository(repoFilePath.toString(),
                                objectMapper);

                // WHEN
                Set<User> existingUsers = userRepository.retrieveAll();

                // THEN
                assertThat(existingUsers).isNotEmpty();
                assertThat(existingUsers.stream().filter(u -> u.email().equals(testEmail1)).findFirst().get())
                                .isEqualTo(testUser1);
                assertThat(existingUsers.stream().filter(u -> u.email().equals(testEmail1)).findFirst().get())
                                .extracting("email", "preferences")
                                .contains(testUser1.email(), testUser1.preferences());
                assertThat(existingUsers.stream().filter(u -> u.email().equals(testEmail2)).findFirst().get())
                                .extracting("email", "preferences")
                                .contains(testUser2.email(), testUser2.preferences());
        }

        @Test
        void testSave_a_new_user() throws IOException {
                // GIVEN
                String tempFile = copyTestDataToTempFile();
                String testEmail1 = "some@email.com";
                String testEmail2 = "some@email222.com";
                String testEmail3 = "some@email333.com";
                UserFileBasedJsonRepository userRepository = new UserFileBasedJsonRepository(tempFile, objectMapper);
                User testUser1 = User.builder().email(testEmail1).preferences(
                                Preferences.builder().daysToSendOn(Set.of("Sunday")).headlineCount(4)
                                                .sites(Set.of("www.bbc.com")).build())
                                .build();
                User testUser2 = User.builder().email(testEmail2).preferences(
                                Preferences.builder().daysToSendOn(Set.of("Wednesday")).headlineCount(48)
                                                .sites(Set.of("www.bbc.com")).build())
                                .build();
                User testUser3 = User.builder().email(testEmail3).preferences(
                                Preferences.builder().daysToSendOn(Set.of("Tuesday")).headlineCount(52)
                                                .sites(Set.of("www.bbc.com")).build())
                                .build();

                // WHEN
                userRepository.save(testUser3);

                // THEN
                Set<User> existingUsers = userRepository.retrieveAll();
                assertThat(existingUsers.stream().filter(u -> u.email().equals(testEmail1)).findFirst().get())
                                .isEqualTo(testUser1);
                assertThat(existingUsers.stream().filter(u -> u.email().equals(testEmail2)).findFirst().get())
                                .isEqualTo(testUser2);
                assertThat(existingUsers.stream().filter(u -> u.email().equals(testEmail3)).findFirst().get())
                                .extracting("email", "preferences")
                                .contains(testUser3.email(), testUser3.preferences());
        }

        @Test
        void testSave_an_existing_user() throws IOException {
                // GIVEN
                String tempFile = copyTestDataToTempFile();
                String testEmail1 = "some@email.com";
                String testEmail2 = "some@email222.com";
                UserFileBasedJsonRepository userRepository = new UserFileBasedJsonRepository(tempFile, objectMapper);
                User testUser1 = User.builder().email(testEmail1).preferences(
                                Preferences.builder().daysToSendOn(Set.of("Sunday")).headlineCount(4)
                                                .sites(Set.of("www.bbc.com")).build())
                                .build();
                User testUser2 = User.builder().email(testEmail2).preferences(
                                Preferences.builder().daysToSendOn(Set.of("Wednesday", "Monday", "Thursday"))
                                                .headlineCount(111).sites(Set.of("SOME RANDOM SITE")).build())
                                .build();

                // WHEN
                userRepository.save(testUser2);

                // THEN
                Set<User> existingUsers = userRepository.retrieveAll();
                assertThat(existingUsers.stream().filter(u -> u.email().equals(testEmail1)).findFirst().get())
                                .isEqualTo(testUser1);
                User persistedUser2 = existingUsers.stream().filter(u -> u.email().equals(testEmail2)).findFirst()
                                .get();
                assertThat(persistedUser2)
                                .extracting("email", "preferences")
                                .contains(testUser2.email(), testUser2.preferences());
        }

        @Test
        void repo_fails_construction_if_repo_file_does_not_exist() throws IOException {
                // GIVEN
                // WHEN
                // THEN
                assertThatExceptionOfType(RuntimeException.class)
                                .isThrownBy(() -> {
                                        new UserFileBasedJsonRepository("non-existing-path.json", objectMapper);
                                });
        }
}
