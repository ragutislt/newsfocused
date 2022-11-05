package eu.adainius.newsfocused.admin.site.back.infrastructure.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import eu.adainius.newsfocused.admin.site.back.domain.EmailSent;
import eu.adainius.newsfocused.admin.site.back.domain.User;
import eu.adainius.newsfocused.admin.site.back.domain.User.Preferences;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUserSerializer {

    @Test
    void serializes_all_fields_to_json() throws StreamReadException, DatabindException, IOException {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        om.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

        SimpleModule module = new SimpleModule();
        module.addSerializer(User.class, new UserSerializer());
        module.addDeserializer(User.class, new UserDeserializer());
        om.registerModule(module);

        User user = User
                .builder().email("aaa@bbb.ccc").emailsSent(Set.of(new EmailSent(new Date()))).preferences(Preferences
                        .builder().daysToSendOn(Set.of("Monday")).sites(Set.of("bbc")).headlineCount(7).build())
                .build();
        String json = om.writeValueAsString(user);

        User sameUser = om.readValue(json.getBytes(), User.class);

        assertThat(user).isEqualTo(sameUser);
    }

    @Test
    void serializers_correctly_if_there_are_null_field_values()
            throws StreamReadException, DatabindException, IOException {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        om.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

        SimpleModule module = new SimpleModule();
        module.addSerializer(User.class, new UserSerializer());
        module.addDeserializer(User.class, new UserDeserializer());
        om.registerModule(module);

        User user = User
                .builder().email("aaa@bbb.ccc").preferences(Preferences
                        .builder().daysToSendOn(Set.of("Monday"))
                        .sites(Set.of("BBC")).build())
                .build();
        String json = om.writeValueAsString(user);

        User sameUser = om.readValue(json.getBytes(), User.class);

        assertThat(user).isEqualTo(sameUser);
    }

}
