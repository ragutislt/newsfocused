package eu.adainius.newsfocused.admin.site.back.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import eu.adainius.newsfocused.admin.site.back.domain.EmailSent;
import eu.adainius.newsfocused.admin.site.back.domain.User;
import eu.adainius.newsfocused.admin.site.back.domain.User.Preferences;

public class UserDeserializer extends StdDeserializer<User> {

    ObjectMapper collectionMapper = new ObjectMapper();

    public UserDeserializer() {
        this(null);
    }

    public UserDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public User deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        JsonNode productNode = jp.getCodec().readTree(jp);

        String email = productNode.get("email").textValue();

        ObjectReader reader = collectionMapper.readerFor(new TypeReference<Set<String>>() {
        });
        Set<String> sites = reader.readValue(productNode.get("preferences").get("sites"));
        Set<String> daysToSendOn = reader.readValue(productNode.get("preferences").get("daysToSendOn"));
        int headlineCount = productNode.get("preferences").get("headlineCount").intValue();

        reader = collectionMapper.readerFor(new TypeReference<Set<EmailSent>>() {
        });

        Date registrationDate = collectionMapper.readValue(productNode.get("registrationDate").asText(), Date.class);

        JsonNode emailsSentArray = productNode.get("emailsSent");
        Set<EmailSent> emailsSent = new HashSet<EmailSent>(emailsSentArray.size());

        for (JsonNode jsonNode : emailsSentArray) {
            Date sentOn = collectionMapper.readValue(jsonNode.get("sentOn").asText(), Date.class);
            emailsSent.add(new EmailSent(sentOn));

        }

        return User.builder().email(email).emailsSent(emailsSent).registrationDate(registrationDate)
                .preferences(
                        Preferences.builder()
                                .daysToSendOn(daysToSendOn)
                                .sites(sites)
                                .headlineCount(headlineCount).build())
                .build();
    }
}
