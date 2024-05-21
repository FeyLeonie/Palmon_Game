package Gameplay;

import csv_handling.CSV_Reader;
import csv_handling.CSV_Searching;
import tools.Normalizer;
import tools.Printing;
import elements.Palmon;
import elements.Move;

import data_structures.Queue;
import tools.ThreadSleep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * The {@code Game} class manages the gameplay, including team settings, assembling Palmons, and starting the final battle
 */
public class Game extends Printing
{
    CSV_Reader data; // for reading the CSV
    CSV_Searching searching; // has some Methods that will be needed

    public Queue<Palmon> playerPalmons;
    public Queue<Palmon> enemyPalmons;

    public boolean includeLevel = false;

    /**
     * Constructs a new Game instance.
     *
     * @param data      the CSV_Reader instance for reading CSV data
     * @param selection the CSV_Searching instance for searching CSV data
     */
    public Game(CSV_Reader data, CSV_Searching selection)
    {
        this.data = data;
        this.searching = selection;
    }

    /**
     * Starts the game by initializing team settings and starting a fight.
     * software runtime is O(n^2)
     */
    public void run()
    {
        Game game = new Game(data, searching);
        Fight fight = new Fight(game, data);
        game.teamSettings(searching, fight);
    }

    /**
     * Manages the team settings for player and enemy, including assembling Palmons and setting the Moves
     *
     * @param searching the CSV_Searching instance for searching through the CSV_data
     * @param fight     the Fight instance for starting the fight after gameplay settings
     *
     * software runtime is O(n^2)
     */
    public void teamSettings(CSV_Searching searching, Fight fight)
    {
        // Team Settings Palmons Player
        print("\n" + InitialMenu.playerName + ", you are now in the team settings.");
        ThreadSleep.sleep(1500);

        // asking for Players Teamsize
        int playersize = 0;
        do
        {
            // I chose an upper bound of 100 Palmons for max amout of Palmons in a Team since more is not realistic (and 100 is even very much)
            playersize = printWithScIntUpperBound("How many Palmons would you like to have in your team, " +InitialMenu.playerName+ "?", playersize, 100);

            // testing, if Input was invalid
            if(playersize == 0)
            {
                print("Please type in a number between 1 and 100."); // if Input was invalid, remind the User what he can type in
            }

        }while(playersize == 0); // round starts again if Input was invalid

        ThreadSleep.sleep(1000);
        int answer = 0;
        do
        {
            answer = printWithScInt("\nDo you want to assign a Level Range for your Palmons and the Palmons from " +InitialMenu.enemyName+ "? \n(1) yes \n(2) no", answer);

            if(answer != 1 && answer != 2)
            {
                print("Please type in (1) for yes and (2) for no.");
            }
        }
        while(answer != 1 && answer != 2);

        int minimumLevel = 0; // tempworth
        int maximumLevel = 0; // tempworth

        if(answer == 1) // If Level Range is wanted
        {
            ThreadSleep.sleep(1000);
            includeLevel = true;
            do
            {
                // the Level Range must be at least 20 to make sure that Moves can be found
                // Working with 0-10 to make it easier for the User, Multiplying it with 10 later on to get the correct value
                minimumLevel = printWithScInt("\nPlease choose the Minimum Level between 0-8", minimumLevel); // Let the User choose a Level between 0 and 8

                if(minimumLevel > 8) // MinimumLevel must not be above 8 (8*10 will be 80 later on and Level Range must be 20 so Max could be 100 with Min 80) to make sure Level Range can be at least 20 since Max Level is 100
                {
                    print("Please choose a number between 1 and 8."); // remind the User what values are possible
                }
            }while(minimumLevel > 8);

            ThreadSleep.sleep(1000);
            maximumLevel = printWithScIntUpperLowerBound("Please choose the Maximum Level. It has to be between " +(minimumLevel+2)+ " and 10.", maximumLevel, 10, minimumLevel+2);

            // Setting the Level to the correct value
            minimumLevel *= 10;
            maximumLevel *= 10;
        }

        // asking for Team assembling method
        ThreadSleep.sleep(1000);
        int selection = 0;
        do
        {
            selection = printWithScInt("\nHow do you want to assemble your team? \n(1) randomly \n(2) by id \n(3) by type", selection);

            if(selection != 1 && selection != 2 && selection != 3)
            {
                print("Please type in (1) for random selection (2) for selection by id (2) for selection by type");
            }
        }while(selection != 1 && selection != 2 && selection != 3);

        // assembling the Palmons for the Player (regarding the choice the Player made)
        playerPalmons = new Queue<>();
        switch(selection)
        {
            case 1: // assembling randomly
                playerPalmons = assembleRandomly(playersize, playerPalmons, minimumLevel, maximumLevel);
                break;

            case 2: // assembling by ID
                playerPalmons = assembleById(playersize, playerPalmons, minimumLevel, maximumLevel);
                break;

            case 3: // assembling by type
                playerPalmons = assembleByFirstType(playersize, searching, playerPalmons,  minimumLevel, maximumLevel);
                break;
        }

        // Setting Moves for Players Palmons
        HashMap<Integer, ArrayList<Move>> playersPalMoves = setMovesForPalmon(data, playerPalmons, includeLevel);

        // Team Settings Palmons Enemy
        ThreadSleep.sleep(1000);
        int enemysize = 0;
        enemysize = printWithScIntUpperBound("How many Palmons would you like in the team from " + InitialMenu.enemyName+ "? \n(0) randomly \n(type in your preferred number)", enemysize, 100);

        while(enemysize == 0) // no if because Random could randomly choose 0 -> another round would be needed
        {
            Random r = new Random();
            enemysize = r.nextInt(playersize * 2);

            // O(1)
            if(enemysize != 0 && enemysize <= 100)
            {
                print(enemysize + " Palmons will be fighting in your opponent's team, " + InitialMenu.enemyName+ "!");
            }
            else
            {
                enemysize = 0;
            }
        }

        // assembling the Palmons for the Enemy (always Random)
        enemyPalmons = new Queue<>();
        enemyPalmons = assembleRandomly(enemysize, enemyPalmons, minimumLevel, maximumLevel); // Always choosing the enemys Palmons by random

        // Setting Moves for Enemies Palmons
        HashMap<Integer, ArrayList<Move>> enemiesPalMoves = setMovesForPalmon(data, enemyPalmons, includeLevel);

        ThreadSleep.sleep(1000);
        print("Your team is set, " +InitialMenu.playerName+ ". Prepare for battle against " +InitialMenu.enemyName+ "!\n");

        ThreadSleep.sleep(1000);

        // Start the Fight
        fight.FightOverview(playerPalmons, enemyPalmons, enemiesPalMoves, playersPalMoves);
    }

    /** Assembles the player's team randomly.
     *
     * @param teamsize      the size of the team
     * @param team          the queue to store the assembled team
     *
     * minimumLevel and maximumLevel only needed if Level Range is wanted
     * @param minimumLevel  the minimum level for the assembled Palmons
     * @param maximumLevel  the maximum level for the assembled Palmons
     *
     * @return the queue containing the assembled team
     *
     * Software Runtime is O(n)
     */
    public Queue<Palmon> assembleRandomly(int teamsize, Queue<Palmon> team, int minimumLevel, int maximumLevel)
    {
        HashMap<Integer, Palmon> palmon_db = CSV_Reader.palmon_db;

        Random r = new Random();
        int randomIndex;
        int randomLevel;
        Palmon randomPalmon;

        // O(n)
        for(int i = 0; i < teamsize; i++)
        {
            randomIndex = r.nextInt(palmon_db.size());
            randomPalmon = palmon_db.get(randomIndex);

            if(randomPalmon != null)
            {
                if(minimumLevel != 0 || maximumLevel != 0) // if Level Range was wished one of them must be not null
                {
                    randomLevel = r.nextInt(maximumLevel - minimumLevel + 1) + minimumLevel; // generate Random number between minimumLevel and maximumLevel
                    randomPalmon.assignLevel(randomLevel); // assign the randomLevel for the current Palmon
                }

                team.enqueue(randomPalmon);
                palmon_db.remove(randomIndex); // Removing the chosen Palmon out of the database
            }
            else
            {
                i--;
            }
        }
        return team;
    }

    /** Assembles the player's team by Palmon ID.
     *
     * @param teamsize      the size of the team
     * @param team          the queue to store the assembled team
     *
     * minimumLevel and maximumLevel only needed if Level Range is wanted
     * @param minimumLevel  the minimum level for the assembled Palmons
     * @param maximumLevel  the maximum level for the assembled Palmons
     *
     * @return the queue containing the assembled team
     *
     * Software Runtime is O(n)
     */
    public Queue<Palmon> assembleById(int teamsize, Queue<Palmon> team, int minimumLevel, int maximumLevel)
    {
        HashMap<Integer, Palmon> palmon_db = CSV_Reader.palmon_db;
        int id = 0;

        Random r = new Random();
        int randomLevel;

        while(team.getQueueSize() < teamsize)
        {
            ThreadSleep.sleep(500);
            id = printWithScInt("select your next Palmon by tiping your preferred ID", id);

            if(palmon_db.containsKey(id)) // if a Palmon with the preferred ID exists
            {
                Palmon palmon = palmon_db.get(id); // pre-save the Palmon
                if(minimumLevel != 0 || maximumLevel != 0) // if Level Range was wished one of them must be not null
                {
                    randomLevel = r.nextInt(maximumLevel - minimumLevel + 1) + minimumLevel; // generate Random number between minimumLevel and maximumLevel
                    palmon.assignLevel(randomLevel); // assign the randomLevel for the current Palmon
                }
                team.enqueue(palmon); // put the Palmon into the Queue
                palmon_db.remove(id); // Removing the chosen Palmon out of the database

                ThreadSleep.sleep(500);
                print("Palmon " +Normalizer.normalize(palmon.getName())+ " with your preferred ID " +palmon.getId()+ " was put in your team.");
            }
            else
            {
                ThreadSleep.sleep(500);
                print("No Palmon with preferred ID. Please choose a different ID");
            }
        }
        return team;
    }

    /**
     * Assembles the player's team by the first type.
     *
     * @param teamsize      the size of the team
     * @param searching     the CSV searching utility
     * @param team          the queue to store the assembled team
     *
     * minimumLevel and maximumLevel only needed if Level Range is wanted
     * @param minimumLevel  the minimum level for the assembled Palmons
     * @param maximumLevel  the maximum level for the assembled Palmons
     *
     * @return the queue containing the assembled team
     *
     * Software runtime is O(n)
     */
    public Queue<Palmon> assembleByFirstType(int teamsize, CSV_Searching searching, Queue<Palmon> team, int minimumLevel, int maximumLevel)
    {
        Random r = new Random();
        int randomLevel;

        for(int i = 1; i <= teamsize; i++)
        {
            print("\nPossible types to choose from:");

            HashSet<String> types = searching.saveAllPalmonTypes(data);

            for(String alltypes: types)
            {
                if(!alltypes.equals("no type found."))
                {
                    ThreadSleep.sleep(250);
                    System.out.println(Normalizer.normalize(alltypes));
                }
            }

            String type = "";
            boolean typeExists = false;
            while(!typeExists)
            {
                ThreadSleep.sleep(1000);
                type = printWithScString("\nWhat type should the " + i + ". Palmon be? (please type in the name)", type);

                type = type.toLowerCase();
                typeExists = types.contains(type);

                if(!typeExists)
                {
                    print("type was not found. Please try again.");
                }
            }

            HashMap<String, Palmon> selected_type = searching.sortByPalmonType(type, data);
            HashSet<String> palmon_names = new HashSet<>(selected_type.keySet()); // Speichern aller Keys der HashMap, also die Namen aller Palmons

            print("\nPossible Palmons to choose from:");
            int count = 0; // counter auf 0 setzen
            for(String name: palmon_names)
            {
                if(count <= 15) // if 15 names have been output, it will stop to avoid overwhelming the user.
                {
                    ThreadSleep.sleep(500);
                    print(Normalizer.normalize(name));
                    count++;
                }
                else
                {
                    break;
                }
            }


            String decision = "";
            boolean decisionCorrect = false;
            while(!decisionCorrect)
            {
                ThreadSleep.sleep(1000);
                print("These are 15 Palmons of the desired type " +Normalizer.normalize(type));
                ThreadSleep.sleep(1000);
                decision = printWithScString("Choose one (by entering its name).", decision);
                decision = decision.toLowerCase();
                decisionCorrect = palmon_names.contains(decision);

                if(!decisionCorrect)
                {
                    print("No Palmon with the Name " +Normalizer.normalize(decision)+ " found. Please try again.");
                }
            }

            Palmon palmon = selected_type.get(decision);

            if(minimumLevel != 0 || maximumLevel != 0) // if Level Range was wished one of them must be not null
            {
                randomLevel = r.nextInt(maximumLevel - minimumLevel + 1) + minimumLevel; // generate Random number between minimumLevel and maximumLevel
                palmon.assignLevel(randomLevel); // assign the randomLevel for the current Palmon
            }

            team.enqueue(palmon); // putting the desired Palmon in the team Queue
            CSV_Reader.palmon_db.remove(palmon.getId()); // Removing the chosen Palmon out of the database
        }
        return team;
    }

    /**
     * Sets the moves for each Palmon in the team.
     *
     * @param data          the CSV reader utility
     * @param palmons       the queue containing the Palmons
     * @param includeLevel  boolean indicating if including the level Range in Move selection is needed
     *
     * @return a hashmap containing Palmon IDs as keys and their associated moves as values
     *
     * Software runtime is O(n)
     */
    public HashMap<Integer, ArrayList<Move>> setMovesForPalmon(CSV_Reader data, Queue<Palmon> palmons, boolean includeLevel)
    {
        ArrayList<Move> palmon_moves; // ArrayList for saving all Moves for the different Palmons
        HashMap<Integer, ArrayList<Move>> palmonMovesDb = new HashMap<>(); // saving the Moves in an ArrayList and connecting that with the Palmons in this HashMap

        ArrayList<Palmon> tempPalmons = palmons.toArrayList(); // saving the queue Palmons into an ArrayList to work with them

        for(Palmon palmon: tempPalmons) // iterating through every Palmon
        {
            int id = palmon.getId();
            palmon_moves = searching.assembleMovesOnlyForPalmon(id, data, includeLevel); // finding the Moves for the current Palmon and saving them in an ArrayList
            palmonMovesDb.put(palmon.getId(), palmon_moves); // saving the current Palmon with its Moves (saved in the ArrayList) in the HashMap
        }
        return palmonMovesDb;
    }
}