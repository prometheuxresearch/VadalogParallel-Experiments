package prometheuxresearch.benchmark.vldb.experiment.flink.defaultPropagation;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DeltaIteration;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;

public class DefaultPropagationFlink {

	public static void main(String[] args) throws Exception {
		// Parse command line parameters using ParameterTool
		ParameterTool params = ParameterTool.fromArgs(args);
		run(params);

	}

	public static long run(ParameterTool params) throws Exception {
		// Access parameters using ParameterTool
		String creditExpPath = params.get("creditExposurePath");
		String securityPath = params.get("securityPath");
		String loanPath = params.get("loanPath");
		String outputPath = params.get("output");
		// test or production
		String execMode = params.get("execution.mode");

		Configuration customConfig = params.getConfiguration();

		// Create a Flink execution environment
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		// Read the initial dataset from the CSV file
		DataSet<Tuple2<Integer, Double>> creditExposure = env.readCsvFile(creditExpPath).ignoreFirstLine()
				.types(Integer.class, Double.class);

		DataSet<Tuple3<Integer, Integer, Double>> security = env.readCsvFile(securityPath).ignoreFirstLine()
				.types(Integer.class, Integer.class, Double.class).filter(t -> t.f2 >= 0.5);

		DataSet<Tuple3<Integer, Integer, Double>> loan = env.readCsvFile(loanPath).ignoreFirstLine()
				.types(Integer.class, Integer.class, Double.class).filter(t -> t.f2 >= 0.3);

		System.out.println("credit exposure size is:" + creditExposure.count());
		long startTimeMillis = System.currentTimeMillis();

		DataSet<Tuple4<Integer, Integer, String, String>> initDefault = creditExposure.filter(t -> t.f1 > 0.5)
				.map(new MapFunction1()).map(new ReasoningKeyMapper());

		DeltaIteration<Tuple4<Integer, Integer, String, String>, Tuple4<Integer, Integer, String, String>> defaultIteration = initDefault
				.iterateDelta(initDefault, 100, 3);

		DataSet<Tuple4<Integer, Integer, String, String>> newDeltaDefault = defaultIteration.getWorkset();

		DataSet<Tuple2<Integer, Integer>> app = newDeltaDefault.join(security).where(0).equalTo(0).projectFirst(1)
				.projectSecond(1);

		DataSet<Tuple4<Integer, Integer, String, String>> appDefault1 = app.map(new ReasoningKeyMapper());

		app = newDeltaDefault.join(loan).where(0).equalTo(0).projectFirst(1).projectSecond(1);

		DataSet<Tuple4<Integer, Integer, String, String>> appDefault2 = app.map(new ReasoningKeyMapper());

		DataSet<Tuple4<Integer, Integer, String, String>> deltaDefault = appDefault1.union(appDefault2)
				.coGroup(defaultIteration.getSolutionSet()).where(3).equalTo(3)
				.with(new CoGroupDeduplicationFunction());

		DataSet<Tuple4<Integer, Integer, String, String>> defaults = defaultIteration.closeWith(deltaDefault,
				deltaDefault);

		long countDefault = defaults.count();

		System.out.println("The number of default relationships found is " + countDefault);

		long endTimeMillis = System.currentTimeMillis();
		long executionTimeMillis = endTimeMillis - startTimeMillis;

		System.out.println("Execution time in milliseconds: " + executionTimeMillis);

		if (execMode.equals("test")) {
			defaults.print();
		} else {
			defaults.writeAsCsv(outputPath);
			// Execute the Flink job
			env.execute("Company Control Flink job");
		}
		return countDefault;
	}

}
