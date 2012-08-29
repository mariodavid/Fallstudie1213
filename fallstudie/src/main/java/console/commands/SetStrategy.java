package console.commands;

import java.util.Scanner;

import net.tomp2p.p2p.Peer;
import distribution.DistributionFactory;
import evaluators.P2PIndexQueryEvaluator;

public class SetStrategy implements Command {

	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		int newStrategyNumber = scanner.nextInt();

		evaluator.getP2PAdapter().setDistributionStrategy(
				DistributionFactory.create(newStrategyNumber));

		System.out.println("Strategy set to:" + newStrategyNumber);
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return "[number of strategy] sets the distribution strategy to the given value (1=OneKeyDistribution, 2=TwoKeyDistribution, 3=ThreeKeyDistribution)";
	}

}