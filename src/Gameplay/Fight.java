package Gameplay;

import csv_handling.CSV_Reader;
import data_structures.Printing;
import data_structures.Queue;
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
    public ArrayList<Effectivity> effectivity_db; // CSV_Reader

    // saving the Index of the Move in the ArrayList to update Max Usages
    int playerMoveIndex = 0;
    int enemyMoveIndex = 0;

    public Fight(Game game, CSV_Reader data)
    {
        this.game = game;
        this.data = data;
    }


    int round = 0;
    public void FightOverview(Queue<Palmon> playerPalmons, Queue<Palmon> enemyPalmons, HashMap<Integer, ArrayList<Move>> enemiesPalMoves, HashMap<Integer, ArrayList<Move>> playersPalMoves)
    {
        Printing print = new Printing();

        Palmon playerPalmon = playerPalmons.dequeue(); // load first Palmon into the Fight
        Palmon enemyPalmon = enemyPalmons.dequeue(); // load first Palmon into the Fight

        while(playerPalmons.getQueueSize() != 0 && enemyPalmons.getQueueSize() != 0)
        {
            round++;
            print.print("The " +round+ ". round is about to start!");

            Move enemyMove = chooseMoveEnemy(enemyPalmon, enemiesPalMoves);
            Move playerMove = chooseMovePlayer(playerPalmon, playersPalMoves);

            while(playerPalmon.getHp() > 0 || enemyPalmon.getHp() > 0)
            {
                if(playerPalmon.speed() >= enemyPalmon.speed())
                {
                    //Player starts attacking
                    print.print("Your Palmon " +playerPalmon.getName()+ " is about to attack! Ready to rumble?");
                    attackingSequence(playerPalmon, playerMove, enemyPalmon, playersPalMoves, playerMoveIndex);
                }
                else
                {
                    // Enemy starts attacking
                    print.print("The enemies Palmon " +enemyPalmon.getName()+ " is about to attack!");
                    attackingSequence(enemyPalmon, enemyMove, playerPalmon, enemiesPalMoves, enemyMoveIndex);
                }
            }

            if(playerPalmon.getHp() <= 0) // if Palmon is defeated
            {
                playerPalmon = playerPalmons.dequeue(); // load next Palmon into fight
            }

            if(enemyPalmon.getHp() <= 0) // if Palmon is defeated
            {
                enemyPalmon = enemyPalmons.dequeue(); // load next Palmon into fight
            }
        }

        if(playerPalmons.getQueueSize() == 0)
        {
            playerLost();
        }

        if(enemyPalmons.getQueueSize() == 0)
        {
            playerWon(playerPalmons);
        }

    }

    public void attackingSequence(Palmon attacker, Move attack, Palmon defender, HashMap<Integer, ArrayList<Move>> MoveHashMap, int index)
    {
        // values are temporary
        double damageFactor = 0; // effectivity factor
        boolean hits = false;
        double damage = 0;

        // Player starts
        hits = setAccuracy(attack);

        if(hits)
        {
            damageFactor = getEffectivity(attacker, defender, effectivity_db); // damage factor is calculated
            damage = (attacker.getAttack() + attack.getDamage() - defender.getDefense()) * (damageFactor/100); // attacking damage is calculated

            defender.adjustHp(damage); // adjusting the defenders HP

            attack.useMove(); // increase the Move max usages by one
            print.print("The hit was successful!" +attacker.getName()+ " made " +damage+ " hp damage to " +defender.getName());
        }
        else
        {
            print.print(attacker.getName()+ " didnt hit. Maybe next time");
        }

        MoveHashMap.get(attacker.getId()).get(index).useMove();
    }

    //  Output: enemyAttack (Enemies Move)
    public Move chooseMoveEnemy(Palmon enemyPalmon, HashMap<Integer, ArrayList<Move>> enemiesPalMoves)
    {
        // Choose Random Move from Enemies Palmon
        ArrayList<Move> enemiesMoves = enemiesPalMoves.get(enemyPalmon.getId()); // saving the ArrayList with all the Moves of the current Palmon

        Random r = new Random();
        int randomMove = r.nextInt(enemiesMoves.size());

        Move enemyAttack = enemiesMoves.get(randomMove); // Saving the current Move

        while(enemyAttack.usagesLeft() <= 0 || enemyAttack == null) // if selected Move is already on Max Usages this will be needed
        {
            r = new Random();
            randomMove = r.nextInt(enemiesMoves.size());

            enemyAttack = enemiesMoves.get(randomMove); // Saving the current Move
        }

        enemyMoveIndex = randomMove; // Saving the Index of the Move in the ArrayList
        return enemyAttack;
    }

    // Output: playerAttack (Players Move)
    public Move chooseMovePlayer(Palmon playerPalmon, HashMap<Integer, ArrayList<Move>> playersPalMoves)
    {
        Printing print = new Printing();

        // Let the Player choose the preferred Move

        // saving the ArrayList with all the Moves of the current Palmon
        ArrayList<Move> playersMoves;
        playersMoves = playersPalMoves.get(playerPalmon.getId());

        Move tempMove;
        // Printing out every Move the Player can choose from with its damage
        for(int i = 0; i < playersMoves.size(); i++)
        {
            tempMove = playersMoves.get(i);

            if(tempMove !=null)
            {
                print.print("Move " + tempMove.getName()+ " with possible damage: " + tempMove.getDamage());
            }
            else
            {
                print.print("Move ist null...");
            }
        }

        String choice = "";
        print.printssc("These are the Moves you can choose one. Please type in the name of your preferred Move", choice); // letting the Player choose a Move

        Move playerAttack = null;

        // Finding the correct Move based on the player's choice
        for (int i = 0; i < playersMoves.size(); i++)
        {
            if (playersMoves.get(i).getName().equals(choice))
            {
                playerAttack = playersMoves.get(i); // save the selected Move
                playerMoveIndex = i; // Save the index of the selected Move
                break; // Exit the loop once the Move is found
            }
        }

        if(playerAttack.usagesLeft() <= 0) // if selected Move is already on Max Usages this will be needed
        {
           print.print("This Move is not usable anymore. You used it too often. Please choose a different Move");
           chooseMovePlayer(playerPalmon, playersPalMoves);
        }

        // saving the Index of the Move to update the Max Usages when using the Move


        return playerAttack;
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

    public void playerWon(Queue<Palmon> playerPalmons)
    {
        print.print("You won! CONGRATULATIONS! Here are the names of your Palmons that supported you...");
        while(playerPalmons.getQueueSize() != 0)
        {
            System.out.println(playerPalmons.showTop().getName()); // printing out the Palmon
            playerPalmons.dequeue(); // Pushing the Palmon out of the Stack
        }
    }

    public void playerLost()
    {
        print.print("You sadly lost, sorry for that :( But dont be sad, I´m sure you´ll win next round, try again!");
    }
}
