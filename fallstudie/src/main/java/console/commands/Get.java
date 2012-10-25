package console.commands;

import java.util.Scanner;

import lupos.datastructures.items.Triple;
import luposdate.evaluators.P2PIndexQueryEvaluator;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;

/**
 * Führt ein GET-Befehl im P2P-Netzwerk aus.
 */
public class Get implements Command {
	/** Future Objekt. */
	private FutureDHT future;

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#execute(java.util.Scanner,
	 * net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {
		String key = scanner.next();
		future = peer.get(Number160.createHash(key)).setAll().start();
		future.awaitUninterruptibly();

		if (future.isSuccess()) {
			printResults(peer, Number160.createHash(key));
		} else {
			System.out.println("NOT FOUND!");
		}

	}

	/**
	 * Prints the results.
	 * 
	 * @param peer
	 *            the peer
	 * @param key
	 *            the key
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {
		return "[key] gets a value from a given key";
	}

}
