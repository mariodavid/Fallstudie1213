package console.commands;

import java.util.Scanner;

import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import evaluators.P2PIndexQueryEvaluator;

public class GetAll implements Command {
	private FutureDHT	future;

	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {
		// String key = scanner.next();
		// future = peer.get(Number160.createHash(key)).;
		// future.awaitUninterruptibly();
		//
		// if (future.isSuccess()) {
		// printResults();
		// } else {
		// System.out.println("NOT FOUND!");
		// }

	}

	// private void printResults() {
	// try {
	// for (Data result : future.getData().values()) {
	// if (result.getObject().getClass() == String.class) {
	// System.out.print(result.getObject().toString());
	// } else if (result.getObject().getClass() == Triple.class) {
	// System.out.println(((Triple) result.getObject()).toN3String());
	// } else {
	// System.out.println("Unbekanntes Format!");
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public String getDescription() {

		return "[key] gets all values from a given key";
	}

}
