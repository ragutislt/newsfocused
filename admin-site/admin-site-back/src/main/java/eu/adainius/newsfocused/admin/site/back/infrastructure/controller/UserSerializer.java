package eu.adainius.newsfocused.admin.site.back.infrastructure.controller;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import eu.adainius.newsfocused.admin.site.back.domain.EmailSent;
import eu.adainius.newsfocused.admin.site.back.domain.User;

public class UserSerializer extends StdSerializer<User> {

    public UserSerializer() {
        this(null);
    }

    public UserSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(
            User user, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeStringField("email", user.email());

        jgen.writeNumberField("registrationDate", user.registrationDate().getTime());

        jgen.writeFieldName("emailsSent");
        jgen.writeStartArray();
        for (EmailSent value : user.emailsSent()) {
            jgen.writeStartObject();
            jgen.writeNumberField("sentOn", value.sentOn().getTime());
            jgen.writeEndObject();
        }
        jgen.writeEndArray();

        jgen.writeFieldName("preferences");
        jgen.writeStartObject();

        jgen.writeNumberField("headlineCount", user.preferences().headlineCount());

        jgen.writeFieldName("sites");
        jgen.writeStartArray();
        for (String value : user.preferences().sites()) {
            jgen.writeString(value);
        }
        jgen.writeEndArray();

        jgen.writeFieldName("daysToSendOn");
        jgen.writeStartArray();
        for (String value : user.preferences().daysToSendOn()) {
            jgen.writeString(value);
        }
        jgen.writeEndArray();

        jgen.writeEndObject();

        jgen.writeEndObject();
    }
}
