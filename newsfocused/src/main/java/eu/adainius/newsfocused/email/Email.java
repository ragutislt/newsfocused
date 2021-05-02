package eu.adainius.newsfocused.email;

import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.validator.routines.EmailValidator;

import eu.adainius.newsfocused.ApplicationException;
import eu.adainius.newsfocused.EmailConfiguration;
import eu.adainius.newsfocused.headline.Headline;
import eu.adainius.newsfocused.headline.Headlines;
import freemarker.template.Template;

public class Email {
    private static final int DEFAULT_HEADLINE_COUNT = 2;
    private String template;
    private Headlines headlines;
    private String emailBody;
    private String address;
    private int headlineDailyCount;
    EmailValidator emailValidator = EmailValidator.getInstance();

    // TODO - this is getting ugly, add a builder
    public Email(String template, Headlines headlines, String address, int headlineDailyCount) {
        this.template = template;
        this.headlines = headlines;
        this.address = address;
        this.headlineDailyCount = headlineDailyCount;

        validateEmailAddress();
    }

    public Email(String template, Headlines headlines, String address) {
        this(template, headlines, address, DEFAULT_HEADLINE_COUNT);
    }

    private void validateEmailAddress() {
        boolean addressValid = emailValidator.isValid(this.address);
        if (!addressValid) {
            throw new ApplicationException(String.format("Email address %s is not valid", this.address));
        }
    }

    public Headlines headlines() {
        return this.headlines;
    }

    public int headlineDailyCount() {
        return this.headlineDailyCount;
    }

    public String template() {
        return this.template;
    }

    public String address() {
        return address;
    }

    public String body() {
        if (emailBody != null) {
            return emailBody;
        }

        // TODO above in the flow, check if it's a saturday (or a day from config
        // (cron?))
        Map<String, Object> input = new HashMap<String, Object>();

        List<DayDto> days = new ArrayList<>(7);

        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            List<Headline> headlinesFromTheDay = headlines.from(dateString).stream().limit(headlineDailyCount)
                    .collect(Collectors.toList());
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
            this.emailBody = writer.toString();
            return writer.toString();
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }
}
