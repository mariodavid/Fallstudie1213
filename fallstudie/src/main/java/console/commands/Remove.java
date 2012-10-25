package console.commands;

import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;

public class Remove implements Command {
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {
		String key = scanner.next();
		peer.remove(Number160.createHash(key)).setAll().start()
				.awaitUninterruptibly();
	}

	public String getDescription() {

		return "removes a key-value pair for a given key, if it has been inserted with a put command";
	}
}
