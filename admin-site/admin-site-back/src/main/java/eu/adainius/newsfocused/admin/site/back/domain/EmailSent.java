package eu.adainius.newsfocused.admin.site.back.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public class EmailSent {
    Date sentOn;
}
