package Tools;

import java.util.Scanner;

/**
 * The {@code Printing} class provides methods for user input and output.
 * It contains methods to read integers with optional bounds and strings
 * from the console, with validation and error handling.
 */

public class Printing
{
    /**
     * Printing with Scanner for Integer
     * Prompts the user to enter an integer, validating that it is non-negative.
     *
     * @param input     The prompt message displayed to the user
     * @param scworth   The variable to store the user's input
     *
     * @return          The integer value entered by the user
     *
     * Software Runtime is O(1),
     */
    public int printWithScInt(String input, int scworth) {
        Scanner sc = new Scanner(System.in);
        boolean validInput = false;

        do {
            try {
                System.out.println(input);
                scworth = sc.nextInt();
                if (scworth < 0) {
                    throw new IllegalArgumentException("Negative numbers are not allowed.");
                }
                validInput = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                sc.nextLine(); // Clear the input buffer
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a correct value.");
                sc.nextLine(); // Clear the input buffer
            }
        } while (!validInput);

        return scworth;
    }

    /**
     * Printing with Scanner for Integer with upper bound
     * Prompts the user to enter an integer, validating that it is within the specified upper bound.
     *
     * @param input the prompt message displayed to the user
     * @param scworth the variable to store the user's input
     * @param upperbound the maximum allowed value (inclusive)
     * @return the integer value entered by the user
     *
     * Software Runtime is O(1)
     */
    public int printWithScIntUpperBound(String input, int scworth, int upperbound) {
        Scanner sc = new Scanner(System.in);
        boolean validInput = false;

        do {
            try {
                System.out.println(input);
                scworth = sc.nextInt();
                if (scworth < 0)
                {
                    throw new IllegalArgumentException("Negative numbers are not allowed.");
                }
                if(scworth > upperbound)
                {
                    throw new IllegalArgumentException("Please do not choose a number above " +upperbound);
                }
                validInput = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                sc.nextLine(); // Clear the input buffer
            } catch (Exception e) {
                System.out.println("Please do not choose a number above " +upperbound);
                sc.nextLine(); // Clear the input buffer
            }
        } while (!validInput);

        return scworth;
    }

    /**
     * Printing with Scanner for Integer with upper bound and lower bound
     * Prompts the user to enter an integer, validating that it is within the specified bounds.
     *
     * @param input the prompt message displayed to the user
     * @param scworth the variable to store the user's input
     * @param upperbound the maximum allowed value (inclusive)
     * @param lowerbound the minimum allowed value (inclusive)
     * @return the integer value entered by the user
     *
     * Software Runtime is O(1)
     */
    public int printWithScIntUpperLowerBound(String input, int scworth, int upperbound, int lowerbound) {
        Scanner sc = new Scanner(System.in);
        boolean validInput = false;

        do {
            try {
                System.out.println(input);
                scworth = sc.nextInt();
                if(scworth > upperbound)
                {
                    throw new IllegalArgumentException("Please do not choose a number above " +upperbound);
                }
                if(scworth < lowerbound)
                {
                    throw new IllegalArgumentException("Please do not choose a number below " +lowerbound);
                }
                validInput = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                sc.nextLine(); // Clear the input buffer
            } catch (Exception e) {
                System.out.println("Please choose a number between " +upperbound+ " and " +lowerbound);
                sc.nextLine(); // Clear the input buffer
            }
        } while (!validInput);

        return scworth;
    }

    /**
     * Printing with Scanner for String
     * Prompts the user to enter a string.
     *
     * @param input the prompt message displayed to the user
     * @param scworth the variable to store the user's input
     * @return the string value entered by the user
     *
     * Software Runtime is O(1)
     */
    public String printWithScString(String input, String scworth)
    {
        Scanner sc = new Scanner(System.in);
        boolean validInput = false;

        do
        {
            try
            {
                System.out.println(input);
                scworth = sc.nextLine();
                validInput = true;
            }
            catch (Exception e)
            {
                System.out.println("Invalid input. Please try again.");
                sc.nextLine(); // Clear the input buffer
            }
        } while (!validInput);

        return scworth;
    }

    /**
     * Substitute for System.out.println
     * Prints the given text to the console.
     *
     * @param text is the text that will be printed
     *
     * Software Runtime is O(1)
     */
    public void print(String text)
    {
        System.out.println(text);
    }
}
