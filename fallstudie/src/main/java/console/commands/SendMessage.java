package console.commands;

import java.io.IOException;
import java.util.Scanner;

import net.tomp2p.futures.FutureData;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.PeerAddress;
import evaluators.P2PIndexQueryEvaluator;

public class SendMessage implements Command {

	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		for (PeerAddress p : peer.getPeerBean().getPeerMap().getAll()) {
			try {
				FutureData futureData = peer.send(p, scanner.next());

				futureData.awaitUninterruptibly();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public String getDescription() {
		return "[text] sends a broadcast message to all nodes in its local peer map";
	}

}
