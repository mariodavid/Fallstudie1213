package p2p.distribution;

import java.io.IOException;

import lupos.datastructures.items.Triple;

/**
 * The Class TwoKeyDistribution is a distribution strategy where two elements of
 * the triple is stored within the key. All combinations are stored h(sp),
 * h(po), h(so)
 * 
 * @author Mario David, Sebastian Walther, Andreas Haller, Thomas Kiencke
 * 
 */
public class TwoKeyDistribution extends AbstractDistributionStrategy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * P2P.TOM.TRIPLE.DISTRIBUTION.AbstractDistributionStrategy#distribute(P2P
	 * .TOM.RDFTriple)
	 */
	@Override
	public void distribute(Triple triple) throws IOException {
		addToNetwork(getSP(triple), triple);
		addToNetwork(getSO(triple), triple);
		addToNetwork(getPO(triple), triple);
	}

	@Override
	public void remove(Triple triple) throws IOException {
		removeFromNetwork(getSP(triple), triple);
		removeFromNetwork(getSO(triple), triple);
		removeFromNetwork(getPO(triple), triple);
	}

	private String getSP(Triple triple) {
		return triple.getSubject().originalString()
				+ triple.getPredicate().originalString();
	}

	private String getSO(Triple triple) {
		return triple.getSubject().originalString()
				+ triple.getObject().originalString();
	}

	private String getPO(Triple triple) {
		return triple.getPredicate().originalString()
				+ triple.getObject().originalString();
	}

	@Override
	public boolean contains(Triple triple) throws IOException {
		return isInNetwork(getSP(triple), triple)
				&& isInNetwork(getSO(triple), triple)
				&& isInNetwork(getPO(triple), triple);
	}

	@Override
	public String toString() {
		return "2: TwoKeyStrategy [h(SP),h(SO),h(PO)]";
	}
}
