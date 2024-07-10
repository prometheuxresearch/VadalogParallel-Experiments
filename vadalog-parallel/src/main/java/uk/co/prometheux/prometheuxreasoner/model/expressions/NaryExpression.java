package uk.co.prometheux.prometheuxreasoner.model.expressions;

import java.util.*;

import uk.co.prometheux.prometheuxreasoner.model.Variable;

/**
 * An abstract class providing default implementations for n-ary expressions
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
abstract class NaryExpression extends Expression {

	private static final long serialVersionUID = 1L;
	
    protected final String operationName;
    protected final List<Expression> operandList;
    private Integer hashCode = null;
    private String toString = null;

    /**
     * A constructor initialising the expression using a list of its operands
     * @param operandList is the list of the expression's operands
     */
    NaryExpression(final String operationName, final List<Expression> operandList) {
        this.operationName = operationName;
        this.operandList = new ArrayList<>(operandList);
    }

    /**
     * A getter for the list of operands
     * @return the list of operands
     */
    public List<Expression> getOperandList() {
        return operandList;
    }

    @Override
    public int getArity() {
        return operandList.size();
    }

    @Override
    public String getOperationName() {
        return operationName;
    }

    /**
     * Returns a default string representation for binary expressions
     * @return teh default string representation for binary expressions
     */
    @Override
    public String toString() {
        if (toString == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(operationName).append("(");
            String delim = "";
            for (Expression operand : operandList) {
                sb.append(delim).append(operand.toString());
                delim = ", ";
            }
            sb.append(")");
            toString = sb.toString();
        }
        return toString;
    }

    /**
     * A default implementation for binary expressions that returns the variables of the two operands
     * @return the variables of the two operands
     */
    @Override
    public Set<Variable> getAllVariables() {
        Set<Variable> aVars = new HashSet<>();
        for (Expression operand : operandList)
            aVars.addAll(operand.getAllVariables());
        return aVars;
    }

    /**
     * A default implementation combining the hashcodes of the operation name and the hash codes of the two arguments
     * @return the computed hash code
     */
    @Override
    public int hashCode() {
        if (hashCode == null) {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((operationName == null) ? 0 : operationName.hashCode());
            result = prime * result + ((operandList == null) ? 0 : operandList.hashCode());
            hashCode = result;
        }
        return hashCode;
    }


    /**
     * A default implementation comparing the current expression to the provided object using the type and the three operands
     * @param obj the object to compare to
     * @return if the expression equals the provided object
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NaryExpression that = (NaryExpression) obj;
        return Objects.equals(this.operationName, that.operationName)
                && this.getArity() == that.getArity()
                && Objects.equals(this.operandList, that.operandList);
    }
}
