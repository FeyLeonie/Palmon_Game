package Main;

import Gameplay.Game;
import Gameplay.InitialMenu;
import csv_handling.CSV_Reader;
import csv_handling.CSV_Searching;

public class MainThread
{
    public static void main (String[] args) throws InterruptedException {
        InitialMenu menu = new InitialMenu();
        menu.start();

        CSV_Reader data = new CSV_Reader();
        data.start();

        // wait for the end of the threads
        menu.join();
        data.join();

        CSV_Searching selection = new CSV_Searching(data); // needed for starting Game
        Game game = new Game(data, selection);
        game.run();
    }
}
