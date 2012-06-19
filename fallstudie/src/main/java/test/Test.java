package test;

import java.util.Collection;
import java.util.LinkedList;

import lupos.datastructures.bindings.Bindings;
import lupos.datastructures.bindings.BindingsMap;
import lupos.datastructures.items.literal.LiteralFactory;
import lupos.datastructures.items.literal.LiteralFactory.MapType;
import lupos.datastructures.items.literal.URILiteral;
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

		int mode = Integer.parseInt(args[0]);
		ev.compileQuery("INSERT DATA {<a> <b> <c>}");
		// ev.compileQuery("INSERT DATA {<2> <b> <2>}");
		// ev.compileQuery("INSERT DATA {<3> <b> <a>}");
		// ev.compileQuery("INSERT DATA {<4> <b> <b>}");
		// ev.compileQuery("INSERT DATA {<5> <b> <c>}");
		// ev.compileQuery("INSERT DATA {<6> <b> <4>}");
		// ev.compileQuery("INSERT DATA {<7> <b> <c>}");
		// ev.compileQuery("INSERT DATA {<8> <b> <c>}");
		// ev.compileQuery("INSERT DATA {<9> <b> <c>}");
		// ev.compileQuery("INSERT DATA {<0> <b> <c>}");
		ev.logicalOptimization();
		ev.physicalOptimization();
		ev.getResult();

		if (mode == 0 || mode == 2) {
			ev.compileQuery("INSERT DATA {<a> <b> <c>}");
			ev.logicalOptimization();
			ev.physicalOptimization();
			ev.getResult();
		}

		if (mode == 1 || mode == 2) {
			ev.compileQuery("SELECT * WHERE {<a> <b> ?c}");
			ev.logicalOptimization();
			ev.physicalOptimization();
			System.out.println(ev.getResult());
		}
		XPref pref = XPref.getInstance("target/classes/");

		// Viewer v = new Viewer(new
		// GraphWrapperBasicOperator(ev.getRootNode()),
		// "Erster Test", true, false);

	}

}
