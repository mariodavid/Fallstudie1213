package p2p.load;

import java.io.Serializable;
import java.util.ArrayList;

import lupos.datastructures.items.Triple;

import net.tomp2p.peers.Number160;

public class PeerCacheEntry implements Serializable {
	Number160 min;
	Number160 max;
	ArrayList<Triple> cachedElements = new ArrayList<Triple>();

	public PeerCacheEntry(Number160 min, Number160 max) {
		this.min = min;
		this.max = max;
	}

	public PeerCacheEntry addElementAndUpdateRange(Triple triple,
			Number160 newValue) {
		addElement(triple);
		if (newValue.compareTo(min) < 0)
			this.min = newValue;
		else if (newValue.compareTo(max) > 0)
			this.max = newValue;
		return this;
	}
	
	public void addElement(Triple triple) {
		this.cachedElements.add(triple);
	}

	public boolean isInRange(Number160 locationKey) {
		return (locationKey.compareTo(max) < 0)
				&& (locationKey.compareTo(min) > 0) ? true : false;
	}

	public void reset() {
		this.cachedElements = new ArrayList<Triple>();
	}

	public Number160 getLocationKey() {
		return min;
	}
	
	public ArrayList<Triple> getTripleList() {
		return this.cachedElements;
	}
	
	public int size() {
		return this.cachedElements.size();
	}
}
