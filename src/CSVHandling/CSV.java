package CSVHandling;

/**
 * The CSV interface defines the contract for any class that handles
 * reading data from CSV files related to Palmon data, move data, Effectivity data, and the relationship between Palmon and moves.
 */
public interface CSV
{
    /**
     * Reads Palmon data from a CSV file and stores it in the appropriate data structure.
     */
    public void PalmonDataReader();

    /**
     * Reads move data from a CSV file and stores it in the appropriate data structure.
     */
    public void MoveDataReader();

    /**
     * Reads effectivity data from a CSV file and stores it in the appropriate data structure.
     */
    public void EffectivityDataReader();

    /**
     * Reads the relationships between Palmon and their moves from a CSV file and stores it in the appropriate data structure.
     */
    public void PalmonMoveDataReader();
}
