package com.gamedev.techtronic.lunargame.AI;

import com.gamedev.techtronic.lunargame.Level.Level;
import com.gamedev.techtronic.lunargame.Level.Tile;

public class Graph {

    private final Node[][] nodeArray;

    /**
     * Graph generates a weighted graph consisting of linked nodes from a tile based level for use in A* pathfinding.
     * Only needs to be constructed once as tile based level does not change.
     * Each node in the graph has multiple connections to other nodes, a maximum of 4, up, down, left and right.
     * A node may connect to another node if it is above, below, left or right of the current node and is also not collidable
     * (note: a node represents a valid position in the level an object like the player can be in)
     * @param tileArray
     */
    public Graph(Tile[][] tileArray) {

        nodeArray = new Node[tileArray.length][tileArray[0].length];

        // Read the tile data into the node array
        for (int x = 0; x < nodeArray.length; x++) {
            for (int y = 0; y < nodeArray[0].length; y++) {
                nodeArray[x][y] = new Node((int)tileArray[x][y].position.x, (int)tileArray[x][y].position.y, tileArray[x][y].isCollidable());
            }
        }

        for (int x = 0; x < nodeArray.length; x++) {
            for (int y = 0; y < nodeArray[0].length; y++) {

                // A blocked tile can't be a node
                if (nodeArray[x][y].isBlocked()){continue;}

                // Validation, no values less than 0
                // Maximum of 4 connections, up/down/left/right, all equal cost
                if ( !(y-1<0)){
                    if (!(nodeArray[x][y-1].isBlocked())){
                        //Get node above current node
                        nodeArray[x][y].addConnection(new Connection(nodeArray[x][y], nodeArray[x][y-1]));
                    }

                }
                if (!(y+1>= nodeArray[0].length) ){
                    if (!(nodeArray[x][y+1].isBlocked())){
                        //Get node below current node
                        nodeArray[x][y].addConnection(new Connection(nodeArray[x][y], nodeArray[x][y+1]));
                    }

                }
                if (!(x-1<0)){
                    if (!(nodeArray[x-1][y].isBlocked())){
                        //Get node to the left of current node
                        nodeArray[x][y].addConnection(new Connection(nodeArray[x][y], nodeArray[x-1][y]));
                    }

                }
                if (!(x+1>= nodeArray.length)){
                    if (!(nodeArray[x+1][y].isBlocked())){
                        //Get node to the right of current node
                        nodeArray[x][y].addConnection(new Connection(nodeArray[x][y], nodeArray[x+1][y]));
                    }

                }
            }
        }
    }

    public Node[][] getNodeArray() {
        return nodeArray;
    }

}
