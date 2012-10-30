package console.commands;

import java.util.Scanner;

import p2p.P2PAdapter;

import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;

/**
 * Gibt die Address Informationen zurück zu einen bestimmten location Key.
 */
public class GetPeerForContent implements Command {
	
	/* (non-Javadoc)
	 * @see console.commands.Command#execute(java.util.Scanner, net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		String input = scanner.next();

		Number160 locationKey = Number160.createHash(input);
		
		PeerAddress destination = ((P2PAdapter) evaluator.getP2PAdapter()).getPeerAddressFromLocationKey(locationKey);

		System.out.println("location key: " + locationKey);
		System.out.println("destination hash: " + destination);
	}

	/* (non-Javadoc)
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {

		return "returns the IP Address for a given location key";
	}

}
