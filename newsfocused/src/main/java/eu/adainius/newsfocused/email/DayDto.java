package eu.adainius.newsfocused.email;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DayDto {
    private List<HeadlineDto> headlines;
    private String day;

    public String toString() {
        return day;
    }
}
