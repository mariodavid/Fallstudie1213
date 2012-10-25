package console.commands;

import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;

/**
 * Sendet eine Nachricht an einen gegebenen location Key.
 */
public class SendMessage implements Command {

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#execute(java.util.Scanner,
	 * net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		String input = scanner.next();
		String message = scanner.next();

		Number160 contentHash = Number160.createHash(input);

		String response = evaluator.getP2PAdapter().sendMessage(contentHash,
				message);

		System.out.println(response);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {
		return "[locationKey] [text] sends a message to the node, which is responsible for the locationKey";
	}

}
