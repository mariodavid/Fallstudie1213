package console.commands;

import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;

public class GetPeerForContent implements Command {
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		String input = scanner.next();

		Number160 contentHash = Number160.createHash(input);

		// finds the peer id for the given content hash
		Number160 destination = peer.getPeerBean().getStorage()
				.findPeerIDForResponsibleContent(contentHash);

		// if destination is null, no peer is responsible for this content
		// in this case, the destination is this node
		if (destination == null) {
			System.out.println("Der null fall ist aufgetreten");
			destination = peer.getPeerID();
		}

		System.out.println("content hash: " + contentHash);
		System.out.println("destination hash: " + destination);
	}

	public String getDescription() {

		return "returns the destination hash (hash of the ip) for a given content";
	}

}
