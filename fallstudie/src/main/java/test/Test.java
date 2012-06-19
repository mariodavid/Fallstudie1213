package test;

import java.util.Collection;
import java.util.LinkedList;

import lupos.datastructures.bindings.Bindings;
import lupos.datastructures.bindings.BindingsMap;
import lupos.datastructures.items.literal.LiteralFactory;
import lupos.datastructures.items.literal.LiteralFactory.MapType;
import lupos.datastructures.items.literal.URILiteral;
import lupos.gui.operatorgraph.graphwrapper.GraphWrapperBasicOperator;
import lupos.gui.operatorgraph.viewer.Viewer;
import xpref.XPref;
import evaluators.P2PIndexQueryEvaluator;

public class Test {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// Literale werden als String gespeichert. Möglich wären auch Maps, bei
		// denen die Strings als Integer representiert werden, Dies funktioniert
		// allerdings nicht in einem verteilten System
		LiteralFactory.setType(MapType.NOCODEMAP);

		// ebenso wegen dem VS
		Bindings.instanceClass = BindingsMap.class;

		P2PIndexQueryEvaluator ev = new P2PIndexQueryEvaluator();

		Collection<URILiteral> defaultGraphs = new LinkedList<URILiteral>();
		defaultGraphs.add(LiteralFactory
				.createURILiteralWithoutLazyLiteral("<inlinedata:>"));
		Collection<URILiteral> namedGraphs = new LinkedList<URILiteral>();
		ev.prepareInputData(defaultGraphs, namedGraphs);

		ev.compileQuery("INSERT DATA {<a> <b> <c>}");
		ev.logicalOptimization();
		ev.physicalOptimization();
		ev.getResult();

		ev.compileQuery("SELECT * WHERE {<a> <b> ?c}");
		ev.logicalOptimization();
		ev.physicalOptimization();
		System.out.println(ev.getResult());

		XPref pref = XPref.getInstance("target/classes/");

		Viewer v = new Viewer(new GraphWrapperBasicOperator(ev.getRootNode()),
				"Erster Test", true, false);

	}

}
