package luposdate.logicalOptimization;

import lupos.optimizations.logical.rules.generated.runtime.Rule;
import lupos.optimizations.logical.rules.generated.runtime.RulePackage;

// TODO: Auto-generated Javadoc
/**
 * The Class P2PRulePackage.
 */
public class P2PRulePackage extends RulePackage {
	
	/**
	 * Instantiates a new p2 p rule package.
	 */
	public P2PRulePackage() {
		this.rules = new Rule[] { new P2PRuleGlobalJoin() };
	}
}
