package csv_handling;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The BattleDocumentation class is responsible for managing the battle log,
 * including initializing the log file, adding entries, and printing the log.
 */
public class BattleDocumentation
{
    static final String fileName = "battleLog.csv"; // name of the CSV
    static int battleCount = 0; // battle count, +1 for each round played and saved

    /**
     * Checks if the log file exists and creates it if needed.
     * It also counts the number of battles recorded in the file.
     *
     * software complexity is O(n)
     */
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

    /**
     * Adds a new entry to the BattleLog file.
     *
     * @param totalRounds The total number of rounds played in the battle.
     * @param playerName The name of the player.
     * @param playerTeam The team of the player.
     * @param enemyName The name of the enemy.
     * @param enemyTeam The team of the enemy.
     * @param outcome The outcome of the battle.
     *
     * software complexity is O(1)
     */
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

    /**
     * Converts an ArrayList of strings to a CSV-friendly string.
     *
     * @param convertingList The ArrayList to be converted.
     * @return A string representation of the ArrayList suitable for CSV.
     *
     * software complexity is O(n), where n is the number of elements in the ArrayList.
     */
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

    /**
     * Prints out the battle log for the user.
     *
     * software complexity is O(n), where n is the number of lines in the file.
     */
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
