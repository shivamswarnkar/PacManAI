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

public class MyPacManAStar extends Controller<MOVE> {

    public class Node {

        public Node(Game game, MOVE currMove, Node parentNode) {
            state = game;
            parent = parentNode;
            prevMove = currMove;
        }

        Game state;
        MOVE prevMove;
        Node parent;


        public Node head() {
            Node current = this;

            while (current.parent != null && current.parent.parent != null) {
                current = current.parent;
            }
            return current;
        }

        public void setParent(Node parentNode) {
            parent = parentNode;
        }
    }

    static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) { //the hurestic function
            if (n1.state.getScore() > n2.state.getScore()) {
                return 1;
            } else if (n1.state.getScore() < n2.state.getScore()) {
                return -1;
            }
            return 0;
        }
    }


    private MOVE myMove = MOVE.NEUTRAL;

    public MOVE getMove(Game game, long timeDue) {
        //Place your game logic here to play the game as Ms Pac-Man
        //MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
        //myMove = possibleMoves[new Random().nextInt(possibleMoves.length)];

        myMove = aStar(game, timeDue);
        return myMove;
    }


    public MOVE aStar(Game game, long timeDue) {
        MOVE bestMove = MOVE.NEUTRAL;
        int best = 0;
        int value;
        int counter = 0;
        Game copy;
        Node current, child;


        Comparator<Node> comparator = new NodeComparator();
        Queue<Node> close = new LinkedList<>();
        PriorityQueue<Node> open = new PriorityQueue<>(comparator);
        open.add(new Node(game, null, null));

        while (!open.isEmpty() && counter < 1000) {
            current = open.remove();
            close.add(current);

            if (current.state.gameOver()) {
                if (current.state.getNumberOfActivePills() == 0 && current.state.getNumberOfActivePowerPills() == 0) {
                    return current.head().prevMove;
                }
            }

            else {

                value = current.state.getScore();

                if (value > best){
                    best = value;
                    bestMove = current.head().prevMove;
                }

                for (MOVE move : current.state.getPossibleMoves(current.state.getPacmanCurrentNodeIndex())) {
                    copy = current.state.copy();
                    copy.advanceGame(move, new StarterGhosts().getMove());
                    child = new Node(copy, move, current);

                    value = copy.getScore();

                    if (value > best){
                        best = value;
                        bestMove = child.head().prevMove;
                    }


                    if (has(open, child) || has(close, child)) { //change the condition to work
                        //Do stuff when child is already in open or close
                        if (new NodeComparator().compare(child.parent, current) == -1) {
                            child.setParent(current);
                        }
                    } else {
                        open.add(child);
                    }

                }

                ++counter;

            }
        }

        //System.out.println("Go one move");

        return bestMove;
    }

    public boolean has(Queue<Node> q, Node elem) {
        for (Node x : q) {
            if (x.state == elem.state) return true;
        }

        return false;
    }
}


