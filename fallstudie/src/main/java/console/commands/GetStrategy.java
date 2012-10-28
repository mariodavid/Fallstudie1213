package console.commands;

import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;
import net.tomp2p.p2p.Peer;
import p2p.distribution.DistributionStrategy;

/**
 * Gibt die Peer Informationen des aktuellen Peers zurück.
 */
public class GetStrategy implements Command {

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#execute(java.util.Scanner,
	 * net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		DistributionStrategy distributionStrategy = evaluator.getP2PAdapter()
				.getDistributionStrategy();

		System.out.println("Strategy is set to: " + distributionStrategy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {

		return "returns the current distribution strategy";
	}

}
