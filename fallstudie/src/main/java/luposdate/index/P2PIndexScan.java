package luposdate.index;

import java.util.Collection;

import lupos.datastructures.bindings.Bindings;
import lupos.datastructures.items.Item;
import lupos.datastructures.items.Triple;
import lupos.datastructures.items.Variable;
import lupos.datastructures.items.literal.Literal;
import lupos.datastructures.items.literal.string.StringURILiteral;
import lupos.datastructures.queryresult.QueryResult;
import lupos.engine.operators.OperatorIDTuple;
import lupos.engine.operators.index.BasicIndexScan;
import lupos.engine.operators.index.Indices;
import lupos.engine.operators.index.Root;
import lupos.engine.operators.tripleoperator.TriplePattern;

// TODO: Klasse erklären
/**
 * ist für die ein oder mehrere Tripelmuster auszuführen.
 */
public class P2PIndexScan extends BasicIndexScan {

	/**
	 * Instantiates a new p2p index scan.
	 * 
	 * @param indexCollection
	 *            the index collection
	 */
	public P2PIndexScan(Root indexCollection) {
		super(indexCollection);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new p2p index scan.
	 * 
	 * @param succeedingOperator
	 *            the succeeding operator
	 * @param triplePattern
	 *            the triple pattern
	 * @param data
	 *            the data
	 * @param indexCollection
	 *            the index collection
	 */
	public P2PIndexScan(OperatorIDTuple succeedingOperator,
			Collection<TriplePattern> triplePattern, Item data,
			Root indexCollection) {
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
	 * 
	 * @param indices
	 *            the indices
	 * @param bindings
	 *            the bindings
	 * @return the query result
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
				if (b != null) {
					result.add(b);
				}
			}
		}

		return result;
	}

	/**
	 * Adds the variables to bindings.
	 * 
	 * @param items
	 *            the items
	 * @param t
	 *            the t
	 * @return the bindings
	 */
	private Bindings addVariablesToBindings(Item[] items, Triple t) {
		Bindings b = Bindings.createNewInstance();
		for (int i = 0; i < items.length; i++) {
			Item item = items[i];
			if (item.getClass() == Variable.class) {
				Variable v = (Variable) item;
				b.add(v, t.getPos(i));

			} else {
				if (t.getPos(i)
						.compareToNotNecessarilySPARQLSpecificationConform(
								(Literal) item) != 0) {
					return null;
				}
			}
		}
		return b;
	}

	/**
	 * Generate key.
	 * 
	 * @param items
	 *            the items
	 * @return the string
	 */
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
