package eu.adainius.newsfocused.admin.site.back.controller;

import java.util.Set;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RegisterUserRequest {
    String email;
    Set<String> daysToSendOn;
    Set<String> sites;
    int headlineCount;
}
