package uk.co.prometheux.prometheuxreasoner.errorMessage.aggregations.evaluator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.errorMessage.ErrorMessage;
import uk.co.prometheux.prometheuxreasoner.errorMessage.aggregations.ModelMonotonicAggregationErrorMessage;
import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Condition;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.Variable;
import uk.co.prometheux.prometheuxreasoner.model.expressions.AggregateOperator;
import uk.co.prometheux.prometheuxreasoner.model.expressions.Expression;

public class ModelMonotonicAggregationEvaluator implements AggregationEvaluator {
	
	private Expression aggregationExpression;
	private List<Literal> bodyLiterals;
	private Rule rule;
	private AggregateOperator operator;

	public ModelMonotonicAggregationEvaluator(List<Atom> headAtoms, List<Condition> conditions,
			List<Literal> bodyLiterals, Expression aggregationExpression, AggregateOperator operator) {
		this.aggregationExpression = aggregationExpression;
		this.bodyLiterals = bodyLiterals;
		this.rule = new Rule(headAtoms, bodyLiterals, conditions);
		this.operator = operator;
	}

	@Override
	public void evaluate() {
		boolean containsAll = false;
		Set<Variable> aggregateVariables = this.aggregationExpression.getAllVariables();
		Set<Variable> bodyVariables = new HashSet<Variable>();
		for (Literal l : bodyLiterals) {
			bodyVariables.addAll(l.getAtom().getVariablesAsSet());
		}
		for(Condition c : rule.getConditions()) {
			bodyVariables.add(c.getLhs());
		}
		if (bodyVariables.containsAll(aggregateVariables)) {
				containsAll = true;
		}
		if (!containsAll) {
			String inputMessage = "the monotonic aggregate " + operator + " in the rule " + this.rule
					+ " can operate only on variables present in the body, " + "but some of these variables "
					+ aggregateVariables + " are not present in the body";
			ErrorMessage error = new ModelMonotonicAggregationErrorMessage(this.rule);
			error.createErrorMessage(inputMessage);
			throw new AggregationException(error.getResponseMessage());
		}
	}

}
