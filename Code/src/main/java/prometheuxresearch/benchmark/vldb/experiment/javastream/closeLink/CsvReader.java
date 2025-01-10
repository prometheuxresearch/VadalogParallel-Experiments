package prometheuxresearch.benchmark.vldb.experiment.javastream.closeLink;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class CsvReader {
	private String path;
	private List<Edge> edges;

	public CsvReader(String path) {
		this.path = path;
		this.edges = new LinkedList<>();
	}

	public String getPath() {
		return path;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void readInputFile() throws IOException {
		Map<Integer, Node> nodesMap = new HashMap<>();
		String finalPath = this.path;
		File csvFile = new File(finalPath);
		if (csvFile.isFile()) {
			boolean isHeader = true;
			BufferedReader csvReader = new BufferedReader(new FileReader(finalPath));
			String row = null;
			while ((row = csvReader.readLine()) != null) {
				if (!isHeader) {
					Object[] data = row.split(",");
					int nodeFromId = Integer.parseInt(String.valueOf(data[0]));
					int nodeToId = Integer.parseInt(String.valueOf(data[1]));
					if (!nodesMap.containsKey(nodeFromId)) {
						Node nodeFrom = new Node(nodeFromId);
						nodesMap.put(nodeFromId, nodeFrom);
					}
					if (!nodesMap.containsKey(nodeToId)) {
						Node nodeTo = new Node(nodeToId);
						nodesMap.put(nodeToId, nodeTo);
					}
					Node nodeFrom = nodesMap.get(nodeFromId);
					Node nodeTo = nodesMap.get(nodeToId);
					Double weight = Double.parseDouble(String.valueOf(data[2]));
					Edge edge = new Edge(nodeFrom, nodeTo, weight);
					nodeFrom.addEdge(edge);
					this.edges.add(edge);
				}
				isHeader = false;
			}
			csvReader.close();
		}
	}

}
