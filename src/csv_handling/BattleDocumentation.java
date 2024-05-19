package csv_handling;

import data_structures.Normalizer;
import elements.Palmon;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

//TODO TRY CATCH??
// kommentieren!

public class BattleDocumentation
{
    static final String fileName = "battleLog.csv"; // name of the CSV
    static int battleCount = 0; // battle count, +1 for each round played and saved

    // checks if the file exists and creates it if needed
    public static void initializeLogFile() {
        File file = new File(fileName);

        if (!file.exists())  // if the file does not exist...
        {
            // create the file and add the header
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
            {
                writer.write("Battle Number;Rounds played;Player;Player team;Enemy;Enemy Team;Outcome\n"); // Header
            } catch (IOException e)
            {
                System.err.println("Error while setting up the BattleLog file. " + e.getMessage());
            }
        }
        else // if the file already exists
        {
            //count the battles played
            try (Scanner scanner = new Scanner(file))
            {
                while (scanner.hasNextLine()) // while there is still another dataset
                {
                    scanner.nextLine(); // scan the next Line
                    battleCount++; // and increase the battleCount
                }
                // in the end substracting one from the count because of the header
                battleCount--;
            } catch (IOException e)
            {
                System.err.println("Error while reading the BattleLog file. " + e.getMessage());
            }
        }
    }

    // adding a new entry to the BattleLog File
    public static void logBattle(int totalRounds, String playerName, ArrayList<String> playerTeam, String enemyName, ArrayList<String> enemyTeam, String outcome) {
        battleCount++; // increasing the battleCount by one since another round was played

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true)))
        {
            String playerTeamString = convertArrayList(playerTeam);
            String enemyTeamString = convertArrayList(enemyTeam);

            // Write the data to the CSV file
            writer.write(battleCount + ";" + totalRounds + ";" + playerName + ";" + playerTeamString + ";" + enemyName + ";" + enemyTeamString + ";" + outcome + "\n");
        } catch (IOException e)
        {
            System.err.println("Error while writing into the BattleLog file. " + e.getMessage());
        }
    }

    // Helper method to convert an ArrayList of Palmon objects to a CSV-friendly string
    public static String convertArrayList(ArrayList<String> convertingList) {
        StringBuilder output = new StringBuilder();

        for (String palmon : convertingList) {
            if (!output.isEmpty()) {
                output.append(", "); // Trennzeichen zwischen den Namen der Palmon-Objekte
            }
            output.append(palmon); // Hinzufügen des Namens des Palmon-Objekts
        }

        return output.toString();
    }

    // Prints out the battle log for the user
    public static void printBattleLog() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Skip the header line
            reader.readLine();
            System.out.println("Battle Log:");
            while ((line = reader.readLine()) != null) {
                // Split the line into columns
                String[] columns = line.split(";");

                // Extract the details from the columns
                int battleNumber = Integer.parseInt(columns[0]);
                int totalRounds = Integer.parseInt(columns[1]);
                String playerName = columns[2];
                String playerTeam = columns[3];
                String enemyName = columns[4];
                String enemyTeam = columns[5];
                String outcome = columns[6];

                // Print out the details
                System.out.println("Battle Number: " + battleNumber);
                System.out.println("Total Rounds: " + totalRounds);
                System.out.println("Player Name: " + playerName);
                System.out.println("Player Team: " + playerTeam);
                System.out.println("Enemy Name: " + enemyName);
                System.out.println("Enemy Team: " + enemyTeam);
                System.out.println("Outcome: " + outcome);
                System.out.println("------");
            }
        } catch (IOException e) {
            System.err.println("Error while reading the BattleLog file. " + e.getMessage());
        }
    }
}
