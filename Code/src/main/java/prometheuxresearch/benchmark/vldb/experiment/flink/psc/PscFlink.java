package prometheuxresearch.benchmark.vldb.experiment.flink.psc;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DeltaIteration;
import org.apache.flink.api.java.tuple.Tuple1;
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

public class PscFlink {

	public static void main(String[] args) throws Exception {
		// Parse command line parameters using ParameterTool
		ParameterTool params = ParameterTool.fromArgs(args);

		run(params);

	}

	public static long run(ParameterTool params) throws Exception {
		// Access parameters using ParameterTool
		String controlPath = params.get("controlPath");
		String personPath = params.get("personPath");
		String companyPath = params.get("companyPath");
		String keyPersonPath = params.get("keyPersonPath");
		String outputPath = params.get("output");
		// test or production
		String execMode = params.get("execution.mode");

		Configuration customConfig = params.getConfiguration();

		// Create a Flink execution environment
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		// Read the initial dataset from the CSV file
		DataSet<Tuple2<String, String>> control = env.readCsvFile(controlPath).ignoreFirstLine().types(String.class,
				String.class);

		DataSet<Tuple1<String>> person = env.readCsvFile(personPath).ignoreFirstLine().types(String.class);

		DataSet<org.apache.flink.api.java.tuple.Tuple1<String>> company = env.readCsvFile(companyPath).ignoreFirstLine()
				.types(String.class);

		DataSet<Tuple2<String, String>> keyPerson = env.readCsvFile(keyPersonPath).ignoreFirstLine().types(String.class,
				String.class);

		System.out.println("control size is:" + control.count());
		System.out.println("person size is:" + person.count());
		System.out.println("key person size is:" + keyPerson.count());
		System.out.println("company size is:" + company.count());
		long startTimeMillis = System.currentTimeMillis();

		DataSet<Tuple3<String, String, String>> pscInitApp = keyPerson.join(person).where(1).equalTo(0)
				.projectFirst(0, 0).projectSecond(0);

		DataSet<Tuple4<String, String, String, String>> pscInit = pscInitApp.map(new ReasoningKeyMapper())
				.union(company.map(new ReasoningKeyMapper2()));

		DeltaIteration<Tuple4<String, String, String, String>, Tuple4<String, String, String, String>> pscIteration = pscInit
				.iterateDelta(pscInit, 100, 3);

		DataSet<Tuple4<String, String, String, String>> newDeltaPsc = pscIteration.getWorkset();

		DataSet<Tuple3<String, String, String>> deltaPscApp = control.join(newDeltaPsc).where(0).equalTo(0)
				.projectFirst(1).projectSecond(1, 2);

		DataSet<Tuple4<String, String, String, String>> deltaPsc = deltaPscApp.map(new ReasoningKeyMapper());

		deltaPsc = deltaPsc.coGroup(pscIteration.getSolutionSet()).where(3).equalTo(3)
				.with(new CoGroupDeduplicationFunction());

		DataSet<Tuple4<String, String, String, String>> psc = pscIteration.closeWith(deltaPsc, deltaPsc);

		long countPsc = psc.count();

		System.out.println("The number of psc relationships found is " + countPsc);

		long endTimeMillis = System.currentTimeMillis();
		long executionTimeMillis = endTimeMillis - startTimeMillis;

		System.out.println("Execution time in milliseconds: " + executionTimeMillis);

		if (execMode.equals("test")) {
			psc.print();
		} else {
			psc.writeAsCsv(outputPath);
			// Execute the Flink job
			env.execute("Company Control Flink job");
		}
		return countPsc;
	}

}
