package console.commands;

import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.PeerAddress;

public class GetAllNodes implements Command {
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {
		System.out.println("Nodes available: " + peer.getPeerBean().getPeerMap().size());
		for (PeerAddress curPeer : peer.getPeerBean().getPeerMap().getAll()) {
			System.out.println(curPeer.toString());
		}
	}

	public String getDescription() {

		return "returns all nodes in your peer map";
	}

}
