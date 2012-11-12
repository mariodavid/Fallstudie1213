package console.commands;

import java.io.IOException;
import java.util.Scanner;

import lupos.datastructures.items.Triple;
import luposdate.evaluators.P2PIndexQueryEvaluator;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;

/**
 * Gibt den lokalen Speicher eines Peers aus.
 */
public class GetResponsibleKeySize implements Command {

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#execute(java.util.Scanner,
	 * net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		System.out.println("this node is responsable for : "
				+ peer.getPeerBean().getStorage().findContentForResponsiblePeerID(peer.getPeerID()).size() + " keys");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {

		return "returns the size of the local storage";
	}

}
