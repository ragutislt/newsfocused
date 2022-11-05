package eu.adainius.newsfocused.admin.site.back.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.adainius.newsfocused.admin.site.back.infrastructure.controller.UserDeserializer;
import eu.adainius.newsfocused.admin.site.back.infrastructure.controller.UserSerializer;
import eu.adainius.newsfocused.admin.site.back.domain.User;
import eu.adainius.newsfocused.admin.site.back.repositories.AdminFromPropertiesRepository;
import eu.adainius.newsfocused.admin.site.back.repositories.UserFileBasedJsonRepository;

@Configuration
public class SpringBeans {

    @Bean("applicationProperties")
    public Properties applicationProperties() throws IOException {
        Properties applicationProperties = new Properties();
        applicationProperties.load(SpringBeans.class.getClassLoader().getResourceAsStream("application.properties"));
        return applicationProperties;
    }

    @Bean
    public AdminFromPropertiesRepository adminRepository(
            @Autowired @Qualifier("applicationProperties") Properties applicationProperties) throws IOException {
        return new AdminFromPropertiesRepository(applicationProperties);
    }

    @Bean
    public UserFileBasedJsonRepository userRepository(@Autowired ObjectMapper objectMapper) throws IOException {
        Properties applicationProperties = new Properties();
        applicationProperties.load(SpringBeans.class.getClassLoader().getResourceAsStream("application.properties"));
        return new UserFileBasedJsonRepository(applicationProperties.getProperty("userRepoPath"), objectMapper);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .visibility(PropertyAccessor.ALL, Visibility.NONE)
                .visibility(PropertyAccessor.FIELD, Visibility.ANY)
                .deserializerByType(User.class, new UserDeserializer())
                .serializerByType(User.class, new UserSerializer())
                .failOnEmptyBeans(true);
    }
}
