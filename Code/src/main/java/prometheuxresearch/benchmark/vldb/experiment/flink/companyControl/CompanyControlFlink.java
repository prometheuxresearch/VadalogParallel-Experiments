package prometheuxresearch.benchmark.vldb.experiment.flink.companyControl;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class CompanyControlFlink {

	public static void main(String[] args) throws Exception {

		// Parse command line parameters using ParameterTool
		ParameterTool params = ParameterTool.fromArgs(args);
		run(params);

	}

	public static long run(ParameterTool params) throws Exception {
		// Access parameters using ParameterTool
		String inputPath = params.get("input", "default_input_path");
		String outputPath = params.get("output");
		// test or production
		String execMode = params.get("execution.mode");

		Configuration customConfig = params.getConfiguration();
		// Remove the configuration property by setting it to null

		// Create a Flink execution environment
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		// Use the parameters in your Flink job
		// Read the initial dataset from the CSV file
		DataSet<Tuple3<Integer, Integer, Double>> own = env.readCsvFile(inputPath).ignoreFirstLine()
				.types(Integer.class, Integer.class, Double.class);

		System.out.println("Own size is:" + own.count());
		long startTimeMillis = System.currentTimeMillis();

		// run the exit rules
		DataSet<Tuple4<Integer, Integer, Integer, Double>> controlledSharesInit = own.filter(t -> !t.f0.equals(t.f1))
				.map(new MapFunction1());

		DataSet<Tuple3<Integer, Integer, Double>> totalControlledSharesInit = controlledSharesInit.groupBy(0, 1).sum(3)
				.map(new MapFunction2());

		// encoder and decoders to update and extract the iteration deltas
		TotalControlledSharesEncoder tcse = new TotalControlledSharesEncoder();
		TotalControlledSharesDecoder tcsd = new TotalControlledSharesDecoder();

		DataSet<Tuple4<Integer, Integer, Integer, Double>> controlledShares = controlledSharesInit;
		DataSet<Tuple3<String, String, Object[]>> totalControlledShares = totalControlledSharesInit.map(tcse);

		DataSet<Tuple2<Integer, Integer>> deltaControl = totalControlledSharesInit.filter(t -> t.f2 >= 0.5)
				.map(new MapFunction4()).distinct();

		while (deltaControl.count() != 0) {

			DataSet<Tuple4<Integer, Integer, Integer, Double>> deltaControlledSharesApp = deltaControl.join(own)
					.where(1).equalTo(0).projectFirst(0).projectSecond(1, 0, 2);

			DataSet<Tuple4<Integer, Integer, Integer, Double>> deltaControlledSharesApp2 = deltaControlledSharesApp
					.filter(t -> !t.f0.equals(t.f1) && !t.f0.equals(t.f2)).distinct();

			DataSet<Tuple4<Integer, Integer, Integer, Double>> deltaControlledShares = deltaControlledSharesApp2
					.leftOuterJoin(controlledShares).where("*").equalTo("*").with(new LeftAntiJoinFunction())
					.filter(t -> !t.f3.equals(-1.0));

			controlledShares = controlledShares.union(deltaControlledShares);

			DataSet<Tuple3<Integer, Integer, Double>> deltaTotalControlledShares = deltaControlledShares.groupBy(0, 1)
					.sum(3).map(new MapFunction3());

			deltaTotalControlledShares = deltaTotalControlledShares.map(tcse).coGroup(totalControlledShares).where(0, 1)
					.equalTo(0, 1).with(new TotalControlledSharesCoGroup());

			totalControlledShares = totalControlledShares.union(deltaTotalControlledShares.map(tcse)).groupBy(0, 1)
					.reduceGroup(new TotalControlledSharesReducer());

			deltaControl = deltaTotalControlledShares.filter(t -> t.f2 >= 0.5).map(new MapFunction4());

		}

		DataSet<Tuple3<Integer, Integer, Double>> control = totalControlledShares.map(tcsd).filter(t -> t.f2 >= 0.5);
		long countControlRel = control.count();

		System.out.println("The number of control relationship found is " + countControlRel);

		long endTimeMillis = System.currentTimeMillis();
		long executionTimeMillis = endTimeMillis - startTimeMillis;

		System.out.println("Execution time in milliseconds: " + executionTimeMillis);

		if (execMode.equals("test")) {
			control.print();
		} else {
			control.writeAsCsv(outputPath);
			// Execute the Flink job
			env.execute("Company Control Flink job");
		}
		return countControlRel;
	}

}
