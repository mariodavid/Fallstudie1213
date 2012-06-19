package index;

import java.util.Collection;

import lupos.datastructures.bindings.Bindings;
import lupos.datastructures.items.Item;
import lupos.datastructures.items.Triple;
import lupos.datastructures.items.Variable;
import lupos.datastructures.items.literal.Literal;
import lupos.datastructures.queryresult.QueryResult;
import lupos.engine.operators.OperatorIDTuple;
import lupos.engine.operators.index.BasicIndex;
import lupos.engine.operators.index.IndexCollection;
import lupos.engine.operators.index.Indices;
import lupos.engine.operators.tripleoperator.TriplePattern;

/**
 * ist für die ein oder mehrere Tripelmuster auszuführen
 * 
 * @author Mario David, Sebastian Walther
 * 
 */
public class P2PIndexScan extends BasicIndex {

	public P2PIndexScan(IndexCollection indexCollection) {
		super(indexCollection);
		// TODO Auto-generated constructor stub
	}

	public P2PIndexScan(OperatorIDTuple succeedingOperator,
			Collection<TriplePattern> triplePattern, Item data,
			IndexCollection indexCollection) {
		super(succeedingOperator, triplePattern, data, indexCollection);
	}

	/**
	 * tripelpattern muss ausgewertet werden. Dazu muss das P2P Netzwerk über
	 * die "indices" variable angesprochenw werden.
	 * 
	 * Als Eingabe kommt: "?s rdf:type ?class" z.b
	 * 
	 * Als Ergebnis soll dann zurückgegeben werden: "?s=<Hallo>, ?class=<Test>"
	 * "?s=<Hallo2>, ?class=<Test2>" "?s=<Hallo>, ?class=<Test3>" ...
	 * 
	 * bindings ist normalerweise leer, außer wenn die graph operation in der
	 * SPARQL Anfrage vwerden wird. dabei werden dann bei den bindings an
	 * gegebene Variablen direkt werte gebunden. Dann muss im tripelmuster
	 * nachgeschaut werden, ob diese variable existiert und ggf. ersetzt werden
	 * durch den übergebenen wert. Die Variablen die übergeben werden müssen
	 * dann im Ergebnis auch auftauchen
	 */
	@Override
	public QueryResult join(Indices indices, Bindings bindings) {

		P2PIndices p2pIndices = (P2PIndices) indices;
		QueryResult result = QueryResult.createInstance();
		for (TriplePattern pattern : this.triplePatterns) {

			Literal s = (Literal) pattern.getItems()[0];
			Literal p = (Literal) pattern.getItems()[1];
			Variable o = (Variable) pattern.getItems()[2];

			String key = s.originalString() + p.originalString();
			for (Triple t : p2pIndices.getAll(key)) {
				Bindings b = Bindings.createNewInstance();
				b.add(o, t.getPos(2));
				result.add(b);
			}
			//
			// if (!s.isVariable()) {
			// Literal l = (Literal) s;
			// l.originalString(); // String Repräsentation eines item
			//
			// } else {
			// Bindings b = Bindings.createNewInstance();
			// Variable v = (Variable) s;
			// Literal lit = getLiteralFromP2PNetwork();
			// b.add(v, lit);
			//
			// result.add(b);
			//
			// }

		}

		return result;
	}

}
