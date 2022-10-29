package eu.adainius.newsfocused.admin.site.back.controller;

import static org.junit.jupiter.api.DynamicTest.stream;

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

import eu.adainius.newsfocused.admin.site.back.controller.MyUser.MyPreferences;
import eu.adainius.newsfocused.admin.site.back.domain.EmailSent;

public class MyUserDeserializer extends StdDeserializer<MyUser> {

    ObjectMapper collectionMapper = new ObjectMapper();

    public MyUserDeserializer() {
        this(null);
    }

    public MyUserDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public MyUser deserialize(JsonParser jp, DeserializationContext ctxt)
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

        return MyUser.builder().email(email).emailsSent(emailsSent).registrationDate(registrationDate)
                .preferences(
                        MyPreferences.builder()
                                .daysToSendOn(daysToSendOn)
                                .sites(sites)
                                .headlineCount(headlineCount).build())
                .build();
    }
}
