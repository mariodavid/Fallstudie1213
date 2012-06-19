package index;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import lupos.datastructures.items.Triple;
import lupos.datastructures.items.literal.URILiteral;
import lupos.engine.operators.index.Indices;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

/**
 * ist für die Verwaltung des eigentlichen Index zuständig.
 * 
 * weitere Methoden mussen hinzugefügt werden, um weitere Funktionalitäten wie
 * GET .. zu bekommen
 * 
 * @author Mario David, Sebastian Walther
 * 
 */
public class P2PIndices extends Indices {

	private Peer	peer;

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
		connect();
	}

	/**
	 * stellt die Verbindung zum P2P Netzwerk her
	 */
	private void connect() {
		peer = new Peer(Number160.createHash("sad"));
		try {
			peer.listen(4000, 4000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FutureBootstrap fb = peer.bootstrapBroadcast(4000);
		fb.awaitUninterruptibly();

	}

	@Override
	public boolean add(Triple t) {

		String key = t.getPos(0).originalString()
				+ t.getPos(1).originalString();
		try {
			peer.put(Number160.createHash(key),
 new Data(t))
					.awaitUninterruptibly();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean remove(Triple t) {
		// TODO implementieren
		return false;
	}

	@Override
	public boolean contains(Triple t) {
		// TODO implementieren
		return false;
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
		FutureDHT future = peer.getAll(Number160.createHash(key));
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

}
