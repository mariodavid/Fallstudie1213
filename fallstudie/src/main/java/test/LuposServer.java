package test;

import java.util.Collection;
import java.util.LinkedList;

import lupos.datastructures.bindings.Bindings;
import lupos.datastructures.bindings.BindingsMap;
import lupos.datastructures.items.literal.LiteralFactory;
import lupos.datastructures.items.literal.LiteralFactory.MapType;
import lupos.datastructures.items.literal.URILiteral;
import console.P2PAdapter;
import evaluators.P2PIndexQueryEvaluator;

public class LuposServer {


	private P2PIndexQueryEvaluator	evaluator;

	public P2PIndexQueryEvaluator getEvaluator() {
		return evaluator;
	}


	public void start(P2PAdapter config) {

		try {
			Bindings.instanceClass = BindingsMap.class;

			evaluator = new P2PIndexQueryEvaluator();

			evaluator.setP2PAdapter(config);
			Collection<URILiteral> defaultGraphs = new LinkedList<URILiteral>();

			LiteralFactory.setType(MapType.NOCODEMAP);
			defaultGraphs.add(LiteralFactory
					.createURILiteralWithoutLazyLiteral("<inlinedata:>"));
			Collection<URILiteral> namedGraphs = new LinkedList<URILiteral>();
			evaluator.prepareInputData(defaultGraphs, namedGraphs);

		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


}
