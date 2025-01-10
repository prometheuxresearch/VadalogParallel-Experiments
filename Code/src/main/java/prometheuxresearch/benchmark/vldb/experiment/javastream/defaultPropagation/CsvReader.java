package prometheuxresearch.benchmark.vldb.experiment.javastream.defaultPropagation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class CsvReader {

	private String pathEntity;
	private String pathSecurity;
	private String pathLoan;
	private Map<Integer, EntityNode> nodes;
	private List<LoanEdge> loans;
	private List<SecurityEdge> securities;

	public CsvReader(String pathEntity, String pathSecurity, String pathLoan) {
		super();
		this.pathEntity = pathEntity;
		this.pathSecurity = pathSecurity;
		this.pathLoan = pathLoan;
		this.loans = new LinkedList<>();
		this.securities = new LinkedList<>();
		this.nodes = new HashMap<>();
	}

	public List<LoanEdge> getLoans() {
		return loans;
	}

	public List<SecurityEdge> getSecurities() {
		return securities;
	}

	public List<EntityNode> getNodes() {
		return this.nodes.values().stream().collect(Collectors.toList());
	}

	public void readNodes() throws IOException {
		String finalPath = this.pathEntity;
		File csvFile = new File(finalPath);
		if (csvFile.isFile()) {
			boolean isHeader = true;
			BufferedReader csvReader = new BufferedReader(new FileReader(finalPath));
			String row = null;
			while ((row = csvReader.readLine()) != null) {
				if (!isHeader) {
					Object[] data = row.split(",");
					Integer id = Integer.parseInt(String.valueOf(data[0]));
					Double dp = Double.parseDouble(String.valueOf(data[1]));
					if (!this.nodes.containsKey(id)) {
						this.nodes.put(id, new EntityNode(id, dp));
					}
				}
				isHeader = false;
			}
			csvReader.close();
		}
	}

	public void readLoan() throws IOException {
		String finalPath = this.pathLoan;
		File csvFile = new File(finalPath);
		if (csvFile.isFile()) {
			boolean isHeader = true;
			BufferedReader csvReader = new BufferedReader(new FileReader(finalPath));
			String row = null;
			while ((row = csvReader.readLine()) != null) {
				if (!isHeader) {
					Object[] data = row.split(",");
					int nodeIdFrom = Integer.parseInt(String.valueOf(data[0]));
					int nodeIdTo = Integer.parseInt(String.valueOf(data[1]));
					Double weight = Double.parseDouble(String.valueOf(data[2]));
					if (!this.nodes.containsKey(nodeIdFrom)) {
						this.nodes.put(nodeIdFrom, new EntityNode(nodeIdFrom));
					}
					if (!this.nodes.containsKey(nodeIdTo)) {
						this.nodes.put(nodeIdTo, new EntityNode(nodeIdTo));
					}
					EntityNode nodeFrom = this.nodes.get(nodeIdFrom);
					EntityNode nodeTo = this.nodes.get(nodeIdTo);
					LoanEdge loan = new LoanEdge(nodeFrom, nodeTo, weight);
					this.loans.add(loan);
					nodeFrom.addLoan(loan);
				}
				isHeader = false;
			}
			csvReader.close();
		}
	}

	public void readSecurity() throws IOException {
		String finalPath = this.pathSecurity;
		File csvFile = new File(finalPath);
		if (csvFile.isFile()) {
			boolean isHeader = true;
			BufferedReader csvReader = new BufferedReader(new FileReader(finalPath));
			String row = null;
			while ((row = csvReader.readLine()) != null) {
				if (!isHeader) {
					Object[] data = row.split(",");
					int nodeIdFrom = Integer.parseInt(String.valueOf(data[0]));
					int nodeIdTo = Integer.parseInt(String.valueOf(data[1]));
					Double weight = Double.parseDouble(String.valueOf(data[2]));
					if (!this.nodes.containsKey(nodeIdFrom)) {
						this.nodes.put(nodeIdFrom, new EntityNode(nodeIdFrom));
					}
					if (!this.nodes.containsKey(nodeIdTo)) {
						this.nodes.put(nodeIdTo, new EntityNode(nodeIdTo));
					}
					EntityNode nodeFrom = this.nodes.get(nodeIdFrom);
					EntityNode nodeTo = this.nodes.get(nodeIdTo);
					SecurityEdge security = new SecurityEdge(nodeFrom, nodeTo, weight);
					this.securities.add(security);
					nodeFrom.addSecurity(security);
				}
				isHeader = false;

			}
			csvReader.close();
		}
	}

}
