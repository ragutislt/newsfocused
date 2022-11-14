package eu.adainius.newsfocused.admin.site.back.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@ToString
@Accessors(fluent = true)
public class EmailSent {
    Date sentOn;
}
