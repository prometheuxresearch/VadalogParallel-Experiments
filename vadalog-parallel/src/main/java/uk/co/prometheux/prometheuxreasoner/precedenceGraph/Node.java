package uk.co.prometheux.prometheuxreasoner.precedenceGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This Class represents a Node of a precedence graph
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */

public class Node {
    private String atomName;
    private List<Edge> relations;

    public Node(String atomName){
        this.atomName = atomName;
        this.relations = new ArrayList<>();
    }

    public String getAtomName(){
        return this.atomName;
    }

    public List<Edge> getRelations() {
        return this.relations;
    }

    public void updateRelations(List<Edge> relations) {
        for(Edge e : relations){
        	addEdge(e);
        }
    }

    public void setRelations(List<Edge> relations) {
    	this.relations.clear();
    	updateRelations(relations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(atomName, node.atomName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atomName);
    }

    public String toString(){
        return this.getAtomName();
    }

    public void addEdge(Edge edge) {
        if(!relations.contains(edge))
            relations.add(edge);
    }
}
