package eu.adainius.newsfocused.config;

import java.util.List;
import java.util.Locale;

import eu.adainius.newsfocused.App;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class EmailConfiguration {
    private static Configuration templateEngineConfiguration;

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
        return "news@newsfocused.eu";
    }
}
