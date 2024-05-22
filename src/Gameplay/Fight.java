package Gameplay;

import csv_handling.BattleDocumentation;
import csv_handling.CSV_Reader;
import tools.Language;
import tools.Normalizer;
import tools.Printing;
import data_structures.Queue;
import elements.Effectivity;
import elements.Move;
import elements.Palmon;
import tools.ThreadSleep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Represents the fight between the Player and the Enemy
 */
public class Fight extends Printing
{
    public Game game;
    public CSV_Reader data; // CSV_Reader

    // saving the Index of the Move in the ArrayList to update Max Usages
    int playerMoveIndex = 0;
    int enemyMoveIndex = 0;

    ArrayList<Palmon> playersPalmonssArr;
    ArrayList<Palmon> enemiesPalmonssArr;

    /**
     * Initializes a new Fight instance.
     *
     * @param game The game instance.
     * @param data The CSV reader instance.
     */
    public Fight(Game game, CSV_Reader data)
    {
        this.game = game;
        this.data = data;
    }

    int round = 0;

    /**
     * Provides an overview of the fight between two players.
     *
     * @param playerPalmons   The queue of Palmons for the player.
     * @param enemyPalmons    The queue of Palmons for the enemy.
     * @param enemiesPalMoves The map containing moves for enemy Palmons.
     * @param playersPalMoves The map containing moves for player Palmons.
     *
     * Software Runtime Complexity is O(n)
     */
    public void FightOverview(Queue<Palmon> playerPalmons, Queue<Palmon> enemyPalmons, HashMap<Integer, ArrayList<Move>> enemiesPalMoves, HashMap<Integer, ArrayList<Move>> playersPalMoves)
    {
        // Introducing the Fight
        print(Language.getMessage("FWelcomeToArena", InitialMenu.playerName, InitialMenu.enemyName));
        ThreadSleep.sleep(1000);
        print(Language.getMessage("FBestPalWin"));

        // initializing the ArrayLists where the defeated Palmons will be saved in
        playersPalmonssArr = playerPalmons.toArrayList();
        enemiesPalmonssArr = enemyPalmons.toArrayList();

        // loading the first Palmons into the Fight
        Palmon playerPalmon = playerPalmons.dequeue();
        Palmon enemyPalmon = enemyPalmons.dequeue();

        // for saving the Name of the current Palmon from the Player/Enemy
        String playerPalmonName;
        String enemyPalmonName;

        ThreadSleep.sleep(1000);

        while(playerPalmon != null && enemyPalmon != null)
        {
            // saving the Name of the current Palmon from the Player/Enemy
            playerPalmonName = Normalizer.normalize(playerPalmon.getName());
            enemyPalmonName = Normalizer.normalize(enemyPalmon.getName());

            // Calling a new Round
            round++;
            print(Language.getMessage("FCallNewRound", round));
            ThreadSleep.sleep(1000);

            print(Language.getMessage("FFacingOff", playerPalmonName, InitialMenu.playerName, enemyPalmonName, InitialMenu.enemyName));
            ThreadSleep.sleep(2000);

            Move enemyMove = chooseMoveEnemy(enemyPalmon, enemiesPalMoves);
            Move playerMove = chooseMovePlayer(playerPalmon, enemyPalmon, playersPalMoves);

            if(enemyMove == null && playerMove == null) // If both Palmons are blocked since they dont have any Moves to perform they will be defeated
            {
                print("Both Palmons " +Normalizer.normalize(playerPalmon.getName())+ " and " +Normalizer.normalize(enemyPalmon.getName())+ " are blocked since they dont have a Move. They will be defeated.");
                playerPalmon.adjustHp(10000.0);
                enemyPalmon.adjustHp(10000.0);
            }
            else
            {
                if(playerPalmon.getSpeed() >= enemyPalmon.getSpeed())
                {
                    //Player starts attacking
                    ThreadSleep.sleep(1000);
                    print("\nYour Palmon " +playerPalmonName+ " starts attacking " +enemyPalmonName+ "(" +InitialMenu.enemyName+ " Palmon)");
                    enemyPalmon = attackingSequence(playerPalmon, playerMove, enemyPalmon, playersPalMoves, playerMoveIndex);

                    if(enemyPalmon.getHp() > 0)
                    {
                        ThreadSleep.sleep(1000);
                        // Enemy starts attacking
                        print("\nThe Palmon " +enemyPalmonName+ " from " +InitialMenu.enemyName+ " is about to attack " +playerPalmonName+ " (Your Palmon)");
                        playerPalmon = attackingSequence(enemyPalmon, enemyMove, playerPalmon, enemiesPalMoves, enemyMoveIndex);
                    }
                    else
                    {
                        ThreadSleep.sleep(1000);
                        // Enemy Palmon is already defeated and can not attack anymore
                        print("You defeated the Palmon " +enemyPalmonName+ " from " +InitialMenu.enemyName+ "!");
                    }
                }
                else
                {
                    ThreadSleep.sleep(1000);
                    // Enemy starts attacking
                    print("\nThe enemies Palmon " +enemyPalmonName+ " starts to attack! Prepare yourself.");
                    playerPalmon = attackingSequence(enemyPalmon, enemyMove, playerPalmon, enemiesPalMoves, enemyMoveIndex);

                    if(playerPalmon.getHp() > 0)
                    {
                        ThreadSleep.sleep(1000);
                        //Player starts attacking
                        print("Your Palmon " +playerPalmonName+ " is about to attack! Ready to rumble?");
                        enemyPalmon = attackingSequence(playerPalmon, playerMove, enemyPalmon, playersPalMoves, playerMoveIndex);
                    }
                    else
                    {
                        ThreadSleep.sleep(1000);
                        // Player Palmon is already defeated and can not attack anymore
                        print("Your Palmon " +playerPalmonName+ " got defeated by " +enemyPalmonName+ ". It can not attack anymore :(");
                    }
                }
            }
            ThreadSleep.sleep(1);
            String choice = "";
            choice = printWithScString("\nThe " +round+ ". round is about to end! \n(i) Overview of the Palmons HP and teams (type anything else) start next round", choice);

            if(choice.equals("i"))
            {
                print("\nPlayers stats:");
                ThreadSleep.sleep(500);
                print("Current Palmon fighting: " +playerPalmonName+ " with " +playerPalmon.getHp()+ " HP left.");
                ThreadSleep.sleep(500);
                int palsLeft;
                if(playerPalmon.getHp() <= 0) // if current fighting Palmon is defeated
                {
                    palsLeft = playerPalmons.getQueueSize(); // current team size is the Queue size
                }
                else // if current Palmon still has some HP
                {
                    palsLeft = playerPalmons.getQueueSize() + 1; // teamsize is the current Palmon + Queuesize
                }
                print("Amount of Palmons left: " +palsLeft);

                ThreadSleep.sleep(1000);

                print("\nEnemy stats:");
                ThreadSleep.sleep(500);
                print("Current Palmon fighting: " +enemyPalmonName+ " with " +enemyPalmon.getHp()+ " HP left.");
                ThreadSleep.sleep(500);
                if(enemyPalmon.getHp() <= 0) // if current fighting Palmon is defeated
                {
                    palsLeft = enemyPalmons.getQueueSize(); // current team size is the Queue size
                }
                else // if current Palmon still has some HP
                {
                    palsLeft = enemyPalmons.getQueueSize() + 1; // teamsize is the current Palmon + Queuesize
                }
                print("Amount of Palmons left: " +palsLeft);
            }


            //TODO ab hier ist es falsch...
            if(playerPalmon.getHp() <= 0) // if Palmon is defeated
            {
                if(playerPalmons.getQueueSize() == 0) // and if theres no Palmon in the Players Queue anymore
                {
                    playerLost(round, playersPalmonssArr, enemiesPalmonssArr); // the Player Lost
                    break;
                }
                ThreadSleep.sleep(1000);
                print("\nSince your Palmon " +Normalizer.normalize(playerPalmon.getName())+ " got defeated your next Palmon will take over the Arena now!");
                //TODO irgendwie ist es hier immer noch das alte Palmon?
                playerPalmon = playerPalmons.dequeue(); // otherwise: load next Palmon into fight
            }

            if(enemyPalmon.getHp() <= 0) // if Palmon is defeated
            {
                if(enemyPalmons.getQueueSize() == 0)// and if theres no Palmon in the Enemies Queue anymore
                {
                    playerWon(round, playersPalmonssArr, enemiesPalmonssArr); // the Player won !!! (Glückwunsch Sebastiaaaan!!)
                    break;
                }
                ThreadSleep.sleep(1000);
                print("\nSince the Palmon " +Normalizer.normalize(enemyPalmon.getName())+ " from " +InitialMenu.enemyName+ " got defeated the next Palmon will fight now!");
                enemyPalmon = enemyPalmons.dequeue(); // otherwise: load next Palmon into fight
            }
            ThreadSleep.sleep(1000);
        }
    }

    /**
     * Handles the attacking sequence between two Palmons.
     *
     * @param attacker        The attacking Palmon.
     * @param defender        The defending Palmon.
     *
     * move            The move used by the attacking Palmon.
     * palMoves        The map of moves available to the Palmon.
     * moveIndex       The index of the move in the list.
     *
     * @return The updated defending Palmon after the attack.
     *
     * Software Runtime Complexity is O(1)
     */
    public Palmon attackingSequence(Palmon attacker, Move attack, Palmon defender, HashMap<Integer, ArrayList<Move>> moveHashMap, int index)
    {
        // values are temporary
        double damageFactor; // effectivity factor
        boolean hits;
        double damage;

        String attackerName = Normalizer.normalize(attacker.getName());
        String defenderName = Normalizer.normalize(defender.getName());

        if(attack == null)
        {
            print("Palmon " +attackerName+ " is blocked. It is not able to perform any Move.");
            return defender;
        }

        // Player starts
        hits = setAccuracy(attack);

        ThreadSleep.sleep(1000);
        print("Attack is about to happen! " +defenderName+ " currently has " +defender.getHp()+ " HP. Will " +attackerName+ " hit?");
        ThreadSleep.sleep(2250);

            if(hits)
            {
                damageFactor = getEffectivity(attacker, defender); // damage factor is calculated
                damage = (attacker.getAttack() + attack.getDamage() - defender.getDefense()) * damageFactor; // attacking damage is calculated

                if(damage > 0) // damage zero or below zero e.g. if Players defense is higher than the actual attack
                {
                    if(damage > defender.getHp())
                    {
                        damage = defender.getHp();
                    }

                    defender.adjustHp(damage); // adjusting the defenders HP

                    print("The hit was successful! " + attackerName+ " made " +damage+ " HP damage to " +defenderName);
                    print(defenderName+ " has " +defender.getHp()+ " HP left.");
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

    /**
     * Chooses a move for the enemy's Palmon.
     *
     * palmon  The enemy's Palmon.
     * palMoves The map of moves available to the enemy's Palmon.
     *
     * @return The chosen move.
     *
     * Software Runtime Complexity is O(1)
     */
    public Move chooseMoveEnemy(Palmon enemyPalmon, HashMap<Integer, ArrayList<Move>> enemiesPalMoves)
    {
        // Choose Random Move from Enemies Palmon
        ArrayList<Move> enemiesMoves = enemiesPalMoves.get(enemyPalmon.getId()); // saving the ArrayList with all the Moves of the current Palmon

        if(enemiesMoves.isEmpty())
        {
            print("Palmon " +Normalizer.normalize(enemyPalmon.getName())+ " from " +InitialMenu.enemyName+ " is blocked since it does not have any Move to perform. Lucky on you!");
            return null;
        }

        Random r = new Random();
        //TODO hier kommt es in ausnahmefällen noch zu einer Exception (siehe Eingabe Hannah)
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


    /**
     * Chooses a move for the player's Palmon.
     *
     * @param playerPalmon The player's Palmon.
     * @param enemyPalmon  The enemy's Palmon.
     *
     * palMoves he map of moves available to the player's Palmon.
     *
     * @return The chosen move.
     *
     * Software Runtime Complexity is O(1)
     */
    public Move chooseMovePlayer(Palmon playerPalmon, Palmon enemyPalmon, HashMap<Integer, ArrayList<Move>> playersPalMoves)
    {
        // telling the Player where he currently is
        ThreadSleep.sleep(500);
        print("It's time to choose a Move for your Palmon, " +InitialMenu.playerName);

        ThreadSleep.sleep(1000);

        // Let the Player choose the preferred Move
        // saving the ArrayList with all the Moves of the current Palmon
        ArrayList<Move> playersMoves;
        playersMoves = playersPalMoves.get(playerPalmon.getId());

        if(playersMoves.isEmpty())
        {
            print("Your Palmon is blocked since it does not have any Move to perform. :(");
            return null;
        }

        Move tempMove;
        // Printing out every Move the Player can choose from with its damage
        print("Possible Moves to choose from:");
        for (Move playersMove : playersMoves)
        {
            tempMove = playersMove;

            if (tempMove != null)
            {
                ThreadSleep.sleep(500);
                print(Normalizer.normalize(tempMove.getName()) + " with possible damage: " + tempMove.getDamage());
            }
        }

        ThreadSleep.sleep(2000);

        String choice = "";
        Move playerAttack = null;
        boolean moveFound = false;

        while(!moveFound)
        {
            choice = printWithScString("\nPlease type in the name of your preferred Move \n(i) Info of the currently battling Palmons", choice); // letting the Player choose a Move
            choice = choice.toLowerCase();

            if(choice.equals("i"))
            {
                ThreadSleep.sleep(1000);
                print("\nCurrent Stats from Palmon " +Normalizer.normalize(playerPalmon.getName())+ " (Palmon from " +InitialMenu.playerName+ ")");
                ThreadSleep.sleep(500);
                print("Heigt: " +playerPalmon.getHeight()+ "\nWeight: " +playerPalmon.getWeight()+ "\nTypes: " +Normalizer.normalize(playerPalmon.getTypeOne())+ " " +Normalizer.normalize(playerPalmon.getTypeTwo())+ "\nHP left: " +playerPalmon.getHp()+ "\nAttack: " +playerPalmon.getAttack()+ "\nDefense: " +playerPalmon.getDefense()+ "\nSpeed: " +playerPalmon.getSpeed());

                ThreadSleep.sleep(2000);

                print("\nCurrent Stats from Palmon " +Normalizer.normalize(enemyPalmon.getName())+ " (Palmon from " +InitialMenu.enemyName+ ")");
                ThreadSleep.sleep(500);
                print("Heigt: " +enemyPalmon.getHeight()+ "\nWeight: " +enemyPalmon.getWeight()+ "\nTypes: " +Normalizer.normalize(enemyPalmon.getTypeOne())+ " " +Normalizer.normalize(enemyPalmon.getTypeTwo())+ "\nHP left: " +enemyPalmon.getHp()+ "\nAttack: " +enemyPalmon.getAttack()+ "\nDefense: " +enemyPalmon.getDefense()+ "\nSpeed: " +enemyPalmon.getSpeed());

                ThreadSleep.sleep(1000);
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
                        print("You chose the Move " +playerAttack.getName());
                        break; // Exit the loop once the Move is found
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
           print("The chosen Move " +Normalizer.normalize(playerAttack.getName())+ " is not usable anymore. You used it too often. \nPlease choose a different Move");
           chooseMovePlayer(playerPalmon, enemyPalmon, playersPalMoves);
        }
        // saving the Index of the Move to update the Max Usages when using the Move
        return playerAttack;
    }

    /**
     * Determines the effectivity of an attack from one Palmon to another based on their types.
     *
     * @param attacker The attacking Palmon.
     * @param defender The defending Palmon.
     *
     * @return The smallest damage factor based on type effectivity.
     *
     * Software Runtime Complexity is O(n)
     */
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

    /**
     * Determines if a move hits based on its accuracy.
     *
     * @param move The move being used.
     * @return True if the move hits, otherwise false.
     *
     * Software Runtime Complexity is O(1)
     */
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

    /**
     * Handles the scenario when the player wins the battle.
     *
     * @param round             The round in which the player won.
     * @param playersPalmonssArr The list of player's Palmons.
     * @param enemiesPalmonssArr The list of enemy's Palmons.
     *
     * Software Runtime Complexity is O(n)
     */
    public void playerWon(int round, ArrayList<Palmon> playersPalmonssArr, ArrayList<Palmon> enemiesPalmonssArr)
    {
        // Copying the Palmons into an ArrayList for the CSV Battle Log in the end
        ArrayList <String> playerPalmons = new ArrayList<>();
        ArrayList <String> enemyPalmons = new ArrayList<>();

        print("You won! CONGRATULATIONS! Here are the names of your Palmons that supported you...");

        // putting every Palmon Name in both ArrayLists
        for(Palmon pPalmon: playersPalmonssArr)
        {
            playerPalmons.add(Normalizer.normalize(pPalmon.getName()));
            // printing out the Players Palmons
            ThreadSleep.sleep(500);
            print(Normalizer.normalize(pPalmon.getName()));
        }

        for(Palmon ePalmon: enemiesPalmonssArr)
        {
            enemyPalmons.add(Normalizer.normalize(ePalmon.getName()));
        }

        // creating an entry in Battle Log
        BattleDocumentation.initializeLogFile();
        BattleDocumentation.logBattle(round, InitialMenu.playerName, playerPalmons, InitialMenu.enemyName, enemyPalmons, "Player Won");

        ThreadSleep.sleep(1000);

        String input = "";
        input = printWithScString("(i) see battle log \n(type in anything else) end game", input);

        if(input.equals("i"))
        {
            BattleDocumentation.printBattleLog();
        }
    }

    /**
     * Prints the result and statistics when the player loses.
     *
     * @param round             The round number when the player lost.
     * @param playersPalmonssArr The list of player's Palmons.
     * @param enemiesPalmonssArr The list of enemy's Palmons.
     *
     * Software complexity is O(n)
     */
    public void playerLost(int round, ArrayList<Palmon> playersPalmonssArr, ArrayList<Palmon> enemiesPalmonssArr)
    {
        // Copying the Palmons into an ArrayList for the CSV Battle Log in the end
        ArrayList <String> playerPalmons = new ArrayList<>();
        ArrayList <String> enemyPalmons = new ArrayList<>();

        // putting every Palmon Name in both ArrayLists
        for(Palmon pPalmon: playersPalmonssArr)
        {
            playerPalmons.add(Normalizer.normalize(pPalmon.getName()));
        }

        for(Palmon ePalmon: enemiesPalmonssArr)
        {
            enemyPalmons.add(Normalizer.normalize(ePalmon.getName()));
        }

        // comforting the User
        print("You sadly lost, sorry for that :( \nBut dont be sad, I´m sure you´ll win next round, try again!");

        ThreadSleep.sleep(1000);

        // creating an entry in Battle Log
        BattleDocumentation.initializeLogFile();
        BattleDocumentation.logBattle(round, InitialMenu.playerName, playerPalmons, InitialMenu.enemyName, enemyPalmons, "Player Lost");

        String input = "";
        input = printWithScString("(i) see battle log \n(type in anything else) end game", input);

        if(input.equals("i"))
        {
            BattleDocumentation.printBattleLog();
        }
    }
}
