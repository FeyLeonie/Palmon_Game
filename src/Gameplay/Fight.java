package Gameplay;

import CSVHandling.BattleDocumentation;
import CSVHandling.CSVHandler;
import Tools.Language;
import Tools.Normalizer;
import Tools.Printing;
import DataStructures.Queue;
import Elements.Effectivity;
import Elements.Move;
import Elements.Palmon;
import Tools.ThreadSleep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Represents the fight between the Player and the Enemy
 */
public class Fight extends Printing
{
    public Game game;
    public CSVHandler data; // CSVHandler

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
    public Fight(Game game, CSVHandler data)
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
     * Software Runtime is O(n)
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

            double effectivityPlayer = getEffectivity(playerPalmon, enemyPalmon);
            double effectivityEnemy = getEffectivity(enemyPalmon, playerPalmon);

            if((enemyMove == null && playerMove == null) && (effectivityPlayer == 0 && effectivityEnemy == 0)) // If both Palmons are blocked since they dont have any Moves to perform they will be defeated
            {
                print(Language.getMessage("FBothPalmonsBlocked", Normalizer.normalize(playerPalmon.getName()), InitialMenu.playerName, Normalizer.normalize(enemyPalmon.getName()), InitialMenu.enemyName));
                playerPalmon.adjustHp(10000.0);
                enemyPalmon.adjustHp(10000.0);
            }
            else
            {
                if(playerPalmon.getSpeed() >= enemyPalmon.getSpeed())
                {
                    //Player starts attacking
                    ThreadSleep.sleep(1000);
                    print(Language.getMessage("FMessagePlayerStartsAttacking", playerPalmonName));
                    enemyPalmon = attackingSequence(playerPalmon, playerMove, enemyPalmon, playersPalMoves, playerMoveIndex);

                    if(enemyPalmon.getHp() > 0)
                    {
                        ThreadSleep.sleep(1000);
                        // Enemy starts attacking
                        print(Language.getMessage("FMessageEnemyAttacksSecond",enemyPalmonName, InitialMenu.enemyName, playerPalmonName));
                        playerPalmon = attackingSequence(enemyPalmon, enemyMove, playerPalmon, enemiesPalMoves, enemyMoveIndex);
                    }
                    else
                    {
                        ThreadSleep.sleep(1000);
                        // Enemy Palmon is already defeated and can not attack anymore
                        print(Language.getMessage("FPlayerDefeatedPalmon", playerPalmonName, enemyPalmonName));
                    }
                }
                else
                {
                    ThreadSleep.sleep(1000);
                    // Enemy starts attacking
                    print(Language.getMessage("FEnemyStartsAttacking", enemyPalmonName, InitialMenu.enemyName));
                    playerPalmon = attackingSequence(enemyPalmon, enemyMove, playerPalmon, enemiesPalMoves, enemyMoveIndex);

                    if(playerPalmon.getHp() > 0)
                    {
                        ThreadSleep.sleep(1000);
                        //Player starts attacking
                        print(Language.getMessage("FMessagePlayerAttacksSecond", playerPalmonName, enemyPalmonName));
                        enemyPalmon = attackingSequence(playerPalmon, playerMove, enemyPalmon, playersPalMoves, playerMoveIndex);
                    }
                    else
                    {
                        ThreadSleep.sleep(1000);
                        // Player Palmon is already defeated and can not attack anymore
                        print(Language.getMessage("FPlayerPalmonDefeated", playerPalmonName, enemyPalmonName));
                    }
                }
            }
            ThreadSleep.sleep(1);
            String choice = "";
            choice = printWithScString(Language.getMessage("FRoundAboutToEndMessage", round), choice);

            if(choice.equals("i"))
            {
                print(Language.getMessage("FPlayerStats", InitialMenu.playerName));
                ThreadSleep.sleep(500);
                print(Language.getMessage("FPSCurrentPalmon", playerPalmonName, playerPalmon.getHp()));
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
                print(Language.getMessage("FPSAmountPalmonsLeft", palsLeft, InitialMenu.playerName));

                ThreadSleep.sleep(1000);

                print(Language.getMessage("FEnemyStats", InitialMenu.enemyName));
                ThreadSleep.sleep(500);
                print(Language.getMessage("FPSCurrentEnemy", enemyPalmonName, enemyPalmon.getHp()));
                ThreadSleep.sleep(500);
                if(enemyPalmon.getHp() <= 0) // if current fighting Palmon is defeated
                {
                    palsLeft = enemyPalmons.getQueueSize(); // current team size is the Queue size
                }
                else // if current Palmon still has some HP
                {
                    palsLeft = enemyPalmons.getQueueSize() + 1; // teamsize is the current Palmon + Queuesize
                }
                print(Language.getMessage("FPSAmountPalmonsLeftEnemy", palsLeft, InitialMenu.enemyName));
            }


            if(playerPalmon.getHp() <= 0) // if Palmon is defeated
            {
                if(playerPalmons.getQueueSize() == 0) // and if theres no Palmon in the Players Queue anymore
                {
                    playerLost(round, playersPalmonssArr, enemiesPalmonssArr); // the Player Lost
                    break;
                }
                ThreadSleep.sleep(1000);
                playerPalmon = playerPalmons.dequeue(); // otherwise: load next Palmon into fight
                print(Language.getMessage("FLoadNextPlayerPalmon", Normalizer.normalize(playerPalmon.getName())));
            }

            if(enemyPalmon.getHp() <= 0) // if Palmon is defeated
            {
                if(enemyPalmons.getQueueSize() == 0)// and if theres no Palmon in the Enemies Queue anymore
                {
                    playerWon(round, playersPalmonssArr, enemiesPalmonssArr); // the Player won !!! (Glückwunsch Sebastiaaaan!!)
                    break;
                }
                ThreadSleep.sleep(1000);
                enemyPalmon = enemyPalmons.dequeue(); // otherwise: load next Palmon into fight
                print(Language.getMessage("FLoadNextEnemyPalmon", InitialMenu.enemyName, Normalizer.normalize(enemyPalmon.getName())));
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
     * @param attack          The move used by the attacking Palmon.
     * @param moveHashMap     The map of moves available to the Palmon.
     * @param index           The index of the move in the list.
     *
     * @return The updated defending Palmon after the attack.
     *
     * Software Runtime is O(1)
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
            print(Language.getMessage("FAttackPalBlocked", attackerName));
            return defender;
        }

        // Player starts
        hits = setAccuracy(attack);

        ThreadSleep.sleep(1000);
        print(Language.getMessage("FAttackBoutToHappen", attackerName, defenderName, defender.getHp()));
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

                    print(Language.getMessage("FHitSuccessful", attackerName, damage, defenderName));

                    defender.adjustHp(damage); // adjusting the defenders HP

                    print(Language.getMessage("FDefenderHPLeft", defenderName, defender.getHp()));
                }
                else
                {
                    print(Language.getMessage("FDamageFactorZero"));
                }
            }
            else
            {
                print(Language.getMessage("FHitNotSuccessfull", attackerName));
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
     * @param enemyPalmon       The enemy's Palmon.
     * @param enemiesPalMoves   The map of moves available to the enemies Palmon.
     *
     * @return The chosen move.
     *
     * Software Runtime is O(1)
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
     * @param playerPalmon      The player's Palmon.
     * @param enemyPalmon       The enemy's Palmon.
     * @param playersPalMoves   The map of moves available to the Players Palmon.
     *
     * @return The chosen move.
     *
     * Software Runtime is O(1)
     */
    public Move chooseMovePlayer(Palmon playerPalmon, Palmon enemyPalmon, HashMap<Integer, ArrayList<Move>> playersPalMoves)
    {
        // telling the Player where he currently is
        ThreadSleep.sleep(500);
        print( Language.getMessage("FMoveChooseMessage", InitialMenu.playerName, Normalizer.normalize(playerPalmon.getName())));

        ThreadSleep.sleep(1000);

        // Let the Player choose the preferred Move
        // saving the ArrayList with all the Moves of the current Palmon
        ArrayList<Move> playersMoves;
        playersMoves = playersPalMoves.get(playerPalmon.getId());

        if(playersMoves.isEmpty())
        {
            print(Language.getMessage("FNoMovePalmonBlocked", Normalizer.normalize(playerPalmon.getName())));
            return null;
        }

        Move tempMove;
        // Printing out every Move the Player can choose from with its damage
        print(Language.getMessage("FPossibleMoves"));
        for (Move playersMove : playersMoves)
        {
            tempMove = playersMove;

            if (tempMove != null)
            {
                ThreadSleep.sleep(500);
                print(Language.getMessage("FMove", Normalizer.normalize(tempMove.getName()), tempMove.getDamage()));
            }
        }

        ThreadSleep.sleep(2000);

        String choice = "";
        Move playerAttack = null;
        boolean moveFound = false;

        while(!moveFound)
        {
            choice = printWithScString(Language.getMessage("FPlayerChoosesMove"), choice); // letting the Player choose a Move
            choice = choice.toLowerCase();

            if(choice.equals("i"))
            {
                ThreadSleep.sleep(1000);

                // Stats Player
                print(Language.getMessage("FPalmonStatsPrint", Normalizer.normalize(playerPalmon.getName()), InitialMenu.playerName));
                ThreadSleep.sleep(500);
                print(Language.getMessage("FPalmonStats", playerPalmon.getHeight(), playerPalmon.getWeight(), Normalizer.normalize(playerPalmon.getTypeOne()), Normalizer.normalize(playerPalmon.getTypeTwo()), playerPalmon.getHp(), playerPalmon.getAttack(), playerPalmon.getDefense(), playerPalmon.getSpeed()));

                ThreadSleep.sleep(2000);

                // Stats Enemy
                print(Language.getMessage("FPalmonStatsPrint", Normalizer.normalize(enemyPalmon.getName()), InitialMenu.enemyName));
                ThreadSleep.sleep(500);
                print(Language.getMessage("FPalmonStats", enemyPalmon.getHeight(), enemyPalmon.getWeight(), Normalizer.normalize(enemyPalmon.getTypeOne()), Normalizer.normalize(enemyPalmon.getTypeTwo()), enemyPalmon.getHp(), enemyPalmon.getAttack(), enemyPalmon.getDefense(), enemyPalmon.getSpeed()));

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
                        print(Language.getMessage("FMoveChosen", Normalizer.normalize(playerAttack.getName())));
                        break; // Exit the loop once the Move is found
                    }
                }

                if(playerAttack == null)
                {
                    print(Language.getMessage("FMoveChosenNotFound"));
                }
            }
        }


        if(playerAttack.usagesLeft() <= 0) // if selected Move is already on Max Usages this will be needed
        {
            print(Language.getMessage("FMoveChosenNotUsableAnymore", Normalizer.normalize(playerAttack.getName())));
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

        ArrayList<Effectivity> effectivity_db = CSVHandler.effectivity_db;

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

        print(Language.getMessage("FWinningGratulations"));

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
        BattleDocumentation.logBattle(round, InitialMenu.playerName, playerPalmons, InitialMenu.enemyName, enemyPalmons, Language.getMessage("FOutcomeWon"));

        ThreadSleep.sleep(1000);

        String input = "";
        input = printWithScString(Language.getMessage("FBattleLogEndGame"), input);

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
        print(Language.getMessage("FSorryYouLost"));

        ThreadSleep.sleep(1000);

        // creating an entry in Battle Log
        BattleDocumentation.initializeLogFile();
        BattleDocumentation.logBattle(round, InitialMenu.playerName, playerPalmons, InitialMenu.enemyName, enemyPalmons, Language.getMessage("FOutcomeLost"));

        String input = "";
        input = printWithScString(Language.getMessage("FBattleLogEndGame"), input);

        if(input.equals("i"))
        {
            BattleDocumentation.printBattleLog();
        }
    }
}
