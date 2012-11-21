package console.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Scanner;

import lupos.datastructures.items.Triple;
import lupos.engine.evaluators.CommonCoreQueryEvaluator;
import lupos.engine.operators.tripleoperator.TripleConsumer;
import luposdate.evaluators.P2PIndexQueryEvaluator;
import net.tomp2p.p2p.Peer;
import p2p.P2PAdapter;
import p2p.load.TripleCache;

/**
 * Läd eine RDF Datei ein und speichert die Triple in das Netzwerk
 * 
 */
public class LoadN3 implements Command {

	private static final int OUTPUT_LIMIT = 100;
	/** The filename. */
	public P2PAdapter adapter;
	TripleCache tripleCache;
	private int					counter;

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
		
		load(evaluator, is);
		

	}

	public int load(P2PIndexQueryEvaluator evaluator, String file) {
		InputStream is = getClass().getClassLoader().getResourceAsStream(file);
		return load(evaluator, is);
	}

	public int load(P2PIndexQueryEvaluator evaluator, InputStream is) {
		this.adapter = (P2PAdapter) evaluator.getP2PAdapter();
		tripleCache = new TripleCache(adapter);
		counter = 0;
		final TripleConsumer tc = new TripleConsumer() {
			// int counter = 0;
			public void consume(final Triple triple) {
				try {
					counter++;
					adapter.distributionStrategy.distribute(triple);
					if (counter % OUTPUT_LIMIT == 0) {
						System.out.println(counter + " Triple eingelesen ...");
					}
					// tripleCache.add(triple);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		};

		// tripleCache.flush(true);

		try {
//			N3Parser.parseRDFData(is, tc, "UTF-8");
			CommonCoreQueryEvaluator.readTriples("N3", is, tc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return counter;
	}

	public String getDescription() {
		return "load n3 file from a http link";
	}

}
