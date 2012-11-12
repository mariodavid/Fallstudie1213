package console.commands;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Scanner;

import p2p.P2PAdapter;
import p2p.load.TripleCache;

import lupos.datastructures.items.Triple;
import lupos.engine.operators.tripleoperator.TripleConsumer;
import lupos.rdf.parser.N3Parser;
import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;

/**
 * Läd eine RDF Datei ein und speichert die Triple in das Netzwerk
 * 
 */
public class LoadN3 implements Command {

	/** The filename. */
	public P2PAdapter adapter;
	TripleCache tripleCache;

	/*
	 * (non-Javadoc)
	 * 
	 * @see P2P.TOM.COMMANDS.Command#execute(java.util.Scanner,
	 * net.tomp2p.p2p.Peer)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		URL url;
		InputStream  is = null;
		try {
			url = new URL( scanner.next() );
			is = url.openStream();
		} catch (Exception e1) {
			System.out.println("NOT FOUND!");
			e1.printStackTrace();
		}
		
		this.adapter = (P2PAdapter) evaluator.getP2PAdapter();
		tripleCache = new TripleCache(adapter);
		
		final TripleConsumer tc = new TripleConsumer() {
			int counter = 0;
			public void consume(final Triple triple) {
//				try {
					counter++;
					tripleCache.add(triple);
//					adapter.distributionStrategy.distribute(triple);
//					if (counter % 25 == 0)
//						System.out.println(counter + " Triple eingelesen ...");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}

		};

		try {
			N3Parser.parseRDFData(is, tc, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		tripleCache.flush(true);

	}

	public String getDescription() {
		return "load n3 file from a http link";
	}

}