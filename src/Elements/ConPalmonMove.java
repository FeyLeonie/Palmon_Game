package Elements;

/**
 * Represents an ConPalmonMove entity with attributes and behaviors.
 */
public class ConPalmonMove // Connection class for Palmon and Moves
{
    Palmon palmon;
    Move move;
    int level;

    /**
     * Constructor to initialize an Effectivity object with the given attributes
     *
     * @param palmon  the current Palmon
     * @param move    the current Move
     * @param level   the learned on level, where the Palmon is able to use the Move
     */
    public ConPalmonMove(Palmon palmon, Move move, int level)
    {
        this.palmon = palmon;
        this.move = move;
        this.level = level;
    }

    /**
     * gets the Move
     *
     * @return The Move
     *
     * Software runtime complexity is O(1)
     */
    public Move getMove()
    {
        return move;
    }

    /**
     * gets the learned on Level
     *
     * @return The level
     *
     * Software runtime complexity is O(1)
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * gets the Palmon
     *
     * @return The Palmon
     *
     * Software runtime complexity is O(1)
     */
    public Palmon getPalmon()
    {
        return palmon;
    }
}
