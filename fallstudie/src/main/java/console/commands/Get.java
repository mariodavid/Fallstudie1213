package console.commands;

import java.util.Scanner;

import lupos.datastructures.items.Triple;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;
import evaluators.P2PIndexQueryEvaluator;

public class Get implements Command {
	private FutureDHT future;

	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {
		String key = scanner.next();
		future = peer.get(Number160.createHash(key)).start();
		Number160 contentKey = Number160.createHash("<s> <p> <o> .");
		// future =
		// peer.get(Number160.createHash(key)).setContentKey(contentKey)
		// .start();
		future = peer.get(Number160.createHash(key)).setAll().start();
		future.awaitUninterruptibly();

		if (future.isSuccess()) {
			printResults(peer, Number160.createHash(key));
		} else {
			System.out.println("NOT FOUND!");
		}

	}

	private void printResults(Peer peer, Number160 key) {
		try {
			for (Data result : future.getDataMap().values()) {

				System.out.println("Peer id: "
						+ peer.getPeerBean().getStorage()
								.findPeerIDForResponsibleContent(key));
				if (result.getObject().getClass() == String.class) {
					System.out.print(result.getObject().toString());
				} else if (result.getObject().getClass() == Triple.class) {
					System.out.println(((Triple) result.getObject())
							.toN3String());
				} else if (result.getObject().getClass() == PeerAddress.class) {
					System.out.println(((PeerAddress) result.getObject())
							.toString());
				} else {
					System.out.println("Unbekanntes Format!");
				}

				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getDescription() {
		return "[key] gets a value from a given key";
	}

}
