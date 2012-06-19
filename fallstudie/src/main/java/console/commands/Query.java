package console.commands;

import java.util.Scanner;

import net.tomp2p.p2p.Peer;

public class Query implements Command {

	public void execute(Scanner scanner, Peer peer) {
		// statt eines Peer Objektes muss hier ein Lupos P2PIndexEvaluator
		// uebergeben werden, damit ein SPARQL Query ausgefuehrt werden kann

	}

	public String getDescription() {
		return "[key] gets a value from a given key";
	}

}
