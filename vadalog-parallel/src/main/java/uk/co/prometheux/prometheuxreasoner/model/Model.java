package uk.co.prometheux.prometheuxreasoner.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.prometheux.prometheuxreasoner.PrometheuxRuntimeException;
import uk.co.prometheux.prometheuxreasoner.errorMessage.syntax.handler.SyntaxErrorMessageIdentifier;
import uk.co.prometheux.prometheuxreasoner.model.annotations.AnnotationFactory;
import uk.co.prometheux.prometheuxreasoner.model.annotations.BindAnnotation;
//import uk.co.prometheux.prometheuxreasoner.model.annotations.ChaseAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.annotations.DatalogAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.annotations.MappingAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.annotations.OptimizationAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.annotations.PostProcessingAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.expressions.AggregateOperator;
//import uk.co.prometheux.prometheuxreasoner.model.expressions.AggregationExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.AndExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.ConcatExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.ContainsExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.DivExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.EndsWithExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.EqEqExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.Expression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.ExpressionVisitorDefault;
import uk.co.prometheux.prometheuxreasoner.model.expressions.GeExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.GtExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.ImplementationExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.IndexOfExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.IntersectionExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.LeExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.LtExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.MinusExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.NeqExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.NotExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.OrExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.PlusExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.PrecExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.ProdExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.SkolemExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.StartsWithExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.StringLengthExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.SubstringExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.UnaryMinusExpression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.UnionExpression;
import uk.co.prometheux.prometheuxreasoner.operators.RuleOperatorDecorator;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogBaseListener;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogLexer;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogListener;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParserErrorListener;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.AggregationContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.AndExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.AnnotationBodyContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.AnnotationContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.AnonTermContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.AtomContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.AvgAggExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.BodyContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.ClauseContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.ConcatExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.ContainsExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.CountAggExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.DivExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.DomStarContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.EgdHeadContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.EndsWithExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.EqConditionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.EqEqExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.ExternalExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.FactContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.FalseHeadContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.GeConditionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.GeExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.GtConditionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.GtExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.HeadContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.InConditionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.IndexOfExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.IntersectionExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.LeConditionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.LeExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.ListAggExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.ListContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.LtConditionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.LtExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.MaxAggExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.McountAggExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.MinAggExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.MinusExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.MmaxAggExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.MminAggExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.MprodAggExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.MsumAggExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.MunionAggExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.NegLiteralContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.NeqConditionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.NeqExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.NotExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.NotInConditionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.OrExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.PlusExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.PosLiteralContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.PrecExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.ProdAggExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.ProdExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.ProgramContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.RruleContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.SetAggExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.SkolemExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.StartsWithExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.StringLengthExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.SubstringExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.SumAggExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.UnaryMinusExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.UnionAggExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.UnionExpressionContext;
import uk.co.prometheux.prometheuxreasoner.parser.DatalogParser.VarTermContext;

/**
 * This Class is the Model
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class Model {

	// core members coming from parsing
	private List<Atom> facts = new ArrayList<>();
	private Set<DatalogAnnotation> annotations = new HashSet<>();
	private List<Rule> rules = new ArrayList<>();

	// computable/adorning members
	private Set<PredicateSignature> signaturesOfPredicates = new HashSet<>();
	private List<String> comments = new ArrayList<>();

	private List<String> dataspaceNames = new ArrayList<>();

	private int maxArity = 0;

	private Logger log = LoggerFactory.getLogger(Model.class);

	// determines whether the wardedness check should be relaxed to ignore variables
	// computed by non-skolem expressions
	private boolean usingRelaxedWardedness = false;

	/**
	 * Clone constructor.
	 * 
	 * @param aModel the model to clone
	 */
	public Model(Model aModel) {

		/* we clone all the rules */
		this.rules = aModel.getRules().stream().map(Rule::new).collect(Collectors.toList());

		/* we clone all the facts */
		this.facts = aModel.getFacts().stream().map(Atom::new).collect(Collectors.toList());

		this.dataspaceNames.addAll(aModel.dataspaceNames);

		/* we clone all the predicates */
		this.signaturesOfPredicates = aModel.signaturesOfPredicates.stream().map(PredicateSignature::new)
				.collect(Collectors.toSet());

		this.maxArity = aModel.maxArity;

		/* annotations are shared, no major problems. */
		this.annotations = aModel.annotations;

		this.usingRelaxedWardedness = aModel.isUsingRelaxedWardedness();

		this.comments = new ArrayList<>(aModel.comments);

		this.log = aModel.log;
	}

	public Model() {

	}
	
	public List<String> getRulesString() {
		List<String> rulesString = new ArrayList<>();
		for(Rule r : this.rules) {
			rulesString.add(r.toString());
		}
		return rulesString;
	}

	public void readProgram(String s) {
		try {
			readProgramFromStream(new ByteArrayInputStream(s.getBytes()));
		} catch (IOException e) {
			throw new PrometheuxRuntimeException(e.getMessage());
		}
	}

	public void readProgram(StringBuffer sb) throws IOException {
		readProgramFromStream(new ByteArrayInputStream(sb.toString().getBytes()));
	}

	/**
	 * It reads a Datalog program from a generic InputStream
	 * 
	 * @param is an input stream, to read the program from
	 * @throws IOException throws if the reading goes wrong.
	 */
	public void readProgramFromStream(InputStream is) throws IOException {
        // Create a ByteArrayOutputStream to store the copied data
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Create a buffer to read data from the input stream
        byte[] buffer = new byte[1024];
        int bytesRead;

        // Read from the input stream and write to the ByteArrayOutputStream
        while ((bytesRead = is.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        // Create a ByteArrayInputStream from the copied data
        InputStream inputStreamClone = new ByteArrayInputStream(baos.toByteArray());

		SyntaxErrorMessageIdentifier.resetVadalogProgram();
		SyntaxErrorMessageIdentifier.setVadalogProgram(inputStreamClone);
		
		is = new ByteArrayInputStream(baos.toByteArray());
		
		CharStream input = CharStreams.fromStream(is);
		DatalogLexer lexer = new DatalogLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		DatalogParser parser = new DatalogParser(tokens);
		parser.addErrorListener(new DatalogParserErrorListener());
		ParseTree tree = parser.program();
		ParseTreeWalker walker = new ParseTreeWalker();

		ConfListener c = new ConfListener();
		walker.walk(c, tree);
	}

	public void readRule(String vadalogString) {
		CharStream input = CharStreams.fromString(vadalogString);
		DatalogLexer lexer = new DatalogLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		DatalogParser parser = new DatalogParser(tokens);
		parser.addErrorListener(new DatalogParserErrorListener());
		ParseTree tree = parser.program();
		ParseTreeWalker walker = new ParseTreeWalker();

		ConfListener c = new ConfListener();
		walker.walk(c, tree);
	}

	public Set<PredicateSignature> getSignaturesOfPredicates() {
		return signaturesOfPredicates;
	}

	public List<String> getComments() {
		return this.comments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}

	public List<Atom> getFacts() {
		return facts;
	}

	/**
	 * It returns all the facts grouped by predicate.
	 * 
	 * @return The facts grouped by predicate.
	 */
	public Map<String, List<Atom>> getFactsByPredicate() {
		Map<String, List<Atom>> factsByPred = new HashMap<>();
		for (Atom a : this.facts) {
			facts = factsByPred.get(a.getName());
			if (facts == null)
				facts = new ArrayList<>();
			facts.add(a);
			factsByPred.put(a.getName(), facts);
		}
		return factsByPred;
	}

	public Set<DatalogAnnotation> getAnnotations() {
		return this.annotations;
	}

	public int getMaximumArity() {
		return maxArity;
	}

	public List<Rule> getRules() {
		return rules;
	}

	/**
	 * It returns the TGDs of the model, i.e.the rules that don't have an equality
	 * atom in the head. e.g. a(X,Z) :- p(X,Y).
	 * 
	 * 
	 * @return the TGDs of the model
	 */
	public List<Rule> getTDGs() {
		return this.rules.stream().filter(rule -> rule.isTGD()).collect(Collectors.toList());
	}

	/**
	 * It returns the EGDs of the model, i.e. the rules that have an equality atom
	 * in the head. e.g. X=Y :- p(X,Y).
	 * 
	 * @return the EGDs of the model
	 */
	public List<Rule> getEGDs() {
		return this.rules.stream().filter(rule -> rule.isEGD()).collect(Collectors.toList());
	}

	/**
	 * It performs lexicographic sort of the rules in the model.
	 */
	public void sortRules() {
		this.rules.sort((o1, o2) -> (o1.toString().compareTo(o2.toString())));
		this.rules.forEach(Rule::sortBody);
	}

	/**
	 * It performs lexicographic sort of the facts in the model.
	 */
	public void sortFacts() {
		this.facts.sort(Comparator.comparing(Atom::toString));
	}

	/**
	 * It deduplicates the rules in the model.
	 */
	public void deduplicateRules() {

		/* deduplicate the same rules */
		List<Rule> rList = new ArrayList<>(this.rules);

		/* if there is a homomorphism from r1 to r2, just keep r1 */
		for (Rule r1 : this.rules) {
			RuleOperatorDecorator rod1 = new RuleOperatorDecorator(r1);
			for (Rule r2 : this.rules) {
				RuleOperatorDecorator rod2 = new RuleOperatorDecorator(r2);

				if (rList.contains(r1) && rList.contains(r2)) {
					if (!r1.equals(r2) && rod1.isHomomorphic().to(r2) && rod2.isHomomorphic().to(r1)) {
						if (log.isDebugEnabled())
							log.debug("Rule " + r1 + " isomorphic to " + r2);
						// comparison just to introduce some determinism
						if (r1.toString().compareTo(r2.toString()) < 0) {
							rList.remove(r2);
							if (log.isDebugEnabled())
								log.debug("Dropping " + r2);
						} else {
							rList.remove(r1);
							if (log.isDebugEnabled())
								log.debug("Dropping " + r1);
						}
					}
				}
			}
		}

		Set<Rule> rSet = new HashSet<>(rList);

		this.rules.clear();
		this.rules.addAll(rSet);

	}

	public Set<Constant<?>> getAllConstants() {
		Set<Constant<?>> allConstants = new HashSet<>();
		this.rules.forEach(x -> allConstants.addAll(x.getAllConstants()));
		return allConstants;
	}

	/**
	 * It returns the bind annotations for a given predicateName.
	 * 
	 * @param predicateName the predicate name to look for
	 * @return the BindAnnotations
	 */
	public List<BindAnnotation> getBindAnnotationsByPredicateName(String predicateName) {
		List<BindAnnotation> bas = new ArrayList<>();
		for (DatalogAnnotation da : this.annotations) {
			if (da instanceof BindAnnotation && ((BindAnnotation) da).getPredicateName().equals(predicateName)) {
				bas.add((BindAnnotation) da);
			}
		}
		return bas;
	}

	/**
	 * It returns the post annotation for a given predicate name. An empty list is
	 * returned is none is present.
	 * 
	 * @param predicateName the predicate name to look for
	 * @return the PostProcessing annotations
	 */
	public List<PostProcessingAnnotation> getPostProcessingAnnotationByPredicateName(String predicateName) {
		List<PostProcessingAnnotation> resList = new ArrayList<>();
		for (DatalogAnnotation da : this.annotations)
			if (da instanceof PostProcessingAnnotation
					&& ((PostProcessingAnnotation) da).getPredicateName().equals(predicateName))
				resList.add((PostProcessingAnnotation) da);
		return resList;
	}
	
	/**
	 * It returns the chase annotation for a given predicate name. An empty list is
	 * returned is none is present.
	 * 
	 * @param predicateName the predicate name to look for
	 */
//	public List<ChaseAnnotation> getChaseAnnotationByPredicateName(String predicateName) {
//		List<ChaseAnnotation> resList = new ArrayList<>();
//		for (DatalogAnnotation da : this.annotations) {
//			if (da instanceof ChaseAnnotation
//					&& ((ChaseAnnotation) da).getPredicateName().equals(predicateName))
//				resList.add((ChaseAnnotation) da);
//		}
//		return resList;
//	}
	
	/**
	 * It returns the chase annotations. An empty list is
	 * returned is none is present.
	 */
//	public List<ChaseAnnotation> getChaseAnnotations() {
//		List<ChaseAnnotation> resList = new ArrayList<>();
//		for (DatalogAnnotation da : this.annotations) {
//			if (da instanceof ChaseAnnotation)
//				resList.add((ChaseAnnotation) da);
//		}
//		return resList;
//	}

	/**
	 * It returns all the mapping annotations associated to a predicate. It returns
	 * null if not present
	 * 
	 * @param predicateName the predicate name to look for
	 * @return the MappingAnnotation
	 */
	public List<MappingAnnotation> getMappingAnnotationsByPredicateName(String predicateName) {
		List<MappingAnnotation> ma = new ArrayList<>();
		for (DatalogAnnotation da : this.annotations)
			if (da instanceof MappingAnnotation && ((MappingAnnotation) da).getPredicateName().equals(predicateName))
				ma.add((MappingAnnotation) da);
		return ma;
	}

	public String getOptimizationStrategy() {
		for (DatalogAnnotation da : this.annotations) {
			if (da instanceof OptimizationAnnotation) {
				return ((OptimizationAnnotation) da).getOptimizationStrategy();
			}
		}
		return "unset";
	}

	/**
	 * It returns all the variables of all the rules
	 * 
	 * @return all the variables of all the rules
	 */
	public Set<Variable> getAllVariables() {
		Set<Variable> allVariables = new HashSet<>();
		for (Rule r : this.rules) {
			allVariables.addAll(r.getBodyVariables());
			allVariables.addAll(r.getSingleHead().getVariablesAsSet());
		}
		return allVariables;
	}

	public boolean isUsingRelaxedWardedness() {
		return usingRelaxedWardedness;
	}

	public void removeRule(Rule r) {
		this.rules.remove(r);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for (String s : comments)
			sb.append(s).append("\n");
		for (DatalogAnnotation a : annotations)
			sb.append(a.toString()).append(".\n");
		for (Atom f : facts)
			sb.append(f.toString()).append(".\n");
		for (Rule r : this.rules)
			sb.append(r.toString()).append("\n");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((facts == null) ? 0 : facts.hashCode());
		result = prime * result + ((rules == null) ? 0 : rules.hashCode());
		// FIXME: also include annotations
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Model other = (Model) obj;
		// FIXME: also compare on annotations
		// FIXME: compare as sets, not as lists
		return Objects.equals(this.facts, other.facts) && Objects.equals(this.rules, other.rules);
	}

	/**
	 * The accept method used in the Visitor pattern
	 * 
	 * @param visitor the visitor
	 */
	public void accept(Visitor visitor) {
		visitor.visitRules(rules);
		rules.forEach(visitor::visitRule);
		visitor.visitFacts(this.facts);
		facts.forEach(visitor::visitFact);
		visitor.visitAnnotations(annotations);
		annotations.forEach(visitor::visitAnnotation);
	}

	/**
	 * An interface used in the Visitor patter for the Model class
	 */
	private interface Visitor {

		default void visitRules(List<Rule> rules) {
		}

		default void visitRule(Rule rule) {
		}

		default void visitFacts(List<Atom> atoms) {
		}

		default void visitFact(Atom atom) {
		}

		default void visitAnnotations(Set<DatalogAnnotation> annotations) {
		}

		default void visitAnnotation(DatalogAnnotation annotation) {
		}

	}

	private class ConfListener extends DatalogBaseListener implements DatalogListener {

		private String currentPredicate;
		private boolean inBody = false;
		private List<Atom> headAtoms;
		private List<Literal> bodyLiterals;
		private boolean inPositiveLiteral = false;
		private boolean inAnnotation = false;
		private boolean inRuleAnnotation = false;
		private boolean isDomStar = false;
		private Atom annotationAtom;
		private Atom inRuleAnnotationAtom;
		private List<DatalogAnnotation> inRuleAnnotations;
		private List<Term> egdHeadVariable;
		private Term falseHead;
		private int egdCount = 1;
		private Expression egdEqEqExpression;
		private List<Condition> conditions;
		private Deque<Expression> currentOperands = new ArrayDeque<>();
		private Deque<Integer> operandsCountAtExpressionsStart = new ArrayDeque<>();
		private StringBuffer anonTermStringBuffer = new StringBuffer();

		@Override
		public void exitClause(ClauseContext ctx) {
			inBody = false;
			inAnnotation = false;
		}

		@Override
		public void exitProgram(ProgramContext ctx) {
			// we do also sorting in PrometheuxReasonerController#evaluateProgram()
			for (Rule r : Model.this.getRules()) {
//				r.getLiterals().sort(new ParametricQbindComparator(Model.this));
			}

			super.exitProgram(ctx);
		}

		@Override
		public void enterRrule(RruleContext ctx) {
			this.egdHeadVariable = new ArrayList<>();
			this.inRuleAnnotations = new ArrayList<>();
		}

		@Override
		public void exitRrule(RruleContext ctx) {
			if (!this.egdHeadVariable.isEmpty()) {
				Atom equalityAtom = new Atom("egd" + this.egdCount, this.egdHeadVariable);
				equalityAtom.setEqualityAtom(true);
				headAtoms.add(equalityAtom);
				PredicateSignature predicate = new PredicateSignature("egd" + this.egdCount,
						this.egdHeadVariable.size());
				signaturesOfPredicates.add(predicate);
				this.egdCount++;
			}
			if (this.falseHead != null) {
				Atom falseAtom = new Atom("falseHead", new ArrayList<>());
				falseAtom.setFalseAtom(true);
				headAtoms.add(falseAtom);
				PredicateSignature predicate = new PredicateSignature("falseHead", 0);
				signaturesOfPredicates.add(predicate);
			}
			Rule r = new Rule(headAtoms, bodyLiterals, conditions);
			r.setAnnotations(this.inRuleAnnotations);
			r.setDomStar(isDomStar);

			fixGroupByVariablesAndHeadPredicateAndPosition(r);
			sortLiterals();
			rules.add(r);
			isDomStar = false;
		}

		/**
		 * We sort the literals of this rule so that the negated ones are at the end.
		 */
		private void sortLiterals() {
			this.bodyLiterals.sort(new BodyLiteralsNegComparator());
		}

		/**
		 * This method fixes the group by variables as well as head predicate and
		 * position for all the aggregation expressions of the current rule r.
		 * 
		 * @param r the rule for which we have to fix the group by variables
		 */
		private void fixGroupByVariablesAndHeadPredicateAndPosition(Rule r) {

			/* we fix the grouping variables for this rule */
			List<Variable> groupingVariables = r.getHeadVariablesAsSet().stream()
					.filter(x -> !r.getExistentiallyQuantifiedVariables().contains(x)).collect(Collectors.toList());

//			AggregateExpressionsCollector aggCollector = new AggregateExpressionsCollector();
			String headname = r.getHead().get(0).getName();
			for (Condition c : r.getConditions()) {
//				List<AggregationExpression> aggexpressions = new ArrayList<>();
//				int headPosition = r.getHead().get(0).getArguments().indexOf(c.getLhs());
//				// we collect all aggregate expressions inside of this condition
//				c.getRhs().accept(aggCollector, aggexpressions);
//				aggexpressions.forEach(x -> {
//					// fix group by variables
//					x.setGroupByVariables(new ArrayList<>(groupingVariables));
//					// fix head predicate and position
//					x.setHeadPredicateAndPosition(headname, headPosition);
//				});
			}
		}

		@Override
		public void exitFact(FactContext ctx) {
			facts.add(headAtoms.get(0));
			this.headAtoms.get(0).setAnnotations(this.inRuleAnnotations);
		}

		@Override
		public void exitAnnotation(AnnotationContext ctx) {
			// add a new annotation to the model
			annotations.add(AnnotationFactory.getInstance().getAnnotation(annotationAtom));
			switch (annotationAtom.getName()) {
			case "relaxedSafety":
				usingRelaxedWardedness = true;
				break;
			}
			annotationAtom = null;
		}

		public void exitAnnotationBody(AnnotationBodyContext ctx) {
			/* we are out */
			if (this.inRuleAnnotation) {
				this.inRuleAnnotation = false;
				this.inRuleAnnotations.add(AnnotationFactory.getInstance().getAnnotation(this.inRuleAnnotationAtom));
			}
		}

		@Override
		public void enterBody(BodyContext ctx) {
			inBody = true;
			bodyLiterals = new LinkedList<>();
			conditions = new ArrayList<>();
		}

		@Override
		public void enterHead(HeadContext ctx) {
			headAtoms = new LinkedList<>();
			inPositiveLiteral = true; // BN: head literals are always positive
		}

		@Override
		public void enterEgdHead(EgdHeadContext ctx) {
			headAtoms = new LinkedList<>();
			inPositiveLiteral = true;
			this.egdHeadVariable.add(new Variable(ctx.getChild(0).getText()));
			this.egdHeadVariable.add(new Variable(ctx.getChild(2).getText()));
			this.egdEqEqExpression = new EqEqExpression(this.egdHeadVariable.get(0), this.egdHeadVariable.get(1));
			this.currentOperands.add(this.egdEqEqExpression);
		}

		@Override
		public void enterFalseHead(FalseHeadContext ctx) {
			this.falseHead = new Constant<Boolean>(false);
		}

		@Override
		public void enterFact(FactContext ctx) {
			this.inRuleAnnotations = new ArrayList<>();
			this.headAtoms = new LinkedList<>();
		}

		@Override
		public void enterAnnotationBody(AnnotationBodyContext ctx) {
			/* if we enter here for rule and fact annotations */
			/* we must take note of this context. */
			if (!inAnnotation) {
				this.inRuleAnnotation = true;
			}
		}

		@Override
		public void enterAnnotation(AnnotationContext ctx) {
			inAnnotation = true;
		}

		@Override
		public void enterPosLiteral(PosLiteralContext ctx) {
			inPositiveLiteral = true;
		}

		@Override
		public void enterNegLiteral(NegLiteralContext ctx) {
			inPositiveLiteral = false;
		}

		@Override
		public void exitUnaryMinusExpression(UnaryMinusExpressionContext ctx) {
			Expression e = this.currentOperands.pop();
			this.currentOperands.push(new UnaryMinusExpression(e));
		}

		@Override
		public void exitNotExpression(NotExpressionContext ctx) {
			Expression e = this.currentOperands.pop();
			this.currentOperands.push(new NotExpression(e));
		}

		@Override
		public void exitPlusExpression(PlusExpressionContext ctx) {
			Expression e1 = this.currentOperands.pop();
			Expression e2 = this.currentOperands.pop();
			this.currentOperands.push(new PlusExpression(e2, e1));
		}

		@Override
		public void exitAndExpression(AndExpressionContext ctx) {
			Expression e1 = this.currentOperands.pop();
			Expression e2 = this.currentOperands.pop();
			this.currentOperands.push(new AndExpression(e2, e1));
		}

		private List<Expression> getOperands() {
			int operandsCountAtExpressionStart = operandsCountAtExpressionsStart.pop();
			List<Expression> operands = new ArrayList<>();
			while (this.currentOperands.size() > operandsCountAtExpressionStart)
				operands.add(currentOperands.pop());
			Collections.reverse(operands);
			return operands;
		}

		@Override
		public void exitSkolemExpression(SkolemExpressionContext ctx) {
			List<Expression> operands = getOperands();
			this.currentOperands.push(new SkolemExpression(ctx.children.get(1).getText(), operands));
		}

		@Override
		public void exitExternalExpression(ExternalExpressionContext ctx) {
			List<Expression> operands = getOperands();
			this.currentOperands.push(new ImplementationExpression(ctx.children.get(0).getText(), operands));
		}

		@Override
		public void exitLeExpression(LeExpressionContext ctx) {
			Expression e1 = this.currentOperands.pop();
			Expression e2 = this.currentOperands.pop();
			this.currentOperands.push(new LeExpression(e2, e1));
		}

		@Override
		public void exitGtExpression(GtExpressionContext ctx) {
			Expression e1 = this.currentOperands.pop();
			Expression e2 = this.currentOperands.pop();
			this.currentOperands.push(new GtExpression(e2, e1));
		}

		@Override
		public void exitGeExpression(GeExpressionContext ctx) {
			Expression e1 = this.currentOperands.pop();
			Expression e2 = this.currentOperands.pop();
			this.currentOperands.push(new GeExpression(e2, e1));
		}

		@Override
		public void exitNeqExpression(NeqExpressionContext ctx) {
			Expression e1 = this.currentOperands.pop();
			Expression e2 = this.currentOperands.pop();
			this.currentOperands.push(new NeqExpression(e2, e1));
		}

		@Override
		public void exitLtExpression(LtExpressionContext ctx) {
			Expression e1 = this.currentOperands.pop();
			Expression e2 = this.currentOperands.pop();
			this.currentOperands.push(new LtExpression(e2, e1));
		}

		@Override
		public void exitEqEqExpression(EqEqExpressionContext ctx) {
			Expression e1 = this.currentOperands.pop();
			Expression e2 = this.currentOperands.pop();
			this.currentOperands.push(new EqEqExpression(e2, e1));
		}

		@Override
		public void exitOrExpression(OrExpressionContext ctx) {
			Expression e1 = this.currentOperands.pop();
			Expression e2 = this.currentOperands.pop();
			this.currentOperands.push(new OrExpression(e2, e1));
		}

		@Override
		public void exitUnionExpression(UnionExpressionContext ctx) {
			Expression e1 = this.currentOperands.pop();
			Expression e2 = this.currentOperands.pop();
			this.currentOperands.push(new UnionExpression(e1, e2));
		}

		@Override
		public void exitIntersectionExpression(IntersectionExpressionContext ctx) {
			Expression e1 = this.currentOperands.pop();
			Expression e2 = this.currentOperands.pop();
			this.currentOperands.push(new IntersectionExpression(e1, e2));
		}

		@Override
		public void exitMinusExpression(MinusExpressionContext ctx) {
			Expression e1 = this.currentOperands.pop();
			Expression e2 = this.currentOperands.pop();
			this.currentOperands.push(new MinusExpression(e2, e1));
		}

		@Override
		public void exitProdExpression(ProdExpressionContext ctx) {
			Expression e1 = this.currentOperands.pop();
			Expression e2 = this.currentOperands.pop();
			this.currentOperands.push(new ProdExpression(e2, e1));
		}

		@Override
		public void exitDivExpression(DivExpressionContext ctx) {
			Expression e1 = this.currentOperands.pop();
			Expression e2 = this.currentOperands.pop();
			this.currentOperands.push(new DivExpression(e2, e1));
		}

		@Override
		public void exitPrecExpression(PrecExpressionContext ctx) {
			Expression e1 = this.currentOperands.pop();
			this.currentOperands.push(new PrecExpression(e1));
		}

		/* aggregate expressions */

		private void handleAggregate(AggregateOperator operator, AggregationContext ctx) {
//			Expression e1 = this.currentOperands.pop();
//			List<Variable> varList = getVarList(ctx);
//			List<Variable> groupVars = getGroupingVariables(e1);
//			AggregationExpression e = new AggregationExpression(operator, e1, varList, groupVars);
//			this.currentOperands.push(e);
		}

		@Override
		public void exitMcountAggExpression(McountAggExpressionContext ctx) {
			handleAggregate(AggregateOperator.MCOUNT, ctx);
		}

		@Override
		public void exitMsumAggExpression(MsumAggExpressionContext ctx) {
			handleAggregate(AggregateOperator.MSUM, ctx);
		}

		@Override
		public void exitMunionAggExpression(MunionAggExpressionContext ctx) {
			handleAggregate(AggregateOperator.MUNION, ctx);
		}

		@Override
		public void exitMprodAggExpression(MprodAggExpressionContext ctx) {
			handleAggregate(AggregateOperator.MPROD, ctx);
		}

		@Override
		public void exitMmaxAggExpression(MmaxAggExpressionContext ctx) {
			handleAggregate(AggregateOperator.MMAX, ctx);
		}

		@Override
		public void exitMminAggExpression(MminAggExpressionContext ctx) {
			handleAggregate(AggregateOperator.MMIN, ctx);
		}

		/* non-monotonic aggregates */

		@Override
		public void exitUnionAggExpression(UnionAggExpressionContext ctx) {
			handleAggregate(AggregateOperator.UNION, ctx);
		}

		@Override
		public void exitListAggExpression(ListAggExpressionContext ctx) {
			handleAggregate(AggregateOperator.LIST, ctx);
		}

		@Override
		public void exitSetAggExpression(SetAggExpressionContext ctx) {
			handleAggregate(AggregateOperator.SET, ctx);
		}

		@Override
		public void exitMinAggExpression(MinAggExpressionContext ctx) {
			handleAggregate(AggregateOperator.MIN, ctx);
		}

		@Override
		public void exitMaxAggExpression(MaxAggExpressionContext ctx) {
			handleAggregate(AggregateOperator.MAX, ctx);
		}

		@Override
		public void exitSumAggExpression(SumAggExpressionContext ctx) {
			handleAggregate(AggregateOperator.SUM, ctx);
		}

		@Override
		public void exitProdAggExpression(ProdAggExpressionContext ctx) {
			handleAggregate(AggregateOperator.PROD, ctx);
		}

		@Override
		public void exitAvgAggExpression(AvgAggExpressionContext ctx) {
			handleAggregate(AggregateOperator.AVG, ctx);
		}

		@Override
		public void exitCountAggExpression(CountAggExpressionContext ctx) {
			handleAggregate(AggregateOperator.COUNT, ctx);
		}

		/* string operators */

		@Override
		public void exitSubstringExpression(SubstringExpressionContext ctx) {
			Expression op3 = this.currentOperands.pop();
			Expression op2 = this.currentOperands.pop();
			Expression op1 = this.currentOperands.pop();
			this.currentOperands.push(new SubstringExpression(op1, op2, op3));
		}

		@Override
		public void exitContainsExpression(ContainsExpressionContext ctx) {
			Expression op2 = this.currentOperands.pop();
			Expression op1 = this.currentOperands.pop();
			this.currentOperands.push(new ContainsExpression(op1, op2));
		}

		@Override
		public void exitStartsWithExpression(StartsWithExpressionContext ctx) {
			Expression op2 = this.currentOperands.pop();
			Expression op1 = this.currentOperands.pop();
			this.currentOperands.push(new StartsWithExpression(op1, op2));
		}

		@Override
		public void exitEndsWithExpression(EndsWithExpressionContext ctx) {
			Expression op2 = this.currentOperands.pop();
			Expression op1 = this.currentOperands.pop();
			this.currentOperands.push(new EndsWithExpression(op1, op2));
		}

		@Override
		public void exitConcatExpression(ConcatExpressionContext ctx) {
			Expression op2 = this.currentOperands.pop();
			Expression op1 = this.currentOperands.pop();
			this.currentOperands.push(new ConcatExpression(op1, op2));
		}

		@Override
		public void exitIndexOfExpression(IndexOfExpressionContext ctx) {
			Expression op2 = this.currentOperands.pop();
			Expression op1 = this.currentOperands.pop();
			this.currentOperands.push(new IndexOfExpression(op1, op2));
		}

		@Override
		public void exitStringLengthExpression(StringLengthExpressionContext ctx) {
			Expression op = this.currentOperands.pop();
			this.currentOperands.push(new StringLengthExpression(op));
		}

		/**
		 * It retrieves all the grouping variables for an aggregation. They are the
		 * variables that appear in the head of the rule, (which is processed before the
		 * body), but do not appear in the LHS of any expression. If a variable appears
		 * in the LHS of some expression, this means that in the head it is
		 * existentially quantified.
		 * 
		 * @param e1 the expression
		 * @return the list of the grouping variables
		 */
		private List<Variable> getGroupingVariables(Expression e1) {
			/* now we must find the grouping variables */

			/* the variables in the LHS of the expression */
			Set<Variable> exprVars = e1.getAllVariables();

			/* we get the head atom and find all */
			/* the grouping variables */
			List<Variable> headVars = this.headAtoms.get(0).getVariablesAsSet();

			return headVars.stream().filter(x -> (!exprVars.contains(x))).collect(Collectors.toList());
		}

		/**
		 * It retrieves the varList for aggregation expressions
		 * 
		 * @param ctx the context of the aggregation
		 * @return the varList
		 */
		private List<Variable> getVarList(AggregationContext ctx) {
			/* if there is the varList we consider them */
			List<Variable> varList = null;
			if (ctx.getChild(3) != null) {
				varList = new ArrayList<>();
				for (int i = 1; i < ctx.getChild(3).getChildCount() - 1; i++) {
					if (!ctx.getChild(3).getChild(i).getText().equals(",")) {
						Variable var = new Variable(ctx.getChild(3).getChild(i).getText());
						varList.add(var);
					}
				}
			}
			return varList;
		}

		public void exitEqCondition(EqConditionContext ctx) {
			/* we extract the lhs of the condition */
			Variable lhs = new Variable(ctx.getChild(0).getText());
			/* we extract the expression */
			Expression e = this.currentOperands.pop();

			/* and add the condition */
			conditions.add(new Condition(lhs, ComparisonOperatorsEnum.EQ, e));
		}

		public void exitNeqCondition(NeqConditionContext ctx) {
			/* we extract the lhs of the condition */
			Variable lhs = new Variable(ctx.getChild(0).getText());
			/* we extract the expression */
			Expression e = this.currentOperands.pop();
			/* and add the condition */
			conditions.add(new Condition(lhs, ComparisonOperatorsEnum.NEQ, e));
		}

		public void exitLtCondition(LtConditionContext ctx) {
			/* we extract the lhs of the condition */
			Variable lhs = new Variable(ctx.getChild(0).getText());
			/* we extract the expression */
			Expression e = this.currentOperands.pop();
			/* and add the condition */
			conditions.add(new Condition(lhs, ComparisonOperatorsEnum.LT, e));
		}

		public void exitLeCondition(LeConditionContext ctx) {
			/* we extract the lhs of the condition */
			Variable lhs = new Variable(ctx.getChild(0).getText());
			/* we extract the expression */
			Expression e = this.currentOperands.pop();
			/* and add the condition */
			conditions.add(new Condition(lhs, ComparisonOperatorsEnum.LE, e));
		}

		@Override
		public void exitInCondition(InConditionContext ctx) {
			/* we extract the lhs of the condition */
			Variable lhs = new Variable(ctx.getChild(0).getText());
			/* we extract the expression */
			Expression e = this.currentOperands.pop();
			/* and add the condition */
			conditions.add(new Condition(lhs, ComparisonOperatorsEnum.IN, e));
		}

		@Override
		public void exitNotInCondition(NotInConditionContext ctx) {
			/* we extract the lhs of the condition */
			Variable lhs = new Variable(ctx.getChild(0).getText());
			/* we extract the expression */
			Expression e = this.currentOperands.pop();
			/* and add the condition */
			conditions.add(new Condition(lhs, ComparisonOperatorsEnum.NOTIN, e));
		}

		public void exitGtCondition(GtConditionContext ctx) {
			/* we extract the lhs of the condition */
			Variable lhs = new Variable(ctx.getChild(0).getText());
			/* we extract the expression */
			Expression e = this.currentOperands.pop();
			/* and add the condition */
			conditions.add(new Condition(lhs, ComparisonOperatorsEnum.GT, e));
		}

		public void exitGeCondition(GeConditionContext ctx) {
			/* we extract the lhs of the condition */
			Variable lhs = new Variable(ctx.getChild(0).getText());
			/* we extract the expression */
			Expression e = this.currentOperands.pop();
			/* and add the condition */
			conditions.add(new Condition(lhs, ComparisonOperatorsEnum.GE, e));
		}

		@Override
		public void enterAtom(AtomContext ctx) {
			currentPredicate = ctx.ID().getText();
			currentOperands = new ArrayDeque<>();
		}

		private List<Term> getOperandsAsTerms() {
			List<Term> terms = new ArrayList<>();
			while (currentOperands.size() > 0)
				terms.add((Term) currentOperands.pop());
			Collections.reverse(terms);
			return terms;
		}

		@Override
		public void exitAtom(AtomContext ctx) {
			PredicateSignature predicate = new PredicateSignature(currentPredicate, currentOperands.size());
			signaturesOfPredicates.add(predicate);
			if (predicate.arity > maxArity) {
				maxArity = predicate.arity;
			}
			if (inBody) {
				Literal literal = new Literal(currentPredicate, getOperandsAsTerms(), inPositiveLiteral);
				bodyLiterals.add(literal);
			}
			if (!inBody && !inAnnotation && !inRuleAnnotation) {
				headAtoms.add(new Atom(currentPredicate, getOperandsAsTerms()));
			}
			if (inAnnotation && !inRuleAnnotation) {
				annotationAtom = new Atom(currentPredicate, getOperandsAsTerms());
			}
			if (inRuleAnnotation) {
				this.inRuleAnnotationAtom = new Atom(currentPredicate, getOperandsAsTerms());
			}
		}

		@Override
		public void exitDomStar(DomStarContext ctx) {
			isDomStar = true;
		}

		@Override
		public void enterEmptySet(DatalogParser.EmptySetContext ctx) {
			Set<String> termSet = new HashSet<>();
			currentOperands.push(new Constant<Set<?>>(termSet));
		}

		@Override
		public void enterStringSet(DatalogParser.StringSetContext ctx) {
			Set<String> termSet = new HashSet<>();
			for (int i = 0; i < ctx.getChildCount(); i++)
				if (i % 2 == 1)
					termSet.add(ctx.getChild(i).getText());
			currentOperands.push(new Constant<>(termSet));
		}

		@Override
		public void enterIntegerSet(DatalogParser.IntegerSetContext ctx) {
			Set<Integer> termSet = new HashSet<>();
			for (int i = 0; i < ctx.getChildCount(); i++)
				if (i % 2 == 1)
					termSet.add(Integer.parseInt(ctx.getChild(i).getText()));
			currentOperands.push(new Constant<>(termSet));
		}

		@Override
		public void enterDoubleSet(DatalogParser.DoubleSetContext ctx) {
			Set<Double> termSet = new HashSet<>();
			for (int i = 0; i < ctx.getChildCount(); i++)
				if (i % 2 == 1)
					termSet.add(Double.parseDouble(ctx.getChild(i).getText()));
			currentOperands.push(new Constant<>(termSet));
		}

		@Override
		public void enterExternalExpression(ExternalExpressionContext ctx) {
			this.operandsCountAtExpressionsStart.push(currentOperands.size());
		}

		@Override
		public void enterSkolemExpression(SkolemExpressionContext ctx) {
			this.operandsCountAtExpressionsStart.push(currentOperands.size());
		}

		@Override
		public void enterStringConstTerm(DatalogParser.StringConstTermContext ctx) {
			currentOperands.push(new Constant<>(ctx.getText()));
		}

		@Override
		public void enterIntegerConstTerm(DatalogParser.IntegerConstTermContext ctx) {
			currentOperands.push(new Constant<>(Integer.parseInt(ctx.getText())));
		}

		@Override
		public void enterBooleanConstTerm(DatalogParser.BooleanConstTermContext ctx) {
			currentOperands.push(new Constant<>(ctx.getText().equals("#T")));
		}

		@Override
		public void enterDoubleConstTerm(DatalogParser.DoubleConstTermContext ctx) {
			currentOperands.push(new Constant<>(Double.parseDouble(ctx.getText())));
		}

		public void enterDateConstTerm(DatalogParser.DateConstTermContext ctx) {
			String[] splitString = ctx.getText().split(" ");
			String[] firstPart = splitString[0].split("-");
			GregorianCalendar calendar;

			if (splitString.length > 1) {
				String[] secondPart = splitString[1].split(":");
				calendar = new GregorianCalendar(Integer.parseInt(firstPart[0]), Integer.parseInt(firstPart[1]) - 1,
						Integer.parseInt(firstPart[2]), Integer.parseInt(secondPart[0]),
						Integer.parseInt(secondPart[1]), Integer.parseInt(secondPart[2]));
			} else {
				calendar = new GregorianCalendar(Integer.parseInt(firstPart[0]), Integer.parseInt(firstPart[1]) - 1,
						Integer.parseInt(firstPart[2]));
			}
			currentOperands.push(new Constant<>(calendar));
		}

		@Override
		public void enterVarTerm(VarTermContext ctx) {
			currentOperands.push(new Variable(ctx.getText()));
		}

		@Override
		public void enterAnonTerm(AnonTermContext ctx) {
			anonTermStringBuffer.setLength(0);
			int atomIndex = headAtoms.size();
			if (inBody)
				atomIndex += bodyLiterals.size();
			anonTermStringBuffer.append("_").append(atomIndex).append("_").append(currentOperands.size());
			currentOperands.push(new Variable(anonTermStringBuffer.toString()));
		}

		@Override
		public void enterList(ListContext ctx) {
			this.operandsCountAtExpressionsStart.push(currentOperands.size());
		}

		@Override
		public void exitList(ListContext ctx) {
			currentOperands.push(new Constant<>(getOperands()));
		}

	}

//	private static class AggregateExpressionsCollector extends ExpressionVisitorDefault<List<AggregationExpression>> {
//
//		@Override
//		public Void visit(AggregationExpression expression, List<AggregationExpression> input) {
//			input.add(expression);
//			super.visit(expression, input);
//			return null;
//		}
//
//	}

}