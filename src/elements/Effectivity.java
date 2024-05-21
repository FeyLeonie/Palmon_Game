package elements;

/**
 * Represents an Effectivity entity with attributes and behaviors.
 */
public class Effectivity
{
    String attackerType;
    String targetType;
    double damage;

    /**
     * Constructor to initialize an Effectivity object with the given attributes
     *
     * @param attackerType  the type of the Palmon that is attacking
     * @param targetType    the type of the Palmon that is defending itself
     * @param damage        the damage factor for the attack
     */
    public Effectivity(String attackerType, String targetType, double damage)
    {
        this.attackerType = attackerType;
        this.targetType = targetType;
        this.damage = damage;
    }

    /**
     * gets the Attacker Type
     *
     * @return The Attacker Type
     *
     * Software runtime complexity is O(1)
     */
    public String getAttackerType()
        {
            return attackerType;
        }

    /**
     * gets the Target Type
     *
     * @return The Target Type
     *
     * Software runtime complexity is O(1)
     */
    public String getTargetType()
        {
            return targetType;
        }

    /**
     * gets the Damage Type
     *
     * @return The Damage Type
     *
     * Software runtime complexity is O(1)
     */
    public double getDamageFactor()
        {
            return damage;
        }
}
