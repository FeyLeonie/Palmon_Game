package Main;

import csv_handling.CSV_Reader;
import csv_handling.CSV_Searching;
import data_structures.Printing;
import Game.Game;

public class Main
{
    public static void main(String[] args) {
        Printing print = new Printing();
        CSV_Reader data = new CSV_Reader();
        CSV_Searching searching = new CSV_Searching(data);

        data.PalmonDataReader();

        Game game = new Game(print, data, searching);
        game.initialMenuPlayer(searching);
    }
}
