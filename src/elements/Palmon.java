package elements;

/**
 * Represents a Palmon entity with attributes and behaviors.
 */
public class Palmon
{
    final int id;
    final String name;
    final int height;
    final int weight;
    final String[] type = new String[2];
    double hp;
    final int attack;
    final int defense;
    final int speed;
    int level;

    /**
     * Constructor to initialize a Palmon object with the given details.
     *
     * @param palmondetails An array containing Palmon details.
     * @param level         The level of the Palmon.
     */
    public Palmon (String [] palmondetails, int level)
    {
        id      = Integer.parseInt(palmondetails[0]);
        name    = palmondetails[1];
        height  = Integer.parseInt(palmondetails[2]);
        weight  = Integer.parseInt(palmondetails[3]);
        type[0] = palmondetails[4];
        type[1] = palmondetails[5];
        hp      = Double.parseDouble(palmondetails[6]);
        attack  = Integer.parseInt(palmondetails[7]);
        defense = Integer.parseInt(palmondetails[8]);
        speed   = Integer.parseInt(palmondetails[9]);
        this.level = level;
    }

    /**
     * gets the ID of the Palmon.
     *
     * @return The ID of the Palmon.
     *
     * Software runtime complexity is O(1)
     */
    public int getId()
    {
        return id;
    }

    /**
     * gets the Speed of the Palmon.
     *
     * @return The speed of the Palmon.
     *
     * Software runtime complexity is O(1)
     */
    public int getSpeed()
    {
        return speed;
    }

    /**
     * gets the name of the Palmon.
     *
     * @return The name of the Palmon.
     *
     * Software runtime complexity is O(1)
     */
    public String getName()
    {
        return name;
    }

    /**
     * gets the Types of the Palmon.
     *
     * @return The types (Array) of the Palmon.
     *
     * Software runtime complexity is O(1)
     */
    public String[] getTypes()
    {
        return type;
    }

    /**
     * gets the attack damage of the Palmon.
     *
     * @return The attack of the Palmon.
     *
     * Software runtime complexity is O(1)
     */
    public int getAttack()
    {
        return attack;
    }

    /**
     * gets the defense of the Palmon.
     *
     * @return The defense of the Palmon.
     *
     * Software runtime complexity is O(1)
     */
    public int getDefense()
    {
        return defense;
    }

    /**
     * adjusts the HP of the Palmon.
     *
     * Software runtime complexity is O(1)
     */
    public void adjustHp(double damage)
    {
        hp = hp - damage;
        if(hp < 0)
        {
            hp = 0;
        }
    }

    /**
     * gets the first Type of the Palmon.
     *
     * @return The first Type of the Palmon.
     *
     * Software runtime complexity is O(1)
     */
    public String getTypeOne()
    {
        return type[0];
    }

    /**
     * gets the second Type of the Palmon.
     *
     * @return The second Type of the Palmon.
     *
     * Software runtime complexity is O(1)
     */
    public String getTypeTwo()
    {
        return type[1];
    }

    /**
     * gets the HP of the Palmon.
     *
     * @return The HP of the Palmon.
     *
     * Software runtime complexity is O(1)
     */
    public double getHp()
    {
        return hp;
    }

    /**
     * updates the Level of the Palmon
     *
     * Software runtime complexity is O(1)
     */
    public void assignLevel(int newLevel)
    {
        level = newLevel;
    }

    /**
     * gets the Level of the Palmon.
     *
     * @return The Level of the Palmon.
     *
     * Software runtime complexity is O(1)
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * gets the weight of the Palmon.
     *
     * @return The weight of the Palmon.
     *
     * Software runtime complexity is O(1)
     */
    public int getWeight()
    {
        return weight;
    }

    /**
     * gets the height of the Palmon.
     *
     * @return The height of the Palmon.
     *
     * Software runtime complexity is O(1)
     */
    public int getHeight()
    {
        return height;
    }
}