package elements;

public class Palmon // constructor
{
    final int id;
    final String name;
    final int height;
    final int weight;
    final String[] type = new String[2];
    int hp;
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
        hp      = Integer.parseInt(palmondetails[6]);
        attack  = Integer.parseInt(palmondetails[7]);
        defense = Integer.parseInt(palmondetails[8]);
        speed   = Integer.parseInt(palmondetails[9]);
    }

    public boolean speedTest(Palmon player, Palmon enemy)
    {
        boolean enemyfaster = false;

        // if enemys speed is higher than players speed, enemy starts
        if(player.speed < enemy.speed)
        {
            enemyfaster = true;
        }
        return enemyfaster; // return whos faster
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
}