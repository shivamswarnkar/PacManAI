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

public class MyPacManBFS  extends Controller<MOVE>{

    public class Node{

        public Node(Game game, MOVE currMove, Node parentNode) {
            state=game;
            parent=parentNode;
            prevMove = currMove;
        }
        Game state;
        MOVE prevMove;
        Node parent;


        public Node head(){
            Node current = this;

            while(current.parent != null && current.parent.parent != null){
                current = current.parent;
            }
            return current;
        }

        public  void setParent(Node parentNode){
            parent = parentNode;
        }
    }

    private MOVE myMove = MOVE.NEUTRAL;

    public MOVE getMove(Game game, long timeDue)
    {
        //Place your game logic here to play the game as Ms Pac-Man
        //MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
        //myMove = possibleMoves[new Random().nextInt(possibleMoves.length)];

        myMove = bfs(game, timeDue);
        return myMove;
    }


    public MOVE bfs(Game game, long timeDue){
        MOVE bestMove = MOVE.NEUTRAL;
        int best=0;
        int counter = 0;
        int value =0;
        Queue<Node> q = new LinkedList<>();
        Node current;
        Game child;

        q.add(new Node(game.copy(), null, null));

        while(!q.isEmpty() && (counter< 10000)){
            current = q.remove();
            for(MOVE move : current.state.getPossibleMoves(current.state.getPacmanCurrentNodeIndex())){

                if((current.state.getNumberOfActivePills()==0) && (current.state.getNumberOfActivePowerPills()==0)) return current.head().prevMove;
                value = current.state.getScore();
                if( value> best){
                    best = value;
                    bestMove = current.head().prevMove;
                }


                child = current.state.copy();
                child.advanceGame(move, new StarterGhosts().getMove());
                if(!child.gameOver())q.add(new Node(child, move, current));
            }
            ++counter;

        }



        return bestMove;

    }

    //try getting moves for the specific state not the child

	/**/
}
