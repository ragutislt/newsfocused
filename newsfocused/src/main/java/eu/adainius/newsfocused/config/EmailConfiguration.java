package eu.adainius.newsfocused.config;

import java.util.Locale;
import java.util.Properties;

import eu.adainius.newsfocused.App;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class EmailConfiguration {
    private static Configuration templateEngineConfiguration;
    private static Properties emailProtocolProperties;

    public static Configuration templateConfiguration() {
        if (templateEngineConfiguration == null) {
            configureTemplateEngine();
        }
        return templateEngineConfiguration;
    }

    private static void configureTemplateEngine() {
        templateEngineConfiguration = new Configuration();

        // Where do we load the templates from:
        templateEngineConfiguration.setClassForTemplateLoading(App.class, "/templates/");

        // Some other recommended settings:
        templateEngineConfiguration.setIncompatibleImprovements(new Version(2, 3, 20));
        templateEngineConfiguration.setDefaultEncoding("UTF-8");
        templateEngineConfiguration.setLocale(Locale.US);
        templateEngineConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    public static String from() {
        return emailProtocolProperties.getProperty("email.from");
    }

    public static void setEmailProtocolProperties(Properties emailProtocolProperties) {
        EmailConfiguration.emailProtocolProperties = emailProtocolProperties;
    }

    public static Properties emailProtocolProperties() {
        if (emailProtocolProperties == null) {
            emailProtocolProperties = new Properties();

            // by default, mailcatcher - smtp://127.0.0.1:10025
            emailProtocolProperties.setProperty("mail.smtp.auth", "false");
            emailProtocolProperties.setProperty("mail.smtp.starttls.enable", "true");
            emailProtocolProperties.setProperty("mail.smtp.host", "127.0.0.1");
            emailProtocolProperties.setProperty("mail.smtp.port", "10025");
            emailProtocolProperties.setProperty("email.from", "news@newsfocused.eu");
        }
        return EmailConfiguration.emailProtocolProperties;
    }

}
