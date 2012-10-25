package test;

import index.P2PIndexCollection;
import index.P2PIndexScan;

import java.util.Collection;
import java.util.LinkedList;

import lupos.datastructures.items.Variable;
import lupos.datastructures.items.literal.LazyLiteral;
import lupos.datastructures.items.literal.LiteralFactory;
import lupos.datastructures.items.literal.LiteralFactory.MapType;
import lupos.datastructures.items.literal.URILiteral;
import lupos.engine.operators.application.Output;
import lupos.engine.operators.messages.BoundVariablesMessage;
import lupos.engine.operators.singleinput.Result;
import lupos.engine.operators.tripleoperator.TriplePattern;
import console.Connection;
import console.P2PAdapter;
import evaluators.P2PIndexQueryEvaluator;

public class DeserializerTest {

	public static void main(String[] args) throws Exception {

		Connection connection = new Connection();
		// LuposServer server = new LuposServer();

		connection.connect();
		P2PIndexQueryEvaluator evaluator = new P2PIndexQueryEvaluator();
		P2PAdapter adapter = new P2PAdapter(connection.getPeer(), evaluator);
		// server.start(config);

		evaluator.setP2PAdapter(adapter);

		Collection<URILiteral> defaultGraphs = new LinkedList<URILiteral>();

		LiteralFactory.setType(MapType.NOCODEMAP);
		defaultGraphs.add(LiteralFactory
				.createURILiteralWithoutLazyLiteral("<inlinedata:>"));
		Collection<URILiteral> namedGraphs = new LinkedList<URILiteral>();
		evaluator.prepareInputData(defaultGraphs, namedGraphs);

		try {
			evaluator.compileQuery("INSERT DATA {<s> <p> <o>}");
			evaluator.logicalOptimization();
			evaluator.physicalOptimization();
			evaluator.getResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("hallo");
		// root node
		P2PIndexCollection collection = new P2PIndexCollection(
				evaluator.getDataset());

		P2PIndexScan indexScan = new P2PIndexScan(collection);
		collection.addSucceedingOperator(indexScan);

		LinkedList<TriplePattern> patterns = new LinkedList<TriplePattern>();

		// url ausdenken
		patterns.add(new TriplePattern(new Variable("s"), LazyLiteral
				.getLiteral("<p>"), new Variable("o")));
		indexScan.setTriplePatterns(patterns);

		Result result = new Result();
		result.addApplication(new Output());

		indexScan.addSucceedingOperator(result);

		// erzeugt die Vorgaenger der Collection, wie bei addSucceedingOperator
		// (rekursiv fuer den gesamten Baum)
		collection.setParents();

		// erkennt zyklen im op graphen (vermutlich nicht relevant, evtl. bei
		// spaeteren erweiterungen relevant)
		collection.detectCycles();

		// berechnet an welcher stelle welche variablen gebunden sind und
		// gebunden sein koennen
		collection.sendMessage(new BoundVariablesMessage());

		// uebergabe des root node an den evaluator zur ausfuehrung
		evaluator.setRootNode(collection);

		evaluator.evaluateQuery();
	}

}
