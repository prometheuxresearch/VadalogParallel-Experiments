package uk.co.prometheux.prometheuxreasoner.model;

import java.util.*;
import java.util.stream.Collectors;

import uk.co.prometheux.prometheuxreasoner.model.annotations.DatalogAnnotation;

/**
 * This Class represent an Atom
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */

public class Atom {

	// core members
	private String name;
	private List<Term> arguments;
	private List<DatalogAnnotation> annotations;
	
	/* a Skolem atom is an atom that expresses the fact */
	/* that the values of the arguments can be used */
	/* in a function named after the predicate */
	/* to calculate the calculatedVariable. */
	private boolean isSkolemAtom = false;
	/*A special atom that is used in EGD head*/
	private boolean isEqualityAtom = false; 
	/*A special atom that is used in costrains with negation in the head*/
	private boolean isFalseAtom = false; 
	
	private Variable calculatedVariable = null;

	public Atom() {
		super();
		name = "";
		arguments = new ArrayList<>();
		annotations = new ArrayList<>();
	}

	public Atom(final String name) {
		this.name = name;
		arguments = new ArrayList<>();
		annotations = new ArrayList<>();
	}

	public Atom(final String name, final List<Term> arguments) {
		this.name = name;
		this.arguments = arguments;
		annotations = new ArrayList<>();
	}
	
	public Atom(Atom anAtom) {
		this(anAtom, false);
		this.setSkolemAtom(anAtom.isSkolemAtom());
		if(this.isSkolemAtom)
			this.setCalculatedVariable(anAtom.getCalculatedVariable());
	}
	
	public boolean isSkolemAtom() {
		return isSkolemAtom;
	}
	
	public boolean isEqualityAtom() {
		return isEqualityAtom; 
	}
	
	public boolean isFalseAtom() {
		return isFalseAtom; 
	}
	
	public void setAnnotations(List<DatalogAnnotation> annotations) {
		this.annotations = annotations;
	}
	
	public List<DatalogAnnotation> getAnnotations() {
		return this.annotations;
	}

	public Variable getCalculatedVariable() {
		return calculatedVariable;
	}

	public void setCalculatedVariable(Variable calculatedVariable) {
		this.calculatedVariable = calculatedVariable;
	}

	public void setSkolemAtom(boolean isSkolemAtom) {
		this.isSkolemAtom = isSkolemAtom;
	}
	
	public void setEqualityAtom(boolean isEqualityAtom) {
		this.isEqualityAtom = isEqualityAtom; 
	}
	
	public void setFalseAtom(boolean isFalseAtom) {
		this.isFalseAtom = isFalseAtom; 
	}

	/** 
	 * It clones the Atom and allows to rename the variables
	 * @param anAtom the atom to clone
	 * @param renameVariables whether the variables have to be renamed
	 */
	Atom(Atom anAtom, boolean renameVariables) {
		this.name = anAtom.name;
		this.arguments = new ArrayList<>();
		this.isSkolemAtom = anAtom.isSkolemAtom;
		this.isEqualityAtom = anAtom.isEqualityAtom; 
		this.isFalseAtom = anAtom.isFalseAtom; 
		this.annotations = new ArrayList<>();
		if(this.isSkolemAtom)
			this.setCalculatedVariable(anAtom.getCalculatedVariable());
		for(Term t : anAtom.getArguments())
			this.arguments.add(t.clone(renameVariables));
        this.annotations.addAll(anAtom.getAnnotations());
		
	}

	/**
	 * It returns the name of this atom, returning the calculated variable
	 * if this is a Skolem atom.
	 * @return the name of this atom
	 */
	public String getName() {
		if(!this.isSkolemAtom)
			return name;
		else if(this.calculatedVariable!=null)
			return name + "_" + this.calculatedVariable;
		else 
			return name;
	}
	

	/**
	 * It returns the simple name for this atom, which means that it does not
	 * consider the calculated variable.
	 * @return the name of this atom
	 */
	public String getSimpleName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Term> getArguments() {
		return arguments;
	}

	public void setArguments(List<Term> arguments) {
		this.arguments = arguments;
	}
	/**
	 * It returns the set of all the variables in this Atom
	 * We do not use an hash set since we want to preserve the order
	 * @return the set of the variables in this Atom.
	 */
	public List<Variable> getVariablesAsSet() {
		List<Variable> variableAsSet = new ArrayList<Variable>();
		List<Variable> variableList = this.getVariableList();
		for(Variable v : variableList) {
			if(!variableAsSet.contains(v)) {
				variableAsSet.add(v);
			}
		}
		return variableAsSet;
	}
	
	/**
	 * It returns the list of all the variables in this Atom
	 * @return the List of variables in this Atom.
	 */
	public List<Variable> getVariableList() {
		List<Variable> var;
		var = this.arguments.stream()
				.filter((x -> x instanceof Variable))
				.map(x -> (Variable)x)
				.collect(Collectors.toList());
		return var;
	}
	
	
	public Set<Constant<?>> getAllConstants() {
		return this.arguments.stream()
			.filter(x -> x instanceof Constant<?>)
			.map(x -> (Constant<?>)x)
			.collect(Collectors.toSet());
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		StringJoiner sj;
		if(this.annotations!=null) {
			sj = new StringJoiner(" ");
			for(DatalogAnnotation da : this.annotations)
				sj.add(da.toString());
		
			if(!this.annotations.isEmpty())
				s.append(sj.toString()).append(" ");
		}
		
		/*Checks if it is a standard atom*/
		if (!isEqualityAtom && !isFalseAtom) {
			s.append(getName());
			if (arguments.size() > 0) {
				s.append("(");
				final Iterator<Term> i = arguments.iterator();
				while (i.hasNext()) {
					final Term argument = i.next();
					s.append(argument);
					if (i.hasNext())
						s.append(",");
				}
				s.append(")");
			} 
		}
		else {
			/*It checks whether is a negated atom*/
			if(isFalseAtom) {
				s.append("#F");
			}
			else {
				/*It is an equality atom which has always two arguments*/
				s.append(arguments.get(0)); 
				s.append("="); 
				s.append(arguments.get(1)); 	
			}	
		}
		return s.toString();

	}
	
	public String toClauseString() {
		return toString() + ".";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Objects.hashCode(arguments);
        result = prime * result + Objects.hashCode(calculatedVariable);
		result = prime * result + (isSkolemAtom ? 1231 : 1237);
        result = prime * result + Objects.hashCode(name);
        // FIXME: include annotations
		return result;
	}

	@Override
	public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Atom other = (Atom) obj;
        return Objects.equals(this.arguments, other.arguments) &&
                Objects.equals(this.calculatedVariable, other.calculatedVariable) &&
                Objects.equals(this.isSkolemAtom, other.isSkolemAtom) &&
                Objects.equals(this.isFalseAtom, other.isFalseAtom) &&
                Objects.equals(this.isEqualityAtom, other.isEqualityAtom) &&
                Objects.equals(this.name, other.name);
		// FIXME: include annotations
	}

}
