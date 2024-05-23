package Tools;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Works as an Language Handler
 * makes choosing between english and german possible
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
     * Gets a localized message from the resource bundle.
     *
     * @param Key         The resource bundle key of the message
     * @param args        Variable amount of values to be dynamically inserted into the message
     * @return            The localized message
     */
    public static String getMessage(String Key, Object... args)
    {
        String pattern = bundle.getString(Key);
        return MessageFormat.format(pattern, args); // method automatically replaces placeholders like {0} or {1} with the given Objects arguments
    }
}
