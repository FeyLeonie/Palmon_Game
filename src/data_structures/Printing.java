package data_structures;

import java.util.Scanner;

public class Printing
{
    public int printsc(String text, int scworth)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println(text);
        scworth = sc.nextInt();

        return scworth;
    }

    // printing for String
    public String printssc(String text, String scworth)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println(text);
        scworth = sc.nextLine();

        return scworth;
    }

    public void print(String text)
    {
        System.out.println(text);
    }
}
