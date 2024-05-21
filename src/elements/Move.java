package elements;

import tools.Normalizer;

/**
 * Represents a Palmon entity with attributes and behaviors.
 */
public class Move
{
    final int id;
    String name;
    final int damage;
    final int accuracy;
    final String type;
    int max_usages;
    int usages = 0;

    /**
     * Constructor to initialize a Palmon object with the given details.
     *
     * @param movedetails An array containing Move details.
     */
    public Move(String [] movedetails)
    {
        this.id = Integer.parseInt(movedetails[0]);
        this.name = movedetails[1];
        this.damage = Integer.parseInt(movedetails[2]);
        this.max_usages = Integer.parseInt(movedetails[3]);
        this.accuracy = Integer.parseInt(movedetails[4]);
        this.type = movedetails[5];
    }

    /**
     * increases the usages by one
     *
     * Software runtime complexity is O(1)
     */
    public void useMove()
    {
        this.usages+=1;
    }

    /**
     * calculates and returns the amount of usages left
     *
     * @return The amount of usages left from the Move.
     *
     * Software runtime complexity is O(1)
     */
    public int usagesLeft() {
        int usagesLeft = max_usages - usages;
        return usagesLeft;
    }

    /**
     * gets the Name of the Move.
     *
     * @return The name of the Move.
     *
     * Software runtime complexity is O(1)
     */
    public String getName()
    {
        return name;
    }

    /**
     * gets the damage from the Move.
     *
     * @return The damage from the Move.
     *
     * Software runtime complexity is O(1)
     */
    public int getDamage()
    {
        return damage;
    }

    /**
     * gets the accuracy of the Move.
     *
     * @return The accuracy of the Move.
     *
     * Software runtime complexity is O(1)
     */
    public int getAccuracy()
    {
        return accuracy;
    }

    /**
     * normalizes the Name of the Move (by using class Normalizer)
     *
     * Software runtime complexity is O(1)
     */
    public void normalizeMoveName()
    {
        name = Normalizer.normalize(name);
    }
}
