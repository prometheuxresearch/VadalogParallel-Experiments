package uk.co.prometheux.prometheuxreasoner.common.utils;

import java.util.Arrays;
import java.util.Collection;

import scala.collection.Iterable;
import scala.collection.JavaConverters;
import scala.collection.mutable.WrappedArray;

public class JavaScalaConverters {
	
	@SuppressWarnings("unchecked")
	public static Collection<Object> convertToJavaList(Object toConvert){
		if(toConvert instanceof Object[]) {
			return Arrays.asList((Object[]) toConvert);
		}
		else if(toConvert instanceof WrappedArray<?>) {
			return JavaConverters.asJavaCollection((WrappedArray<Object>)toConvert);
		}
		return JavaConverters.asJavaCollection((Iterable<Object>) toConvert);
	}

}
