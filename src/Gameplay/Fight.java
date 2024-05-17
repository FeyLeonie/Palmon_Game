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

public class Fight extends Printing
{
    public Game game;
    public CSV_Reader data; // CSV_Reader

    // saving the Index of the Move in the ArrayList to update Max Usages
    int playerMoveIndex = 0;
    int enemyMoveIndex = 0;

    Queue <Palmon> playersDefeatedPalmons;

    public Fight(Game game, CSV_Reader data)
    {
        this.game = game;
        this.data = data;
    }


    int round = 0;
    public void FightOverview(Queue<Palmon> playerPalmons, Queue<Palmon> enemyPalmons, HashMap<Integer, ArrayList<Move>> enemiesPalMoves, HashMap<Integer, ArrayList<Move>> playersPalMoves)
    {
        // Introducing the Fight
        print("Welcome to the arena, " + InitialMenu.playerName + ". Prepare to fight against " + InitialMenu.enemyName + ". May the best Palmon Team win!");

        playersDefeatedPalmons = new Queue<>();

        Palmon playerPalmon = playerPalmons.dequeue(); // load first Palmon into the Fight
        Palmon enemyPalmon = enemyPalmons.dequeue(); // load first Palmon into the Fight

        while(playerPalmon != null && enemyPalmon != null)
        {
            round++;
            print("The " +round+ ". round is about to start!");

            print("The Palmons are in the arena and ready.");
            print(InitialMenu.playerName + "'s Palmon " + playerPalmon.getName() + " faces off against " + InitialMenu.enemyName + "'s Palmon " + enemyPalmon.getName() + ". Let the battle begin!");

            // TODO ab hier gibt es eine Exception beim Starten mit Threading
            Move enemyMove = chooseMoveEnemy(enemyPalmon, enemiesPalMoves);
            Move playerMove = chooseMovePlayer(playerPalmon, playersPalMoves);

            if(playerPalmon.speed() >= enemyPalmon.speed())
            {
                //Player starts attacking
                print("Your Palmon " +playerPalmon.getName()+ " is about to attack the Enemies Palmon " +enemyPalmon.getName()+ "! Ready to rumble?");
                enemyPalmon = attackingSequence(playerPalmon, playerMove, enemyPalmon, playersPalMoves, playerMoveIndex);

                if(enemyPalmon.getHp() > 0)
                {
                    // Enemy starts attacking
                    print("The enemies Palmon " +enemyPalmon.getName()+ " is about to attack your Palmon " +playerPalmon.getName()+ "! Prepare yourself.");
                    playerPalmon = attackingSequence(enemyPalmon, enemyMove, playerPalmon, enemiesPalMoves, enemyMoveIndex);
                }
                else
                {
                    // Enemy Palmon is already defeated and can not attack anymore
                    print("You defeated the Enemies Palmon " +enemyPalmon.getName()+ "! Congrats on that, it can not attack anymore :(");
                }
            }
            else
            {
                // Enemy starts attacking
                print("The enemies Palmon " +enemyPalmon.getName()+ " is about to attack! Prepare yourself.");
                playerPalmon = attackingSequence(enemyPalmon, enemyMove, playerPalmon, enemiesPalMoves, enemyMoveIndex);

                if(playerPalmon.getHp() > 0)
                {
                    //Player starts attacking
                    print("Your Palmon " +playerPalmon.getName()+ " is about to attack! Ready to rumble?");
                    enemyPalmon = attackingSequence(playerPalmon, playerMove, enemyPalmon, playersPalMoves, playerMoveIndex);
                }
                else
                {
                    // Player Palmon is already defeated and can not attack anymore
                    print("Your Palmon " +playerPalmon.getName()+ " got defeated by " +enemyPalmon.getName()+ ". It can not attack anymore :(");
                }
            }

            print("The " +round+ ". round is about to end! \nHere are the current stats...");
            print("\nPlayers stats: \nCurrent Palmon fighting: " +playerPalmon.getName()+ " with " +playerPalmon.getHp()+ " HP left.");
            print("\nEnemy stats: \nCurrent Palmon fighting: " +enemyPalmon.getName()+ " with " +enemyPalmon.getHp()+ " HP left.");

            if(playerPalmon.getHp() <= 0) // if Palmon is defeated
            {
                playersDefeatedPalmons.enqueue(playerPalmon);
                if(playerPalmons.getQueueSize() == 0) // and if theres no Palmon in the Players Queue anymore
                {
                    playerLost(); // the Player Lost
                    break;
                }
                playerPalmon = playerPalmons.dequeue(); // otherwise: load next Palmon into fight
            }

            if(enemyPalmon.getHp() <= 0) // if Palmon is defeated
            {
                if(enemyPalmons.getQueueSize() == 0)// and if theres no Palmon in the Enemies Queue anymore
                {
                    playerWon(playerPalmons, playerPalmon); // the Player won !!! (Glückwunsch Sebastiaaaan!!)
                    break;
                }
                enemyPalmon = enemyPalmons.dequeue(); // otherwise: load next Palmon into fight
            }
        }
    }

    public Palmon attackingSequence(Palmon attacker, Move attack, Palmon defender, HashMap<Integer, ArrayList<Move>> moveHashMap, int index)
    {
        // values are temporary
        double damageFactor = 0; // effectivity factor
        boolean hits = false;
        double damage = 0;

        // Player starts
        hits = setAccuracy(attack);

        if(hits)
        {
            damageFactor = getEffectivity(attacker, defender); // damage factor is calculated
            damage = (attacker.getAttack() + attack.getDamage() - defender.getDefense()) * damageFactor; // attacking damage is calculated

            if(damage > 0) // damage zero or below zero e.g. if Players defense is higher than the actual attack
            {
                defender.adjustHp(damage); // adjusting the defenders HP

                print("The hit was successful! " +attacker.getName()+ " made " +damage+ " hp damage to " +defender.getName());
                print(defender.getName()+ " has " +defender.getHp()+ " HP left.");
            }
        }
        else
        {
            print(attacker.getName()+ " didnt hit. Maybe next time");
        }

        //TODO ist das hier richtig?
        // Updating the usage of the Move by one
        ArrayList<Move> moves = moveHashMap.get(attacker.getId());
        Move move = moves.get(index);
        move.useMove();

        return defender;
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

        enemyMoveIndex = randomMove; // Saving the Index of the Move in the ArrayList
        return enemyAttack;
    }

    // Output: playerAttack (Players Move)
    public Move chooseMovePlayer(Palmon playerPalmon, HashMap<Integer, ArrayList<Move>> playersPalMoves)
    {
        // telling the Player where he currently is
        print("It's time to choose a Move for your Palmon, " +InitialMenu.playerName);

        // Let the Player choose the preferred Move
        // saving the ArrayList with all the Moves of the current Palmon
        ArrayList<Move> playersMoves;
        playersMoves = playersPalMoves.get(playerPalmon.getId());

        Move tempMove;
        // Printing out every Move the Player can choose from with its damage
        print("Possible Moves to choose from:");
        for (Move playersMove : playersMoves)
        {
            tempMove = playersMove;
            if (tempMove != null)
            {
                    print(tempMove.getName() + " with possible damage: " + tempMove.getDamage());
            }
        }

        String choice = "";
        Move playerAttack = null;
        boolean moveFound = false;

        while(!moveFound)
        {
            choice = printssc("Please type in the name of your preferred Move", choice); // letting the Player choose a Move

            // Finding the correct Move based on the player's choice
            for (int i = 0; i < playersMoves.size(); i++)
            {
                Move move = playersMoves.get(i);
                String moveName = move.getName();

                if (moveName.equals(choice))
                {
                    playerAttack = move; // save the selected Move
                    playerMoveIndex = i; // Save the index of the selected Move
                    moveFound = true;
                    break; // Exit the loop once the Move is found
                }
                else
                {
                    print("Your chosen Move was not found. Type in again.");
                }
            }
        }


        if(playerAttack.usagesLeft() <= 0) // if selected Move is already on Max Usages this will be needed
        {
           print("This Move is not usable anymore. You used it too often. Please choose a different Move");
           chooseMovePlayer(playerPalmon, playersPalMoves);
        }
        // saving the Index of the Move to update the Max Usages when using the Move
        return playerAttack;
    }

    public double getEffectivity(Palmon attacker, Palmon defender)
    {
        // Grundgedanke mal auf Deutsch:
        // Ich nehme die geringste Effektivität (es gibt bei Palmons mit mehreren Types ja mehrere zur Auswahl)

        ArrayList<Effectivity> effectivity_db = CSV_Reader.effectivity_db;

        String [] attackerTypes = attacker.getTypes();
        String [] targetTypes = defender.getTypes();
        double damageFactor;
        double oldDamageFactor = -1;

        for(Effectivity effectivity: effectivity_db)
        {
            // at least one of the attackers types must be equal with the effectivity initial type and at leat one of the targets types must be equal with the effectivity target type
            if((attackerTypes[0].equals(effectivity.getAttackerType()) || attackerTypes[1].equals(effectivity.getAttackerType()))
                    && (targetTypes[0].equals(effectivity.getTargetType()) || targetTypes[1].equals(effectivity.getTargetType())))
            {
                damageFactor = effectivity.getDamageFactor();

                if(oldDamageFactor == -1) // in first round tempDamageFactor needs to overwritten as well
                {
                    oldDamageFactor = effectivity.getDamageFactor();
                }

                if(damageFactor <= oldDamageFactor) // if the new Effectivity is smaller than the old Effectivity
                {
                    oldDamageFactor = effectivity.getDamageFactor(); // overwrite the old Effectivity
                }

            }
        }

        return oldDamageFactor; // return smallest Effectivity
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

    public void playerWon(Queue<Palmon> playerPalmons, Palmon playerPalmon)
    {
        print("You won! CONGRATULATIONS! Here are the names of your Palmons that supported you...");

        while(playerPalmons.getQueueSize() != 0) // Printing out the Palmons that were in the Players team but didnt fight
        {
            System.out.println(playerPalmons.showTop().getName()); // printing out the Palmon
            playerPalmons.dequeue(); // Pushing the Palmon out of the Stack
        }

        if(playerPalmon != null) // Printing out the Palmon that was in the last fight
        {
            System.out.println(playerPalmon.getName());
        }

        print("And not to forget about the Palmons that went at their limit to help you. The ones who got defeated...");
        while(playersDefeatedPalmons.getQueueSize() != 0) // Printing out the defeated Palmons
        {
            System.out.println(playersDefeatedPalmons.showTop().getName());
            playersDefeatedPalmons.dequeue();
        }
    }

    public void playerLost()
    {
        print("You sadly lost, sorry for that :( But dont be sad, I´m sure you´ll win next round, try again!");
    }
}
