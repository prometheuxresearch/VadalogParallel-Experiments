package research.benchmark.vldb.experiment;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import prometheuxresearch.benchmark.vldb.experiment.sparkSQL.closeLink.CloseLinkSparkSQL;
import prometheuxresearch.benchmark.vldb.experiment.sparkSQL.companyControl.CompanyControlSparkSQL;
import prometheuxresearch.benchmark.vldb.experiment.sparkSQL.defaultPropagation.DefaultPropagationSparkSQL;
import prometheuxresearch.benchmark.vldb.experiment.sparkSQL.psc.PscSparkSQL;
import prometheuxresearch.benchmark.vldb.experiment.sparkSQL.strongLink.StrongLinkSparkSQL;

@SpringBootTest(classes = TestSparkSQLJobs.class)
public class TestSparkSQLJobs {

	@Test
	public void testCloseLink() throws Exception {
		// test on input 1
		Map<String, String> testProperties = new HashMap<>();

		testProperties.put("spark.homeDir", "C:\\Users\\user");
		testProperties.put("spark.executor.memory", "2g");
		testProperties.put("spark.master", "local[*]");
		testProperties.put("spark.sql.shuffle.partitions", "4");
		testProperties.put("spark.app.name", "Close links sparkSQL");

		testProperties.put("execution.mode", "test");
		testProperties.put("inputFilePath", "programs_data/test/own_test1.csv");
		long count = CloseLinkSparkSQL.run(testProperties);
		assert (count != 0);
		System.out.println(count);
	}

	@Test
	public void testCloseLink2() throws Exception {
		// test on input 1
		Map<String, String> testProperties = new HashMap<>();

		testProperties.put("spark.homeDir", "C:\\Users\\user");
		testProperties.put("spark.executor.memory", "2g");
		testProperties.put("spark.master", "local[*]");
		testProperties.put("spark.sql.shuffle.partitions", "4");
		testProperties.put("spark.app.name", "Close links sparkSQL");

		testProperties.put("execution.mode", "test");
		testProperties.put("inputFilePath", "programs_data/test/own_test2.csv");
		long count = CloseLinkSparkSQL.run(testProperties);
		assert (count != 0);
		System.out.println(count);
	}

	@Test
	public void testCompanyControl() throws Exception {
		// test on input 1
		Map<String, String> testProperties = new HashMap<>();

		testProperties.put("spark.homeDir", "C:\\Users\\user");
		testProperties.put("spark.executor.memory", "2g");
		testProperties.put("spark.master", "local[*]");
		testProperties.put("spark.sql.shuffle.partitions", "4");
		testProperties.put("spark.app.name", "Company control sparkSQL");

		testProperties.put("execution.mode", "test");
		testProperties.put("inputFilePath", "programs_data/test/own_test2.csv");
		long count = CompanyControlSparkSQL.run(testProperties);
		assert (count != 0);
		System.out.println(count);
	}

	@Test
	public void testCompanyControlBigInput() throws Exception {
		// test on input 1
		Map<String, String> testProperties = new HashMap<>();

		testProperties.put("spark.homeDir", "C:\\Users\\user");
		testProperties.put("spark.executor.memory", "2g");
		testProperties.put("spark.master", "local[*]");
		testProperties.put("spark.sql.shuffle.partitions", "4");
		testProperties.put("spark.app.name", "Company control sparkSQL");

		testProperties.put("execution.mode", "test");
		testProperties.put("inputFilePath", "programs_data/syntheticGraphs/GW500_edges.csv");
		long count = CompanyControlSparkSQL.run(testProperties);
		assert (count != 0);
		System.out.println(count);
	}

	@Test
	public void testDefaultPropagation() throws Exception {
		// test on input 1
		Map<String, String> testProperties = new HashMap<>();
		testProperties.put("spark.homeDir", "C:\\Users\\user");
		testProperties.put("spark.executor.memory", "2g");
		testProperties.put("spark.master", "local[*]");
		testProperties.put("spark.sql.shuffle.partitions", "4");
		testProperties.put("spark.app.name", "Default Propagation sparkSQL");

		testProperties.put("execution.mode", "test");
		testProperties.put("securityPath", "programs_data/test/security-test.csv");
		testProperties.put("loanPath", "programs_data/test/loan-test.csv");
		testProperties.put("creditExposurePath", "programs_data/test/creditExposure-test.csv");
		long count = DefaultPropagationSparkSQL.run(testProperties);
		assert (count != 0);
	}

	@Test
	public void testPSC() throws Exception {
		// test on input 1
		Map<String, String> testProperties = new HashMap<>();
		testProperties.put("spark.homeDir", "C:\\Users\\user");
		testProperties.put("spark.executor.memory", "2g");
		testProperties.put("spark.master", "local[*]");
		testProperties.put("spark.sql.shuffle.partitions", "4");
		testProperties.put("spark.app.name", "Default Propagation sparkSQL");
		
		testProperties.put("execution.mode", "test");
		testProperties.put("controlPath", "programs_data/test/control_test.csv");
		testProperties.put("personPath", "programs_data/test/person_test.csv");
		testProperties.put("companyPath", "programs_data/test/company_test.csv");
		testProperties.put("keyPersonPath", "programs_data/test/keyPerson-test.csv");
		long count = PscSparkSQL.run(testProperties);
		assert (count != 0);
	}

	@Test
	public void testStrongLink() throws Exception {
		// test on input 1
		Map<String, String> testProperties = new HashMap<>();
		testProperties.put("spark.homeDir", "C:\\Users\\user");
		testProperties.put("spark.executor.memory", "2g");
		testProperties.put("spark.master", "local[*]");
		testProperties.put("spark.sql.shuffle.partitions", "4");
		testProperties.put("spark.app.name", "Default Propagation sparkSQL");
		
		testProperties.put("execution.mode", "test");
		testProperties.put("controlPath", "programs_data/test/control_test.csv");
		testProperties.put("personPath", "programs_data/test/person_test.csv");
		testProperties.put("companyPath", "programs_data/test/company_test.csv");
		testProperties.put("keyPersonPath", "programs_data/test/keyPerson-test.csv");
		long count = StrongLinkSparkSQL.run(testProperties);
		assert (count != 0);
	}

}
