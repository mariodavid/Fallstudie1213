package console.commands;

import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;

public class RPC implements Command {

	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		String input = scanner.next();
		String message = scanner.next();

		Number160 contentHash = Number160.createHash(input);

//		Number160 destination = peer.getPeerBean().getStorage()
//				.findPeerIDForResponsibleContent(contentHash);
		String response = evaluator.getP2PAdapter().sendMessage(contentHash,
				message);

		System.out.println(response);

	}

	public String getDescription() {
		return "[text] sends a broadcast message to all nodes in its local peer map";
	}

}
