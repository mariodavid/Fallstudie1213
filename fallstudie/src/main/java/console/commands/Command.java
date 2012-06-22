package console.commands;

import java.util.Scanner;

import net.tomp2p.p2p.Peer;
import evaluators.P2PIndexQueryEvaluator;

public interface Command {
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator);

	public String getDescription();

}
