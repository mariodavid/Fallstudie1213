package p2p;

import java.util.Collection;

import lupos.datastructures.items.Triple;
import p2p.distribution.DistributionStrategy;

/**
 * Der DataStoreAdapter stellt die Schnittstelle zwischen Lupos und einen
 * darunterliegenden Speicher dar. Dieser kann beispielweise ein P2P Netzwerk
 * sein.
 */
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

	/**
	 * Gibt alle Triple zurück die unter einem Key gespeichert sind.
	 * 
	 * @param key
	 *            den Key
	 * @return eine Collection von Triple
	 */
	public abstract Collection<Triple> get(String key);

	/**
	 * Fügt ein Triple hinzu.
	 * 
	 * @param triple
	 *            Triple
	 * @return true, wenn erfolgreich hinzugefügt.
	 */
	public abstract boolean add(Triple triple);

	/**
	 * Löscht ein Triple.
	 * 
	 * @param triple
	 *            Triple
	 * @return true, wenn erfolgreich entfernt
	 */
	public abstract boolean remove(Triple triple);

	/**
	 * Gibt True Zurück wenn ein Triple vorhanden ist.
	 * 
	 * @param triple
	 *            Triple
	 * @return true, wenn Triple vorhanden
	 */
	public abstract boolean contains(Triple triple);

}