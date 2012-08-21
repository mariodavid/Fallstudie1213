package distribution;

import java.io.IOException;

import lupos.datastructures.items.Triple;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.config.ConfigurationStore;
import net.tomp2p.p2p.config.Configurations;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

/**
 * The Class AbstractDistributionStrategy.
 */
public abstract class AbstractDistributionStrategy implements
		DistributionStrategy {

	/** The peer. */
	private Peer peer;

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
		Data data = new Data(value);

		/*
		 * Jeder Eintrag kriegt eine eindeutige ID, damit ein einzelner Wert zu
		 * einen bestimmten Hash rausgeloescht werden kann.
		 */
		ConfigurationStore cs = Configurations.defaultStoreConfiguration();
		cs.setContentKey(contentKey);
		peer.put(hash, data, cs).awaitUninterruptibly();
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
		System.out.println(triple.toN3String());

		Number160 contentKey = Number160.createHash(triple.toN3String());

		peer.remove(hash, contentKey);
		// peer.removeAll(hash).awaitUninterruptibly();
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
		FutureDHT future = peer.getAll(Number160.createHash(key));
		future.awaitUninterruptibly();

		for (Data value : future.getData().values()) {
			try {
				if (value.getObject().getClass() == Triple.class) {
					Triple curTriple = (Triple) value.getObject();
					if (curTriple.equals(triple))
						return true;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

}
