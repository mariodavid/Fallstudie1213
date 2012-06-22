package console.commands;

import java.util.Scanner;

import net.tomp2p.p2p.Peer;
import distribution.DistributionFactory;
import evaluators.P2PIndexQueryEvaluator;

public class SetStrategy implements Command {

	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		int newStrategy = scanner.nextInt();

		evaluator.getP2PAdapter().setDistributionStrategy(
				DistributionFactory.create(newStrategy));

		System.out.println("Strategy set to:" + newStrategy);
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
