package benchmarks.sp2b;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.LinkedList;

import lupos.datastructures.items.literal.LiteralFactory;
import lupos.datastructures.items.literal.LiteralFactory.MapType;
import lupos.datastructures.items.literal.URILiteral;
import lupos.datastructures.queryresult.QueryResult;
import lupos.engine.evaluators.MemoryIndexQueryEvaluator;

import org.junit.BeforeClass;
import org.junit.Test;

public class MemoryEvaluatorTest extends Sp2bTest {

	static MemoryIndexQueryEvaluator	memoryEvaluator;

	@BeforeClass
	public static void initAndLoadMemoryEvaluator() {

		memoryEvaluator = initMemoryEvaluator();

		// load dataset to with any sample query (because the lupos api requires
		// it)
		try {
			memoryEvaluator.getResult(full_filepath(default_file),
					"SELECT * WHERE {<s> <p> <o>}");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testQ1() throws Exception {

		String selectQuery = readFile(q1_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ2() throws Exception {

		String selectQuery = readFile(q2_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println(expected);

		System.out.println(expected.toString().length());
		System.out.println(actual);
		System.out.println(actual.toString().length());
		assertEquals(expected, actual);
	}

	@Test
	public void testQ3a() throws Exception {

		String selectQuery = readFile(q3a_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ3b() throws Exception {

		String selectQuery = readFile(q3b_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ3c() throws Exception {

		String selectQuery = readFile(q3c_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ4() throws Exception {

		String selectQuery = readFile(q4_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ5a() throws Exception {

		String selectQuery = readFile(q5a_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ5b() throws Exception {

		String selectQuery = readFile(q5b_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ6() throws Exception {

		String selectQuery = readFile(q6_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ7() throws Exception {

		String selectQuery = readFile(q7_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ8() throws Exception {

		String selectQuery = readFile(q8_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ9() throws Exception {

		String selectQuery = readFile(q9_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ10() throws Exception {

		String selectQuery = readFile(q10_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ11() throws Exception {

		String selectQuery = readFile(q11_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ12a() throws Exception {

		String selectQuery = readFile(q12a_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}
	

	@Test
	public void testQ12b() throws Exception {

		String selectQuery = readFile(q12b_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}
	

	@Test
	public void testQ12c() throws Exception {

		String selectQuery = readFile(q12c_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}


	private static MemoryIndexQueryEvaluator initMemoryEvaluator() {
		try {
			memoryEvaluator = new MemoryIndexQueryEvaluator();
			Collection<URILiteral> defaultGraphs = new LinkedList<URILiteral>();
			defaultGraphs.add(LiteralFactory
					.createURILiteralWithoutLazyLiteral("<inlinedata:>"));

			LiteralFactory.setType(MapType.NOCODEMAP);
			Collection<URILiteral> namedGraphs = new LinkedList<URILiteral>();
			memoryEvaluator.prepareInputData(defaultGraphs, namedGraphs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return memoryEvaluator;
	}

	private static String full_filepath(String filename) {
		return MemoryEvaluatorTest.class.getClassLoader()
				.getResource(filename).getPath();
	}

}