package uk.co.prometheux.prometheuxreasoner.warded.distributed.ruleTraversal;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.model.Rule;

/**
 * This class represents a connection between two rules R1 and R2 are connected
 * if a literal of the body of R1 unifies with the head of R2
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class RuleEdge {
	private Rule rFrom;
	private Rule rTo;
	private int operandInterested = 0;
	private List<List<Integer>> rFromBodyrToBodyMappings = new ArrayList<List<Integer>>();
	private Set<RuleEdge> visited = new HashSet<RuleEdge>();

	public RuleEdge() {
	}

	public RuleEdge(Rule from, Rule to, int operandIterested) {
		this.rFrom = from;
		this.rTo = to;
		this.operandInterested = operandIterested;
		this.rFromBodyrToBodyMappings = PositionMapper.mapFromBodyToHead(this.rTo);
	}

	public Rule getRFrom() {
		return rFrom;
	}

	public void setRFrom(Rule from) {
		this.rFrom = from;
	}

	public Rule getRTo() {
		return rTo;
	}

	public void setRTo(Rule to) {
		this.rTo = to;
	}

	public int getOperandInterested() {
		return operandInterested;
	}

	public void setOperandInterested(int operandInterested) {
		this.operandInterested = operandInterested;
	}

	public List<List<Integer>> getrFromBodyrToBodyMappings() {
		return rFromBodyrToBodyMappings;
	}

	void addToVisited(RuleEdge re) {
		this.visited.add(re);
	}

	boolean isVisited(RuleEdge re) {
		return this.visited.contains(re);
	}

	public Set<RuleEdge> getVisited() {
		return this.visited;
	}

	void addIntermediateVisited(Set<RuleEdge> visited) {
		this.visited.addAll(visited);
	}

	/**
	 * This method infer the fromBodyToBody mappings in case of a transitive closure
	 * (r1,r2) (r2,r3) we get the mappings from the body of r2 to the body of r3 by
	 * passing through the body of r2 suppose that fromBodyToBody of r1,r2 is
	 * [[0,1],[2],[],[]], and fromBodyToBody of r2,r3 is [[0,1],[2],[3]] the
	 * fromBodyToBody of r1,r3 is [[0,1,2],[3],[],[]]
	 * 
	 * @param prevRFromBodyRToBodyMappings is [[0,1],[2],[],[]]
	 * @param nextRFromBodyRToBodyMappings is [[0,1],[2],[3]]
	 */
	void inferFromBodyToBodyMapping(List<List<Integer>> prevRFromBodyRToBodyMappings,
			List<List<Integer>> nextRFromBodyRToBodyMappings) {
		for (int i = 0; i < prevRFromBodyRToBodyMappings.size(); i++) {
			List<Integer> rFromBodyposIniRToBodyPosIni = new ArrayList<Integer>();
			for (int rFromPosIni : prevRFromBodyRToBodyMappings.get(i)) {
				if (nextRFromBodyRToBodyMappings.size() > rFromPosIni) {
					List<Integer> inferredMappings = nextRFromBodyRToBodyMappings.get(rFromPosIni);
					rFromBodyposIniRToBodyPosIni.addAll(inferredMappings);
				}
			}
			this.rFromBodyrToBodyMappings.add(rFromBodyposIniRToBodyPosIni);
		}
	}
	
	
	/**
	 * This method computes a mapping from the position of the variables in rTo that
	 * are in the body of rFrom
	 * 
	 * @return
	 */
	public Map<String, List<AbstractMap.SimpleEntry<Integer, Integer>>> rFromHeadPositions2rToBodyPositionsOfInterestedOperand() {
		Map<String, List<AbstractMap.SimpleEntry<Integer, Integer>>> singleAtomBodyMapOfInterestedOperand = new HashMap<String, List<AbstractMap.SimpleEntry<Integer, Integer>>>();
		int varPositionInBody = 0;
		for (List<Integer> operandVarToPositions : this.rFromBodyrToBodyMappings) {
			for (Integer operandVarToPosition : operandVarToPositions) {
				AbstractMap.SimpleEntry<String, Integer> operandNameAndPositionInAtomFromVarPos = PositionMapper
						.inferOperandNameAndPositionInAtomFromVarPos(operandVarToPosition, this.rTo.getBody());
				int headVarPositionFromBodyVarPosition = PositionMapper.headVarPositionFromBodyVarPosition(
						this.rFrom.getSingleHead(),
						this.rFrom.getBody().get(operandInterested).getAtom().getVariableList().get(varPositionInBody));
				/*
				 * If we do not have the projection of that body variable to the head, we skip it
				 */
				if(headVarPositionFromBodyVarPosition == -1) {
					continue;
				}
				if (!singleAtomBodyMapOfInterestedOperand.containsKey(operandNameAndPositionInAtomFromVarPos.getKey())) {
					List<AbstractMap.SimpleEntry<Integer, Integer>> headPositionsToBodyPositions = new ArrayList<AbstractMap.SimpleEntry<Integer, Integer>>();
					singleAtomBodyMapOfInterestedOperand.put(operandNameAndPositionInAtomFromVarPos.getKey(),
							headPositionsToBodyPositions);
				}
				AbstractMap.SimpleEntry<Integer, Integer> headPositionToBodyPosition = new AbstractMap.SimpleEntry<Integer, Integer>(headVarPositionFromBodyVarPosition, operandNameAndPositionInAtomFromVarPos.getValue());
				singleAtomBodyMapOfInterestedOperand.get(operandNameAndPositionInAtomFromVarPos.getKey())
						.add(headPositionToBodyPosition);
			}
			varPositionInBody++;

		}
		return singleAtomBodyMapOfInterestedOperand;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rFrom == null) ? 0 : rFrom.hashCode());
		result = prime * result + ((rTo == null) ? 0 : rTo.hashCode());
		result = prime * result + ((visited == null) ? 0 : visited.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RuleEdge other = (RuleEdge) obj;
		if (rFrom == null) {
			if (other.rFrom != null)
				return false;
		} else if (!rFrom.equals(other.rFrom))
			return false;
		if (rTo == null) {
			if (other.rTo != null)
				return false;
		} else if (!rTo.equals(other.rTo))
			return false;
		if (visited == null) {
			if (other.visited != null)
				return false;
		} else if (!visited.equals(other.visited))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RuleEdge [rFrom=" + rFrom + "\n rTo=" + rTo + "\n operandInterested=" + operandInterested
				+ "\n rFromBodyrToBodyMappings=" + rFromBodyrToBodyMappings + "]";
	}

}
