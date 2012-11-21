package benchmarks.sp2b;

import java.io.IOException;
import java.io.InputStream;

import lupos.datastructures.queryresult.QueryResult;
import lupos.engine.evaluators.BasicIndexQueryEvaluator;
import luposdate.LuposServer;
import luposdate.evaluators.P2PIndexQueryEvaluator;

import org.junit.After;
import org.junit.Before;

import p2p.P2PAdapter;
import p2p.P2PConnection;

public class Sp2bTest {

	protected P2PIndexQueryEvaluator p2pEvaluator;
	private P2PAdapter p2pAdapter;

	public Sp2bTest() {
		super();
	}

	@Before
	public void setUp() {
		p2pEvaluator = initP2PQueryEvaluator();
	}
	
	@After
	public void tearDown() {
		p2pAdapter.peer.shutdown();
	}

	private P2PIndexQueryEvaluator initP2PQueryEvaluator() {

		try {
			P2PConnection connection = new P2PConnection();
			LuposServer server = new LuposServer();
			connection.connect();
			p2pAdapter = new P2PAdapter(connection.getPeer());
			server.start(p2pAdapter);
			p2pAdapter.setEvaluator(server.getEvaluator());

			return server.getEvaluator();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	protected QueryResult executeQuery(BasicIndexQueryEvaluator evaluator,
			String query) throws Exception {
		evaluator.compileQuery(query);
		evaluator.logicalOptimization();
		evaluator.physicalOptimization();
		return evaluator.getResult();
	}

	protected String readFile(String file) {
		InputStream is = getClass().getClassLoader().getResourceAsStream(file);
		String selectQuery = convertStreamToString(is);
		return selectQuery;
	}

	private String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}

}