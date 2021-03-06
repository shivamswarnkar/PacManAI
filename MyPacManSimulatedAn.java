package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import javax.swing.plaf.multi.MultiViewportUI;
import java.util.*;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacManSimulatedAn extends Controller<MOVE>
{

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

    static  class NodeComparator implements Comparator<Node>{
        @Override
        public int compare(Node n1, Node n2){ //the hurestic function
            if(n1.state.getScore() > n2.state.getScore()){
                return 1;
            }
            else if(n1.state.getScore() < n2.state.getScore()){
                return -1;
            }
            return 0;
        }
    }



    private MOVE myMove = MOVE.NEUTRAL;

    public MOVE getMove(Game game, long timeDue)
    {
        //Place your game logic here to play the game as Ms Pac-Man
        //MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
        //myMove = possibleMoves[new Random().nextInt(possibleMoves.length)];

        myMove = simulatedAnnealing(game, timeDue);
        return myMove;
    }


    public MOVE simulatedAnnealing(Game game, long timeDue) {
        MOVE bestMove = MOVE.NEUTRAL;
        int best=0;

        int value =0;

        int T = Integer.MAX_VALUE/100000;
        int deltaE;
        MOVE[] q = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
        Node current = new Node(game.copy(), null, null);
        Node next;
        Game nextState;
        MOVE nextMove;


        while(true){
            --T;
            if(T==0)return current.head().prevMove;
            nextMove = q[new Random().nextInt(q.length)];
            nextState=current.state.copy();
            nextState.advanceGame(nextMove, new StarterGhosts().getMove());
            next = new Node(nextState, nextMove, current);
            deltaE = next.state.getScore() - current.state.getScore();

            if(deltaE >0){
                current = next;
            }
            else{
                if(prob(Math.exp(deltaE/T)))current = next;
            }

        }


    }

    public boolean prob(double prob){
        double num = prob*1000;
        int randomInt = new Random().nextInt(100);
        if(randomInt <=num)return true;
        return  false;
    }



}


