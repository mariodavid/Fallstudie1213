package console.commands;

import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;

public interface Command {
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator);

	public String getDescription();

}
