package uk.co.prometheux.prometheuxreasoner.warded;


/**
 * A singleton class representing the reasoning semantics to be adopted for the
 * current Vadalog program.
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */
public class ReasoningSemantics {
	
    public static ReasoningSemantics instance; 
    
    private ReasoningSemanticsEnum semantics;
    private boolean doExplainability = false;
	
	private ReasoningSemantics() {
		this.semantics = ReasoningSemanticsEnum.SET_SEMANTICS;
	}
	
	public static ReasoningSemantics getInstance() {
		if(instance == null) {
			instance = new ReasoningSemantics();
		}
		return instance; 
	}
	
	public void setWardedSemantics() {
		this.semantics = ReasoningSemanticsEnum.WARDED_SEMANTICS;
	}
	
	public void setRelaxedWardedSemantics() {
		this.semantics = ReasoningSemanticsEnum.RELAXED_WARDED_SEMANTICS;
	}

	public void setSetSemantics() {
		this.semantics = ReasoningSemanticsEnum.SET_SEMANTICS;
	}
	
	public ReasoningSemanticsEnum getSemantics() {
		return this.semantics;
	}
	

	public void setDoExplainability(boolean doExplainability) {
		this.doExplainability = doExplainability;
	}

	public boolean getDoExplainability() {
		return this.doExplainability;
	}

}
