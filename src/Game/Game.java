package Game;

import csv_handling.CSV_Reader;
import csv_handling.CSV_Searching;
import data_structures.Printing;
import elements.Palmon;
import elements.Move;

import java.util.*;

public class Game
{
    Printing print;
    CSV_Reader data;
    CSV_Searching searching;
    Game game;

    public Game(Printing print, CSV_Reader data, CSV_Searching selection)
    {
        this.print = print;
        this.data = data;
        this.searching = selection;
    }

    public void initialMenuPlayer(CSV_Searching searching)
    {
        int playersize = 0;
        playersize = print.printsc("Amount of Palmons in your Team?", playersize);

        int selection = 0;
        selection = print.printsc("How do you want to assemble your team? \\(1) randomly \\(2) by id \\(3) by type", selection);

        Stack<Palmon> playerPalmons = new Stack<>();
        switch(selection)
        {
            case 1:
                playerPalmons = assembleRandomly(playersize);
                break;

            case 2:
                playerPalmons = assembleById(playersize);
                break;

            case 3:
                playerPalmons = assembleByFirstType(playersize, searching);
                break;
        }

        initialMenuEnemy(playersize);
        HashMap<Integer, ArrayList<Move>> PlayersPalmonsWithMoves = setMovesForPalmon(data, playerPalmons);
    }

    public void initialMenuEnemy(int playersize)
    {
        int enemysize = 0;
        enemysize = print.printsc("Amount of Palmons in your Enemys Team? \\(0) randomly", enemysize);

        while(enemysize == 0) // no if because Random could randomly choose 0 -> another round would be needed
        {
            Random r = new Random();
            enemysize = r.nextInt(playersize * 2);
            print.print(enemysize + " Palmons will be fighting in your enemys team!");
        }

        Stack<Palmon> enemyPalmons = assembleRandomly(enemysize); // Always choosing the enemys Palmons by random
        HashMap<Integer, ArrayList<Move>> EnemysPalmonsWithMoves = setMovesForPalmon(data, enemyPalmons);
    }

    public Stack<Palmon> assembleRandomly(int teamsize)
    {
        HashMap<Integer, Palmon> palmon_db = data.palmon_db;

        Random r = new Random();
        int randomIndex;
        Palmon randomPalmon = null;

        Stack<Palmon> team = new Stack<>();

        for(int i = 0; i < teamsize; i++)
        {
            randomIndex = r.nextInt(palmon_db.size());
            randomPalmon = palmon_db.get(randomIndex);
            if(randomPalmon != null)
            {
                team.push(randomPalmon);
            }
        }
        return team;
    }

    public Stack<Palmon> assembleById(int teamsize)
    {
        HashMap<Integer, Palmon> palmon_db = data.palmon_db;
        Stack<Palmon> team = new Stack<>();
        int id = 0;

        for(int i = 0; i < teamsize; i++)
        {
            id = print.printsc("select your next Palmon by tiping your preferred ID", id);

            if(palmon_db.containsKey(id))
            {
                team.push(palmon_db.get(id));
            }
            else
            {
                print.print("No Palmon with preferred ID. Please choose a different ID");
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

    public Stack<Palmon> assembleByFirstType(int teamsize, CSV_Searching searching)
    {
        Stack<Palmon> team = new Stack<>();

        for(int count = 0; count < teamsize; count++)
        {
            if(count == 0) // only relevant for first round to display the amount in print correctly
            {
                count++;
            }

            String type = "";
            print.print("What type should the " + count + ". Palmon be? (please type in the name)");

            HashSet<String> types = searching.saveAllTypes(data);
            for(String alltypes: types)
            {
                System.out.println(type);
            }

            HashMap<String, Palmon> selected_type = searching.sortByFirstType(type, data);
            Set<String> palmon_names = selected_type.keySet(); // Speichern aller Keys der HashMap, also die Namen aller Palmons

            count = 0; // counter auf 0 setzen
            for(String name: palmon_names)
            {
                if(count < 15) // wenn 15 Namen ausgegeben wurden wird gestoppt, um den User nicht zu überfordern
                {
                    print.print(name);
                    count++;
                }
                else
                {
                    break;
                }
            }

            String decision = "";
            decision = print.printssc("These are 15 Palmons of the desired type. Choose one (by entering its name).", decision);
            team.push(selected_type.get(decision)); // putting the desired Palmon in the team Stack
        }
        return team;
    }

    public HashMap<Integer, ArrayList<Move>> setMovesForPalmon(CSV_Reader data, Stack<Palmon> palmons)
    {
        ArrayList<Move> palmon_moves = new ArrayList<Move>(); // ArrayList for saving all Moves for the different Palmons
        HashMap<Integer, ArrayList<Move>> palmonMovesDb = new HashMap<>(); // saving the Moves in an ArrayList and connecting that with the Palmons in this HashMap

        for(Palmon palmon: palmons) // iterating through every Palmon
        {
            palmon_moves = searching.assembleMovesOnlyForPalmon(palmon.getId(), data);
            palmonMovesDb.put(palmon.getId(), palmon_moves); // saving the current Palmon with its Moves in the HashMap
        }
        return palmonMovesDb;
    }
}