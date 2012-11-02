package luposdate.logicalOptimization;

import lupos.optimizations.logical.rules.generated.runtime.Rule;
import lupos.optimizations.logical.rules.generated.runtime.RulePackage;
import p2p.P2PAdapter;

// TODO: Auto-generated Javadoc
/**
 * The Class P2PRulePackage.
 */
public class P2PRulePackage extends RulePackage {
	
	/**
	 * Instantiates a new p2 p rule package.
	 */
	public P2PRulePackage(P2PAdapter p2pAdapter) {
		this.rules = new Rule[] { new P2PRule(p2pAdapter) };
	}
}
