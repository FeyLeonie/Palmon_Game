package elements;

public class Move
{
    final int id;
    final String name;
    final int damage;
    final int accuracy;
    final String type;
    int max_usages;
    int usages = 0;

    // Constructor
    public Move(String [] movedetails)
    {
        this.id = Integer.parseInt(movedetails[0]);
        this.name = movedetails[1];
        this.damage = Integer.parseInt(movedetails[2]);
        this.max_usages = Integer.parseInt(movedetails[3]);
        this.accuracy = Integer.parseInt(movedetails[4]);
        this.type = movedetails[5];
    }

    public void useMove()
    {
        this.usages+=1;
    }

    public int usagesLeft() {
        int usagesLeft = max_usages - usages;
        return usagesLeft;
    }

    public String getName()
    {
        return name;
    }

    public int getDamage()
    {
        return damage;
    }

    public int getAccuracy()
    {
        return accuracy;
    }
}
