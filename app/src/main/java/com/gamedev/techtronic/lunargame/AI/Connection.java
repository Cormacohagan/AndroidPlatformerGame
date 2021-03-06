package com.gamedev.techtronic.lunargame.AI;

/*
 * A connection describes the relationship between nodes in a graph.
 * Used to generate a directional weighted graph.
 *
 * */
public class Connection {

    private int cost;
    private Node fromNode;
    private Node toNode;

    /**
     * @param fromNode The node you are coming from
     * @param toNode The node you are going to
     */
    public Connection(Node fromNode, Node toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;

        // Default cost assumed to be 1 (for tile based graph cost is uniform)
        cost=1;
    }

    public int getCost(){
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Node getFromNode(){
        return fromNode;
    }

    public Node getToNode(){
        return toNode;
    }
}
