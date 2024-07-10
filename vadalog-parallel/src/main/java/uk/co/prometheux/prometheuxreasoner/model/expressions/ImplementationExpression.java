package uk.co.prometheux.prometheuxreasoner.model.expressions;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an n-ary expression with an operation
 *
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class ImplementationExpression extends NaryExpression {

    private static final long serialVersionUID = 1L;

    // Currently, the implementation expression encompasses its implementation.
    // Consider fully decoupling syntax (model objects) from their definitions (i.e. implementations).
    protected Implementation implementation;

    public ImplementationExpression(final String operationName, final List<Expression> operandList) {
        super(operationName, operandList);
    }

    @Override
    public Expression clone(boolean renameVariables) {
        List<Expression> newOperands = new ArrayList<>();
        for (Expression operand : getOperandList())
            newOperands.add(operand.clone(renameVariables));
        return new ImplementationExpression(operationName, newOperands);
    }

    @Override
    public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
        return visitor.visit(this, input);
    }

    /**
     * A getter for the implementation of this expression
     * @return the expression's implementation
     */
    public Implementation getImplementation() {
        return implementation;
    }

    /**
     * A setter for the implementation of this expression
     * @param implementation the new implementation
     */
    public void setImplementation(Implementation implementation) {
        this.implementation = implementation;
    }
}
