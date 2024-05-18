package data_structures;

// Use case: Normalizing Strings
public class Normalizer
{
    public static String normalize(String input)
    {
        // String can not be Empty
        if (input == null || input.isEmpty()) {
            return "";
        }

        // extracting letters and numbers
        String normalizedInpur = input.replaceAll("[^a-zA-Z]", "");

        if(normalizedInpur.isEmpty()) // if User typed in numbers only the String will be Empty again
        {
            return normalizedInpur;
        }

        // converting the first letter into a capital letter
        normalizedInpur = normalizedInpur.toLowerCase(); // first every Letter to LowerCase
        normalizedInpur = Character.toUpperCase(normalizedInpur.charAt(0)) + normalizedInpur.substring(1); // converting the first letter to a capital letter

        return normalizedInpur;
    }
}
