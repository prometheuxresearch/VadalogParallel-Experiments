package uk.co.prometheux.prometheuxreasoner.model.expressions;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.model.Variable;

/**
 * An abstract class providing default implementations for unary expressions
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public abstract class UnaryExpression extends Expression {

    private static final long serialVersionUID = 1L;
    
	protected final Expression operand;

    /**
     * Initialize the expression with its operand
     * @param operand the expression's operand
     */
    UnaryExpression(Expression operand) {
        this.operand = operand;
    }

    @Override
    public int getArity() {
        return 1;
    }

    /**
     * A getter for the expression's opearand
     * @return the expression's operand
     */
    public Expression getOperand() {
        return operand;
    }

    /**
     * Returns a default string representation for binary expressions
     * @return teh default string representation for binary expressions
     */
    @Override
    public String toString() {
        return getOperationName() + "(" + Objects.toString(operand) + ")";

    }

    /**
     * A default implementation for binary expressions that returns the variables of the two operands
     * @return the variables of the two operands
     */
    @Override
    public Set<Variable> getAllVariables() {
        return new HashSet<>(operand.getAllVariables());
    }

    /**
     * A default implementation combining the hashcodes of the operation name and the hash codes of the two arguments
     * @return the computed hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getOperationName().hashCode();
        result = prime * result + Objects.hashCode(operand);
        return result;
    }

    /**
     * A default implementation comparing the current unary operation and the given object using the operation name and its operand
     * @param obj the object to compare with
     * @return whether the two objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        UnaryExpression that = (UnaryExpression ) obj;
        return Objects.equals(this.getOperationName(), that.getOperationName()) && Objects.equals(this.operand, that.operand);
    }
}
