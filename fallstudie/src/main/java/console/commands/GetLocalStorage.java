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
public class GetLocalStorage implements Command {

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#execute(java.util.Scanner,
	 * net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {


		printHeader(scanner, peer, evaluator);

		printElements(peer);

		printFooter(peer);
	}

	private void printHeader(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		Command getMyIdCommand = new GetMyID();
		getMyIdCommand.execute(scanner, peer, evaluator);

		System.out.println();
		System.out.println("Key\t\t\t\t\t\tValue");
	}

	private void printFooter(Peer peer) {
		System.out.println();
		System.out.println("local storagesize: "
				+ peer.getPeerBean().getStorage()
						.findContentForResponsiblePeerID(peer.getPeerID())
						.size());
	}

	private void printElements(Peer peer) {
		for (Number160 key : peer.getPeerBean().getStorage()
				.findContentForResponsiblePeerID(peer.getPeerID())) {

			System.out.print(key);
			FutureDHT future = peer.get(key).setAll().start();
			future.awaitUninterruptibly();

			int size = future.getDataMap().values().size();
			int i = 0;
			for (Data result : future.getDataMap().values()) {
				try {
					if (i == 0) {
						System.out.print("\t");
					}
					if (result.getObject().getClass() == String.class) {
						System.out.print(result.getObject().toString());
					} else if (result.getObject().getClass() == Triple.class) {
						System.out.print(((Triple) result.getObject())
								.toN3String());
					} else if (result.getObject().getClass() == PeerAddress.class) {
						System.out.print("PeerAdress von ID: "
								+ ((PeerAddress) result.getObject()).getID());
					} else {
						System.out.println("Unbekanntes Format!");
					}
					if (i < size - 1) {
						System.out.print(", ");
					} else {
						System.out.println();
					}
					i++;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {

		return "returns all elements that are in the local peer storage";
	}

}
