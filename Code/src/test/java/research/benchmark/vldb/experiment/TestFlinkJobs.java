package research.benchmark.vldb.experiment;

import java.util.HashMap;
import java.util.Map;

import org.apache.flink.api.java.utils.ParameterTool;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import prometheuxresearch.benchmark.vldb.experiment.flink.closeLink.CloseLinkFlink;
import prometheuxresearch.benchmark.vldb.experiment.flink.companyControl.CompanyControlFlink;
import prometheuxresearch.benchmark.vldb.experiment.flink.defaultPropagation.DefaultPropagationFlink;
import prometheuxresearch.benchmark.vldb.experiment.flink.psc.PscFlink;
import prometheuxresearch.benchmark.vldb.experiment.flink.strongLink.StrongLinkFlink;

@SpringBootTest(classes = TestFlinkJobs.class)
public class TestFlinkJobs {
	
	@Test
	public void test() throws Exception {
		// test on input 1
		Map<String, String> testProperties = new HashMap<>();
		testProperties.put("parallelism.default", "10");
		testProperties.put("execution.mode", "test");
		testProperties.put("input", "programs_data/test/own_test1.csv");
		ParameterTool params = ParameterTool.fromMap(testProperties);
		assert(params.getInt("parallelism.default") == 10);
		
		String[] args = {"--parallelism.default", "10"};
		params = ParameterTool.fromArgs(args);
		assert(params.getInt("parallelism.default") == 10);
	}

	@Test
	public void testCloseLink() throws Exception {
		// test on input 1
		Map<String, String> testProperties = new HashMap<>();
		testProperties.put("parallelism.default", "1");
		testProperties.put("execution.mode", "test");
		testProperties.put("input", "programs_data/test/own_test1.csv");
		ParameterTool params = ParameterTool.fromMap(testProperties);
		long count = CloseLinkFlink.run(params);
		assert (count != 0);

		// test on input 2
		testProperties = new HashMap<>();
		testProperties.put("parallelism.default", "1");
		testProperties.put("execution.mode", "test");
		testProperties.put("input", "programs_data/test/own_test2.csv");
		params = ParameterTool.fromMap(testProperties);
		count = CloseLinkFlink.run(params);
		assert (count != 0);
	}

	@Test
	public void testCompanyControl() throws Exception {
		// test on input 1
		Map<String, String> testProperties = new HashMap<>();
		testProperties.put("parallelism.default", "1");
		testProperties.put("execution.mode", "test");
		testProperties.put("input", "programs_data/test/own_test2.csv");
		ParameterTool params = ParameterTool.fromMap(testProperties);
		long count = CompanyControlFlink.run(params);
		assert (count != 0);
		System.out.println(count);
	}

	@Test
	public void testDefaultPropagation() throws Exception {
		// test on input 1
		Map<String, String> testProperties = new HashMap<>();
		testProperties.put("parallelism.default", "1");
		testProperties.put("execution.mode", "test");
		testProperties.put("securityPath", "programs_data/test/security-test.csv");
		testProperties.put("loanPath", "programs_data/test/loan-test.csv");
		testProperties.put("creditExposurePath", "programs_data/test/creditExposure-test.csv");
		ParameterTool params = ParameterTool.fromMap(testProperties);
		long count = DefaultPropagationFlink.run(params);
		assert (count != 0);
	}
	
	@Test
	public void testPSC() throws Exception {
		// test on input 1
		Map<String, String> testProperties = new HashMap<>();
		testProperties.put("parallelism.default", "1");
		testProperties.put("execution.mode", "test");
		testProperties.put("controlPath", "programs_data/test/control_test.csv");
		testProperties.put("personPath", "programs_data/test/person_test.csv");
		testProperties.put("companyPath", "programs_data/test/company_test.csv");
		testProperties.put("keyPersonPath", "programs_data/test/keyPerson-test.csv");
		ParameterTool params = ParameterTool.fromMap(testProperties);
		long count = PscFlink.run(params);
		assert (count != 0);
	}
	
	@Test
	public void testStrongLink() throws Exception {
		// test on input 1
		Map<String, String> testProperties = new HashMap<>();
		testProperties.put("parallelism.default", "1");
		testProperties.put("execution.mode", "test");
		testProperties.put("controlPath", "programs_data/test/control_test.csv");
		testProperties.put("personPath", "programs_data/test/person_test.csv");
		testProperties.put("companyPath", "programs_data/test/company_test.csv");
		testProperties.put("keyPersonPath", "programs_data/test/keyPerson-test.csv");
		ParameterTool params = ParameterTool.fromMap(testProperties);
		long count = StrongLinkFlink.run(params);
		assert (count != 0);
	}
}
