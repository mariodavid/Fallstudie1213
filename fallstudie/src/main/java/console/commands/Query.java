package console.commands;

import java.net.URL;
import java.util.Scanner;

import xpref.XPref;

import lupos.endpoint.server.DebugEndpoint;
import lupos.endpoint.server.Endpoint;
import lupos.gui.Demo_Applet;
import lupos.gui.GUI;
import lupos.gui.operatorgraph.graphwrapper.GraphWrapperBasicOperator;
import lupos.gui.operatorgraph.viewer.Viewer;
import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;

public class Query implements Command {

	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		String query = scanner.nextLine();

		try {
			evaluator.compileQuery(query);
			evaluator.logicalOptimization();
			evaluator.physicalOptimization();
			
			try {
				XPref.getInstance(Demo_Applet.class.getResource("/preferencesMenu.xml"));
			} catch(Exception e){
				XPref.getInstance(new URL("file:"+GUI.class.getResource("/preferencesMenu.xml").getFile()));
			}
			new Viewer(new GraphWrapperBasicOperator(evaluator.getRootNode()),
					"test", true, false);
			
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
