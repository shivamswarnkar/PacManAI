package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import javax.swing.plaf.multi.MultiViewportUI;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.EnumMap;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */

public class MyPacManItrDep extends Controller<MOVE>{
    private MOVE myMove = MOVE.NEUTRAL;

    public MOVE getMove(Game game, long timeDue)
    {
        //Place your game logic here to play the game as Ms Pac-Man
        //MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
        //myMove = possibleMoves[new Random().nextInt(possibleMoves.length)];

        myMove = iterativeDep(game, timeDue);
        return myMove;
    }


    public MOVE iterativeDep(Game game, long timeDue){
        MOVE bestMove = MOVE.NEUTRAL;
        int best=0;
        int counter = 0;
        int value;
        Game copy;
        for(MOVE move : game.getPossibleMoves(game.getPacmanCurrentNodeIndex())){

            copy = game.copy();
            copy.advanceGame(move, new StarterGhosts().getMove());
            for(int depth=0; depth<30; ++depth ){
                value= depthLimit(copy, depth);
                if(value > best){
                    best = value;
                    bestMove = move;
                }
            }
        }
        return bestMove;

    }

    public int depthLimit(Game game, int depth){
        int best=game.getScore();
        int value;
        Game copy;
        for(MOVE move: game.getPossibleMoves(game.getPacmanCurrentNodeIndex())){
            copy = game.copy();
            copy.advanceGame(move, new StarterGhosts().getMove());
            if(copy.gameOver() || depth<=0){
                value = copy.getScore();
                if(copy.getNumberOfActivePowerPills()==0 && copy.getNumberOfActivePills()==0){
                    return value;
                }
                if(value>best ){
                    best = value;
                }
            }
            else{
                return depthLimit(copy, --depth);
            }
        }
        return best;
    }

	/**/
}
