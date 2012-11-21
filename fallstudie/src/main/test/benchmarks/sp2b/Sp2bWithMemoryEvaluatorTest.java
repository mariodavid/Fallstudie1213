package benchmarks.sp2b;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.LinkedList;
import java.util.NavigableMap;

import lupos.datastructures.items.literal.LiteralFactory;
import lupos.datastructures.items.literal.LiteralFactory.MapType;
import lupos.datastructures.items.literal.URILiteral;
import lupos.datastructures.queryresult.QueryResult;
import lupos.engine.evaluators.MemoryIndexQueryEvaluator;
import net.tomp2p.peers.Number480;
import net.tomp2p.storage.Data;

import org.junit.Before;
import org.junit.Test;

import p2p.P2PAdapter;
import console.commands.LoadN3;

public class Sp2bWithMemoryEvaluatorTest extends Sp2bTest {

	MemoryIndexQueryEvaluator	memoryEvaluator;

	private final String		file_100_triples	= "benchmarks/sp2b/sp2b_100.n3";
	private final String		file_1000_triples	= "benchmarks/sp2b/sp2b_1000.n3";
	private final String		file_10000_triples	= "benchmarks/sp2b/sp2b_10000.n3";
	private final String		file_500_triples	= "benchmarks/sp2b/sp2b_500.n3";
	private final String		file_5000_triples	= "benchmarks/sp2b/sp2b_5000.n3";
	private final String		file_50000_triples	= "benchmarks/sp2b/sp2b_50000.n3";

	private final String		q1_query_filename	= "benchmarks/sp2b/queries/q1.sparql";
	private final String		q2_query_filename	= "benchmarks/sp2b/queries/q2.sparql";
	private final String		q3a_query_filename	= "benchmarks/sp2b/queries/q3a.sparql";
	private final String		q3b_query_filename	= "benchmarks/sp2b/queries/q3b.sparql";
	private final String		q3c_query_filename	= "benchmarks/sp2b/queries/q3c.sparql";
	private final String		q4_query_filename	= "benchmarks/sp2b/queries/q4.sparql";
	private final String		q5a_query_filename	= "benchmarks/sp2b/queries/q5a.sparql";
	private final String		q5b_query_filename	= "benchmarks/sp2b/queries/q5b.sparql";
	private final String		q6_query_filename	= "benchmarks/sp2b/queries/q6.sparql";
	private final String		q7_query_filename	= "benchmarks/sp2b/queries/q7.sparql";
	private final String		q8_query_filename	= "benchmarks/sp2b/queries/q8.sparql";
	private final String		q9_query_filename	= "benchmarks/sp2b/queries/q9.sparql";
	private final String		q10_query_filename	= "benchmarks/sp2b/queries/q10.sparql";
	private final String		q11_query_filename	= "benchmarks/sp2b/queries/q11.sparql";
	private final String		q12a_query_filename	= "benchmarks/sp2b/queries/q12a.sparql";
	private final String		q12b_query_filename	= "benchmarks/sp2b/queries/q12b.sparql";
	private final String		q12c_query_filename	= "benchmarks/sp2b/queries/q12c.sparql";

	@Override
	@Before
	public void setUp() {
		super.setUp();
		memoryEvaluator = initMemoryEvaluator();

	}

	// @Test
	// public void testSimpleInsertData() throws Exception {
	//
	// String insertQuery = "INSERT DATA {<s> <p> <o>}";
	// String selectQuery = "SELECT * WHERE {?s ?p <o>}";
	//
	// executeQuery(memoryEvaluator, insertQuery);
	// QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
	//
	// executeQuery(p2pEvaluator, insertQuery);
	// // das p2p netz ist zu langsam, um das ergebnis bereits jetzt zu
	// // liefern. also ein wenig warten...
	// while (!p2pEvaluator.getP2PAdapter().getDistributionStrategy()
	// .isDistributionReady()) {
	// System.out.println("Tralala");
	// Thread.sleep(100);
	// }
	// QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
	//
	// assertEquals(expected, actual);
	// }

	@Test
	public void testQ1() throws Exception {

		String selectQuery = readFile(q1_query_filename);

		QueryResult expected = memoryEvaluator.getResult(
				full_filepath(file_1000_triples), selectQuery);

		loadInP2PNetwork(file_1000_triples);

		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ2() throws Exception {

		String selectQuery = readFile(q2_query_filename);

		QueryResult expected = memoryEvaluator.getResult(
				full_filepath(file_5000_triples), selectQuery);

		loadInP2PNetwork(file_5000_triples);

		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println(expected);
		System.out.println(actual);
		assertEquals(expected, actual);
	}


	private void loadInP2PNetwork(String file) throws InterruptedException {
		LoadN3 loader = new LoadN3();
		int loadedTriples = 7 * loader.load(p2pEvaluator, file);
		NavigableMap<Number480, Data> p2pMap = ((P2PAdapter) p2pEvaluator
				.getP2PAdapter()).peer.getPeerBean().getStorage().map();

		while (loadedTriples != p2pMap.size()) {
			System.out.println(p2pMap.size());
			Thread.sleep(500);
		}
	}

	// @Test
	// public void testQ2() throws Exception {
	//
	// String selectQuery = readFile(q2_query_filename);
	//
	// LoadN3 loader = new LoadN3();
	// loader.load(p2pEvaluator, file_1000_triples);
	//
	// QueryResult expected = memoryEvaluator.getResult(
	// full_filepath(file_1000_triples), selectQuery);
	//
	// Thread.sleep(10000);
	//
	// QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
	//
	// assertEquals(expected, actual);
	// }

	private MemoryIndexQueryEvaluator initMemoryEvaluator() {
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

	private String full_filepath(String filename) {
		return getClass().getClassLoader().getResource(filename).getPath();
	}

}
