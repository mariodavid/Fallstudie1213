package console.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Scanner;

import p2p.P2PAdapter;

import lupos.datastructures.items.Triple;
import lupos.datastructures.items.literal.Literal;
import lupos.engine.operators.tripleoperator.TripleConsumer;
import lupos.rdf.parser.N3Parser;
import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;

/**
 * Läd eine RDF Datei ein und speichert die Triple in das Netzwerk
 * 
 */
public class ExecuteSP2B implements Command {

	/** The filename. */
	public P2PAdapter adapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see P2P.TOM.COMMANDS.Command#execute(java.util.Scanner,
	 * net.tomp2p.p2p.Peer)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		FileInputStream fis = null;
		try {
			fis = new FileInputStream("sp2b.n3");
		} catch (FileNotFoundException e1) {
			System.out.println("Keine Datei vorhanden!");
			return;
		}

		this.adapter = (P2PAdapter) evaluator.getP2PAdapter();

		final TripleConsumer tc = new TripleConsumer() {
			int counter = 0;

			public void consume(final Triple triple) {
				try {
					counter++;
					adapter.distributionStrategy.distribute(triple);
					if (counter % 250 == 0)
						System.out.println(counter + " Triple eingelesen ...");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		};

		try {
			N3Parser.parseRDFData(fis, tc, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public String getDescription() {
		return "executes the SP2B Benchmark";
	}

}
