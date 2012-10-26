package console.commands;

import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.PeerAddress;

/**
 *	Gibt alle bekannten Knoten eines Peers zurück.
 */
public class GetAllNodes implements Command {
	
	/* (non-Javadoc)
	 * @see console.commands.Command#execute(java.util.Scanner, net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {
		System.out.println("Nodes available: " + peer.getPeerBean().getPeerMap().size());
		for (PeerAddress curPeer : peer.getPeerBean().getPeerMap().getAll()) {
			System.out.println(curPeer.toString());
		}
	}

	/* (non-Javadoc)
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {

		return "returns all nodes in your peer map";
	}

}
