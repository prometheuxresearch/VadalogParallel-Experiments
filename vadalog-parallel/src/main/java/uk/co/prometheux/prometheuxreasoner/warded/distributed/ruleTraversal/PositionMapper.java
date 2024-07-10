package uk.co.prometheux.prometheuxreasoner.warded.distributed.ruleTraversal;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.Variable;

class PositionMapper {

	/**
	 * This method creates a list of mapping positions from the body to the head of
	 * a rule for example if we have this rule: a(X,Y,Z) :- a(X,Y,X), b(X,Y,Z) the
	 * list of mapping positions from the body to the head is [[0,2,3],[1,4],[5]]
	 * 
	 * 
	 * 
	 * @author Prometheux Limited
	 * 
	 * @return the list of mapping positions from the body of r to the head of r
	 */
	static List<List<Integer>> mapFromBodyToHead(Rule r) {
		List<Variable> toHeadVariables = r.getSingleHead().getVariableList();
		List<Variable> toBodyVariables = r.getBodyVariablesList();
		List<List<Integer>> mapFromBodyToHeadPositions = new ArrayList<List<Integer>>();
		for (Variable vHead : toHeadVariables) {
			int j = 0;
			List<Integer> vHeadList = new ArrayList<Integer>();
			for (Variable vBody : toBodyVariables) {
				if (vHead.equals(vBody)) {
					vHeadList.add(j);
				}
				j++;
			}
			mapFromBodyToHeadPositions.add(vHeadList);
		}
		return mapFromBodyToHeadPositions;
	}
	
	/**
	 * This method computes the operand atom name and the local position of a variable from the global positions
	 * 
	 * i.e. for the body a(X,Y,Z), b(A,B,C) the global position 5 returns {b,2}
	 * @param operandVarToPosition
	 * @param body
	 * @return
	 */
	public static AbstractMap.SimpleEntry<String,Integer> inferOperandNameAndPositionInAtomFromVarPos(Integer operandVarToPosition, List<Literal> body) {
		Literal lhs = body.get(0);
		int lhsVariablesSize = lhs.getAtom().getVariableList().size();
		// if the lhs contains the operand
		if(lhsVariablesSize > operandVarToPosition) {
			return new AbstractMap.SimpleEntry<>(lhs.getAtom().getName(), operandVarToPosition);
		}
		// the rhs contains the operand
		Literal rhs = body.get(1);
		return new AbstractMap.SimpleEntry<>(rhs.getAtom().getName(), operandVarToPosition - lhsVariablesSize);
	}

	/**
	 * This method return the position in the head of a Variable in the body
	 */
	public static Integer headVarPositionFromBodyVarPosition(Atom singleHead, Variable bodyVarToFind) {
		int headVariablePos = 0;
		for(Variable vHead : singleHead.getVariableList()) {
			if(vHead.equals(bodyVarToFind)) {
				return headVariablePos;
			}
			headVariablePos++;
		}
		return -1;
	}


}
