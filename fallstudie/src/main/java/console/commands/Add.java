package console.commands;

import java.io.IOException;
import java.util.Scanner;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class Add implements Command {
	public void execute(Scanner scanner, Peer peer) {
		String key = scanner.next();
		String data = scanner.next();
		
		try {
			peer.add(Number160.createHash(key), new Data(data))
					.awaitUninterruptibly();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getDescription() {
		return "[key] [value] adds a given key value pair to the data storage. If add is used more than one time, all values will be stored within this key";
	}

}
