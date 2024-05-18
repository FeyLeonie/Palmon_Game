package elements;

public class ConPalmonMove // Connection class for Palmon and Moves
{
    Palmon palmon;
    Move move;
    int level;

    public ConPalmonMove(Palmon palmon, Move move, int level)
    {
        this.palmon = palmon;
        this.move = move;
        this.level = level;
    }

    public Move getMove()
    {
        return move;
    }

    public int getLevel()
    {
        return level;
    }

    public Palmon getPalmon()
    {
        return palmon;
    }
}
