package uk.co.prometheux.prometheuxreasoner.precedenceGraph;

import java.util.Objects;

/**
 * An edge representing a dependecies with another node.
 * The dependency edge is marked negative if is related to a negation or an aggregation
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class Edge {
    private Node to;
    private boolean isPositive;

    public Edge(Node t, boolean isPositive){
        this.to = t;
        this.isPositive = isPositive;
    }

    public Node getTo() {
        return to;
    }

    public boolean isPositive() {
        return isPositive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return isPositive == edge.isPositive &&
                Objects.equals(to, edge.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(to, isPositive);
    }

    public String toString(){
        if(this.isPositive)
            return this.to.getAtomName() + " +";
        else
            return this.to.getAtomName() + " -";
    }
}
