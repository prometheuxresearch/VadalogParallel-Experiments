package uk.co.prometheux.prometheuxreasoner.model;

import java.util.*;
import java.util.stream.Collectors;

import uk.co.prometheux.prometheuxreasoner.common.IdGenerator;
import uk.co.prometheux.prometheuxreasoner.model.annotations.DatalogAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.expressions.SkolemExpression;

/**
 * This Class implements a Vadalog Rule
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */

public class Rule {
	private List<Atom> head;
	private List<Literal> body;
	private List<Condition> conditions;
	private List<DatalogAnnotation> annotations;

	/* dom* implies that the facts for the body */
	/* must all be read from the database */
	private boolean isDomStar = false;
	/* if it was originally a rule with msum and contributors */
	private boolean hadContributors = false;


	public Rule(List<Atom> head, List<Literal> body, List<Condition> conditions) {
		this.head = head;
		this.body = body;
		this.annotations = new ArrayList<>();
		if (conditions == null)
			this.conditions = new ArrayList<>();
		else
			this.conditions = conditions;
	}

	public Rule(List<Atom> head, List<Literal> body, List<Condition> conditions, List<DatalogAnnotation> annotations) {
		this(head, body, conditions);
		this.setAnnotations(annotations);
	}

	public Rule(Atom head, List<Literal> body, List<Condition> conditions, List<DatalogAnnotation> annotations) {
		this(head, body, conditions);
		this.setAnnotations(annotations);
	}

	public Rule(Atom head, List<Literal> body, List<Condition> conditions) {
		this.head = new LinkedList<>();
		this.head.add(head);
		this.body = body;
		this.annotations = new ArrayList<>();
		if (conditions == null)
			this.conditions = new ArrayList<>();
		else
			this.conditions = conditions;
	}

	public Rule(Atom head, Literal body, List<Condition> conditions, List<DatalogAnnotation> annotations) {
		this(head, body, conditions);
		this.setAnnotations(annotations);
	}

	public Rule(Atom head, Literal body, List<Condition> conditions) {
		this.head = new LinkedList<>();
		this.head.add(head);
		this.body = new LinkedList<>();
		this.body.add(body);
		this.annotations = new ArrayList<>();
		if (conditions == null)
			this.conditions = new ArrayList<>();
		else
			this.conditions = conditions;
	}

	/**
	 * It returns the first Literal whose atom conforms to a given name. It returns
	 * null if none is found
	 * 
	 * @param name The name to look for
	 * @return the first literal conforming to the given name
	 */
	public Literal getFirstLiteralByName(String name) {
		for (Literal l : this.body)
			if (l.getAtom().getName().equals((name)))
				return l;
		return null;
	}

	/**
	 * It creates a new rule by cloning the current one.
	 * 
	 * @param aRule The rule to clone.
	 */
	public Rule(Rule aRule) {
		this(aRule, false);
	}

	/**
	 * It returns all the Skolem atoms in this rule.
	 * 
	 * @return all the Skolem atoms in this rule.
	 */
	public Set<Atom> getSkolemAtoms() {
		return this.body.stream().filter(x -> x.getAtom().isSkolemAtom()).map(Literal::getAtom)
				.collect(Collectors.toSet());
	}

	/**
	 * It returns all the Skolem atoms in this rule.
	 * 
	 * @return all the Skolem atoms in this rule.
	 */
	public List<Atom> getSkolemAtomsList() {
		return this.body.stream().filter(x -> x.getAtom().isSkolemAtom()).map(Literal::getAtom)
				.collect(Collectors.toList());
	}

	public List<Variable> getAllSkolemAtomsVariables() {
		return this.getSkolemAtoms().stream().map(x -> x.getCalculatedVariable()).collect(Collectors.toList());
	}

	/**
	 * It returns all the Skolem atoms that calculate a specific variable
	 * 
	 * @param t the variable to search
	 * @return the set of Skolem atoms
	 */
	public Set<Atom> getSkolemAtomsByCalculatedVariable(Variable t) {
		Set<Atom> res = new HashSet<>();
		for (Literal l : this.getBody())
			if (l.getAtom().isSkolemAtom()) {
				if (l.getAtom().getCalculatedVariable().equals(t))
					res.add(l.getAtom());
			}
		return res;
	}

	/**
	 * It drops all the Skolem atoms from this rule.
	 */
	public void dropSkolemAtoms() {
		this.body = this.body.stream().filter(x -> !x.getAtom().isSkolemAtom()).collect(Collectors.toList());
	}

	/**
	 * It returns a map where each name of Skolem atom is mapped into the set of
	 * atoms in the body of this rule having that name.
	 * 
	 * @return the map from Skolem atom names to the atoms
	 */
	public Map<String, Set<Atom>> getSkolemAtomsByName() {
		Map<String, Set<Atom>> namesToSkolemAtoms = new HashMap<>();
		for (Atom a : this.getSkolemAtoms()) {
			Set<Atom> curSet = namesToSkolemAtoms.get(a.getName());
			if (curSet == null)
				curSet = new HashSet<>();
			curSet.add(a);
			namesToSkolemAtoms.put(a.getName(), curSet);
		}
		return namesToSkolemAtoms;
	}

	/**
	 * It returns a map where each name of Skolem atom is mapped into the list of
	 * atoms in the body of this rule having that name.
	 * 
	 * @return the map from Skolem atom names to the atoms
	 */
	public Map<String, List<Atom>> getSkolemAtomsByNameList() {
		Map<String, List<Atom>> namesToSkolemAtoms = new HashMap<>();
		for (Atom a : this.getSkolemAtomsList()) {
			List<Atom> curList = namesToSkolemAtoms.get(a.getName());
			if (curList == null)
				curList = new ArrayList<>();
			curList.add(a);
			namesToSkolemAtoms.put(a.getName(), curList);
		}
		return namesToSkolemAtoms;
	}

	/**
	 * It returns a map where each name of body atom is mapped into the list of
	 * atoms in the body of this rule having that name.
	 * 
	 * @return the map from body atom names to the atoms
	 */
	public Map<String, List<Atom>> getBodyAtomsByNameList() {
		Map<String, List<Atom>> namesToBodyAtoms = new HashMap<>();
		for (Atom a : this.getBodyAtomsList()) {
			List<Atom> curList = namesToBodyAtoms.get(a.getName());
			if (curList == null)
				curList = new ArrayList<>();
			curList.add(a);
			namesToBodyAtoms.put(a.getName(), curList);
		}
		return namesToBodyAtoms;
	}

	/**
	 * It renames a variable in the rule avoiding specific names.
	 * 
	 * @param namesToAvoid the names to avoid
	 */
	public void renameVariables(Set<String> namesToAvoid) {

		/* we clone it, to keep the original identity */
		Rule placeHolder = new Rule(this);

		Atom head = this.getSingleHead();

		for (Variable var : head.getVariableList()) {
			var.setName(IdGenerator.getNewDeterministicVariableSymbol(placeHolder, var.getName(), namesToAvoid));
		}

		for (Literal l : this.body) {
			for (Variable var : l.getAtom().getVariableList())
				var.setName(IdGenerator.getNewDeterministicVariableSymbol(placeHolder, var.getName(), namesToAvoid));
		}

		for (Condition c : this.conditions) {
			for (Variable var : c.getAllVariables()) {
				var.setName(IdGenerator.getNewDeterministicVariableSymbol(placeHolder, var.getName(), namesToAvoid));
			}
		}

		// Rest IdGenerator to avoid problems with same atom variable rewritings
		IdGenerator.resetVarSymbols();

		/* we do not rename the conditions, not used in homomorphisms */
	}

	/**
	 * It eliminates duplicate literals in the body.
	 */
	public void deduplicateBody() {
		Set<Literal> dedupBody = new HashSet<>(this.body);
		this.body.clear();
		this.body.addAll(dedupBody);
	}

	/**
	 * It eliminates duplicate conditions in the rule.
	 */
	public void deduplicateConditions() {
		Set<Condition> dedupConds = new HashSet<>(this.conditions);
		this.conditions.clear();
		this.conditions.addAll(dedupConds);
	}

	/**
	 * It creates a new rule by cloning the current one and renaming all the
	 * variables.
	 * 
	 * @param aRule           The rule to clone.
	 * @param renameVariables Whether the variables have to be renamed.
	 */
	public Rule(Rule aRule, boolean renameVariables) {
		this.head = new LinkedList<>();
		this.conditions = new ArrayList<>();
		this.annotations = new ArrayList<>();

		for (Atom a : aRule.getHead())
			this.head.add(new Atom(a, renameVariables));
		this.body = new LinkedList<>();
		for (Literal l : aRule.getBody())
			this.body.add(new Literal(l, renameVariables));
		for (Condition c : aRule.getConditions()) {
			this.conditions.add(new Condition(c, renameVariables));
		}
		this.annotations.addAll(aRule.getAnnotations());

		this.isDomStar = aRule.isDomStar;

	}

	public List<Literal> getPositiveLiterals() {
		return this.body.stream().filter(Literal::isPositive).collect(Collectors.toList());
	}

	public List<String> getNamesOfPositiveLiterals() {
		return this.body.stream().filter(Literal::isPositive).map(x -> x.getAtom().getName())
				.collect(Collectors.toList());
	}

	public List<Literal> getNegativeLiterals() {
		return this.body.stream().filter(x -> !x.isPositive()).collect(Collectors.toList());
	}

	public List<Literal> getLiterals() {
		return this.body;
	}

	/**
	 * This method return all the literal in the body negated. It means true literal
	 * if was false and vice versa
	 * 
	 * @return body literals negated
	 */
	public List<Literal> getNegatedLiterals() {
		List<Literal> negatedBodyLiterals = new LinkedList<Literal>();
		for (Literal l : this.body) {
			negatedBodyLiterals.add(new Literal(l.getAtom(), !l.isPositive()));
		}
		return negatedBodyLiterals;
	}

	public Set<Constant<?>> getAllConstants() {
		Set<Constant<?>> allConstants = new HashSet<>();
		this.getLiterals().forEach(x -> allConstants.addAll(x.getAtom().getAllConstants()));
		allConstants.addAll(this.getSingleHead().getAllConstants());
		return allConstants;
	}

	/**
	 * It builds a map from each variable of the body to a pair (o,p), where o is
	 * the position of an atom the variable appears in and p is a position in the
	 * atom where the variable appears.
	 * 
	 * @return the map
	 */
	public Map<Variable, List<Integer>> getVariableToABodyPositionMap() {
		Map<Variable, List<Integer>> rMap = new HashMap<>();
		int posL = 0;
		for (Literal l : this.body) {
			if (l.isPositive()) {// fix for negation and conditions. we only take positions in positive literals.
				Atom a = l.getAtom();
				int posV = 0;
				for (Term t : a.getArguments()) {
					if (t instanceof Variable) {
						List<Integer> termPos = new ArrayList<>();
						termPos.add(posL);
						termPos.add(posV);
						rMap.put((Variable) t, termPos);
					}
					posV++;
				}
				posL++;
			}
		}

		return rMap;
	}

	public List<Condition> getConditions() {
		return this.conditions;
	}

	/**
	 * It returns the conditions that calculate an existentially quantified variable
	 * in this rule.
	 * 
	 * @return the conditions that calculate an existentially quantified variable
	 */
	public List<Condition> getCalculatingConditions() {
		List<Condition> conds = new ArrayList<>();
		this.conditions.stream().filter(c -> this.getSingleHead().getVariablesAsSet().contains(c.getLhs()))
				.forEach(conds::add);
		return conds;
	}

	public List<Atom> getHead() {
		return this.head;
	}

	public List<Literal> getBody() {
		return this.body;
	}

	public void setBody(List<Literal> body) {
		this.body = body;
	}

	public void clearBody() {
		this.body.clear();
	}

	public Atom getSingleHead() {
		if (this.head.size() > 0)
			return this.head.get(0);
		else
			return null;
	}

	public void setSingleHead(Atom a) {
		this.head = new ArrayList<>();
		this.head.add(a);
	}

	public void setAnnotations(List<DatalogAnnotation> annotations) {
		this.annotations = annotations;
		/* here we process the annotations and set specific rule properties */
	}

	public List<DatalogAnnotation> getAnnotations() {
		return this.annotations;
	}

	/**
	 * Returns all variables calculated in expressions
	 * 
	 * @return the set of variables
	 */
	public Set<Variable> getCalculatedVariables() {
		Set<Variable> conditionedVariables = this.getConditions().stream().map(Condition::getLhs)
				.collect(Collectors.toSet());
		Set<Variable> positivelyBoundVariables = getBodyPositiveVariables();
		conditionedVariables.removeAll(positivelyBoundVariables);
		return conditionedVariables;
	}

	/**
	 * It returns the existentially quantified variables that are calculated.
	 * 
	 * @return The existentially quantified variables that are calculated.
	 */
	public Set<Variable> getCalculatedExistentiallyQuantifiedVariables() {
		Set<Variable> ex = this.getExistentiallyQuantifiedVariables();
		Set<Variable> conditionedVariables = this.getConditions().stream().map(Condition::getLhs)
				.collect(Collectors.toSet());

		return conditionedVariables.stream().filter(ex::contains).collect(Collectors.toSet());

	}

	/**
	 * It returns the existentially quantified variables that are not calculated,
	 * but produce marked nulls.
	 * 
	 * @return The existentially quantified variables that give rise to nulls.
	 */
	public Set<Variable> getExistentiallyQuantifiedVariablesNotCalculated() {
		Set<Variable> ex = this.getExistentiallyQuantifiedVariables();
		Set<Variable> conditionedVariables = this.getConditions().stream().map(Condition::getLhs)
				.collect(Collectors.toSet());

		ex.removeAll(conditionedVariables);
		return ex;
	}

	/**
	 * It returns the set of existentially quantified variables
	 * 
	 * @return the set of Variables in this Rule.
	 */
	public Set<Variable> getExistentiallyQuantifiedVariables() {
		Set<Variable> headVars = new HashSet<Variable>(this.getSingleHead().getVariablesAsSet());
		Set<Variable> bodyVars = new HashSet<>();

		/* the following builds the body variables */
		/* by extracting them from all the body atoms */
		for (Set<Variable> varSet : (this.body.stream().map(x -> new HashSet<>(x.getAtom().getVariablesAsSet()))
				.collect(Collectors.toSet())))
			bodyVars.addAll(varSet);

		headVars.removeAll(bodyVars);
		return headVars;
	}

	/**
	 * returns all truly existential variables. I.e. the existential variables
	 * excluding the ones computed by a non-skolem expression.
	 * 
	 * @return the set of unsafe variables
	 */
	public Set<Variable> getExistentiallyQuantifiedVariables_Unsafe() {
		Set<Variable> variables = getExistentiallyQuantifiedVariables();
		conditions.forEach(c -> {
			if (!(c.getRhs() instanceof SkolemExpression))
				variables.remove(c.getLhs());
		});
		return variables;
	}

	/**
	 * It returns the set of all the variables in the body.
	 * 
	 * @return all the variables in the bodu
	 */
	public Set<Variable> getBodyVariables() {
		return this.getBodyVariables(false);
	}

	/**
	 * It returns the set of all body atoms.
	 * 
	 * @return all the atoms in the body
	 */
	public Set<Atom> getBodyAtoms() {
		return this.getBody().stream().map(Literal::getAtom).collect(Collectors.toSet());
	}

	/**
	 * It returns the list of all body atoms.
	 * 
	 * @return all the atoms in the body
	 */
	public List<Atom> getBodyAtomsList() {
		return this.getBody().stream().map(Literal::getAtom).collect(Collectors.toList());
	}

	/**
	 * It returns the set of all body atom names.
	 * 
	 * @return all the atom names in the body
	 */
	public List<String> getBodyAtomNamesList() {
		return this.getBody().stream().map(Literal::getAtom).map(Atom::getName).collect(Collectors.toList());
	}

	/**
	 * It returns the set of all the variables in the body
	 * 
	 * @return all the variables in the body
	 */
	public Set<Variable> getBodyVariables(boolean excludeSkolemAtoms) {
		Set<Variable> bodyVariables = new HashSet<>();
		for (Literal l : this.getLiterals()) {
			/* if we only want Skolem atoms */
			if (excludeSkolemAtoms && !l.getAtom().isSkolemAtom())
				bodyVariables.addAll(l.getAtom().getVariablesAsSet());
			/* if we want everything */
			else if (!excludeSkolemAtoms) {
				bodyVariables.addAll(l.getAtom().getVariablesAsSet());
			}
		}
		return bodyVariables;
	}

	public List<Variable> getBodyVariablesList() {
		List<Variable> bodyVariables = new ArrayList<>();
		for (Literal l : this.getLiterals()) {
			bodyVariables.addAll(l.getAtom().getVariableList());
		}
		return bodyVariables;
	}

	/**
	 * It returns all the variables for this rule
	 * 
	 * @return all the variables for this rule
	 */
	public Set<Variable> getVariables() {
		Set<Variable> allVariables = new HashSet<>();
		allVariables.addAll(this.getBodyVariables());
		allVariables.addAll(this.getSingleHead().getVariablesAsSet());
		return allVariables;
	}

	/**
	 * It returns the set of all variables in positive literals of the body
	 * 
	 * @return all the variables in positive literals of the body
	 */
	public Set<Variable> getBodyPositiveVariables() {
		Set<Variable> bodyVariables = new HashSet<>();
		for (Literal l : this.getPositiveLiterals()) {
			if (!l.getAtom().isSkolemAtom())
				bodyVariables.addAll(l.getAtom().getVariablesAsSet());
		}
		return bodyVariables;
	}

	/**
	 * It returns all the atoms of the rule body that contain the given variable
	 * 
	 * @param x the variable to look for
	 * @return the set of atoms containing those variables
	 */

	public Set<Atom> getBodyAtomsByVariable(Variable x) {
		return this.getBody().stream().filter(q -> q.getAtom().getVariableList().contains(x)).map(Literal::getAtom)
				.collect(Collectors.toSet());
	}

	/**
	 * It returns all the literals of the rule body that cointain a set of variable.
	 * 
	 * @param x the variable to look for
	 * @return the set of literals containing variable x
	 */
	public Set<Literal> getBodyLiteralsByVariable(Variable x) {
		return this.getBody().stream().filter(q -> q.getAtom().getVariableList().contains(x))
				.filter(q -> !q.getAtom().isSkolemAtom()).collect(Collectors.toSet());
	}

	/**
	 * It returns the set of all the variables that appear in the head of this rule.
	 * We adopt a List in order to preserve the order
	 * 
	 * @return the set of all the variables that appear in the head of this rule.
	 */
	public List<Variable> getHeadVariablesAsSet() {

		List<Variable> variablesInHeadAsSet = new ArrayList<>();

		for (Atom head : this.getHead()) {
			for(Variable v : head.getVariableList()) {
				if(!variablesInHeadAsSet.contains(v)) {
					variablesInHeadAsSet.add(v);
				}
			}
		}
		return variablesInHeadAsSet;
	}

	/**
	 * It returns the set of all the variables that appear in the conditions of this
	 * rule.
	 * 
	 * @return the set of all the variables that appear in the conditions of this
	 *         rule.
	 */
	public Set<Variable> getConditionsVariables() {

		Set<Variable> variablesInConditions = new HashSet<>();

		for (Condition c : this.conditions) {
			variablesInConditions.addAll(c.getAllVariables());
		}

		return variablesInConditions;
	}

	/**
	 * It returns a map of from the variables of this rule to the literals they
	 * appear in
	 * 
	 * @return a mapping from the body variables to literals.
	 */
	public Map<Variable, Set<Literal>> getBodyVariablesToLiteralsMap() {
		Map<Variable, Set<Literal>> varsToLiterals = new HashMap<>();

		/* we create a map variable -> literal */
		for (Literal l : this.getLiterals()) {
			for (Variable v : l.getAtom().getVariablesAsSet()) {
				Set<Literal> literalSet = varsToLiterals.get(v);

				if (literalSet == null)
					literalSet = new HashSet<>();

				literalSet.add(l);
				varsToLiterals.put(v, literalSet);

			}
		}

		return varsToLiterals;
	}

	/**
	 * It returns whether this variable is linear or not.
	 * 
	 * @return whether the rule is linear or not.
	 */
	public boolean isLinear() {
		return this.getBody().size() == 1;
	}

	/**
	 * It sorts the body of a rule using a Comparator of Literals passed as
	 * argument.
	 */
	public void sortBodyByComparator(Comparator<Literal> c) {
		this.body.sort(c);
	}

	/**
	 * It sorts the body of a rule putting the negated atoms in final positions.
	 */
	public void sortBodyByNegation() {
		this.body.sort(new BodyLiteralsNegComparator());
	}

	/**
	 * It sorts the body of a rule in lexicographic order.
	 */
	void sortBody() {
		this.body.sort(Comparator.comparing(Literal::toString));
	}

	/**
	 * Checks if a rule is relational and positive.
	 */

	public boolean isPositiveRelational() {
		return this.getConditions().isEmpty() && this.getNegativeLiterals().isEmpty();
	}

	/**
	 * Returns the set of atoms from both the head and the body.
	 */

	public Set<Atom> getAllAtoms() {
		Set<Atom> atoms = new HashSet<>();
		atoms.addAll(new HashSet<>(getBodyAtoms()));
		atoms.addAll(new HashSet<>(getHead()));
		return atoms;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		if (this.annotations != null) {
			StringJoiner sj = new StringJoiner(" ");
			for (DatalogAnnotation da : this.annotations)
				sj.add(da.toString());

			if (!this.annotations.isEmpty())
				s.append(sj.toString()).append(" ");
		}
		Iterator<Atom> i = head.iterator();
		while (i.hasNext()) {
			s.append(i.next().toString());
			if (i.hasNext())
				s.append(", ");
		}
		s.append(" :- ");
		Iterator<Literal> j = body.iterator();
		while (j.hasNext()) {
			s.append(j.next().toString());
			if (j.hasNext())
				s.append(", ");
		}
		if (this.isDomStar)
			s.append(", dom(*)");

		Iterator<Condition> k = conditions.iterator();
		if (k.hasNext())
			s.append(", ");

		while (k.hasNext()) {
			s.append(k.next().toString());
			if (k.hasNext())
				s.append(", ");
		}

		s.append(".");
		return s.toString();
	}

	public boolean isDomStar() {
		return isDomStar;
	}

	public void setDomStar(boolean isDomStar) {
		this.isDomStar = isDomStar;
	}

	/**
	 * If there is an equality atom in the head of the rule is an EGD e.g. X=Y :-
	 * p(X,Y).
	 * 
	 * @return true if the rule is an EGD
	 */
	public boolean isEGD() {
		return this.getHead().stream().anyMatch(singleHead -> singleHead.isEqualityAtom());
	}

	/**
	 * If there is not an equality atom in the head of the rule is a TGD
	 * 
	 * @return true if the rule is a TGD
	 */
	public boolean isTGD() {
		return !this.isEGD();
	}

	/**
	 * It checks if the rule's head is expressing a negation as failure. e.g. #F :-
	 * p(X,Y).
	 * 
	 * @return true if the rule is expressing a special costrain named negation as
	 *         failure which makes the reasoning process to fail.
	 * 
	 */
	public boolean isNegationAsFailure() {
		return this.getHead().stream().anyMatch(singleHead -> singleHead.isFalseAtom());
	}

	public boolean isTautology() {
		/*
		 * it cannot appear a condition in the head, if it appear in the body return
		 * false
		 */
		if (this.getConditions().size() != 0)
			return false;
		List<Literal> bodyLiterals = this.getBody();
		/*
		 * if at least one literal in the body is negative, it cannot be equal to the
		 * head
		 */
		for (Literal bodyLiteral : bodyLiterals) {
			if (!bodyLiteral.isPositive())
				return false;
		}
		List<Atom> bodyAtoms = new LinkedList<Atom>(new HashSet<Atom>(this.getBodyAtomsList()));
		List<Atom> headAtoms = new LinkedList<Atom>(new HashSet<Atom>(this.getHead()));
		if (bodyAtoms.size() != headAtoms.size())
			return false;
		else {
			for (Atom bodyAtom : bodyAtoms) {
				if (headAtoms.contains(bodyAtom))
					headAtoms.remove(bodyAtom);
				else
					return false;
			}
			if (headAtoms.size() != 0)
				return false;
			else
				return true;
		}
	}
	
	public void setHadContributors(boolean hadContributors) {
		this.hadContributors = hadContributors;
	}
	
	public boolean hadContributors() {
		return this.hadContributors;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Objects.hashCode(body);
		result = prime * result + Objects.hashCode(conditions);
		result = prime * result + Objects.hashCode(head);
		result = prime * result + (isDomStar ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		// FIXME: compare as sets, not as lists
		return Objects.equals(this.body, other.body) && Objects.equals(this.conditions, other.conditions)
				&& Objects.equals(this.head, other.head) && Objects.equals(this.isDomStar, other.isDomStar);
	}

	/**
	 * The accept method from the Visitor pattern
	 * 
	 * @param visitor the visitor
	 */
	public void accept(Visitor visitor) {
		visitor.visitHeadAtoms(head);
		head.forEach(visitor::visitHeadAtom);
		visitor.visitBodyLiterals(body);
		body.forEach(visitor::visitBodyLiteral);
		visitor.visitConditions(conditions);
		conditions.forEach(visitor::visitCondition);
		visitor.visitAnnotations(annotations);
		annotations.forEach(visitor::visitAnnotation);
	}

	/**
	 * An interface realising the Visitor pattern for the rule
	 */
	private interface Visitor {
		default void visitHeadAtoms(List<Atom> atoms) {
		}

		default void visitHeadAtom(Atom atom) {
		}

		default void visitBodyLiterals(List<Literal> literals) {
		}

		default void visitBodyLiteral(Literal literal) {
		}

		default void visitConditions(List<Condition> conditions) {
		}

		default void visitCondition(Condition condition) {
		}

		default void visitAnnotations(List<DatalogAnnotation> annotations) {
		}

		default void visitAnnotation(DatalogAnnotation annotation) {
		}
	}

}