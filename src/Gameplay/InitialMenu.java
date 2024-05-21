package Gameplay;

import tools.Printing;
import tools.Normalizer;
import tools.ThreadSleep;

/**
 * The {@code InitialMenu} class represents the initial menu of the game.
 * It allows the player to choose their language, enter their name, and the opponent's name.
 * The software runtime complexity for each method is denoted in Big O notation.
 */
public class InitialMenu extends Thread {

    /**
     * The Palmon logo displayed at the start of the game
     */
    public final String palmon_logo =
            "███████     ████     ███       ███      ███      ██████     ███     ██\n" +
                    "██   ████   ████     ███       ████    ████    ███   ████   ████    ██\n" +
                    "██    ███  ██████    ███       ██████ █████   ███     ███   █████   ██\n" +
                    "████████  ███  ███   ███       ███ ████  ██   ██       ███  ██  ███ ██\n" +//
                    "██        ████████   ███       ███  ██   ██   ███     ███   ██   █████\n" +//
                    "██       ███    ███  ████████  ███       ██    ████ █████   ██    ████\n" +
                    "██      ███      ███ ████████  ███       ██      █████      ██     ███";

    /**
     * The chosen language (1 for English, 2 for German)
     */
    static int language = 0;

    /**
     * The player's name.
     */
    static String playerName = "";

    /**
     * The opponent's name.
     */
    static String enemyName = "";

    /**
     * The instance of the Printing class for printing messages.
     */
    Printing print;

    /**
     * The entry point for running the initial menu.
     * The software runtime complexity is O(1)
     */
    public void run() {
        startInitialMenu();
    }

    /**
     * Starts the initial menu.
     * The software runtime complexity is O(n)
     */
    public void startInitialMenu() {
        print = new Printing();

        // Choose language (English or Deutsch)
        do {
            language = print.printWithScInt("Please choose your language. \n(1) English \n(2) German", language);

            if (language != 1 && language != 2) {
                print.print("Please type in (1) for English or (2) for German.");
            }
        } while (language != 1 && language != 2);

        print.print("███████     ████     ███       ███      ███      ██████     ███     ██");
        ThreadSleep.sleep(250);
        print.print("██   ████   ████     ███       ████    ████    ███   ████   ████    ██");
        ThreadSleep.sleep(250);
        print.print("██    ███  ██████    ███       ██████ █████   ███     ███   █████   ██");
        ThreadSleep.sleep(250);
        print.print("████████  ███  ███   ███       ███ ████  ██   ██       ███  ██  ███ ██");
        ThreadSleep.sleep(250);
        print.print("██        ████████   ███       ███  ██   ██   ███     ███   ██   █████");
        ThreadSleep.sleep(250);
        print.print("██       ███    ███  ████████  ███       ██    ████ █████   ██    ████");
        ThreadSleep.sleep(250);
        print.print("██      ███      ███ ████████  ███       ██      █████      ██     ███");

        ThreadSleep.sleep(1000);

        // Welcome message
        print.print("\nWelcome to Palmon");
        ThreadSleep.sleep(1000);
        print.print("In this arena, strength is the only language spoken!");
        ThreadSleep.sleep(1000);
        print.print("Let the combat begin...");

        ThreadSleep.sleep(1000);

        // Enter player's name
        getPlayerName();
        ThreadSleep.sleep(1000);

        // Enter opponent's name
        getEnemyName();

        ThreadSleep.sleep(1000);

        // Display preparation message
        print.print("\nLet's not waste any more time.");
        ThreadSleep.sleep(1000);
        print.print("Prepare yourself, and let the battle against " + enemyName + " begin, " + playerName + "!");

        ThreadSleep.sleep(1500);
    }

    /**
     * Prompts the player to enter their name and normalizes it.
     * The software runtime complexity is O(n)
     */
    public void getPlayerName()
    {
        while (playerName.equals("")) {
            playerName = print.printWithScString("\nWhat's your name, warrior?", playerName);
            playerName = Normalizer.normalize(playerName); // Normalize the player's name

            if (playerName.isEmpty()) {
                print.print("Please type in a name. Numbers will be removed.");
            }
        }
    }

    /**
     * Prompts the player to enter the opponent's name and normalizes it.
     * The software runtime complexity is O(n)
     */
    public void getEnemyName()
    {
        while (enemyName.equals(""))
        {
            print.print("\nAlright, " + playerName);
            ThreadSleep.sleep(1);
            enemyName = print.printWithScString("What's the name of your opponent?", enemyName);
            enemyName = Normalizer.normalize(enemyName); // Normalize the opponent's name

            if (enemyName.isEmpty())
            {
                print.print("Please type in a name. Numbers will be removed.");
            }
        }
    }
}
