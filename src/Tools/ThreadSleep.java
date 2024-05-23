package Tools;

/**
 * The {@code ThreadSleep} class provides a method to pause the execution of the console for a specified amount of milliseconds.
 *
 * Use case: increasing the User-Experience by not overwhelming the user with too much text at once.
 */

public class ThreadSleep
{
    /**
     * Pauses the execution for the specified number of seconds.
     *
     * @param milliseconds the number of milliseconds to pause
     *
     * Software Runtime is O(1)
     */
    public static void sleep(int milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds); //Converting the milliseconds in seconds to be able to work with the Method easier
        }
        catch (InterruptedException e) // Catching an error
        {
            Thread.currentThread().interrupt();
        }
    }
}
