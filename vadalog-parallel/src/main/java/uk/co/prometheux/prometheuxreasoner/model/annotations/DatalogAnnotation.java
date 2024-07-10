package uk.co.prometheux.prometheuxreasoner.model.annotations;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import uk.co.prometheux.prometheuxreasoner.model.Constant;

/**
 * It is a fact @f(c1, c2, ... cn) used in a program to define specific configurations.
 * Parameters c1, ..., cn are specific to f and positional.
 * For example, is f is "input", then c1,..., cn hold the
 * connection, schema, table name and so on.
 *  
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 * 
 */
public abstract class DatalogAnnotation {

    private final String name;
    private List<Object> arguments;

    /**
     * Constructs a DatalogAnnotation from it's name and arguments
     * @param name the annotation name
     * @param arguments the annotation's arguments
     */
    DatalogAnnotation(String name, List<Object> arguments)  {
        this.name = name;
        this.arguments = arguments;
    }

    /**
     * A getter for the annotation's name
     * @return the annotation name
     */
    public String getName() {
        return name;
    }

    public List<Object> getArguments() {
        return arguments;
    }

    /**
     * The accept method from the Visitor's pattern
     * @param visitor the visitor to be accepted
     */
    public void accept(Visitor visitor) {
        visitor.visitName(getName());
        List<Constant<?>> arguments = getArgumentsAsConstants();
        visitor.visitArguments(arguments);
        for (int i = 0; i < arguments.size(); i++)
            visitor.visitAnnotationArgument(arguments.get(i), i);
    }

    /**
     * Returns the annotation arguments as a list of constants
     * @return the list of constants
     */
    private List<Constant<?>> getArgumentsAsConstants() {
        return getArguments().stream().map(s-> new Constant<>(s instanceof String ? "\"" + escape((String)s) + "\"" : s)).collect(Collectors.toList());
    }

    /**
     * An interface from the Visitor pattern
     */
    private interface Visitor {
        /**
         * Visits the annotation name
         * @param name the annotation name
         */
        default void visitName(String name){}

        /**
         * Visits the annotation arguments
         * @param arguments the annotation arguments
         */
        default void visitArguments(List<Constant<?>> arguments){}

        /**
         * Visits an annotation argument
         * @param argument the annotation argument
         * @param position the position of the annotation argument
         */
        default void visitAnnotationArgument(Constant<?> argument, int position){}
    }

    @Override
    public String toString() {
        List<Constant<?>> argumentsAsConstants = getArgumentsAsConstants();
        String argumentList = "";
        if (argumentsAsConstants.size() > 0) {
            StringJoiner sj = new StringJoiner(",", "(", ")");
            argumentsAsConstants.forEach(x -> sj.add(String.valueOf(x)));
            argumentList = sj.toString();
        }
        return "@" + getName() + argumentList;
    }

    private String escape(String s) {
        return s.replaceAll("\"", "\"\"");
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		DatalogAnnotation other = (DatalogAnnotation) obj;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
    
    
}
