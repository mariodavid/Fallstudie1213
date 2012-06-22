package console.commands;

import java.util.Scanner;

import net.tomp2p.p2p.Peer;
import evaluators.P2PIndexQueryEvaluator;

public class Query implements Command {

	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		String query = scanner.nextLine();

		try {
			evaluator.compileQuery(query);
			evaluator.logicalOptimization();
			evaluator.physicalOptimization();
			System.out.println(evaluator.getResult());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public String getDescription() {
		return "[key] gets a value from a given key";
	}

}
