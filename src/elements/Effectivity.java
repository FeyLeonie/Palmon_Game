package elements;

public class Effectivity
{
        String attackerType;
        String targetType;
        double damage;

        // Constructor for Effectivity
        public Effectivity(String damage_type_id, String target_type_id, double damage_factor)
        {
            this.attackerType = damage_type_id;
            this.targetType = target_type_id;
            this.damage = damage_factor;
        }

        // get Methods for returning the needed Parameters

        public String getAttackerType()
        {
            return attackerType;
        }

        public String getTargetType()
        {
            return targetType;
        }

        public double getDamageFactor()
        {
            return damage;
        }
}
