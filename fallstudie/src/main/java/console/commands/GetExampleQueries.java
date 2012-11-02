package console.commands;

import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;
import net.tomp2p.p2p.Peer;

/**
 * Gibt Beispiel Queries zurück.
 */
public class GetExampleQueries implements Command {

	/** The Constant queries. */
	private final static String[] queries = {
			"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT * WHERE{ ?s rdf:type ?o. }",
			"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT * WHERE{ ?s rdf:type ?class. ?s ?p ?o.}",
			"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> INSERT DATA { <a> rdf:type <b> }",
			"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT * WHERE{ <a> rdf:type ?o. }",
			"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> DELETE DATA { <a> rdf:type <b> }",
			"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> INSERT { ?s rdf:type2 ?o } WHERE { ?s rdf:type ?o. }",
			"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT * WHERE{ ?s rdf:type2 ?o. }",
			"query SELECT * WHERE {?s <p> ?o, ?o2 .}" };

	/* (non-Javadoc)
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {
		return "shows some sample queries for copy and paste";
	}

	/* (non-Javadoc)
	 * @see console.commands.Command#execute(java.util.Scanner, net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		for (String q : queries) {
			System.out.println(q);
		}

	}
}
