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
import eu.adainius.newsfocused.config.EmailConfiguration;
import eu.adainius.newsfocused.headline.Headline;
import eu.adainius.newsfocused.headline.Headlines;
import eu.adainius.newsfocused.util.Today;
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

        Map<String, Object> input = new HashMap<String, Object>();

        List<DayDto> days = new ArrayList<>(7);

        for (int i = 0; i < 7; i++) {
            LocalDate date = Today.getToday().minusDays(i);
            String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            List<Headline> headlinesFromTheDay = headlines.from(dateString).stream().collect(Collectors.toList());

            if (headlinesFromTheDay.isEmpty()) {
                continue;
            }

            List<String> sites = headlinesFromTheDay.stream().map(headline -> headline.website()).distinct()
                    .collect(Collectors.toList());

            List<HeadlineDto> headlinesForEmail = collectHeadlinesFromEachSite(headlinesFromTheDay, sites);

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

    private List<HeadlineDto> collectHeadlinesFromEachSite(List<Headline> headlinesFromTheDay, List<String> sites) {
        List<HeadlineDto> headlinesForEmail = new ArrayList<>();
        
        for (String site : sites) {
            List<Headline> headlinesFromSite = headlinesFromTheDay.stream().filter(h -> h.website().equals(site))
                    .limit(headlineDailyCount).collect(Collectors.toList());
            headlinesForEmail.addAll(headlinesFromSite.stream()
                    .map(h -> new HeadlineDto(h.urlLink(), h.htmlLink(), h.website())).collect(Collectors.toList()));
        }

        return headlinesForEmail;
    }
}
