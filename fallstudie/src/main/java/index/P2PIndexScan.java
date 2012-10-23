package index;

import java.util.Collection;

import lupos.datastructures.bindings.Bindings;
import lupos.datastructures.items.Item;
import lupos.datastructures.items.Triple;
import lupos.datastructures.items.Variable;
import lupos.datastructures.items.literal.Literal;
import lupos.datastructures.items.literal.string.StringURILiteral;
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
	 * Bindings ist normalerweise leer, außer wenn die graph operation in der
	 * SPARQL Anfrage verwendet wird. Dabei werden dann bei den bindings an
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

			Item[] items = pattern.getItems();

			String key = generateKey(items);

			System.out.println("Hash: h(" + key + ")");

			for (Triple triple : p2pIndices.getAll(key)) {
				Bindings b = addVariablesToBindings(items, triple);
				result.add(b);
			}
		}

		return result;
	}

	private Bindings addVariablesToBindings(Item[] items, Triple t) {
		Bindings b = Bindings.createNewInstance();
		for (int i = 0; i < items.length; i++) {
			Item item = items[i];
			if (item.getClass() == Variable.class) {
				Variable v = (Variable) item;
				b.add(v, t.getPos(i));

			}
		}
		return b;
	}

	private String generateKey(Item[] items) {
		StringBuilder key = new StringBuilder();

		for (Item item : items) {
			if (item.getClass() == StringURILiteral.class) {
				Literal lit = (Literal) item;
				key.append(lit.originalString());
			}
		}
		return key.toString();
	}

}
