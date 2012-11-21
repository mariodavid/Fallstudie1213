package benchmarks.sp2b;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;

import lupos.datastructures.bindings.Bindings;
import lupos.datastructures.bindings.BindingsMap;
import lupos.datastructures.items.literal.LiteralFactory;
import lupos.datastructures.items.literal.URILiteral;
import lupos.datastructures.items.literal.LiteralFactory.MapType;
import lupos.datastructures.queryresult.QueryResult;
import lupos.engine.evaluators.MemoryIndexQueryEvaluator;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import console.Console;
import console.commands.LoadN3;


public class Sp2bWithMemoryEvaluatorTest extends Sp2bTest {


	MemoryIndexQueryEvaluator memoryEvaluator;
	
	@Before
	public void setUp() {
		super.setUp();
		memoryEvaluator = initMemoryEvaluator();
	}
	


	@Test
	public void testSimpleInsertData() throws Exception {

		String insertQuery = "INSERT DATA {<s> <p> <o>}";
		String selectQuery = "SELECT * WHERE {?s ?p <o>}";

		executeQuery(memoryEvaluator, insertQuery);
		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);


		executeQuery(p2pEvaluator, insertQuery);
		// das p2p netz ist zu langsam, um das ergebnis bereits jetzt zu
		// liefern. also ein wenig warten...
		Thread.sleep(200);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);


		assertEquals(expected, actual);
	}
	
	@Test
	public void testLoadSp2bData() throws Exception {
		
		
		InputStream file = getClass().getClassLoader().getResourceAsStream("benchmarks/sp2b/sp2b_100.n3");
		
		LoadN3 loader = new LoadN3();
		loader.load(p2pEvaluator, file);
				
		
//		// das p2p netz ist zu langsam, um das ergebnis bereits jetzt zu
//		// liefern. also ein wenig warten...
		Thread.sleep(10000);
		
		String selectQuery = readFile("benchmarks/sp2b/queries/q1.sparql");
		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		System.out.println(expected);

//		assertEquals(expected, actual);
	}



	private MemoryIndexQueryEvaluator initMemoryEvaluator(){
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

}
