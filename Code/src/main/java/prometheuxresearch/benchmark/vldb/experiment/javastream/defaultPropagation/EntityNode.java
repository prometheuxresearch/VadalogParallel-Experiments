package prometheuxresearch.benchmark.vldb.experiment.javastream.defaultPropagation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class EntityNode {

	private Integer nodeId;
	private Double defProb = 0.0;

	private List<LoanEdge> loanEdges;
	private Set<LoanEdge> loanEdgesSet;

	private List<SecurityEdge> securityEdges;
	private Set<SecurityEdge> securityEdgesSet;

	public EntityNode(Integer nodeId, Double defProb) {
		super();
		this.nodeId = nodeId;
		this.loanEdges = new ArrayList<>();
		this.loanEdgesSet = new HashSet<>();
		this.securityEdges = new ArrayList<>();
		this.securityEdgesSet = new HashSet<>();
		this.defProb = defProb;
	}

	public EntityNode(Integer nodeId) {
		super();
		this.nodeId = nodeId;
		this.loanEdges = new ArrayList<>();
		this.loanEdgesSet = new HashSet<>();
		this.securityEdges = new ArrayList<>();
		this.securityEdgesSet = new HashSet<>();
	}

	public Integer getNodeId() {
		return this.nodeId;
	}

	public Double getDefProb() {
		return defProb;
	}

	public List<EntityNode> getLoanEntity() {
		return this.loanEdges.stream().map(x -> x.getNodeTo()).collect(Collectors.toList());
	}

	public List<EntityNode> getSecurityEntity() {
		return this.securityEdges.stream().map(x -> x.getNodeTo()).collect(Collectors.toList());
	}

	public void addLoan(LoanEdge loan) {
		if (!this.loanEdgesSet.contains(loan)) {
			this.loanEdgesSet.add(loan);
			this.loanEdges.add(loan);
		}
	}

	public void addSecurity(SecurityEdge security) {
		if (!this.securityEdgesSet.contains(security)) {
			this.securityEdgesSet.add(security);
			this.securityEdges.add(security);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
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
		EntityNode other = (EntityNode) obj;
		if (nodeId == null) {
			if (other.nodeId != null)
				return false;
		} else if (!nodeId.equals(other.nodeId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nodeId.toString();
	}

}
