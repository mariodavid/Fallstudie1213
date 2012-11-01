package luposdate.index;

import java.io.IOException;
import java.util.Collection;

import p2p.P2PAdapter;

import lupos.datastructures.items.Triple;
import lupos.datastructures.items.literal.URILiteral;
import lupos.engine.operators.index.Indices;

/**
 * In dieser Klasse wird der Index verwaltet. Das beinhaltet im wesentlichen die
 * Basisoperationen Add, Remove, Cointains und Get die auf dem P2P-Netzwerk die
 * entsprechenden Methoden ausführen.
 */
public class P2PIndices extends Indices {

	/** Referenz zum P2P-Adapter. */
	private P2PAdapter adapter;

	/**
	 * Hier ist wichtig, dass für jedes P2PIndices ein neues P2PNetzwerk
	 * erstellt wird, wobei jedes P2P Netzwerk dann einer Datenbank entspricht,
	 * die angesprochen werden kann.
	 * 
	 * @param uriLiteral
	 *            the uri literal
	 */
	public P2PIndices(URILiteral uriLiteral) {
		super();
		setRdfName(uriLiteral);
		adapter.connect();
	}

	/**
	 * Instantiates a new p2 p indices.
	 * 
	 * @param uriLiteral
	 *            the uri literal
	 * @param adapter
	 *            the adapter
	 */
	public P2PIndices(URILiteral uriLiteral, P2PAdapter adapter) {
		super();
		setRdfName(uriLiteral);
		this.adapter = adapter;
	}

	/**
	 * Gets the all.
	 * 
	 * @param key
	 *            the key
	 * @return the all
	 */
	public Collection<Triple> getAll(String key) {
		return adapter.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lupos.engine.operators.index.Indices#add(lupos.datastructures.items.Triple
	 * )
	 */
	@Override
	public boolean add(Triple t) {
		return adapter.add(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lupos.engine.operators.index.Indices#remove(lupos.datastructures.items
	 * .Triple)
	 */
	@Override
	public boolean remove(Triple t) {
		return adapter.remove(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lupos.engine.operators.index.Indices#contains(lupos.datastructures.items
	 * .Triple)
	 */
	@Override
	public boolean contains(Triple t) {
		return adapter.contains(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lupos.engine.operators.index.Indices#init(lupos.engine.operators.index
	 * .Indices.DATA_STRUCT)
	 */
	@Override
	public void init(DATA_STRUCT ds) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lupos.engine.operators.index.Indices#constructCompletely()
	 */
	@Override
	public void constructCompletely() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lupos.engine.operators.index.Indices#writeOutAllModifiedPages()
	 */
	@Override
	public void writeOutAllModifiedPages() throws IOException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lupos.engine.operators.index.Indices#numberOfTriples()
	 */
	@Override
	public int numberOfTriples() {
		return 0;
	}

}
