package uk.co.prometheux.prometheuxreasoner.model.expressions;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.model.Variable;

/**
 * An abstract class providing default implementations for ternary expressions
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
abstract class TernaryExpression extends Expression {

	private static final long serialVersionUID = 1L;
	
    protected final Expression firstOperand;
    protected final Expression secondOperand;
    protected final Expression thirdOperand;

    /**
     * A constructor initialising the expression using its two operands
     * @param firstOperand is the first of the expression's operands
     * @param secondOperand is the second of the expression's operands
     * @param thirdOperand is the third of the expression's operands
     */
    TernaryExpression(Expression firstOperand, Expression secondOperand, Expression thirdOperand) {
        super();
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
        this.thirdOperand = thirdOperand;
    }

    /**
     * Returns the first operand of the expression
     * @return the first operand of the expression
     */
    public Expression getFirstOperand() {
        return firstOperand;
    }

    /**
     * Returns the second operand of the expression
     * @return the second operand of the expression
     */
    public Expression getSecondOperand() {
        return secondOperand;
    }

    /**
     * Returns the third operand of the expression
     * @return the third operand
     */
    public Expression getThirdOperand() {
        return thirdOperand;
    }

    /**
     * Returns the arity of the expression
     * @return the expression's arity
     */
    @Override
    public int getArity() {
        return 3;
    }

    /**
     * Returns a default string representation for ternary expressions
     * @return teh default string representation for ternary expressions
     */
    @Override
    public String toString() {
        return getOperationName() + "(" + Objects.toString(firstOperand) + ", " +  Objects.toString(secondOperand) + "," + Objects.toString(thirdOperand) + ")";
    }

    /**
     * A default implementation for binary expressions that returns the variables of the two operands
     * @return the variables of the two operands
     */
    @Override
    public Set<Variable> getAllVariables() {
        Set<Variable> aVars = new HashSet<>(firstOperand.getAllVariables());
        aVars.addAll(secondOperand.getAllVariables());
        aVars.addAll(thirdOperand.getAllVariables());
        return aVars;
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
        result = prime * result + Objects.hashCode(firstOperand);
        result = prime * result + Objects.hashCode(secondOperand);
        result = prime * result + Objects.hashCode(thirdOperand );
        return result;
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
        if (obj == null || getClass() != obj.getClass())
            return false;
        TernaryExpression that = (TernaryExpression) obj;
        return Objects.equals(this.getOperationName(), that.getOperationName()) &&
                Objects.equals(this.firstOperand, that.firstOperand) &&
                Objects.equals(this.secondOperand, that.secondOperand) &&
                Objects.equals(this.thirdOperand, that.thirdOperand);
    }
    
}
