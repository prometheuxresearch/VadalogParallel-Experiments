package uk.co.prometheux.prometheuxreasoner.precedenceGraph;

import java.util.*;

/**
 * A graph data structure representing a vadalog program.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class PrecedenceGraph {
	
	private static final String AGGREGATE_KEYWORD_PART = "AGGR"+UUID.randomUUID().toString().replaceAll("-", "");
	
	public enum EMode {
		/**
		 * Stratification based on inferences (dependencies)
		 */
		STANDARD,
		/**
		 * Stratification based on negations
		 */
		NEGATION_BASED
	}
	
	private final EMode mode;
	
    private HashMap<String, Node> nodes;
    /* all SCCs in the graph */
    private List<List<Node>> SCCs;
    /* a network of SCCs dependencies, the integer is the level, greater level means dependencies with lower level/levels */
    private Map<Integer, List<List<Node>>> orderedNetwork;

    public PrecedenceGraph(){
        this(EMode.NEGATION_BASED);
    }
    
    public PrecedenceGraph(EMode mode){
    	this.mode = mode;
        this.nodes = new HashMap<>();
        this.SCCs = new ArrayList<>();
        this.orderedNetwork = new HashMap<>();
    }

    public void addNode(Node n) {
        nodes.put(n.getAtomName(), n);
    }

    public Node createIfAbsent(String name) {
        return nodes.computeIfAbsent(name, Node::new);
    }

    public Node getNodeFromName(String nodeName) {
        for(Node n : nodes.values()){
            if(n.getAtomName().equals(nodeName))
                return n;
        }
        return null;
    }

    /* Atoms that are not present in head of a rule, are not dependent on others and so can be removed */
    public void pruneNotIdgEdges() {
        List<Node> toRemove = new ArrayList<>();
        for(Node n : nodes.values()){
            if(!isIDG(n) && !n.getAtomName().contains(AGGREGATE_KEYWORD_PART))
                toRemove.add(n);
        }
        toRemove.forEach(node -> this.nodes.remove(node.getAtomName()));
    }

    private boolean isIDG(Node toCheck) {
        for(Node n : nodes.values()){
            List<Edge> relations = n.getRelations();
            for(Edge e : relations){
                if(e.getTo().equals(toCheck)){
                	return true;
                }
            }
        }
        return false;
    }


    private boolean findNegativeEdgeInScc(List<Node> sccComponent){
        for(Node n : sccComponent){
            List<Edge> relations = n.getRelations();
            for(Edge e : relations){
                if(sccComponent.contains(e.getTo()) && !e.isPositive())
                    return true;
            }
        }
        return false;
    }

    /* Used by the ModelConcurrentDecorator to check if a model is Stratifiable
     * A model is Stratifiable iff it has not cycle with negative edges */
    public boolean hasCycleWithNegativeEdge() {
        for(List<Node> sccComponent : this.SCCs){
            if(sccComponent.size() > 1 ){
                if(findNegativeEdgeInScc(sccComponent))
                    return true;
            }
        }
        return false;
    }

    private List<Node> getSccOfNode(Node n){
        for(List<Node> scc : this.SCCs){
            if(scc.contains(n))
                return scc;
        }
        return null;
    }

    public boolean sameStrata(String headAtomName, Set<String> bodyAtomNames){
        List<Node> headScc = getSccOfNode(getNodeFromName(headAtomName));
        List<Node> bodyAtomScc;
        for(String bodyAtomName : bodyAtomNames){
            bodyAtomScc = getSccOfNode(getNodeFromName(bodyAtomName));
            if(bodyAtomScc != null && bodyAtomScc.equals(headScc))
                return true;
        }

        return false;
    }

    /* SCCs produced gives us information about cycles and subprograms that can be evaluated concurrently
     * It uses the Kosaraju’s algorithm */
    public void SCCs(){
        Stack<String> stack = new Stack<>();
        List<Node> visited = new ArrayList<>();

        for(Node n : nodes.values()){
            if(!visited.contains(n))
                fillOrder(n, visited, stack);
        }

        PrecedenceGraph g = this.getTranspose();

        visited = new ArrayList<>();
        while(!stack.empty()){
            List<Node> scc = new ArrayList<>();
            String nodeName = stack.pop();
            Node n = g.getNodeFromName(nodeName);
            if(!visited.contains(n)){
                g.DFSforScc(n, visited, scc);
                addScc(scc);
            }
        }
    }

    private void addScc(List<Node> scc){
        for(Node n : scc)
            n.setRelations(this.getNodeFromName(n.getAtomName()).getRelations());
        this.SCCs.add(scc);
        updateNetwork(scc);
    }

    private void DFSforScc(Node n, List<Node> visited, List<Node> scc) {
        if(!visited.contains(n))
            visited.add(n);
        scc.add(n);
        List<Edge> relations = n.getRelations();
        for(Edge e : relations){
            if(!visited.contains(e.getTo()))
                DFSforScc(e.getTo(), visited, scc);
        }
    }

    private PrecedenceGraph getTranspose(){
        PrecedenceGraph g = new PrecedenceGraph();

        for(Node n : this.nodes.values()){
            g.addNode(new Node(n.getAtomName()));
        }

        for(Node n : g.nodes.values()){
            List<Edge> relations = this.getNodeFromName(n.getAtomName()).getRelations();
            for(Edge e : relations){
                Node v = g.getNodeFromName(e.getTo().getAtomName());
                v.addEdge(new Edge(n, e.isPositive()));
            }
        }

        return g;
    }

    private void fillOrder(Node n, List<Node> visited, Stack<String> stack){
        if(!visited.contains(n))
            visited.add(n);
        List<Edge> relations = n.getRelations();
        for(Edge e : relations){
            if(!visited.contains(e.getTo()))
                fillOrder(e.getTo(), visited, stack);
        }
        stack.push(n.getAtomName());
    }


    /**
     * For each SCC founded we update the dependencies network, adding the new SCC to the proper
     * level according to the dependencies of the new SCC's nodes and edges
     * @param scc the new SCC founded
     */
    private void updateNetwork(List<Node> scc){
        List<List<Node>> concurrentScc = new ArrayList<>();
        int orderedNetworkSize = this.orderedNetwork.size();

        if(orderedNetworkSize == 0){
            concurrentScc.add(scc);
            this.orderedNetwork.put(0,concurrentScc);
        }
        else{
            int position = 0;
            for(Integer i : this.orderedNetwork.keySet()){
                List<List<Node>> sccsInStrata = this.orderedNetwork.get(i);
                for(List<Node> singleScc : sccsInStrata){
                    if(before(singleScc, scc)){
                        position = i+1;
                        break;
                    }
                }
            }
            if((orderedNetworkSize - 1) >= position){
                List<List<Node>> updatedStrata = this.orderedNetwork.get(position);
                updatedStrata.add(scc);
                this.orderedNetwork.put(position, updatedStrata);
            }
            else{
                concurrentScc.add(scc);
                this.orderedNetwork.put(position, concurrentScc);
            }

        }
    }

    private boolean before(List<Node> l, List<Node> toCheck){
        for(Node n : l){
            for(Edge e : n.getRelations()){
                if(toCheck.contains(e.getTo())
                		&& ( mode == EMode.STANDARD || mode == EMode.NEGATION_BASED && !e.isPositive() )
                		)
                    return true;
            }
        }
        return false;
    }

    /**
     * This method is just convert the orderedNetwork of SCCs in an orderedNetwork of AtomNames
     * @return The orderedNetowrk of AtomNames
     */
    public Map<Integer, List<List<String>>> getStrata(){
        Map<Integer, List<List<String>>> strata = new HashMap<>();
        for(Integer i : this.orderedNetwork.keySet()){
            List<List<String>> concurrentModelAtoms = new ArrayList<>();
            for(List<Node> scc : this.orderedNetwork.get(i)){
                List<String> strataAtoms = new ArrayList<>();
                for(Node n : scc){
                    strataAtoms.add(n.getAtomName());
                }
                concurrentModelAtoms.add(strataAtoms);
            }
            strata.put(i, concurrentModelAtoms);
        }
        return strata;
    }
    
    public ArrayList<List<List<String>>> getStrataAsList() {
        Map<Integer, List<List<String>>> strata = getStrata();
        int n = strata.size();
        ArrayList<List<List<String>>> strataList = new ArrayList<>(n);
        for (int i=0; i<n; i++) {
        	strataList.add(i, strata.get(i));
        }
        return strataList;
    }

    public String toString() {
        StringBuilder descr = new StringBuilder();
        for(Node n: nodes.values()){
            descr.append(n.getAtomName()).append("\n");
            List<Edge> rel = n.getRelations();
            for(Edge e : rel){
                descr.append("\t").append(e.toString()).append("\n");
            }
        }
        return descr.toString();
    }

}
