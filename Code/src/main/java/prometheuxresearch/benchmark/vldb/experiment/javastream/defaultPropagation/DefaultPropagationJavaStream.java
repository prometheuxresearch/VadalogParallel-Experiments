package prometheuxresearch.benchmark.vldb.experiment.javastream.defaultPropagation;

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

public class DefaultPropagationJavaStream {

	private static List<SecurityEdge> security;
	private static List<LoanEdge> loan;
	private static Set<EntityNode> nodes;
	private static CsvReader csvReader;

	public static void main(String[] args) throws Exception {
		// Parse command line parameters using ParameterTool
		ParameterTool params = ParameterTool.fromArgs(args);
		run(params);
	}

	public static int run(ParameterTool params) throws IOException {
		String securityPath = params.get("security_path", "default_input_path");
		String loanPath = params.get("loan_path", "default_input_path");
		String entityPath = params.get("entity_path", "default_input_path");
		int parallelism = params.getInt("parallelism", 4);
		// read input from csv
		csvReader = new CsvReader(entityPath, securityPath, loanPath);
		initGraph();

		System.out.println("security size is:" + security.size());
		System.out.println("loan size is:" + loan.size());
		System.out.println("financial entities size is:" + nodes.size());
		long startTimeMillis = System.currentTimeMillis();

		int count = exec(parallelism);

		long endTimeMillis = System.currentTimeMillis();
		long executionTimeMillis = endTimeMillis - startTimeMillis;

		System.out.println("Execution time in milliseconds: " + executionTimeMillis);
		return count;
	}

	private static void initGraph() throws IOException {
		csvReader.readNodes();
		csvReader.readLoan();
		csvReader.readSecurity();
		loan = csvReader.getLoans();
		security = csvReader.getSecurities();
		nodes = new HashSet<>(csvReader.getNodes());
	}

	private static int exec(Integer parallelismLevel) throws IOException {

		ForkJoinPool customThreadPool = new ForkJoinPool(parallelismLevel);

		List<Integer> count = customThreadPool.submit(
				() -> nodes.parallelStream().filter(n -> n.getDefProb() != null && n.getDefProb() >= 0.5).parallel()
						.map(n -> new SingleEntityDefaultPropagation(n).visit().size()).collect(Collectors.toList()))
				.join();

		customThreadPool.shutdown();

		int sum = count.stream().reduce(0, Integer::sum);
		System.out.println("The number of defaulting relationships found is " + sum);
		return sum;

	}

}
