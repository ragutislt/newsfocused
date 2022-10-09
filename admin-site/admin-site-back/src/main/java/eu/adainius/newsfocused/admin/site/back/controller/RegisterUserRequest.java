package eu.adainius.newsfocused.admin.site.back.controller;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterUserRequest {
    private String email;
    private Set<String> daysToSendOn;
    private Set<String> sites;
    private int headlineCount;
}
