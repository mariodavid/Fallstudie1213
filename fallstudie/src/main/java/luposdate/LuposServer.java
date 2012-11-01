package luposdate;

import java.util.Collection;
import java.util.LinkedList;

import lupos.datastructures.bindings.Bindings;
import lupos.datastructures.bindings.BindingsMap;
import lupos.datastructures.items.literal.LiteralFactory;
import lupos.datastructures.items.literal.LiteralFactory.MapType;
import lupos.datastructures.items.literal.URILiteral;
import luposdate.evaluators.P2PIndexQueryEvaluator;
import p2p.P2PAdapter;

/**
 * In dieser Klasse wird eine Lupos Instanz gestartet.
 */
public class LuposServer {

	/** Evaluator. */
	private P2PIndexQueryEvaluator evaluator;

	/**
	 * Gibt den Evaluator zurück.
	 * 
	 * @return evaluator
	 */
	public P2PIndexQueryEvaluator getEvaluator() {
		return evaluator;
	}

	/**
	 * An dieser Stellt wird die Lupos Instanz gestartet. Zusätzlich wird die
	 * Verbindung von dem Evauator zum P2PAdapter hergestellt.
	 * 
	 * @param p2pAdapter
	 *            den p2pAdapter
	 */
	public void start(P2PAdapter p2pAdapter) {

		System.out.println("starting up lupos instance...");
		try {
			Bindings.instanceClass = BindingsMap.class;

			evaluator = new P2PIndexQueryEvaluator();

			evaluator.setP2PAdapter(p2pAdapter);
			Collection<URILiteral> defaultGraphs = new LinkedList<URILiteral>();

			LiteralFactory.setType(MapType.NOCODEMAP);
			defaultGraphs.add(LiteralFactory
					.createURILiteralWithoutLazyLiteral("<inlinedata:>"));
			Collection<URILiteral> namedGraphs = new LinkedList<URILiteral>();
			evaluator.prepareInputData(defaultGraphs, namedGraphs);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
