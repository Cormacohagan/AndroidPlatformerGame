package com.gamedev.techtronic.lunargame.AI;

import java.util.ArrayList;

public class Node implements Comparable{
    private int x;
    private int y;
    private boolean isBlocked;

    private Node parent;

    private int depth;

    private float cost;
    private float heuristic;
    private float costSoFar;
    private ArrayList<Connection> connections = new ArrayList<>();

    public Node(int x, int y, boolean isBlocked) {
        this.x = x;
        this.y = y;
        this.isBlocked = isBlocked;
    }


    public Node getParent() {
        return parent;
    }

    public int getDepth() {
        return depth;
    }

    public int setParent(Node parent) {

        if (parent==null){
            return 0;
        }

        depth = parent.depth +1;
        this.parent = parent;

        return depth;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public float getCostSoFar() {
        return costSoFar;
    }

    public void setCostSoFar(float costSoFar) {
        this.costSoFar = costSoFar;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(float heuristic) {
        this.heuristic = heuristic;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int compareTo(Object another) {
        Node o = (Node) another;

        float f = cost;
        float of = o.cost;

        if (f < of) {
            return -1;
        } else if (f > of) {
            return 1;
        } else {
            return 0;
        }

    }
}
