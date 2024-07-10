package uk.co.prometheux.prometheuxreasoner.model;

import java.util.*;

import uk.co.prometheux.prometheuxreasoner.model.annotations.TypeEnum;
import uk.co.prometheux.prometheuxreasoner.model.expressions.Expression;
import uk.co.prometheux.prometheuxreasoner.model.expressions.ExpressionVisitor;

/**
 * A Constant in a Datalog program. It is a typed object.
 * The literals are like in Java. The quotes "" are part of the String
 * for String constants, and directly returned by the parser.
 * 
 * A constant can be also of a composite type, namely Set. In this
 * case the TypeEnum is Set. Notice that also in this case it is
 * immutable.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class Constant<T> extends Term {
	
	private static final long serialVersionUID = 1L;
	private T value;
	private TypeEnum type;

	public Constant() {
		this.type = TypeEnum.UNKNOWN;
	}
	
	public Constant(T value) {
		super();
		this.value = value;
		inferType(value);
	}

	private void inferType(T value) {
		/* we store the data type */
		if(value instanceof String)
			type = TypeEnum.STRING;
		else if(value instanceof Integer)
			type = TypeEnum.INT;
		else if(value instanceof Double)
			type = TypeEnum.DOUBLE;
		else if(value instanceof Boolean)
			type = TypeEnum.BOOLEAN;
		else if(value instanceof GregorianCalendar)
			type = TypeEnum.DATE;
		else if(value instanceof Set)
			type = TypeEnum.SET;
		else if (value instanceof List)
			type = TypeEnum.LIST;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public String toString() {
        if (value instanceof Collection) {
            StringJoiner sj = value instanceof List ? new StringJoiner(",", "[", "]") : new StringJoiner(",", "{", "}");
            for (Object v : (Collection) this.value)
                sj.add(v.toString());
            return sj.toString();
        }
        else if (value instanceof Boolean)
        	return value.equals(true) ? "#T" : "#F";
        else
            return Objects.toString(value);
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void renameVariables(boolean renameVariables) {
		
		if(value instanceof List || value instanceof Set) {
			Collection<Object> l = null;
			if (value instanceof List) {
				l = new LinkedList<Object>();
			}
			if (value instanceof Set) {
				l = new HashSet<Object>();
			}
			for (Object v : (Collection) this.value) {
				if(v instanceof Variable) {
					l.add(((Variable) v).clone(renameVariables));
				}
				else
					l.add(v);
			}
			value =  (T) l;
		}
	}
	
	/**
	 * It returns the data type of this constant
	 * @return the type of the held value
	 */
	public TypeEnum getType() {
		return this.type;
	}
	
	public T getValue() {	
		return this.value;
	}
	
	public Constant<T> clone() {
        return this.clone(false);
	}
	
	public Constant<T> clone(boolean renameVariables) {
		Constant<T> c = new Constant<>(this.value);
		c.renameVariables(renameVariables);
		return c;
	}
	
	/**
	 * It modifies the value for this constant, stripping it, if needed.
	 */
	public void stripIn() {
		this.value = this.strip();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T strip() {
        /* we strip strings */
        if (value instanceof String) {
            return (T) stripString(value.toString());
            /* and sets of strings */
        } else if (value instanceof Collection) {
            Collection stripped = value instanceof Set ? new HashSet<>() : new ArrayList<>();
            for (Object member : (Collection) value) {
                if (member instanceof String)
                    stripped.add(this.stripString((String) member));
				else if (member instanceof Constant<?>)
					stripped.add(new Constant<>(((Constant) member).strip()));
				else
                    stripped.add(member);
            }
            return (T) stripped;
        } else
            return this.value;
    }

	/**
	 * Helper method to strip a string
	 * @param s the string to strip
	 * @return the stripped string
	 */
	private String stripString(String s) {
	    if (s == null || s.length() < 2) {
	        return s;
	    }

	    if (s.charAt(0) == '\"' && s.charAt(s.length() - 1) == '\"') {
	        StringBuilder builder = new StringBuilder(s.substring(1, s.length() - 1));
	        int index = builder.indexOf("\"\"");
	        while (index >= 0) {
	            builder.replace(index, index + 2, "\"");
	            index = builder.indexOf("\"\"", index + 1);
	        }
	        return builder.toString();
	    }

	    return s;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {	
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Constant<?>)) 
			return false;
		else
			return this.value.equals(((Constant<T>)obj).getValue());
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Variable> getAllVariables() {
		HashSet<Variable> variables = new HashSet<>();
		if (type == TypeEnum.LIST)
			for (Expression element : (List<Expression>)value)
				variables.addAll(element.getAllVariables());
		return variables;
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

	@Override
	public String getOperationName() {
		return this.toString();
	}
}