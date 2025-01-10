package research.benchmark.vldb.experiment;

import java.util.HashMap;
import java.util.Map;

import org.apache.flink.api.java.utils.ParameterTool;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import prometheuxresearch.benchmark.vldb.experiment.javastream.closeLink.CloseLinkJavaStream;
import prometheuxresearch.benchmark.vldb.experiment.javastream.defaultPropagation.DefaultPropagationJavaStream;

@SpringBootTest(classes = TestParallelJavaStream.class)
public class TestParallelJavaStream {

	@Test
	public void testCloseLink() throws Exception {
		// test on input 1
		Map<String, String> testProperties = new HashMap<>();
		testProperties.put("parallelism", "4");
		testProperties.put("input", "programs_data/test/own_test1.csv");
		ParameterTool params = ParameterTool.fromMap(testProperties);
		long count = CloseLinkJavaStream.run(params);
		assert (count != 0);
	}
	
	@Test
	public void testDefaultPropagation() throws Exception {
		// test on input 1
		Map<String, String> testProperties = new HashMap<>();
		testProperties.put("parallelism", "4");
		testProperties.put("security_path", "programs_data/test/security-test.csv");
		testProperties.put("loan_path", "programs_data/test/loan-test.csv");
		testProperties.put("entity_path", "programs_data/test/creditExposure-test.csv");
		ParameterTool params = ParameterTool.fromMap(testProperties);
		int count = DefaultPropagationJavaStream.run(params);
		assert (count != 0);
	}
	
	
	@Test
	public void testDefaultPropagationBigInput() throws Exception {
		// test on input 1
		Map<String, String> testProperties = new HashMap<>();
		testProperties.put("parallelism", "4");
		testProperties.put("security_path", "programs_data/syntheticGraphs/GW500_edges.csv");
		testProperties.put("loan_path", "programs_data/test/loan-test.csv");
		testProperties.put("entity_path", "programs_data/syntheticGraphs/GW500_nodes.csv");
		ParameterTool params = ParameterTool.fromMap(testProperties);
		int count = DefaultPropagationJavaStream.run(params);
		assert (count != 0);
	}

}
