package distribution;

import java.io.IOException;

import lupos.datastructures.items.Triple;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

/**
 * The Class AbstractDistributionStrategy.
 */
public abstract class AbstractDistributionStrategy implements
		DistributionStrategy {

	/** The peer. */
	private Peer	peer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * P2P.TOM.TRIPLE.DISTRIBUTION.DistributionStrategy#setPeer(net.tomp2p.p2p
	 * .Peer)
	 */
	public void setPeer(Peer peer) {
		this.peer = peer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * P2P.TOM.TRIPLE.DISTRIBUTION.DistributionStrategy#distribute(P2P.TOM.RDFTriple
	 * )
	 */
	public abstract void distribute(Triple triple) throws IOException;

	/**
	 * Adds a given key value pair to network.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected void addToNetwork(String key, Triple value) throws IOException {
		Number160 hash = Number160.createHash(key);
		Data data = new Data(value);

		// peer.add(hash, data); for async mode
		peer.add(hash, data).awaitUninterruptibly();
	}

}
