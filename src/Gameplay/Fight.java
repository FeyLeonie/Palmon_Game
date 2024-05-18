package Gameplay;

import csv_handling.CSV_Reader;
import data_structures.Normalizer;
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

        String playerPalmonName = Normalizer.normalize(playerPalmon.getName());
        String enemyPalmonName = Normalizer.normalize(enemyPalmon.getName());

        while(playerPalmon != null && enemyPalmon != null)
        {
            playerPalmonName = Normalizer.normalize(playerPalmonName);
            enemyPalmonName = Normalizer.normalize(enemyPalmonName);

            round++;
            print("\nThe " +round+ ". round is about to start!");

            //TODO Palmons NULL
            print("The Palmons are in the arena and ready.");
            print(playerPalmonName+ ", the Palmon from " +InitialMenu.playerName+" faces off against " +enemyPalmonName+ ", the Palmon from " +InitialMenu.enemyName+ ". Let the battle begin!\n");

            // TODO ab hier gibt es eine Exception beim Starten mit Threading
            Move enemyMove = chooseMoveEnemy(enemyPalmon, enemiesPalMoves);
            Move playerMove = chooseMovePlayer(playerPalmon, enemyPalmon, playersPalMoves);

            if(playerPalmon.getSpeed() >= enemyPalmon.getSpeed())
            {
                //Player starts attacking
                print("\nYour Palmon " +playerPalmonName+ " is about to attack the Enemies Palmon " +enemyPalmonName+ "! Ready to rumble?");
                enemyPalmon = attackingSequence(playerPalmon, playerMove, enemyPalmon, playersPalMoves, playerMoveIndex);

                if(enemyPalmon.getHp() > 0)
                {
                    // Enemy starts attacking
                    print("\nThe enemies Palmon " +enemyPalmonName+ " is about to attack your Palmon " +playerPalmonName+ "! Prepare yourself.");
                    playerPalmon = attackingSequence(enemyPalmon, enemyMove, playerPalmon, enemiesPalMoves, enemyMoveIndex);
                }
                else
                {
                    // Enemy Palmon is already defeated and can not attack anymore
                    print("You defeated the Enemies Palmon " +enemyPalmonName+ "! Congrats on that, it can not attack anymore");
                }
            }
            else
            {
                // Enemy starts attacking
                print("\nThe enemies Palmon " +enemyPalmonName+ " is about to attack! Prepare yourself.");
                playerPalmon = attackingSequence(enemyPalmon, enemyMove, playerPalmon, enemiesPalMoves, enemyMoveIndex);

                if(playerPalmon.getHp() > 0)
                {
                    //Player starts attacking
                    print("Your Palmon " +playerPalmonName+ " is about to attack! Ready to rumble?");
                    enemyPalmon = attackingSequence(playerPalmon, playerMove, enemyPalmon, playersPalMoves, playerMoveIndex);
                }
                else
                {
                    // Player Palmon is already defeated and can not attack anymore
                    print("Your Palmon " +playerPalmonName+ " got defeated by " +enemyPalmonName+ ". It can not attack anymore :(");
                }
            }

            print("\nThe " +round+ ". round is about to end! \nHere are the current stats...");
            print("\nPlayers stats: \nCurrent Palmon fighting: " +playerPalmonName+ " with " +playerPalmon.getHp()+ " HP left.");
            print("\nEnemy stats: \nCurrent Palmon fighting: " +enemyPalmonName+ " with " +enemyPalmon.getHp()+ " HP left.");

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

        String attackerName = Normalizer.normalize(attacker.getName());
        String defenderName = Normalizer.normalize(defender.getName());;

        print("Attack is about to happen! " +defenderName+ " currently has " +defender.getHp()+ " HP. Will " +attackerName+ " hit?");

        if(hits)
        {
            damageFactor = getEffectivity(attacker, defender); // damage factor is calculated
            damage = (attacker.getAttack() + attack.getDamage() - defender.getDefense()) * damageFactor; // attacking damage is calculated

            if(damage > 0) // damage zero or below zero e.g. if Players defense is higher than the actual attack
            {
                defender.adjustHp(damage); // adjusting the defenders HP

                print("The hit was successful! " + attackerName+ " made " +damage+ " hp damage to " +defenderName);
                print(defenderName+ " now has " +defender.getHp()+ " HP left.");
            }
        }
        else
        {
            print(attackerName+ " didnt hit. Maybe next time");
        }

        // Updating the Move usage by one
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
    public Move chooseMovePlayer(Palmon playerPalmon, Palmon enemyPalmon, HashMap<Integer, ArrayList<Move>> playersPalMoves)
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
            choice = printWithScString("\nPlease type in the name of your preferred Move \n(i) Info of the currently battling Palmons", choice); // letting the Player choose a Move
            choice = choice.toLowerCase();

            if(choice.equals("i"))
            {
                print("\nCurrent Stats from Palmon " +Normalizer.normalize(playerPalmon.getName())+ " (Palmon from " +InitialMenu.playerName+ ")");
                print("Heigt: " +playerPalmon.getHeight()+ "\nWeight: " +playerPalmon.getWeight()+ "\nTypes: " +Normalizer.normalize(playerPalmon.getTypeOne())+ " " +Normalizer.normalize(playerPalmon.getTypeTwo())+ "\nHP left: " +playerPalmon.getHp()+ "\nAttack: " +playerPalmon.getAttack()+ "\nDefense: " +playerPalmon.getDefense()+ "\nSpeed: " +playerPalmon.getSpeed());

                print("\nCurrent Stats from Palmon " +Normalizer.normalize(enemyPalmon.getName())+ " (Palmon from " +InitialMenu.enemyName+ ")");
                print("Heigt: " +enemyPalmon.getHeight()+ "\nWeight: " +enemyPalmon.getWeight()+ "\nTypes: " +Normalizer.normalize(enemyPalmon.getTypeOne())+ " " +Normalizer.normalize(enemyPalmon.getTypeTwo())+ "\nHP left: " +enemyPalmon.getHp()+ "\nAttack: " +enemyPalmon.getAttack()+ "\nDefense: " +enemyPalmon.getDefense()+ "\nSpeed: " +enemyPalmon.getSpeed());
            }
            else
            {
                // Finding the correct Move based on the player's choice
                for (int i = 0; i < playersMoves.size(); i++)
                {
                    Move move = playersMoves.get(i);
                    String moveName = move.getName().toLowerCase();

                    if (moveName.equals(choice))
                    {
                        playerAttack = move; // save the selected Move
                        playerMoveIndex = i; // Save the index of the selected Move
                        moveFound = true;
                        break; // Exit the loop once the Move is found
                    }

                    if(moveFound)
                    {
                        print("You chose the Move " +playerAttack.getName());
                    }
                }

                if(playerAttack == null)
                {
                    print("Your chosen Move was not found. Type in again.");
                }
            }
        }


        if(playerAttack.usagesLeft() <= 0) // if selected Move is already on Max Usages this will be needed
        {
           print("The chosen Move " +Normalizer.normalize(playerAttack.getName())+ " is not usable anymore. You used it too often. Please choose a different Move");
           chooseMovePlayer(playerPalmon, enemyPalmon, playersPalMoves);
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
            System.out.println(Normalizer.normalize(playerPalmons.showTop().getName())); // printing out the Palmon
            playerPalmons.dequeue(); // Pushing the Palmon out of the Stack
        }

        if(playerPalmon != null) // Printing out the Palmon that was in the last fight
        {
            System.out.println(Normalizer.normalize(playerPalmon.getName()));
        }

        print("And not to forget about the Palmons that went at their limit to help you. The ones who got defeated...");
        while(playersDefeatedPalmons.getQueueSize() != 0) // Printing out the defeated Palmons
        {
            System.out.println(Normalizer.normalize(playersDefeatedPalmons.showTop().getName()));
            playersDefeatedPalmons.dequeue();
        }
    }

    public void playerLost()
    {
        print("You sadly lost, sorry for that :( But dont be sad, I´m sure you´ll win next round, try again!");
    }
}
