package csv_handling;

import data_structures.MultiHashMap;
import elements.ConPalmonMove;
import elements.Effectivity;
import elements.Move;
import elements.Palmon;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class CSV_Reader extends Thread
{
    // Paths
    final String path_palmon = "C://Users//Fey//IdeaProjects//AA_Latest_Palmon//src//csv_dateien//palmon.csv"; // Path Palmon
    final String path_move = "C://Users//Fey//IdeaProjects//AA_Latest_Palmon//src//csv_dateien//moves.csv";
    String path_effectivity  = "C://Users//Fey//IdeaProjects//AA_Latest_Palmon//src//csv_dateien//effectivity.csv";
    final String path_palmonmove = "C://Users//Fey//IdeaProjects//AA_Latest_Palmon//src//csv_dateien//palmon_move.csv";

    // HashMaps
    public static HashMap <Integer, Palmon> palmon_db = new HashMap <>(); // Storage medium for Palmon, Key: ID
    public static HashMap<Integer, Move> move_db = new HashMap<>(); // Storage medium for Move, Key: ID

    // own Data structure MultiHashMap
    public static MultiHashMap<Integer, Move> palsMoves = new MultiHashMap<>(); // All Moves for the Palmon, Key: Palmon ID
    public MultiHashMap<Move, Integer> movesForPals = new MultiHashMap<>(); // All Moves listed with further information out of CSV PalmonMove, Key: Move ID

    // ArrayLists
    public static ArrayList<Effectivity> effectivity_db  = new ArrayList <>(); // Storage medium for Effectivity

    // @Override
    public void run()
    {
        CSV_Reader reader = new CSV_Reader();
        reader.PalmonDataReader();
        reader.MoveDataReader();
        // reader.PalmonMoveDataReader();
        reader.EffectivityDataReader();
    }

    public void PalmonDataReader()
    {
        try (BufferedReader br = new BufferedReader(new FileReader(path_palmon)))
        {
            boolean reading = true;
            br.readLine(); // skipped: not needed since it is a header
            String dataset; // current record of the CSV

            while(reading) // reads until there are no more records available.
            {
                dataset = br.readLine(); // for storing the current line of the CSV file

                if(dataset == null) // if the current record is empty
                {
                    reading = false; // reading is set to false and the loop doesn't execute another iteration
                }
                else
                {
                    String [] palmondetails = dataset.split(";");

                    Palmon palmon = new Palmon(palmondetails);
                    palmon_db.put(Integer.parseInt(palmondetails[0]), palmon);
                }
            }
        }
        catch (FileNotFoundException e)
        { // applies when there is no file
            System.err.println("The Palmon file was not found. Please check the file path and restart the program.");
            System.exit(0); // ends the program
        }
        catch(Exception e)
        {
            System.out.println("Unexpected issue (Palmon). Please contact support (and run screaming in circles).");
        }
    }

    public void MoveDataReader()
    {
        try (BufferedReader br = new BufferedReader(new FileReader(path_move)))
        {
            boolean reading = true;
            br.readLine(); // skipped: not needed since it is a header
            String dataset; // temporäre Variable für den aktuellen Datensatz der CSV

            while(reading) // liest so lange, bis es keinen Datensatz mehr gibt
            {
                dataset = br.readLine(); // zum Zwischenspeichern der aktuellen Zeile der CSV-Datei

                if(dataset == null) // Wenn der aktuelle Datensatz leer ist
                {
                    reading = false; // geht weiterlesen auf false und die Schleife führt keinen neuen Durchlauf aus
                }
                else
                {
                    String [] movedetails = dataset.split(";");

                    Move move = new Move(movedetails);
                    move_db.put(Integer.parseInt(movedetails[0]), move);
                }
            }
        }
        catch (FileNotFoundException e)
        { // greift, wenn es keine Datei gibt
            System.err.println("Die Move Datei wurde nicht gefunden. Bitte überprüfen Sie den Dateipfad und starten Sie das Programm erneut.");
            System.exit(0); // Beendet das Programm
        }
        catch(Exception e)
        {
            System.out.println("unerwartetes Problem (Move). Bitte kontaktier den Support (und renne schreiend im Kreis)");
        }
    }

    public void EffectivityDataReader()
    {
        try (BufferedReader br = new BufferedReader(new FileReader(path_effectivity))) // setting up the Buffered Reader
        {
            boolean keep_reading = true;
            String header = br.readLine(); // first line is skipped since its a header
            String effectivity; // temporary variable for the current record

            while(keep_reading) // as long as there is still another record
            {
                effectivity = br.readLine(); // temporarily storing the current line

                if(effectivity == null) // if the current record is empty
                {
                    keep_reading = false; // loop does not execute a new iteration.
                }
                else
                {
                    // splitting the record in its attributes
                    String [] effectivitydetails = effectivity.split(";");

                    // Converting some attributes to int, storing the rest for clarity (see Effectivity constructor)
                    String damage_factor_percentage = effectivitydetails[2]; // Storage initially in a String because of %.
                    String damage_factor_nopercentage = damage_factor_percentage.replace("%", ""); // String without the %
                    double damage_factor_ohne_percentage_converted = Double.parseDouble(damage_factor_nopercentage); // String conversion
                    double damage_factor = damage_factor_ohne_percentage_converted / 100.0; // Final variable for percentage calculation

                    Effectivity e1 = new Effectivity(effectivitydetails[0], effectivitydetails[1], damage_factor);

                    // Effectivity is threaded into the ArrayList
                    effectivity_db.add(e1);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            // if the path is incorrect
            System.err.println("Needed Effectivity file was not found. Please check the file and restart the program. Crying would be a possibility as well.");
            System.exit(0); // Beendet das Programm
        }
        catch(Exception e)
        {
            // if an unexpected error is occuring
            System.out.println("Unexpected issue (while reading and saving the Effectivity). Please contact support (and run screaming in circles).");
        }
    }

    public void PalmonMoveDataReader()
    {
        int palmonId;
        Move move;
        int level;

        try (BufferedReader br = new BufferedReader(new FileReader(path_palmonmove)))
        {
            boolean reading = true;
            br.readLine(); //a skipped: not needed since it is a header
            String dataset; // current record of the CSV

            while(reading) // reads until there are no more records available.
            {
                dataset = br.readLine(); // for storing the current line of the CSV file

                if(dataset == null) // if the current record is empty
                {
                    reading = false; // reading is set to false and the loop doesn't execute another iteration
                }
                else
                {
                    String [] palmonMoveDetails = dataset.split(";"); // splitting the dataset into its pieces

                    palmonId = Integer.parseInt(palmonMoveDetails[0]);
                    move = move_db.get(Integer.parseInt(palmonMoveDetails[1])); // searching for the correct move
                    level = Integer.parseInt(palmonMoveDetails[2]); // pre saving the level

                    // ConPalmonMove conpalmonmove = new ConPalmonMove(palmon, move, level);

                    palsMoves.put(palmonId, move); // MultiHashMap, Key Palmons
                    movesForPals.put(move, palmonId); // HashMap, Key Moves
                }
            }
        }
        catch (FileNotFoundException e)
        { // applies when there is no file
            System.err.println("The PalmonMove file was not found. Please check the file path and restart the program.");
            System.exit(0); // ends the program
        }
        catch(Exception e)
        {
            System.out.println("Unexpected issue (PalmonMove). Please contact support (and run screaming in circles).");
        }
    }
}