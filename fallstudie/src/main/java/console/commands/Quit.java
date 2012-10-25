package console.commands;

import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;

public class Quit implements Command {

	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {
		peer.shutdown();
		System.exit(0);
	}

	public String getDescription() {
		return "quits the program and does a shutdown message on the network";
	}


}
