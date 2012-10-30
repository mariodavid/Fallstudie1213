package p2p;

import java.util.Collection;

import lupos.datastructures.items.Triple;
import p2p.distribution.DistributionStrategy;

public interface DataStoreAdapter {

	/**
	 * Gibt die Verteilungsstrategie zurück.
	 * 
	 * @return die Verteilungsstrategie
	 */
	public abstract DistributionStrategy getDistributionStrategy();

	/**
	 * Setzt die Verteilungsstrategie.
	 * 
	 * @param distributionStrategy
	 *            die neue Verteilungsstrategie
	 */
	public abstract void setDistributionStrategy(
			DistributionStrategy distributionStrategy);

	public abstract Collection<Triple> get(String key);

	public abstract boolean add(Triple triple);

	public abstract boolean remove(Triple triple);

	public abstract boolean contains(Triple triple);

}