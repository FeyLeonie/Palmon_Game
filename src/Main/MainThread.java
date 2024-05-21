package Main;

import Gameplay.Game;
import Gameplay.InitialMenu;
import csv_handling.CSV_Reader;
import csv_handling.CSV_Searching;

/** Starting the Game with Threading
 * The {@code MainThread} class "entry point" for the application.
 * MainThread initializes and starts the initial menu and CSV reader threads,
 * waits for them to complete, and then starts the game.
 */
public class MainThread
{
    /**
     * The main method initializes and starts the initial menu and CSV-Reader threads,
     * waits for them to complete, and then starts the game.
     * => meanwhile the CSV-Reader is loading the data, the initial Menu is interacting with the User
     *
     * @param args command-line arguments (not used)
     * @throws InterruptedException if any thread is interrupted while waiting
     *
     * The software runtime complexity is O(1) for starting the threads.
     * The join() method has a software runtime complexity of O(1) for waiting for thread completion.
     */
    public static void main (String[] args) throws InterruptedException {
        InitialMenu menu = new InitialMenu();
        // O(1)
        menu.start();

        CSV_Reader data = new CSV_Reader();
        // 0(1)
        data.start();

        // wait for the end of the threads => 0(1)
        menu.join();
        data.join();

        CSV_Searching selection = new CSV_Searching(data); // needed for starting Game
        Game game = new Game(data, selection);
        game.run();
    }
}
