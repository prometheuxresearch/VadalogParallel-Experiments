package prometheuxresearch.benchmark.vldb.experiment.flink.closeLink;

import java.util.Arrays;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.api.java.utils.ParameterTool;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class CloseLinkFlink {

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

		// set up execution environment
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		// Use the parameters in your Flink job
		// Read the initial dataset from the CSV file
		DataSet<Tuple3<Integer, Integer, Double>> own = env.readCsvFile(inputPath).ignoreFirstLine()
				.types(Integer.class, Integer.class, Double.class);

		System.out.println("Own size is:" + own.count());
		long startTimeMillis = System.currentTimeMillis();

		DataSet<Tuple6<Integer, Integer, Integer, Integer[], Double, Double>> mClContributorInit = own
				.filter(t -> !t.f0.equals(t.f1)).groupBy(0, 1).max(2).map(new MapFunction1());

		DataSet<Tuple4<Integer, Integer, Integer[], Double>> mClInit = mClContributorInit.groupBy(0, 1)
				.reduceGroup(new CloseLinkReducer());

		MightCloseLinkContributorEncoder mclce = new MightCloseLinkContributorEncoder();
		MightCloseLinkDecoder mcld = new MightCloseLinkDecoder();
		MightCloseLinkEncoder mcle = new MightCloseLinkEncoder();
		MightCloseLinkEncoder2 mcle2 = new MightCloseLinkEncoder2();

		// 4
		DataSet<Tuple3<String, String, Object[]>> mCl = mClInit.map(mcle);
		// 6
		DataSet<Tuple3<String, String, Object[]>> mClContributor = mClContributorInit.map(mcle2);

		// 4
		DataSet<Tuple4<Integer, Integer, Integer[], Double>> mClDelta = mCl.filter(t -> t.f0.equals("MightCloseLink"))
				.map(mcld);

		// compute the deltas

		while (mClDelta.count() != 0) {

			// 6
			DataSet<Tuple6<Integer, Integer, Integer[], Double, Integer, Double>> appMCLContributorDelta1 = mClDelta
					.join(own).where(1).equalTo(0).projectFirst(0, 1, 2, 3).projectSecond(1, 2);

			// 5
			DataSet<Tuple5<Integer, Integer, Integer, Integer[], Double>> appMCLContributorDelta2 = appMCLContributorDelta1
					.filter(t -> !(Arrays.asList(t.f2)).contains(t.f4) && !t.f1.equals(t.f4)).map(new MapFunction2());

			// 6
			DataSet<Tuple6<Integer, Integer, Integer, Integer[], Double, Double>> mCLContributorDelta = appMCLContributorDelta2
					.map(mclce).coGroup(mClContributor).where(0, 1).equalTo(0, 1).with(new MCLContributorCoGroup());

			// 6
			mClContributor = mClContributor.union(mCLContributorDelta.map(mcle2)).groupBy(0, 1)
					.reduceGroup(new ContributorReducer());

			// 4
			mClDelta = mCLContributorDelta.map(mcle2).coGroup(mCl).where(0, 1).equalTo(0, 1).with(new MCLCoGroup());
			// 4
			mCl = mCl.union(mClDelta.map(mcle)).groupBy(0, 1).reduceGroup(new AggregationReducer());

		}

		DataSet<Tuple3<Integer, Integer, Double>> closeLinks = mCl.filter(t -> t.f0.equals("MightCloseLink")).map(mcld)
				.filter(t -> t.f3 >= 0.5).map(new MapFunction3());

		long countcloseLinks = closeLinks.count();

		System.out.println("The number of close link relationships found is " + countcloseLinks);

		long endTimeMillis = System.currentTimeMillis();
		long executionTimeMillis = endTimeMillis - startTimeMillis;

		System.out.println("Execution time in milliseconds: " + executionTimeMillis);

		if (execMode.equals("test")) {
			closeLinks.print();
		} else {
			closeLinks.writeAsCsv(outputPath);
			// Execute the Flink job
			env.execute("Close Link Flink job");
		}

		return countcloseLinks;
	}

}
