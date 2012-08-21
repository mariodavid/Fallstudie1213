package console.commands;

import java.io.IOException;
import java.util.Scanner;

import lupos.datastructures.items.Triple;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import evaluators.P2PIndexQueryEvaluator;

public class GetLocalStorage implements Command {
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		System.out.println("KEY \t\t\t\t\t        VALUE");
		for (Number160 key : peer.getPeerBean().getStorage()
				.findContentForResponsiblePeerID(peer.getPeerID())) {

			System.out.print(key);
			FutureDHT future = peer.getAll(key);
			future.awaitUninterruptibly();

			int size = future.getData().values().size();
			int i = 0;
			for (Data result : future.getData().values()) {
				try {
					if (i == 0) {
						System.out.print("\t");
					}
					if (result.getObject().getClass() == String.class) {
						System.out.print(result.getObject().toString());
					} else if (result.getObject().getClass() == Triple.class) {
						System.out.print(((Triple) result.getObject()).toN3String());
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
		
		

		System.out.println("local storage size: "
				+ peer.getPeerBean().getStorage()
						.findContentForResponsiblePeerID(peer.getPeerID())
						.size());
	}

	public String getDescription() {

		return "returns all elements that are in the local peer storage";
	}

}
