package Tools;

/**
 * Use case: Normalizing Strings
 * The {@code Normalizer} class provides a method for normalizing strings.
 * It removes non-alphabetic characters and ensures the first letter is capitalized.
 */
public class Normalizer
{
    /**
     * Normalizes the input string by removing all non-alphabetic characters,
     * converting the string to lowercase, and capitalizing the first letter.
     *
     * @param input the string to be normalized
     * @return the normalized string, or an empty string if the input is null, empty, or contains no alphabetic characters
     *
     * The software runtime complexity for this method is O(n)
     */
    public static String normalize(String input)
    {
        // String can not be Empty
        if (input == null || input.isEmpty()) {
            return "";
        }

        // Extracting letters and numbers
        String normalizedInput = input.replaceAll("[^a-zA-Z]", "");

        // If the string is empty after removing non-alphabetic characters, return an empty string
        if(normalizedInput.isEmpty()) // e.g. if User typed in numbers only the String will be Empty again
        {
            return normalizedInput;
        }

        // Converting the first letter into a capital letter
        normalizedInput = normalizedInput.toLowerCase(); // first every Letter to LowerCase
        normalizedInput = Character.toUpperCase(normalizedInput.charAt(0)) + normalizedInput.substring(1); // converting the first letter to a capital letter

        return normalizedInput;
    }
}
