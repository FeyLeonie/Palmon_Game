package tools;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Manages the language
 * => English and German
 */
public class Language
{
    static final String basePath = "resources/console_messages";
    static ResourceBundle bundle;

    /**
     * Configures the language based on user input.
     */
    public static void configureLanguage(int language)
    {
        String userLanguage = "";
        switch(language)
        {
            case 1:
                userLanguage = "en";
                break;
            case 2:
                userLanguage = "de";
                break;
        }
        bundle = ResourceBundle.getBundle(basePath, new Locale(userLanguage));
    }

    /**
     * Retrieves a localized message from the resource bundle.
     *
     * @param Key the resource bundle key of the message
     * @param args        variable amount of values to be dynamically inserted into the message
     * @return the localized message
     */
    public static String getMessage(String Key, Object... args)
    {
        String pattern = bundle.getString(Key);
        return MessageFormat.format(pattern, args); //method automatically replaces placeholders like {0} with arguments
    }

    // private Localization(){} // Private constructor to prevent instantiation
}
