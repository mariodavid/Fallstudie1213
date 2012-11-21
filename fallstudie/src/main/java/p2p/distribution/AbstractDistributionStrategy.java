package p2p.distribution;

import java.io.IOException;

import lupos.datastructures.items.Triple;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

/**
 * The Class AbstractDistributionStrategy.
 */
public abstract class AbstractDistributionStrategy implements
		DistributionStrategy {

	/** The peer. */
	protected Peer peer;

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

	public abstract void remove(Triple triple) throws IOException;

	public abstract boolean contains(Triple triple) throws IOException;

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
		Number160 contentKey = Number160.createHash(value.toN3String());

		// peer.put(hash).setData(contentKey, new Data(value)).start()
		// .awaitUninterruptibly();
		// asynchron
		peer.put(hash).setData(contentKey, new Data(value)).start()
				.addListener(new BaseFutureAdapter<FutureDHT>() {
					public void operationComplete(FutureDHT future)
							throws Exception {
						future.shutdown();
					}
				});
	}

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
	protected void removeFromNetwork(String key, Triple triple)
			throws IOException {
		Number160 hash = Number160.createHash(key);
		Number160 contentKey = Number160.createHash(triple.toN3String());

		peer.remove(hash).setContentKey(contentKey).start()
				.awaitUninterruptibly();
	}

	/**
	 * Check if a key is in the Network.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected boolean isInNetwork(String key, Triple triple) throws IOException {

		Number160 contentKey = Number160.createHash(triple.toN3String());

		FutureDHT future =

		peer.get(Number160.createHash(key)).setContentKey(contentKey).start();

		future.awaitUninterruptibly();

		return future.isSuccess();

	}

}
