package Main;

import CSVHandling.CSVHandler;
import CSVHandling.CSVSearching;
import Gameplay.Game;
import Gameplay.InitialMenu;

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
     * Software Runtime is O(1)
     */
    public static void main (String[] args) throws InterruptedException {
        // starting the Initial Menu while the CSV Data is Loading
        InitialMenu menu = new InitialMenu();
        // O(1)
        menu.start();

        // Loading the CSV Data (meanwhile the Initial Menu is loading)
        CSVHandler data = new CSVHandler();
        // 0(1)
        data.start();

        // wait for the end of the threads => 0(1)
        menu.join();
        data.join();

        CSVSearching selection = new CSVSearching(data); // needed to starting the Game
        Game game = new Game(data, selection);
        game.run();
    }
}
