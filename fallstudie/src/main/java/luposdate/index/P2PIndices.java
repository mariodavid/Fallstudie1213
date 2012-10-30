package luposdate.index;

import java.io.IOException;
import java.util.Collection;

import p2p.P2PAdapter;

import lupos.datastructures.items.Triple;
import lupos.datastructures.items.literal.URILiteral;
import lupos.engine.operators.index.Indices;
/**
 * ist für die Verwaltung des eigentlichen Index zuständig.
 * 
 * 
 * 
 */
public class P2PIndices extends Indices {

	private P2PAdapter adapter;

	/**
	 * uri literals sind für benannte graphen zuständig bzw. für die default
	 * graphen, dann sind sie für die datensätze
	 * 
	 * hier ist wichtig, dass für jedes P2PIndices ein neues P2PNetzwerk
	 * erstellt wird, wobei jedes P2P Netzwerk dann einer Datenbank entspricht,
	 * die angesprochen werden kann
	 * 
	 * @param uriLiteral
	 */
	public P2PIndices(URILiteral uriLiteral) {
		super();
		setRdfName(uriLiteral);
		adapter.connect();
	}

	public P2PIndices(URILiteral uriLiteral, P2PAdapter adapter) {
		super();
		setRdfName(uriLiteral);
		this.adapter = adapter;
	}

	public Collection<Triple> getAll(String key) {
		return adapter.get(key);
	}

	@Override
	public boolean add(Triple t) {
		return adapter.add(t);
	}

	@Override
	public boolean remove(Triple t) {
		return adapter.remove(t);
	}

	@Override
	public boolean contains(Triple t) {
		return adapter.contains(t);
	}

	@Override
	public void init(DATA_STRUCT ds) {
	}

	@Override
	public void constructCompletely() {
	}

	@Override
	public void writeOutAllModifiedPages() throws IOException {
	}

	@Override
	public int numberOfTriples() {
		return 0;
	}

}
