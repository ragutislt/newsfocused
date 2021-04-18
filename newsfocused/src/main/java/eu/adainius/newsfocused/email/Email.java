package eu.adainius.newsfocused.email;

import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.adainius.newsfocused.ApplicationException;
import eu.adainius.newsfocused.EmailConfiguration;
import eu.adainius.newsfocused.headline.Headline;
import eu.adainius.newsfocused.headline.Headlines;
import freemarker.template.Template;

public class Email {
    private String template;
    private Headlines headlines;
    private String email;

    public Email(String template, Headlines headlines) {
        this.template = template;
        this.headlines = headlines;
    }

    public Headlines headlines() {
        return this.headlines;
    }

    public String template() {
        return this.template;
    }

    public String body() {
        if (email != null) {
            return email;
        }

        // TODO above in the flow, check if it's a saturday (or a day from config
        // (cron?))
        Map<String, Object> input = new HashMap<String, Object>();

        List<DayDto> days = new ArrayList<>(7);

        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            List<Headline> headlinesFromTheDay = headlines.from(dateString);
            List<HeadlineDto> headlinesForEmail = new ArrayList<>();
            for (Headline headline : headlinesFromTheDay) {
                headlinesForEmail.add(new HeadlineDto(headline.urlLink(), headline.htmlLink(), headline.website()));
            }
            DayDto day = new DayDto(headlinesForEmail, dateString);
            days.add(day);
        }

        input.put("days", days);

        try {
            Template freemarkerTemplate = EmailConfiguration.templateConfiguration().getTemplate(this.template);
            Writer writer = new StringWriter();
            freemarkerTemplate.process(input, writer);
            this.email = writer.toString();
            return writer.toString();
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }
}
