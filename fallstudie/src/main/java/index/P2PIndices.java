package index;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import lupos.datastructures.items.Triple;
import lupos.datastructures.items.literal.URILiteral;
import lupos.engine.operators.index.Indices;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import console.P2PAdapter;

/**
 * ist f�r die Verwaltung des eigentlichen Index zust�ndig.
 * 
 * weitere Methoden mussen hinzugef�gt werden, um weitere Funktionalit�ten wie
 * GET .. zu bekommen
 * 
 * @author Mario David, Sebastian Walther
 * 
 */
public class P2PIndices extends Indices {


	private P2PAdapter		adapter;
	private int						distributionStrategy;


	/**
	 * uri literals sind f�r benannte graphen zust�ndig bzw. f�r die default
	 * graphen, dann sind sie f�r die datens�tze
	 * 
	 * hier ist wichtig, dass f�r jedes P2PIndices ein neues P2PNetzwerk
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





	@Override
	public boolean add(Triple triple) {

		try {
			adapter.getDistributionStrategy().distribute(triple);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean remove(Triple t) {
		try {
			adapter.getDistributionStrategy().remove(t);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean contains(Triple t) {
		try {
			return adapter.getDistributionStrategy().contains(t);
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public void init(DATA_STRUCT ds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void constructCompletely() {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeOutAllModifiedPages() throws IOException {
		// TODO Auto-generated method stub

	}

	public Collection<Triple> getAll(String key) {
		Collection<Triple> result = new LinkedList<Triple>();
		FutureDHT future = adapter.getPeer().getAll(Number160.createHash(key));
		future.awaitUninterruptibly();

		for (Data r : future.getData().values()) {
			try {
				result.add((Triple) r.getObject());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	public int numberOfTriples() {
		// TODO Auto-generated method stub
		return 0;
	}

}