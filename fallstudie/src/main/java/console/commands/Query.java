package console.commands;

import java.util.Scanner;

import lupos.datastructures.queryresult.QueryResult;
import luposdate.evaluators.P2PIndexQueryEvaluator;
import net.tomp2p.p2p.Peer;

/**
 * Führt eine Anfrage aus.
 */
public class Query implements Command {

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#execute(java.util.Scanner,
	 * net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		String query = scanner.nextLine();

		try {
			evaluator.compileQuery(query);
			evaluator.logicalOptimization();
			evaluator.physicalOptimization();

			// try {
			// XPref.getInstance(Demo_Applet.class.getResource("/preferencesMenu.xml"));
			// } catch(Exception e){
			// XPref.getInstance(new
			// URL("file:"+GUI.class.getResource("/preferencesMenu.xml").getFile()));
			// }
			// new Viewer(new
			// GraphWrapperBasicOperator(evaluator.getRootNode()),
			// "test", true, false);
			QueryResult result = evaluator.getResult();

			System.out.println("Query Result: " + result);

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
		return "[key] gets a value from a given key";
	}

}
