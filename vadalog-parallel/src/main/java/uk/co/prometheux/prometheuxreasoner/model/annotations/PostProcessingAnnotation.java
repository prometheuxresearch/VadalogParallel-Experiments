package uk.co.prometheux.prometheuxreasoner.model.annotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * It is an annotation of the form \@post("p",...) that specifies a post
 * processing directive for an output atom. The post processing is meant to be
 * applied to a predicate interested by an \@output("p") annotation.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class PostProcessingAnnotation extends DatalogAnnotation {

	private final String predicateName;

	private final PostProcessingTypeEnum postProcessingType;

	/* supplementary parameters such as the fields to order by */
	/* or the field to maximise. */
	private final List<Integer> supplementaryParameters;

	PostProcessingAnnotation(String predicateName, PostProcessingTypeEnum postProcessingType,
			List<Integer> supplementaryParameters) {
		super("post", new ArrayList<>(Arrays.asList(predicateName, getInstruction(postProcessingType, supplementaryParameters))));
		this.predicateName = predicateName;
		this.postProcessingType = postProcessingType;
		this.supplementaryParameters = supplementaryParameters;
	}

	public String getPredicateName() {
		return predicateName;
	}

	public List<Integer> getSupplementaryParameters() {
		return supplementaryParameters;
	}

	public PostProcessingTypeEnum getPostProcessingType() {
		return postProcessingType;
	}

	private static String getInstruction(PostProcessingTypeEnum postProcessingType,
			List<Integer> supplementaryParameters) {
		StringJoiner sj = new StringJoiner(",", "(", ")");
		supplementaryParameters.forEach(x -> sj.add(String.valueOf(x)));
		return postProcessingType.toString() + sj.toString();
	}

}