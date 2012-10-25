package console.commands;

import java.util.Scanner;

import p2p.distribution.DistributionFactory;

import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;

/**
 * Verändert die Verteilungsstrategie.
 */
public class SetStrategy implements Command {

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#execute(java.util.Scanner,
	 * net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		int newStrategyNumber = scanner.nextInt();

		evaluator.getP2PAdapter().setDistributionStrategy(
				DistributionFactory.create(newStrategyNumber));

		System.out.println("Strategy set to:" + newStrategyNumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {
		// TODO Auto-generated method stub
		return "[number of strategy] sets the distribution strategy to the given value (1=OneKeyDistribution, 2=TwoKeyDistribution, 3=ThreeKeyDistribution)";
	}

}
