package Gameplay;

import csv_handling.CSV_Reader;
import csv_handling.CSV_Searching;
import data_structures.Printing;
import elements.Palmon;
import elements.Move;

import data_structures.Queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Game extends Printing
{
    CSV_Reader data;
    CSV_Searching searching;


    public Queue<Palmon> playerPalmons;
    public Queue<Palmon> enemyPalmons;

    public Game(CSV_Reader data, CSV_Searching selection)
    {
        this.data = data;
        this.searching = selection;
    }

    // @Override
    public void run()
    {
        Game game = new Game(data, searching);
        Fight fight = new Fight(game, data);
        game.teamSettings(searching, fight);
    }

    public void teamSettings(CSV_Searching searching, Fight fight)
    {
        // Team Settings Palmons Player
        print("\n" + InitialMenu.playerName + ", you are now in the team settings. Here, you will assemble your team for battle and make adjustments to your opponent's team settings.");

        // asking for Players Teamsize
        int playersize = 0;
        playersize = printsc("How many Palmons would you like to have in your team, " +InitialMenu.playerName+ "? Choose wisely.", playersize);

        // asking for Team assembling method
        int selection = 0;
        selection = printsc("How do you want to assemble your team? \n(1) randomly \n(2) by id \n(3) by type", selection);

        // assembling the Palmons for the Player (regarding the choice the Player made)
        playerPalmons = new Queue<>();
        switch(selection)
        {
            case 1: // assembling randomly
                playerPalmons = assembleRandomly(playersize, playerPalmons);
                break;

            case 2: // assembling by ID
                playerPalmons = assembleById(playersize, playerPalmons);
                break;

            case 3: // assembling by type
                playerPalmons = assembleByFirstType(playersize, searching, playerPalmons);
                break;
        }

        // Setting Moves for Players Palmons
        HashMap<Integer, ArrayList<Move>> playersPalMoves = setMovesForPalmon(data, playerPalmons);



        // Team Settings Palmons Enemy

        int enemysize = 0;
        enemysize = printsc("How many Palmons would you like in your the team from " + InitialMenu.enemyName+ "? \n(0) randomly \n(type in your preferred number)", enemysize);

        while(enemysize == 0) // no if because Random could randomly choose 0 -> another round would be needed
        {
            Random r = new Random();
            enemysize = r.nextInt(playersize * 2);
            if(enemysize != 0)
            {
                print(enemysize + " Palmons will be fighting in your opponent's team, " + InitialMenu.enemyName+ "!");
            }
        }

        // assembling the Palmons for the Enemy (always Random)
        enemyPalmons = new Queue<>();
        enemyPalmons = assembleRandomly(enemysize, enemyPalmons); // Always choosing the enemys Palmons by random

        // Setting Moves for Enemies Palmons
        HashMap<Integer, ArrayList<Move>> enemiesPalMoves = setMovesForPalmon(data, enemyPalmons);

        //
        print("Your team is set, " +InitialMenu.playerName+ ". Prepare for battle against " +InitialMenu.enemyName+ "! Let the clash of Palmons begin!");

        // Start the Fight
        fight.FightOverview(playerPalmons, enemyPalmons, enemiesPalMoves, playersPalMoves);
    }

    public Queue<Palmon> assembleRandomly(int teamsize, Queue<Palmon> team)
    {
        HashMap<Integer, Palmon> palmon_db = CSV_Reader.palmon_db;

        Random r = new Random();
        int randomIndex;
        Palmon randomPalmon = null;

        for(int i = 0; i < teamsize; i++)
        {
            randomIndex = r.nextInt(palmon_db.size());
            randomPalmon = palmon_db.get(randomIndex);
            if(randomPalmon != null)
            {
                team.enqueue(randomPalmon);
            }
            else
            {
                i--;
            }
        }
        return team;
    }

    public Queue<Palmon> assembleById(int teamsize, Queue<Palmon> team)
    {
        HashMap<Integer, Palmon> palmon_db = CSV_Reader.palmon_db;
        int id = 0;

        for(int i = 0; i < teamsize; i++)
        {
            id = printsc("select your next Palmon by tiping your preferred ID", id);

            if(palmon_db.containsKey(id))
            {
                team.enqueue(palmon_db.get(id));
            }
            else
            {
                print("No Palmon with preferred ID. Please choose a different ID");
                if(i!=0)
                {
                    i--;
                }
                else
                {
                    teamsize += 1;
                }
            }
        }
        return team;
    }

    public Queue<Palmon> assembleByFirstType(int teamsize, CSV_Searching searching, Queue<Palmon> team)
    {
        for(int i = 1; i <= teamsize; i++)
        {
            HashSet<String> types = searching.saveAllPalmonTypes(data);
            for(String alltypes: types)
            {
                if(!alltypes.equals("no type found."))
                {
                    System.out.println(alltypes);
                }
            }

            String type = "";
            type = printssc("What type should the " + i + ". Palmon be? (please type in the name)", type);

            HashMap<String, Palmon> selected_type = searching.sortByPalmonType(type, data);
            HashSet<String> palmon_names = new HashSet<>(selected_type.keySet()); // Speichern aller Keys der HashMap, also die Namen aller Palmons

            int count = 0; // counter auf 0 setzen
            for(String name: palmon_names)
            {
                if(count <= 15) // wenn 15 Namen ausgegeben wurden wird gestoppt, um den User nicht zu überfordern
                {
                    print(name);
                    count++;
                }
                else
                {
                    break;
                }
            }

            String decision = "";
            decision = printssc("These are 15 Palmons of the desired type. Choose one (by entering its name).", decision);
            team.enqueue(selected_type.get(decision)); // putting the desired Palmon in the team Queue
        }
        return team;
    }

    public HashMap<Integer, ArrayList<Move>> setMovesForPalmon(CSV_Reader data, Queue<Palmon> palmons)
    {
        ArrayList<Move> palmon_moves; // ArrayList for saving all Moves for the different Palmons
        HashMap<Integer, ArrayList<Move>> palmonMovesDb = new HashMap<>(); // saving the Moves in an ArrayList and connecting that with the Palmons in this HashMap

        ArrayList<Palmon> tempPalmons = palmons.toArrayList(); // saving the queue Palmons into an ArrayList to work with them

        for(Palmon palmon: tempPalmons) // iterating through every Palmon
        {
            palmon_moves = searching.assembleMovesOnlyForPalmon(palmon.getId(), data); // finding the Moves for the current Palmon and saving them in an ArrayList
            palmonMovesDb.put(palmon.getId(), palmon_moves); // saving the current Palmon with its Moves (saved in the ArrayList) in the HashMap
        }
        return palmonMovesDb;
    }
}