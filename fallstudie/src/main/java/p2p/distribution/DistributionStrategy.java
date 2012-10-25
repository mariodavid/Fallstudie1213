package p2p.distribution;

import java.io.IOException;

import lupos.datastructures.items.Triple;
import net.tomp2p.p2p.Peer;

/**
 * The Interface for the strategy pattern for the distribution of rdf triples
 * within the p2p network.
 */
public interface DistributionStrategy {

	/**
	 * Sets the peer.
	 * 
	 * @param peer
	 *            the new peer
	 */
	public void setPeer(Peer peer);

	/**
	 * executes the actual distribution.
	 * 
	 * @param triple
	 *            the triple to distribute in the network
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void distribute(Triple triple) throws IOException;
	
	/**
	 * removes on triple (content) based on the choosen strategy
	 * @param triple
	 * @throws IOExecption
	 */
	public void remove(Triple triple) throws IOException;
	
	/**
	 * contains on triple (content) based on the choosen strategy
	 * @param triple
	 * @throws IOExecption
	 */
	public boolean contains(Triple triple) throws IOException;
}
