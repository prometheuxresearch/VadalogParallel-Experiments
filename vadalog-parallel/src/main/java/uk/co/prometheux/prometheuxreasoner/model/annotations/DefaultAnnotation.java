package uk.co.prometheux.prometheuxreasoner.model.annotations;

import java.util.List;

/**
 * It is a default annotation, without specified parameters.
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited 
 */

public class DefaultAnnotation extends DatalogAnnotation {

	public DefaultAnnotation(String annotationName, List<Object> params) {
	    super(annotationName, params);
	}
	
}
