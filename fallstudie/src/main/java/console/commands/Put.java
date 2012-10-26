package console.commands;

import java.io.IOException;
import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

/**
 * Speichert einen Key/Value in das P2P-Netwerk
 */
public class Put implements Command {
	
	/* (non-Javadoc)
	 * @see console.commands.Command#execute(java.util.Scanner, net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {
		String key = scanner.next();
		String data = scanner.next();
		try {
			peer.put(Number160.createHash(key)).setData(new Data(data)).start()
					.awaitUninterruptibly();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {
		return "[key] [value] adds a given key value pair to the data storage. If you use it more than once, the last value will be stored";
	}

}
