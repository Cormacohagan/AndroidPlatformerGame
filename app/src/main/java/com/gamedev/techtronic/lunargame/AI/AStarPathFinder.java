package com.gamedev.techtronic.lunargame.AI;

import android.util.Log;

import com.gamedev.techtronic.lunargame.Level.Level;
import com.gamedev.techtronic.lunargame.gage.util.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

/*
 * Implementation of the A* pathfinding algorithm.<br> Generates a path from a start position to a goal position
 * (Note: Does not generate the perfect shortest path, but a good path due to heuristics)<br>
 * Threaded due to potential high execute times(in extreme scenarios) and not being time critical. <p>
 *
 * Implementation is based on pseudocode from:<br>
 * -Book: "Artificial Intelligence for games", 2nd edition, by Ian Millington & John Funge <br>
 * -Web: https://en.wikipedia.org/wiki/A*_search_algorithm <p>
 *
 * Possible future optimisations:<br>
 * -Finding if a node exists in open or closed (Currently functional but surely can be better/faster)<br>
 * -Graph for the level is currently recalculated every time it's run, it's completely unnecessary as
 * the graph doesn't change (it's based on the level tiles), a number of attempts were made but resulted
 * in memory overflow (which is taking too long to resolve, so I've reluctantly delegated it to
 * "future optimisations" )
 */
public class AStarPathFinder implements Runnable{

    private ArrayList<Vector2> path = new ArrayList<>();
    private Level level;
    private int sx, sy, tx, ty;

    public AStarPathFinder(Level level) {
        this.level = level;
    }

    public void setStartPosition(Vector2 position){
        if (Math.round((position.x+8)/16)>level.getLevelTileArray().length-1 || Math.round((position.y-8)/16) >level.getLevelTileArray()[0].length-1 ||
                Math.round((position.x+8)/16)<0 || Math.round((position.y-8)/16)<0){
            return;
        }

        this.sx = (int)((position.x) / 16);
        this.sy = (int)((position.y) / 16);
    }

    public void setGoalPosition(Vector2 position){
        if (Math.round((position.x)/16)>=level.getLevelTileArray().length-1 || Math.round((position.y-10)/16) >=level.getLevelTileArray()[0].length-1 ||
                Math.round((position.x)/16)<0 || Math.round((position.y-10)/16)<0){
            return;
        }
        this.tx =  (int)((position.x) / 16);
        this.ty = (int)((position.y+10) / 16);

    }

    public ArrayList<Vector2> getPath() {
        return path;
    }

    public static float heuristic(Node a, Node b){
        // Manhattan distance
//        return Math.abs(a.getX()-b.getX()) + Math.abs(a.getY()-b.getY());

        float dx = Math.abs(a.getX() - b.getX());
        float dy = Math.abs(a.getY() - b.getY());
        return (dx+dy);
    }

    @Override
    public void run() {

        Graph g = new Graph(level.getLevelTileArray());
        Node[][] nodes = g.getNodeArray();

        Node start = nodes[sx][sy];
        Node goal = nodes[tx][ty];
        Node current = null;


        // if the start/goal position has no connections, there is no path to find, so don't bother running
        if (start.getConnections().size()==0 || goal.getConnections().size()==0){
            return;
        }

        path = new ArrayList<>();

        // Really nice data structure for sorting elements, muuch faster than using arraylist
        PriorityQueue<Node> open = new PriorityQueue<>(10, new Comparator<Node>() {
            @Override
            public int compare(Node lhs, Node rhs) {
                return lhs.compareTo(rhs);
            }
        });

        // Closed list only needs to add elements and read elements, arrayList is fine for this purpose
        ArrayList<Node> closed = new ArrayList<>();

        // Initialise start node with 0 cost so far and no parent
        start.setCostSoFar(0);
        start.setParent(null);

        open.add(start);

        int counter=0;

        while(!open.isEmpty()) {

            // Set the current Node to the smallest cost so far Node from open, open is a priority queue sorted by each Node's costSoFar
            // the lowest costSoFar value is always on top
            current = open.peek();

            // Go through each of the adjacent nodes to the current node via the current node's connections array (generated by Graph)
            for (Connection connection: current.getConnections()){

                // If the node at the end of the connection is the goal node, set the current node to the end node (for generating the full path at the end)
                // Our work here is done
                if (connection.getToNode().getX() == goal.getX() && connection.getToNode().getY()==goal.getY()){
                    connection.getToNode().setParent(current);
                    current = connection.getToNode();
//                    Log.d("Astar", "Found goal");
                    break;
                }

                // Calculate the various values for the adjacent node in question
                connection.getToNode().setCostSoFar(current.getCostSoFar() + connection.getCost());
                connection.getToNode().setHeuristic(heuristic(connection.getToNode(), goal));
                connection.getToNode().setCost(connection.getToNode().getCostSoFar() + connection.getToNode().getHeuristic());

                boolean check=false;
                boolean inOpen=false;
                boolean inClosed = false;

                Node temp=null;
                for (Node n: open){
                    if ((n.getX() == connection.getToNode().getX()) && (n.getY() == connection.getToNode().getY())){
//                        Log.d("Astar", "Node with same position found in open");
                        temp=n;
                        check=true;
                        inOpen=true;
                    }
                }
                if (check){
                    if (temp.getCost()<connection.getCost()){
//                        Log.d("AStar", "node skipped");
                        // Skip this node
                        continue;
                    }
                }
                check = false;
                for (Node n: closed){
                    if ((n.getX() == connection.getToNode().getX()) && (n.getY() == connection.getToNode().getY())){
//                        Log.d("Astar", "Node with same position found in closed");
                        temp=n;
                        check=true;
                        inClosed=true;
                    }
                }
                if (check){
                    if (temp.getCost()<connection.getCost()){
//                        Log.d("AStar", "node skipped");
                        // Skip this node
                        continue;
                    }
                }
//                Log.d("AStar", "added new node to open");

                if (!inOpen && !inClosed){
                    connection.getToNode().setParent(current);
                    open.add(connection.getToNode());
                }

            }
            if ((current.getX()==goal.getX()) && (current.getY()==goal.getY())){
                break;
            }

            for (Node n : open){
                if ((n.getX() == current.getX()) && (n.getY() == current.getY())){
                    open.remove(n);
                }
            }

            closed.add(current);
        }

//        Log.d("AStar", String.valueOf(current.getX())+", "+String.valueOf(current.getY())+" | "+String.valueOf(start.getX())+", "+ String.valueOf(start.getY()+ " | " + String.valueOf(goal.getX()) + ", " + String.valueOf(goal.getY())));

        while(current.getParent()!=null) {

//            Log.d("AStar", String.valueOf(current.getX()) + ", " + String.valueOf(current.getX()) + " + costSoFar:" + String.valueOf(current.getCostSoFar()) + " + " + String.valueOf(current.getDepth()));
            path.add(new Vector2(current.getX(),current.getY()));
            current = current.getParent();
        }
        // Path is generated from goal to start node, reverse it, now start to goal
        Collections.reverse(path);
//        Log.d("AStar",String.valueOf(open.size())+", "+String.valueOf(closed.size()));
//        Log.d("AStar", "DONE!");

    }
}