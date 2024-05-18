package Gameplay;

import data_structures.Printing;

public class InitialMenu extends Thread
{
    public final String palmon_logo =               "███████     ████     ███       ███      ███      ██████     ███     ██\n" +
                                                    "██   ████   ████     ███       ████    ████    ███   ████   ████    ██\n" +
                                                    "██    ███  ██████    ███       ██████ █████   ███     ███   █████   ██\n" +
                                                    "████████  ███  ███   ███       ███ ████  ██   ██       ███  ██  ███ ██\n" +
                                                    "██        ████████   ███       ███  ██   ██   ███     ███   ██   █████\n" +
                                                    "██       ███    ███  ████████  ███       ██    ████ █████   ██    ████\n" +
                                                    "██      ███      ███ ████████  ███       ██      █████      ██     ███\n";
    static int language = 0;
    static String playerName = "";
    static String enemyName;
    
    Printing print;

    public void run()
    {
        startInitialMenu();;
    }

    public void startInitialMenu()
    {
        print = new Printing();

        do
        {
            language = print.printsc("Please choose your language. \n(1) English (default) \n(2) Deutsch", language);

            if(language != 1 && language != 2)
            {
                print.print("Please type in (1) for yes and (2) for no.");
            }
        }
        while(language != 1 && language != 2);

        print.print("\n" +palmon_logo);
        print.print("\nWelcome to Palmon, where every battle defines destiny. \nIn this arena, strength is the only language spoken, so prepare yourself for a hard fight! \nRise or fall, the choice is yours. Let the combat begin...");


        playerName = print.printssc("\nWhat's your name, warrior?", playerName);

        enemyName = print.printssc("\nPrepare to face your destiny, " +playerName + ". Whats the name of the opponent who dares to challenge you in the arena of Palmon?", enemyName);

        print.print("\nAlright, lets not waste any more time. Prepare yourself, and let the battle against " +enemyName+ " begin, " + playerName + "!");
    }
}
