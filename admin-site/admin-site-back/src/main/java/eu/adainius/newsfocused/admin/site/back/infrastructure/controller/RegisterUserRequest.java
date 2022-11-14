package eu.adainius.newsfocused.admin.site.back.infrastructure.controller;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class RegisterUserRequest {
    private String email;
    private Set<String> daysToSendOn;
    private Set<String> sites;
    private int headlineCount;
}
