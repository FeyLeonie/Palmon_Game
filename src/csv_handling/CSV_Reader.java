package csv_handling;

import elements.ConPalmonMove;
import elements.Move;
import elements.Palmon;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class CSV_Reader extends Thread // extends Thread
{
    // Paths
    final String path_palmon = "C://Users//Fey//IdeaProjects//Programmierprojekt//src//Data_Sources//palmon.csv"; // Path Palmon
    final String path_move = "C://Users//Fey//IdeaProjects//Programmierprojekt//src//Data_Sources//moves.csv";
    final String path_palmonmove = "C://Users//Fey//IdeaProjects//Programmierprojekt//src//palmon_move.csv";

    // HashMaps
    public HashMap <Integer, Palmon> palmon_db = new HashMap <>(); // Storage medium for Palmon, Key: ID
    public HashMap<Integer, Move> move_db = new HashMap<>(); // Storage medium for Move, Key: ID

    public HashMap<Integer, ConPalmonMove> palsMoves = new HashMap<>(); // All Moves for the Palmon, Key: Palmon ID
    public HashMap<Integer, ConPalmonMove> movesForPals = new HashMap<>(); // All Moves listed with further information out of CSV PalmonMove, Key: Move ID

    public void run()
    {
        CSV_Reader reader = new CSV_Reader();
        reader.PalmonDataReader();
        reader.MoveDataReader();
        reader.PalmonMoveDataReader();

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

    public void PalmonMoveDataReader()
    {
        Palmon palmon;
        Move move;
        int level;

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
                    String [] palmonMoveDetails = dataset.split(";"); // splitting the dataset into its pieces

                    palmon = palmon_db.get(Integer.parseInt(palmonMoveDetails[0]));
                    move = move_db.get(Integer.parseInt(palmonMoveDetails[1]));
                    level = Integer.parseInt(palmonMoveDetails[2]);

                    ConPalmonMove conpalmonmove = new ConPalmonMove(palmon, move, level);

                    palsMoves.put(palmon.getId(), conpalmonmove);
                    movesForPals.put(move.getId(), conpalmonmove);
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