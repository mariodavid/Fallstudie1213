package console.commands;

import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;

/**
 * Mit diesem Befehl kann ein Triple einfach hinzugefügt werden. Man gibt
 * einfach "add s p o" an und fügt so automatisch ein Triple mit den Literalen s
 * p o hinzu ohne ein extra Insert-Query ausführen zu müssen.
 */
public class Add implements Command {

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#execute(java.util.Scanner,
	 * net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {
		return "[key] [value] adds a given key value pair to the data storage. If add is used more than one time, all values will be stored within this key";
	}

}
