package p2p.distribution.strategies;

import java.io.IOException;
import java.util.LinkedList;

import p2p.distribution.CombinationDistribution;
import p2p.distribution.DistributionFactory;
import p2p.distribution.DistributionStrategy;

import lupos.datastructures.items.Triple;

/**
 * The Class ThreeKeyDistribution is a distribution strategy where all three
 * elements of the triple is stored within the key. Only one possible
 * combinations are stored h(spo)
 */
public class FourKeyDistribution extends CombinationDistribution {


	public FourKeyDistribution() {
		super();
		strategies.add(DistributionFactory.create(1));
		strategies.add(DistributionFactory.create(3));

	}
	
	@Override
	public String toString() {
		return "4: FourKeyStrategy [h(S),h(P),h(O),h(SPO)]";
	}
}
