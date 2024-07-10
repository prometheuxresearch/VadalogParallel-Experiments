package uk.co.prometheux.prometheuxreasoner.model.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.model.Constant;
import uk.co.prometheux.prometheuxreasoner.model.annotations.TypeEnum;

/**
 * It represents a value of a Type T. It is nullable, and if the case, we store
 * the specific MarkedNull object.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class VadaValue<T> implements Comparable<VadaValue<?>>, Serializable {

	private static final long serialVersionUID = 1L;
	/* if it is null, it must have a specific markedNull */
	private final MarkedNull markedNull;
	private final boolean isNull;
	/* an explicit type for this VadaType, to be set only for evaluation. */
	private final TypeEnum explicitType;

	/* the specific value */
	private final T value;

	private static final VadaValue<Boolean> BOOLEAN_VADA_VALUE_TRUE = new VadaValue<>(true);
	private static final VadaValue<Boolean> BOOLEAN_VADA_VALUE_FALSE = new VadaValue<>(false);

	/**
	 * It builds the value
	 * 
	 * @param value the value
	 */
	@SuppressWarnings("unchecked")
	public VadaValue(T value) {
		assert (value != null);
		/* type inference */
		if (value instanceof List<?>) {
			this.explicitType = TypeEnum.LIST;
			this.value = (T) getNormalizedValue((List<?>) value);
		} else if (value instanceof Set<?>) {
			this.explicitType = TypeEnum.SET;
			this.value = (T) getNormalizedValue((Set<?>) value);
		} else {
			this.value = value;
			if (value instanceof Integer)
				this.explicitType = TypeEnum.INT;
			else if (value instanceof String)
				this.explicitType = TypeEnum.STRING;
			else if (value instanceof Double)
				this.explicitType = TypeEnum.DOUBLE;
			else if (value instanceof Boolean)
				this.explicitType = TypeEnum.BOOLEAN;
			else if (value instanceof GregorianCalendar)
				this.explicitType = TypeEnum.DATE;
			else if (value instanceof Set<?>)
				this.explicitType = TypeEnum.SET;
			else
				this.explicitType = TypeEnum.UNKNOWN;
		}
		this.isNull = false;
		this.markedNull = null;
	}

	/**
	 * Get the inner type for this VadaValue
	 * If it is not a set or a list, it returns the explicit type
	 */
	
	public TypeEnum inferInnerType() {
	    if (value instanceof List<?> || value instanceof Set<?>) {
	        Collection<?> collection = (Collection<?>) value;
	        if (!collection.isEmpty()) {
	            VadaValue<?> firstElement = (VadaValue<?>) collection.iterator().next();
	            return firstElement.getExplicitType();
	        }
	    }
	    return this.explicitType;
	}



	@SuppressWarnings("unchecked")
	private List<VadaValue<?>> getNormalizedValue(List<?> value) {
		// transform a tree of Constant<T>s / or Objects into a tree of VadaValue<T>s
		if (value.isEmpty() || value.get(0) instanceof VadaValue<?>)
			return (List<VadaValue<?>>) value;
		else {
			ArrayList<VadaValue<?>> result = new ArrayList<>();
			for (Object element : value)
				if (element instanceof Constant<?>)
					result.add(normalise((Constant<?>) element));
				else
					result.add(new VadaValue<>(element));
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	private Set<VadaValue<?>> getNormalizedValue(Set<?> value) {
		// transform a tree of Constant<T>s / or Objects into a tree of VadaValue<T>s
		if (value.isEmpty() || value.iterator().next() instanceof VadaValue<?>)
			return (Set<VadaValue<?>>) value;
		else {
			Set<VadaValue<?>> result = new HashSet<>();
			for (Object element : value)
				if (element instanceof Constant<?>)
					result.add(normalise((Constant<?>) element));
				else if (element instanceof Boolean)
					result.add(bool((Boolean) element));
				else
					result.add(new VadaValue<>(element));
			return result;
		}
	}

	private VadaValue<Boolean> bool(boolean value) {
		return value ? BOOLEAN_VADA_VALUE_TRUE : BOOLEAN_VADA_VALUE_FALSE;
	}

	@SuppressWarnings("unchecked")
	private VadaValue<?> normalise(Constant<?> value) {
		if (value.getType() == TypeEnum.LIST) {
			ArrayList<VadaValue<?>> result = new ArrayList<>();
			for (Constant<?> element : (List<Constant<?>>) value.getValue())
				result.add(normalise(element));
			return new VadaValue<>(result);
		}
		return new VadaValue<>(value.getValue());
	}

	/**
	 * It builds a null value. It takes as input a fictional value, which is used
	 * just to get the type for later checks.
	 * 
	 * @param markedNull the marked null value
	 */
	public VadaValue(MarkedNull markedNull) {
		this.isNull = true;
		this.markedNull = markedNull;
		this.explicitType = TypeEnum.UNKNOWN;
		this.value = null;
	}

	public MarkedNull getMarkedNull() {
		return markedNull;
	}

	public boolean isNull() {
		return isNull;
	}

	public T getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isNull ? 1231 : 1237);
		result = prime * result + ((markedNull == null) ? 0 : markedNull.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/**
	 * It compares two VadaValues. Two values are equal if they are of the same type
	 * and have the same value. If one of the two is null and the other is not, then
	 * they are not equal. If both are null they are equal if they have the same
	 * nullName and type.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		VadaValue<?> that = (VadaValue<?>) obj;
		if (isNull)
			return that.isNull && Objects.equals(this.markedNull, that.markedNull);
		else
			return !that.isNull && Objects.equals(this.value, that.value);
	}

	public String toString() {
		if (this.isNull)
			return this.markedNull.toString();
		else
			return this.value.toString();
	}

	public TypeEnum getExplicitType() {
		return explicitType;
	}

	@Override
	public int compareTo(VadaValue<?> that) {
		if (Objects.equals(this, that))
			return 0;

		/* one is null the other is not */
		if ((this.isNull && !that.isNull()) || (!this.isNull && that.isNull()))
			throw new TypeComparisonException("Not possible to compare a marked null with a ground value.");

		/* if both are nulls */
		if (this.isNull && that.isNull) {
			return this.getMarkedNull().compareTo(that.getMarkedNull());
		}

		TypeEnum thatType = that.getExplicitType();
		TypeEnum thisType = this.getExplicitType();

		/*
		 * if we are not in the singleton case, the two elements do not have the same
		 * type
		 */
		/* or no type at all, throw an exception. */
		if (thisType != thatType || thisType == null)
			throw new TypeComparisonException("Not possible to compare VadaValues with different types: "
					+ this.getValue() + " (" + thisType + "), and " + that.getValue() + " (" + thatType + ")");

		/* if they both are not marked nulls */
		switch (thatType) {
		case STRING:
			return ((String) this.getValue()).compareTo((String) that.getValue());
		case INT:
			return ((Integer) this.getValue()).compareTo((Integer) that.getValue());
		case DOUBLE:
			return ((Double) this.getValue()).compareTo((Double) that.getValue());
		case BOOLEAN:
			return ((Boolean) this.getValue()).compareTo((Boolean) that.getValue());
		case DATE:
			return ((GregorianCalendar) this.getValue()).compareTo((GregorianCalendar) that.getValue());
		/* the set can be compared only when one is a subset of the other. */
		case SET:
			return compareToSet(that);
		case LIST:
			// no clear way to compare lists (as sets, as prefix, alphabetically) depends on
			// the application
			return 0;
		default:
			return 0;
		}
	}

	private int compareToSet(VadaValue<?> that) {
		Set<?> thisSet = this.asSet();
		Set<?> thatSet = that.asSet();
		int comp = 0;
		/* if the second contains all the first */
		if (thatSet.containsAll(thisSet))
			comp = -1;
		/* if the first contains all the second */
		if (thisSet.containsAll(thatSet))
			comp += 1;
		/* if they are the same but do not equals, which means they are incomparable */
		if (comp == 0 && !thisSet.equals(thatSet))
			throw new TypeComparisonException("Two incomparable Sets compared: " + thisSet + ", " + thatSet);
		return comp;
	}

	/**
	 * Casts the value to Set. Throws ClassCastException if the value is not SET.
	 * 
	 * @return the result of the cast
	 */
	@SuppressWarnings("unchecked")
	private Set<?> asSet() {
		if (explicitType == TypeEnum.SET)
			return (Set<VadaValue<?>>) getValue();
		return new HashSet<>(Collections.singleton(this));
	}

	/**
	 * Casts the value to Collection. Throws ClassCastException if the value is not
	 * of type LIST or SET.
	 * 
	 * @return the result of the cast
	 */
	public Collection<?> asCollection() {
		switch (explicitType) {
		case LIST:
		case SET:
			return (Collection<?>) getValue();
		default:
			throw new ClassCastException("Cannot cast type " + explicitType + " to " + Collection.class.getName());
		}
	}

}