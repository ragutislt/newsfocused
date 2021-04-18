package eu.adainius.newsfocused.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HeadlineDto {
    private String urlLink;
    private String htmlLink;
    private String siteUrl;
}
