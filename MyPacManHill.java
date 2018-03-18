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
public class MyPacManHill extends Controller<MOVE>
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

        myMove = hill(game, timeDue);

        System.out.println("Go one move");
        if(myMove == null)System.out.println("null");
        if(myMove == MOVE.NEUTRAL)System.out.println("neutral");
        if(myMove == MOVE.DOWN)System.out.println("down");
        if(myMove == MOVE.LEFT)System.out.println("left");
        if(myMove == MOVE.UP)System.out.println("up");
        return myMove;
    }


    public MOVE hill(Game game, long timeDue){
        MOVE bestMove = MOVE.NEUTRAL;
        int best=0;
        int value;
        int counter=0;
        Game copy;
        Node current, neighbor, child;

        current = new Node(game, null, null);
        neighbor = null;




        while(counter < 1000){
            //select best child (neighbor)
            best =0;
            for(MOVE move : current.state.getPossibleMoves(current.state.getPacmanCurrentNodeIndex())){
                copy = current.state.copy();
                copy.advanceGame(move, new StarterGhosts().getMove());
                child = new Node(copy, move, current);
                value = copy.getScore();
                if(child.state.gameOver()){
                    if(child.state.getNumberOfActivePills()==0 && child.state.getNumberOfActivePowerPills()==0) {
                        return current.head().prevMove;
                    }
                }
                else {

                    if (value > best) {
                        best = value;
                        neighbor = child;
                        bestMove = neighbor.head().prevMove;
                    }
                }
            }


            if(neighbor.state.getScore() <= current.state.getScore()){
                return current.head().prevMove;
            }

            current = neighbor;
            ++counter;

        }
        return bestMove;


    }


}