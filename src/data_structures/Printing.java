package data_structures;

import java.util.Scanner;

public class Printing
{
    // printing with Scanner for Integer
    public int printsc(String text, int scworth) {
        Scanner sc = new Scanner(System.in);
        boolean validInput = false;

        do {
            try {
                System.out.println(text);
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

    // printing with Scanner for Integer with upper bound
    public int printsc_ub(String text, int scworth, int upperbound) {
        Scanner sc = new Scanner(System.in);
        boolean validInput = false;

        do {
            try {
                System.out.println(text);
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

    public int printsc_ublb(String text, int scworth, int upperbound, int lowerbound) {
        Scanner sc = new Scanner(System.in);
        boolean validInput = false;

        do {
            try {
                System.out.println(text);
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

    // printing with Scanner for String
    public String printssc(String text, String scworth) {
        Scanner sc = new Scanner(System.in);
        boolean validInput = false;

        do
        {
            try
            {
                System.out.println(text);
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

    public void print(String text)
    {
        System.out.println(text);
    }
}
