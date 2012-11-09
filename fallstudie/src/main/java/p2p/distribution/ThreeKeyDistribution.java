package p2p.distribution;

import java.io.IOException;

import lupos.datastructures.items.Triple;

/**
 * The Class ThreeKeyDistribution is a distribution strategy where all three
 * elements of the triple is stored within the key. Only one possible
 * combinations are stored h(spo)
 */
public class ThreeKeyDistribution extends AbstractDistributionStrategy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * P2P.TOM.TRIPLE.DISTRIBUTION.AbstractDistributionStrategy#distribute(P2P
	 * .TOM.RDFTriple)
	 */
	@Override
	public void distribute(Triple triple) throws IOException {
		addToNetwork(getSPO(triple), triple);
	}

	public String[] getDistributeStrings(Triple triple) {
		String[] result = { getSPO(triple) };
		return result;
	}

	private String getSPO(Triple triple) {
		return triple.getPos(0).originalString()
				+ triple.getPredicate().originalString()
				+ triple.getObject().originalString();
	}

	@Override
	public void remove(Triple triple) throws IOException {
		removeFromNetwork(getSPO(triple), triple);
	}

	@Override
	public boolean contains(Triple triple) throws IOException {
		return isInNetwork(getSPO(triple), triple);
	}

	@Override
	public String toString() {
		return "3: ThreeKeyStrategy [h(SPO)]";
	}
}
