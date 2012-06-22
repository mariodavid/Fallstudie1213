package console.commands;

import java.util.Scanner;

import net.tomp2p.p2p.Peer;
import evaluators.P2PIndexQueryEvaluator;

public class Add implements Command {
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {
		String s = scanner.next();
		String p = scanner.next();
		String o = scanner.next();

		try {
			evaluator.compileQuery("INSERT DATA {<" + s + "> <" + p + "> <" + o
					+ ">}");
			evaluator.logicalOptimization();
			evaluator.physicalOptimization();
			evaluator.getResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String getDescription() {
		return "[key] [value] adds a given key value pair to the data storage. If add is used more than one time, all values will be stored within this key";
	}

}
