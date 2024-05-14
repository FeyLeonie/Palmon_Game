package Gameplay;

import csv_handling.CSV_Reader;
import data_structures.Printing;
import data_structures.Stack;
import elements.Effectivity;
import elements.Move;
import elements.Palmon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Fight
{
    public Game game;
    public CSV_Reader data;
    public Printing print;
    public Stack<Palmon> playerTeam;
    public Stack<Palmon> enemyTeam;
    public HashMap<Integer, ArrayList<Move>> playersPalMoves;
    public HashMap<Integer, ArrayList<Move>> enemiesPalMoves;
    public ArrayList<Effectivity> effectivity_db; // CSV_Reader

    public Fight(Game game, CSV_Reader data)
    {
        this.game = game;
        this.data = data;
    }


    int round = 0;
    public void FightOverview(Stack<Palmon> playerTeam, Stack<Palmon> enemyTeam, HashMap<Integer, ArrayList<Move>> enemiesPalMoves)
    {
        while(playerTeam.getStackSize() != 0 && enemyTeam.getStackSize() != 0)
        {
            round++;
            print.print("The " +round+ " . round is about to start! Prepare yourself for a fair fight");

            Palmon playerPalmon = null;
            Palmon enemyPalmon = null;

            loadNextPalmons(playerTeam, enemyTeam, playerPalmon, enemyPalmon, enemiesPalMoves);
        }

        if(playerTeam.getStackSize() == 0)
        {
            playerLost();
        }

        if(enemyTeam.getStackSize() == 0)
        {
            playerWon();
        }

    }

    // Output: playerPalmon and enemyPalmon
    public void loadNextPalmons(Stack<Palmon> playerTeam, Stack<Palmon> enemyTeam, Palmon playerPalmon, Palmon enemyPalmon, HashMap<Integer, ArrayList<Move>> enemiesPalMoves)
    {
        Move playerAttack = null;
        Move enemyAttack = null;

        if(playerPalmon == null || playerPalmon.getHp() == 0) // if Palmon does not have a value yet or if Palmon got defeated choose the next one
        {
            playerPalmon = playerTeam.pull(playerTeam); // Load next Palmon out of Stack
            playerAttack = chooseMovePlayer(enemyPalmon, enemiesPalMoves); // Let Player choose the Move for the Palmon
        }

        if(enemyPalmon == null || enemyPalmon.getHp() == 0)
        {
            enemyPalmon = enemyTeam.pull(enemyTeam); // Load next Palmon out of Stack
            enemyAttack = chooseMoveEnemy(enemyPalmon, enemiesPalMoves); // Let the Player choose the Move for the Palmon
        }

        attackingMode(playerPalmon, playerAttack, enemyPalmon, enemyAttack);
    }

    //  Output: enemyAttack (Enemies Move)
    public Move chooseMoveEnemy(Palmon enemyPalmon, HashMap<Integer, ArrayList<Move>> enemiesPalMoves)
    {
        // Choose Random Move from Enemies Palmon
        ArrayList<Move> enemiesMoves = enemiesPalMoves.get(enemyPalmon.getId()); // saving the ArrayList with all the Moves of the current Palmon

        Random r = new Random();
        int randomMove = r.nextInt(enemiesMoves.size());

        Move enemyAttack = enemiesMoves.get(randomMove); // Saving the current Move

        while(enemyAttack.usagesLeft() <= 0) // if selected Move is already on Max Usages this will be needed
        {
            r = new Random();
            randomMove = r.nextInt(enemiesMoves.size());

            enemyAttack = enemiesMoves.get(randomMove); // Saving the current Move
        }

        return enemyAttack;
    }

    // Output: playerAttack (Players Move)
    public Move chooseMovePlayer(Palmon playerPalmon, HashMap<Integer, ArrayList<Move>> playerPalMoves)
    {
        // Let the Player choose the preferred Move
        ArrayList<Move> playersMoves = playersPalMoves.get(playerPalmon.getId()); // saving the ArrayList with all the Moves of the current Palmon

        for(Move move: playersMoves) // Printing out every Move the Player can choose from with its damage
        {
            print.print("Move " +move.getName()+ " with possible damage: " +move.getDamage());
        }

        String choice = "";
        print.printssc("These are the Moves you can choose one. Please type in the name of your preferred Move", choice); // letting the Player choose a Move

        Move playerAttack = null; // initialising the Move already
        for(Move move: playersMoves) // finding the correct Move
        {
            if(move.getName().equals(choice))
            {
                playerAttack = move; // Saving the Move
                // TODO try catch, wenn was falsches eingegeben wurde
            }
        }

        if(playerAttack.usagesLeft() <= 0) // if selected Move is already on Max Usages this will be needed
        {
           print.print("This Move is not usable anymore. You used it too often. Please choose a different Move");
           chooseMovePlayer(playerPalmon, playersPalMoves);
        }
        return playerAttack;
    }

    public void attackingMode(Palmon playerPalmon, Move playerAttack, Palmon enemyPalmon, Move enemyAttack)
    {
        // First Attack
        if(playerPalmon.speedTest(enemyPalmon)) // if playerPalmon is faster than enemyPalmon, Player starts with attack
        {
            // Player starts
            playerAttacksFirst(playerPalmon, playerAttack, enemyPalmon, enemyAttack);
        }
        else
        {
            // Enemy starts
            enemyAttacksFirst(playerPalmon, enemyPalmon, enemyAttack, playerAttack);
        }
    }

    public void playerAttacksFirst(Palmon playerPalmon, Move playerAttack, Palmon enemyPalmon, Move enemyAttack)
    {
        double damageFactor = 0; // effectivity factor
        boolean hits = false;
        double damage = 0;

        // Player starts
        hits = setAccuracy(playerAttack);

        if(hits)
        {
            damageFactor = getEffectivity(playerPalmon, enemyPalmon, effectivity_db);
            damage = (playerPalmon.getAttack() + playerAttack.getDamage() - enemyPalmon.getDefense()) * (damageFactor/100);
            enemyPalmon.adjustHp(damage);
            playerAttack.useMove(); // increase max usages by one
            print.print("Your hit was successful! You made " +damage+ " hp damage.");
        }
        else
        {
            print.print("You didnt hit :( Maybe next time");
        }

        //Enemy attacks
        if(enemyPalmon.getHp() > 0) // if Palmon is still able to fight
        {
            hits = setAccuracy(enemyAttack);
            if(hits)
            {
                damageFactor = getEffectivity(enemyPalmon, playerPalmon, effectivity_db); // get Effectivity factor
                damage = (enemyPalmon.getAttack() + enemyAttack.getDamage() - playerPalmon.getDefense()) * (damageFactor/100); // calculate damage
                playerPalmon.adjustHp(damage); // adjust hp
                enemyAttack.useMove(); // increase max usages by one
                print.print("You got hit! Enemy made " +damage+ " hp damage.");
            }
            else
            {
                print.print("The Enemies Palmon didnt hit. Lucky!");
            }
        }
        else
        {
            print.print("You defeated your enemies Palmon " +enemyPalmon.getName() + ". Your enemie cant attack this round.");
            FightOverview(playerTeam, enemyTeam, enemiesPalMoves); // start of next Round
        }

        if(playerPalmon.getHp() > 0 && enemyPalmon.getHp() > 0) // if both Palmons are´nt defeated, round does not start new but new Moves will be chosen
        {
            loadNextPalmons(playerTeam, enemyTeam, playerPalmon, enemyPalmon, enemiesPalMoves);
        }
    }

    public void enemyAttacksFirst(Palmon playerPalmon, Palmon enemyPalmon, Move enemyAttack, Move playerAttack)
    {
        double damageFactor = 0; // effectivity factor
        boolean hits = false;
        double damage = 0;

        hits = setAccuracy(enemyAttack);
        if(hits)
        {
            damageFactor = getEffectivity(enemyPalmon, playerPalmon, effectivity_db); // get Effectivity factor
            damage = (enemyPalmon.getAttack() + enemyAttack.getDamage() - playerPalmon.getDefense()) * (damageFactor/100); // calculate damage
            playerPalmon.adjustHp(damage); // adjust hp
            print.print("You got hit! Enemy made " +damage+ " hp damage.");
        }
        else
        {
            print.print("The Enemies Palmon didnt hit. Lucky!");
        }

        // Player starts
        if(playerPalmon.getHp() > 0)
        {
            hits = setAccuracy(playerAttack);
            if(hits)
            {
                damageFactor = getEffectivity(playerPalmon, enemyPalmon, effectivity_db);
                damage = (playerPalmon.getAttack() + playerAttack.getDamage() - enemyPalmon.getDefense()) * (damageFactor/100);
                enemyPalmon.adjustHp(damage);
                print.print("Your hit was successful! You made " +damage+ " hp damage.");
            }
            else
            {
                print.print("You didnt hit :( Maybe next time");
            }
        }
        else
        {
            print.print("Your Palmon " + playerPalmon.getName() + " got defeated. No attack this round. :(");
            FightOverview(playerTeam, enemyTeam, enemiesPalMoves); // start of next Round
        }

        if(playerPalmon.getHp() > 0 && enemyPalmon.getHp() > 0) // if both Palmons are´nt defeated, round does not start new but new Moves will be chosen
        {
            loadNextPalmons(playerTeam, enemyTeam, playerPalmon, enemyPalmon, enemiesPalMoves);
        }
    }

    public double getEffectivity(Palmon attacker, Palmon defender, ArrayList<Effectivity> effectivity_db)
    {
        String [] attackerTypes = attacker.getTypes();
        String [] targetTypes = defender.getTypes();
        double damageFactor = 0;

        for(Effectivity effectivity: effectivity_db)
        {
            // at least one of the attackers types must be equal with the effectivity initial type and at leat one of the targets types must be equal with the effectivity target type
            if((attackerTypes[0].equals(effectivity.getAttackerType()) || attackerTypes[1].equals(effectivity.getAttackerType()))
                    && (targetTypes[0].equals(effectivity.getTargetType()) || targetTypes[1].equals(effectivity.getTargetType())))
            {
                damageFactor = effectivity.getDamageFactor();
            }
        }
        return damageFactor;
    }

    public boolean setAccuracy(Move move)
    {
        boolean hits = false;
        Random r = new Random();
        int accuracy = r.nextInt(100+1); // Random value between 1 and 100

        if(accuracy <= move.getAccuracy()) // if random value is smaller or equal with the Move Accuracy, the Move hits
        {
            hits = true;
        }
        return hits;
    }

    public void playerWon()
    {
        print.print("You won! CONGRATULATIONS! Here are the names of your Palmons that supported you...");
        while(playerTeam.getStackSize() != 0)
        {
            System.out.println(playerTeam.peek(playerTeam).getName()); // printing out the Palmon
            playerTeam.pull(playerTeam); // Pushing the Palmon out of the Stack
        }
    }

    public void playerLost()
    {
        print.print("You sadly lost, sorry for that :( I´m sure you´ll win next round, try again!");
    }
}
