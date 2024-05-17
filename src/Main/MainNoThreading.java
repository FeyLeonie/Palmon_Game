package Main;

import Gameplay.Game;
import csv_handling.CSV_Reader;
import csv_handling.CSV_Searching;
import data_structures.Printing;

public class MainNoThreading
{
    public static void main(String[] args) throws InterruptedException {
        // Erstellen der Instanzen der Klassen
        CSV_Reader data = new CSV_Reader();
        CSV_Searching selection = new CSV_Searching(data);
        Game game = new Game(data, selection);

        // Erstellen und Starten der Threads für jede Klasse
        data.run();
        data.PalmonDataReader();
        data.MoveDataReader();
        data.PalmonMoveDataReader();
        data.EffectivityDataReader();
        game.run();
    }
}