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

public class MyPacManEvolFirst  extends Controller<MOVE>{

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
        if(game.gameOver()){return myMove;}
        myMove = evolve(game, timeDue);
        return myMove;
    }


    public MOVE evolve(Game game, long timeDue) {


        MOVE bestMove = MOVE.NEUTRAL;
        int best = 0;
        int counter = 0;
        int value = 0;

        Game copy;
        Node x; //node to work with

        Comparator<Node> comparator = new NodeComparator();
        PriorityQueue<Node> q = new PriorityQueue<>(comparator);
        PriorityQueue<Node> holder = new PriorityQueue<>(comparator);
        PriorityQueue<Node> qCopy = new PriorityQueue<>(comparator);
        Node current = new Node(game, null, null);
        Node currNode;

        //initialization of the population
        for (MOVE move : game.getPossibleMoves(game.getPacmanCurrentNodeIndex())) {
            copy = game.copy();
            copy.advanceGame(move, new StarterGhosts().getMove());
            q.add(new Node(copy, move, current));
        }

        while (counter < 2500) {

            holder.clear();
            qCopy.clear();

            //advance all the nodes


            for(Node a : q){
                qCopy.add(a);
            }

            while(!qCopy.isEmpty()){
                currNode = qCopy.remove();
                if(currNode.state.gameOver()){
                    if(currNode.state.getNumberOfActivePills()==0 && currNode.state.getNumberOfActivePowerPills()==0){
                        return currNode.head().prevMove;
                    }
                }
                else{ //advance
                    for(MOVE move : currNode.state.getPossibleMoves(currNode.state.getPacmanCurrentNodeIndex())){
                        copy = currNode.state.copy();
                        copy.advanceGame(move, new StarterGhosts().getMove());
                        x = new Node(copy, move, currNode);
                        q.add(x);
                    }
                }
                q.remove(currNode);
            }

            Iterator<Node> itr = q.iterator();
            while(itr.hasNext() && holder.size()<18){
                holder.add(itr.next());
            }



            while(!q.isEmpty()){
                if(holder.size()>20)break; //end the loop once we have enough population
                x = q.remove();
                holder.add(mutate(x));
            }

            //clear the current population, copy new one
            q.clear();
            while(!holder.isEmpty()){
                q.add(holder.remove());
            }

            ++counter;

        }


        if(!q.isEmpty())bestMove=q.remove().prevMove;
        return  bestMove;


    }

    public Node mutate(Node x){
        Node parent = x.parent;
        Game mutation;
        Random rand = new Random();
        MOVE[] moves = parent.state.getPossibleMoves(parent.state.getPacmanCurrentNodeIndex());
        MOVE randMove = x.prevMove;
        while(randMove == x.prevMove){
            randMove = moves[rand.nextInt(moves.length)];
        }

        mutation = parent.state.copy();
        mutation.advanceGame(randMove, new StarterGhosts().getMove());
        return  new Node(mutation, randMove, parent);
    }
}
