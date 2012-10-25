package console.commands;

import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;

public class RemoveAll implements Command {
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {
		// String key = scanner.next();
		// peer.removeAll(Number160.createHash(key)).awaitUninterruptibly();
	}

	public String getDescription() {
		return "removes all values for a given key";
	}

}
