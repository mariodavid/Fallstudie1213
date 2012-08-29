package logicalOptimization;

import lupos.optimizations.logical.rules.generated.runtime.Rule;
import lupos.optimizations.logical.rules.generated.runtime.RulePackage;
import lupos.optimizations.logical.rules.generated.runtime.RulePackageWithStartNodeMap;

public class P2PRulePackage extends RulePackage {
    public P2PRulePackage() {
        this.rules = new Rule[] {
		        new P2PRuleGlobalJoin()
        };
    }
}
