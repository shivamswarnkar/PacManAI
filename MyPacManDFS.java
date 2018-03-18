package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import javax.swing.plaf.multi.MultiViewportUI;
import java.util.Random;
import java.util.EnumMap;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */

public class MyPacManDFS extends Controller<MOVE>
{
    private MOVE myMove = MOVE.NEUTRAL;

    public MOVE getMove(Game game, long timeDue)
    {
        //Place your game logic here to play the game as Ms Pac-Man
        //MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
        //myMove = possibleMoves[new Random().nextInt(possibleMoves.length)];

        myMove = dfs(game, timeDue);
        return myMove;
    }


    /****DFS***
     **********
     public MOVE dfs(Game game, long timeDue){
     MOVE bestMove = MOVE.NEUTRAL;
     int best =0;

     for(MOVE move : game.getPossibleMoves(game.getPacmanCurrentNodeIndex())){
     Game copy = game.copy();

     copy.advanceGame(move, new StarterGhosts().getMove());
     int value = dfsRecursive(game);
     if(value > best){
     best = value;
     bestMove = move;
     }
     }

     System.out.println("got one move!");
     return bestMove;

     }

     public int dfsRecursive(Game game){
     int best = 0;
     for(MOVE move : game.getPossibleMoves(game.getPacmanCurrentNodeIndex())){
     Game copy  = game.copy();
     copy.advanceGame(move, new StarterGhosts().getMove());
     if(copy.gameOver()){
     int value = copy.getScore();
     if(copy.getNumberOfActivePills()==0)return value;
     if(value >0) best = value;
     }
     else {
     return dfsRecursive(copy);
     }

     }
     return best;
     }
     */

	/*Iterative depeening*/

    public MOVE dfs(Game game, long timeDue){
        MOVE bestMove = MOVE.NEUTRAL;
        int best =0;

        for(MOVE move : game.getPossibleMoves(game.getPacmanCurrentNodeIndex())){

            Game copy = game.copy();

            //EnumMap<pacman.game.Constants.GHOST, MOVE> ghostMove = new EnumMap<>(pacman.game.Constants.GHOST.class);

            copy.advanceGame(move, new StarterGhosts().getMove());
            //copy.advanceGame(move, ghostMove);



            int value = dfsRecursive(game,0);
            if(value > best){
                best = value;
                bestMove = move;
            }
        }

        System.out.println("got one move!");
        return bestMove;

    }

    public int dfsRecursive(Game game, int counter){
        int best = 0;
        for(MOVE move : game.getPossibleMoves(game.getPacmanCurrentNodeIndex())){
            Game copy  = game.copy();
            copy.advanceGame(move, new StarterGhosts().getMove());
            if(copy.gameOver() || counter >=3478){
                int value = copy.getScore();
                if(copy.getNumberOfActivePills()==0)return value;
                if(value >0) best = value;
            }
            else {
                return dfsRecursive(copy, ++counter);
            }

        }
        return best;
    }
	/**/
}