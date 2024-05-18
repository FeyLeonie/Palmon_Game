package csv_handling;

import data_structures.MultiHashMap;
import elements.ConPalmonMove;
import elements.Move;
import elements.Palmon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet; // for saving all types since HashSet does not save duplicates

public class CSV_Searching extends Thread
{
    CSV_Reader data;

    public CSV_Searching(CSV_Reader data)
    {
        this.data = data;
    }

    public HashSet<String> saveAllPalmonTypes(CSV_Reader data)
    {
        // HashSet to store all possible types
        HashSet<String> types = new HashSet<>();

        //  Iterating through the palmon_db to collect all types
        for (Palmon palmon : CSV_Reader.palmon_db.values()) {
            types.add(palmon.getTypeOne());
        }

        return types;
    }

    public HashMap<String, Palmon> sortByPalmonType(String preferred_type, CSV_Reader data)
    {
        HashMap<Integer, Palmon> palmon_db = CSV_Reader.palmon_db;

        HashMap<String, Palmon> type1_normal = new HashMap<>();
        HashMap<String, Palmon> type1_fighting = new HashMap<>();
        HashMap<String, Palmon> type1_flying = new HashMap<>();
        HashMap<String, Palmon> type1_poison = new HashMap<>();
        HashMap<String, Palmon> type1_ground = new HashMap<>();
        HashMap<String, Palmon> type1_rock = new HashMap<>();
        HashMap<String, Palmon> type1_electric = new HashMap<>();
        HashMap<String, Palmon> type1_psychic = new HashMap<>();
        HashMap<String, Palmon> type1_ice = new HashMap<>();
        HashMap<String, Palmon> type1_dragon = new HashMap<>();
        HashMap<String, Palmon> type1_dark = new HashMap<>();
        HashMap<String, Palmon> type1_fairy = new HashMap<>();
        HashMap<String, Palmon> type1_steel = new HashMap<>();
        HashMap<String, Palmon> type1_water = new HashMap<>();
        HashMap<String, Palmon> type1_ghost = new HashMap<>();
        HashMap<String, Palmon> type1_bug = new HashMap<>();
        HashMap<String, Palmon> type1_grass = new HashMap<>();
        HashMap<String, Palmon> type1_fire = new HashMap<>();

        String type;

        for (Palmon palmon : palmon_db.values()) {
            if (palmon != null) {
                type = palmon.getTypeOne();

                switch (type) {
                    case "normal":
                        type1_normal.put(palmon.getName(), palmon);
                        break;
                    case "fighting":
                        type1_fighting.put(palmon.getName(), palmon);
                        break;
                    case "flying":
                        type1_flying.put(palmon.getName(), palmon);
                        break;
                    case "poison":
                        type1_poison.put(palmon.getName(), palmon);
                        break;
                    case "ground":
                        type1_ground.put(palmon.getName(), palmon);
                        break;
                    case "rock":
                        type1_rock.put(palmon.getName(), palmon);
                        break;
                    case "electric":
                        type1_electric.put(palmon.getName(), palmon);
                        break;
                    case "psychic":
                        type1_psychic.put(palmon.getName(), palmon);
                        break;
                    case "ice":
                        type1_ice.put(palmon.getName(), palmon);
                        break;
                    case "dragon":
                        type1_dragon.put(palmon.getName(), palmon);
                        break;
                    case "dark":
                        type1_dark.put(palmon.getName(), palmon);
                        break;
                    case "fairy":
                        type1_fairy.put(palmon.getName(), palmon);
                        break;
                    case "steel":
                        type1_steel.put(palmon.getName(), palmon);
                        break;
                    case "water":
                        type1_water.put(palmon.getName(), palmon);
                        break;
                    case "ghost":
                        type1_ghost.put(palmon.getName(), palmon);
                        break;
                    case "bug":
                        type1_bug.put(palmon.getName(), palmon);
                        break;
                    case "grass":
                        type1_grass.put(palmon.getName(), palmon);
                        break;
                    case "fire":
                        type1_fire.put(palmon.getName(), palmon);
                        break;
                }
            }
        }

        switch (preferred_type) {
            case "normal":
                return type1_normal;
            case "fighting":
                return type1_fighting;
            case "flying":
                return type1_flying;
            case "poison":
                return type1_poison;
            case "ground":
                return type1_ground;
            case "rock":
                return type1_rock;
            case "electric":
                return type1_electric;
            case "psychic":
                return type1_psychic;
            case "ice":
                return type1_ice;
            case "dragon":
                return type1_dragon;
            case "dark":
                return type1_dark;
            case "fairy":
                return type1_fairy;
            case "steel":
                return type1_steel;
            case "water":
                return type1_water;
            case "ghost":
                return type1_ghost;
            case "bug":
                return type1_bug;
            case "grass":
                return type1_grass;
            case "fire":
                return type1_fire;
            default:
                System.out.println("no type found.");
                return new HashMap<>(); // Falls der Type nicht gefunden wird, wird eine leere HashMap zurückgegeben
        }
    }

    // collects all Moves for the Palmon given (int id) and puts them into an ArrayList
    public ArrayList<Move> assembleMovesOnlyForPalmon(int id, CSV_Reader data, boolean includeLevel)
    {
        ArrayList<Move> palmon_move = new ArrayList<>(); // the possible Moves for the Palmon will be saved in here
        ArrayList <ConPalmonMove> palsMoves = CSV_Reader.palsMoves; // "importing" the MultiHashMap with all the Palmon IDs and the possible Moves

        Palmon palmon = CSV_Reader.palmon_db.get(id); // saving the current Palmon

        boolean putInQueue;
        Move currentMove;

            // Iterate over each ConPalmonMove object for the Palmon ID
            for (ConPalmonMove palMove : palsMoves)
            {
                if(palMove.getPalmon().getId() == id)
                {
                    putInQueue = true;

                    if(includeLevel)
                    {
                        if(palmon.getLevel() < palMove.getLevel())
                        {
                            putInQueue = false;
                        }
                    }

                    if(putInQueue)
                    {
                        if(palMove.getMove() != null)
                        {
                            palmon_move.add(palMove.getMove());
                        }
                    }
                }
            }

        ArrayList<Move> fourStrongestMoves = assembleFourStrongestMoves(palmon_move);

        return fourStrongestMoves; // every possible Move for the Palmon
    }

    public ArrayList<Move> assembleFourStrongestMoves(ArrayList<Move> palmon_move)
    {
        //TODO fix

        ArrayList<Move> fourStrongestMoves = new ArrayList<>();
        ArrayList<Move> tempPalmonMove = new ArrayList<>(palmon_move);

        if(tempPalmonMove.size() <= 4) // if there are only less than 4 Moves possible to choose from, nothing has to be done
        {
            return tempPalmonMove;
        }

        // finding the Four strongest Moves by Damage (inspired by Selectionsort, I know its slow but since we only need 4 elements its not needed to sort the whole ArrayList)
        for (int i = 0; i < 4; i++)
        {
            int maxIndex = 0;

            // find the move with the maximum damage
            for (int j = 1; j < tempPalmonMove.size(); j++)
            {
                if (tempPalmonMove.get(j).getDamage() > tempPalmonMove.get(maxIndex).getDamage())
                {
                    if (tempPalmonMove.get(j).getDamage() > tempPalmonMove.get(maxIndex).getDamage())
                    {
                        maxIndex = j;
                    }
                }
            }

            // add the move with the maximum damage to the ArrayList
            fourStrongestMoves.add(tempPalmonMove.get(maxIndex));
            tempPalmonMove.remove(maxIndex); // removing the move to find the next one with the highest damage
        }
        return fourStrongestMoves;
    }
}