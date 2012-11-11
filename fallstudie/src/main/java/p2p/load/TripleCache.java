package p2p.load;

import java.util.HashMap;
import java.util.Map.Entry;

import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import p2p.P2PAdapter;
import lupos.datastructures.items.Triple;

public class TripleCache {
	final static int CACHE_SIZE = 500;
	P2PAdapter adapter;
	HashMap<PeerAddress, PeerCacheEntry> peerAddressMap = new HashMap<PeerAddress, PeerCacheEntry>();

	public TripleCache(P2PAdapter adapter) {
		this.adapter = adapter;
	}

	public void add(Triple triple) {
		for (String key : this.adapter.getDistributionStrategy()
				.getDistributeStrings(triple)) {

			// location Key berechnen
			Number160 locationKey = Number160.createHash(key);

			// Wenn location bereits bekannt wird Triple dor hinzugefügt
			if (!addIfKeyIsInRange(locationKey, triple)) {
				// zuständigen Peer suchen
				PeerAddress pa = this.adapter
						.getPeerAddressFromLocationKey(locationKey);
				PeerCacheEntry pr = peerAddressMap.get(pa);
				// Triple in den Cache packen
				peerAddressMap.put(
						pa,
						pr == null ? new PeerCacheEntry(locationKey,
								locationKey) : pr.addElementAndUpdateRange(
								triple, locationKey));
			}
		}
		flush(false);
	}

	private boolean addIfKeyIsInRange(Number160 locationKey, Triple triple) {
		for (Entry<PeerAddress, PeerCacheEntry> item : peerAddressMap
				.entrySet()) {
			if (item.getValue().isInRange(locationKey)) {
				item.getValue().addElement(triple);
				return true;
			}
		}
		return false;
	}

	public void flush(boolean forceFlush) {
		for (Entry<PeerAddress, PeerCacheEntry> item : peerAddressMap
				.entrySet()) {
			if (item.getValue().size() >= CACHE_SIZE || forceFlush) {
				adapter.sendMessage(item.getValue().getLocationKey(),
						item.getValue());
				System.out.println(item.getValue().getTripleList().size()
						+ " Triple  wurden an "
						+ item.getKey().getInetAddress().getHostAddress()
						+ " geschickt!");
				item.getValue().reset();
			}
		}
	}
}
