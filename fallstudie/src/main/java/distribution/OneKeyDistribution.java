package distribution;


import java.io.IOException;

import lupos.datastructures.items.Triple;


/**
 * The Class OneKeyDistribution is a distribution strategy where only one
 * element of the triple is stored within the key. All combinations are stored
 * h(s), h(p), h(o)
 */
public class OneKeyDistribution extends AbstractDistributionStrategy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * P2P.TOM.TRIPLE.DISTRIBUTION.AbstractDistributionStrategy#distribute(P2P
	 * .TOM.RDFTriple)
	 */
	@Override
	public void distribute(Triple triple) throws IOException {

		addToNetwork(triple.getSubject().originalString(), triple);
		addToNetwork(triple.getPredicate().originalString(), triple);
		addToNetwork(triple.getObject().originalString(), triple);
	}

}
