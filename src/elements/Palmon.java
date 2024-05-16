package elements;

public class Palmon // constructor
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

    // Constructor
    public Palmon (String [] palmondetails)
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
    }

    public int speed()
    {
        return speed;
    }

    public String getTypeOne()
    {
        return type[0];
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }

    public String[] getTypes()
    {
        return type;
    }

    public int getAttack()
    {
        return attack;
    }

    public int getDefense()
    {
        return defense;
    }

    public void adjustHp(double damage)
    {
        hp = hp - damage;
    }

    public double getHp()
    {
        return hp;
    }
}