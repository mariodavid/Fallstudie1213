package p2p.distribution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import net.tomp2p.p2p.Peer;

import lupos.datastructures.items.Triple;

/**
 * The Class ThreeKeyDistribution is a distribution strategy where all three
 * elements of the triple is stored within the key. Only one possible
 * combinations are stored h(spo)
 */
public class CombinationDistribution extends AbstractDistributionStrategy {

	protected LinkedList<DistributionStrategy> strategies;

	public CombinationDistribution() {
		strategies = new LinkedList<DistributionStrategy>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * P2P.TOM.TRIPLE.DISTRIBUTION.AbstractDistributionStrategy#distribute(P2P
	 * .TOM.RDFTriple)
	 */
	@Override
	public void distribute(Triple triple) throws IOException {
		for (DistributionStrategy distribution : strategies) {
			distribution.distribute(triple);
		}
	}

	public String[] getDistributeStrings(Triple triple) {
		ArrayList<String> result = new ArrayList<String>();
		for (DistributionStrategy distribution : strategies) {
			for (String item : distribution.getDistributeStrings(triple))
				result.add(item);
		}
		return result.toArray(new String[result.size()]);
	}

	@Override
	public void remove(Triple triple) throws IOException {
		for (DistributionStrategy distribution : strategies) {
			distribution.remove(triple);
		}
	}

	@Override
	public boolean contains(Triple triple) throws IOException {

		for (DistributionStrategy distribution : strategies) {
			if (distribution.contains(triple)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void setPeer(Peer peer) {
		super.setPeer(peer);

		for (DistributionStrategy strategy : strategies) {
			strategy.setPeer(peer);
		}
	}
}
