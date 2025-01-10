package prometheuxresearch.benchmark.vldb.experiment.javastream.closeLink;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

import org.apache.flink.api.java.utils.ParameterTool;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class CloseLinkJavaStream {

	private static List<Edge> edges;
	private static Set<Node> nodes;
	private static CsvReader csvReader;

	public static void main(String[] args) throws Exception {

		// Parse command line parameters using ParameterTool
		ParameterTool params = ParameterTool.fromArgs(args);
		run(params);

	}

	public static int run(ParameterTool params) throws IOException {
		String inputPath = params.get("input", "default_input_path");
		int parallelism = params.getInt("parallelism", 4);
		// read input from csv
		csvReader = new CsvReader(inputPath);
		initGraph();

		System.out.println("own size is:" + edges.size());
		long startTimeMillis = System.currentTimeMillis();

		int count = execute(parallelism);

		long endTimeMillis = System.currentTimeMillis();
		long executionTimeMillis = endTimeMillis - startTimeMillis;

		System.out.println("Execution time in milliseconds: " + executionTimeMillis);
		return count;
	}

	private static void initGraph() throws IOException {
		csvReader.readInputFile();
		edges = csvReader.getEdges();
		buildNodes();
	}

	private static void buildNodes() {
		nodes = new HashSet<>();
		for (Edge e : edges) {

			Node nodeFrom = e.getNodeFrom();
			nodes.add(nodeFrom);

			Node nodeTo = e.getNodeTo();
			nodes.add(nodeTo);

		}
	}

	private static int execute(Integer parallelismLevel) throws IOException {

		ForkJoinPool customThreadPool = new ForkJoinPool(parallelismLevel);

		List<Integer> count = customThreadPool.submit(() -> nodes.parallelStream()
				.map(n -> new SingleSourceVisit(n).visit().size()).collect(Collectors.toList())).join();

		customThreadPool.shutdown();

		int sum = count.stream().reduce(0, Integer::sum);
		System.out.println("The number of close link relationships found is " + sum);
		return sum;

	}

}
