package console.commands;

import java.util.Scanner;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import evaluators.P2PIndexQueryEvaluator;

public class RemoveAll implements Command {
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {
		String key = scanner.next();
		peer.removeAll(Number160.createHash(key)).awaitUninterruptibly();
	}

	public String getDescription() {
		return "removes all values for a given key";
	}

}
