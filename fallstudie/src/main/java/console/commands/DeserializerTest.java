package console.commands;

import index.P2PIndexCollection;
import index.P2PIndexScan;

import java.util.LinkedList;
import java.util.Scanner;

import lupos.datastructures.items.Variable;
import lupos.datastructures.items.literal.LazyLiteral;
import lupos.engine.operators.application.Output;
import lupos.engine.operators.messages.BoundVariablesMessage;
import lupos.engine.operators.singleinput.Result;
import lupos.engine.operators.tripleoperator.TriplePattern;
import net.tomp2p.p2p.Peer;
import evaluators.P2PIndexQueryEvaluator;

public class DeserializerTest implements Command {
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		// Quasi schritt 2, denn hier muss statt eines Commands ein Listener
		// verwendet werden

		// 1. deserialisierung des Teilgraphen

		P2PIndexCollection collection = new P2PIndexCollection(
				evaluator.getDataset());

		P2PIndexScan indexScan = new P2PIndexScan(collection);
		collection.addSucceedingOperator(indexScan);

		LinkedList<TriplePattern> patterns = new LinkedList<TriplePattern>();

		// url ausdenken
		patterns.add(new TriplePattern(new Variable("s"), LazyLiteral
				.getLiteral("<p>"), new Variable("o")));
		indexScan.setTriplePatterns(patterns);

		Result result = new Result();

		// TODO: hier muss jetzt die P2P Application rein, damit dann auch
		// tatsaechlich zuruck geschickt wird und nicht einfach nur ausgegeben
		// wird
		result.addApplication(new Output());

		indexScan.addSucceedingOperator(result);

		// erzeugt die Vorgaenger der Collection, wie bei addSucceedingOperator
		// (rekursiv fuer den gesamten Baum)
		collection.setParents();

		// erkennt zyklen im op graphen (vermutlich nicht relevant, evtl. bei
		// spaeteren erweiterungen relevant)
		collection.detectCycles();

		// berechnet an welcher stelle welche variablen gebunden sind und
		// gebunden sein koennen
		collection.sendMessage(new BoundVariablesMessage());

		// uebergabe des root node an den evaluator zur ausfuehrung
		evaluator.setRootNode(collection);

		// 2. ausfuehrung des eigentlichen aufrufs in lupos
		try {
			evaluator.evaluateQuery();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getDescription() {
		return "[key] [value] adds a given key value pair to the data storage. If add is used more than one time, all values will be stored within this key";
	}

}
