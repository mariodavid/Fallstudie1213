package console.commands;

import java.io.IOException;
import java.util.Scanner;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import evaluators.P2PIndexQueryEvaluator;

public class Put implements Command {
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {
		String key = scanner.next();
		String data = scanner.next();
		try {
			peer.put(Number160.createHash(key), new Data(data))
					.awaitUninterruptibly();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getDescription() {
		return "[key] [value] adds a given key value pair to the data storage. If you use it more than once, the last value will be stored";
	}

}
