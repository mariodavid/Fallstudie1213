package console.commands;

import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;

/**
 * Wird ausgeführt beim Verlassen des P2P-Netzwerks.
 */
public class Quit implements Command {

	/* (non-Javadoc)
	 * @see console.commands.Command#execute(java.util.Scanner, net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {
		peer.shutdown();
		System.exit(0);
	}

	/* (non-Javadoc)
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {
		return "quits the program and does a shutdown message on the network";
	}


}
