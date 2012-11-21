package p2p.distribution;

import p2p.distribution.strategies.*;

/**
 * This class creates Strategies for the distribution of RDF Triple within the
 * network. The pattern which is used is the Factory Method
 * (http://en.wikipedia.org/wiki/Factory_method)
 */
public class DistributionFactory {

	/**
	 * creates a DistributionStrategy based on the given strategy
	 * 
	 * @param strategy
	 *            1: A distribution strategy with only one element of the triple
	 *            as the hash h(s), h(p), h(o) 2: A distribution strategy with
	 *            two elements of the triple as the hash h(sp), h(po), h(so) 3:
	 *            A distribution strategy with all three elements of the triple
	 *            as the hash h(spo)
	 * @return the corresponding distribution strategy
	 */
	public static DistributionStrategy create(int strategy) {
		switch (strategy) {
			case 1:
				return new OneKeyDistribution();
			case 2:
				return new TwoKeyDistribution();
			case 3:
				return new ThreeKeyDistribution();
			case 4:
				return new FourKeyDistribution();
			case 5:
				return new FiveKeyDistribution();
			case 6:
				return new SixKeyDistribution();
			case 7:
				return new SevenKeyDistribution();
			default:
				return new OneKeyDistribution();
		}
	}
}
