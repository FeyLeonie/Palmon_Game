package Gameplay;

import Tools.Language;
import Tools.Printing;
import Tools.Normalizer;
import Tools.ThreadSleep;

/**
 * The {@code InitialMenu} class represents the initial menu of the game.
 * It allows the player to choose their language, enter their name, and the opponent's name.
 * The software runtime complexity for each method is denoted in Big O notation.
 */
public class InitialMenu extends Thread
{

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
     *
     * TSoftware Runtime is O(1)
     */
    public void run() {
        startInitialMenu();
    }

    /**
     * Starts the initial menu.
     *
     *  Software Runtime is O(n)
     */
    public void startInitialMenu() {
        print = new Printing();

        // Choose language (English or German)
        do {
            language = print.printWithScInt("Please choose your language. \n(1) English \n(2) German", language);

            if (language != 1 && language != 2) {
                print.print("Please type in (1) for English or (2) for German.");
            }
        } while (language != 1 && language != 2);

        // configuring the Language (needed for the properties)
        Language.configureLanguage(language);

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
        print.print(Language.getMessage("IMWelcome"));
        ThreadSleep.sleep(1000);
        print.print(Language.getMessage("IMCombatBegin"));

        ThreadSleep.sleep(1000);

        // Enter player's name
        getPlayerName();
        ThreadSleep.sleep(1000);

        // Enter opponent's name
        getEnemyName();

        ThreadSleep.sleep(1000);

        // Display preparation message
        print.print(Language.getMessage("IMNoTimeWaste"));
        ThreadSleep.sleep(1000);
        print.print(Language.getMessage("IMBattleStart", enemyName, playerName));

        ThreadSleep.sleep(1500);
    }

    /**
     * Prompts the player to enter their name and normalizes it.
     *
     * Software Runtime is  O(n)
     */
    public void getPlayerName()
    {
        while (playerName.isEmpty()) {
            playerName = print.printWithScString(Language.getMessage("IMAskPlayerName"), playerName);
            playerName = Normalizer.normalize(playerName); // Normalize the player's name

            // If Player didnt enter a correct name (e.g. if numbers only were entered)
            if (playerName.isEmpty()) {
                print.print(Language.getMessage("IMWrongPlayerName"));
            }
        }
    }

    /**
     * Prompts the player to enter the opponent's name and normalizes it.
     *
     * Software Runtime is O(n)
     */
    public void getEnemyName()
    {
        while (enemyName.isEmpty())
        {
            print.print(Language.getMessage("IMAlrightPlayer", playerName));
            ThreadSleep.sleep(1);
            enemyName = print.printWithScString(Language.getMessage("IMOpponentName"), enemyName);
            enemyName = Normalizer.normalize(enemyName); // Normalize the opponent's name

            // If Player didnt enter a correct name (e.g. if numbers only were entered)
            if (enemyName.isEmpty())
            {
                print.print(Language.getMessage("IMWrongOpponentName"));
            }
        }
    }
}
