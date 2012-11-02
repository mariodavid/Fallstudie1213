package luposdate.operators;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

import lupos.datastructures.bindings.Bindings;
import lupos.datastructures.items.Item;
import lupos.datastructures.queryresult.QueryResult;
import lupos.endpoint.client.formatreader.MIMEFormatReader;
import lupos.endpoint.client.formatreader.XMLFormatReader;
import lupos.engine.operators.index.BasicIndex;
import lupos.engine.operators.index.Dataset;
import lupos.engine.operators.index.IndexCollection;
import lupos.engine.operators.index.Indices;
import lupos.engine.operators.tripleoperator.TriplePattern;
import lupos.rdf.Prefix;
import luposdate.operators.formatter.SubGraphContainerFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import p2p.P2PAdapter;

// TODO: Auto-generated Javadoc
/**
 * enthaelt die Operatoren, die alle an den Empfaengerknoten verschickt werden
 * sollen.
 * 
 * @author Mario David, Sebastian Walther
 * 
 */
public class SubGraphContainer extends BasicIndex {

	/** The root node of sub graph. */
	IndexCollection						rootNodeOfSubGraph;

	/** The dest_ip. */
	private final String				dest_ip;

	private Collection<TriplePattern>	hashableTriplePatterns;

	private final P2PAdapter			p2pAdapter;

	/**
	 * Instantiates a new sub graph container.
	 * 
	 * @param rootNodeOfOuterGraph
	 *            the root node of outer graph
	 * @param rootNodeOfSubGraph
	 *            the root node of sub graph
	 * @param dest_ip
	 *            the dest_ip
	 */
	public SubGraphContainer(P2PAdapter p2pAdapter,
			IndexCollection rootNodeOfOuterGraph,
			IndexCollection rootNodeOfSubGraph, String dest_ip) {
		super(rootNodeOfOuterGraph);

		this.p2pAdapter = p2pAdapter;
		this.dest_ip = dest_ip;
		this.rootNodeOfSubGraph = rootNodeOfSubGraph;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lupos.engine.operators.index.BasicIndex#join(lupos.engine.operators.index
	 * .Indices, lupos.datastructures.bindings.Bindings)
	 */
	@Override
	public QueryResult join(Indices indices, Bindings bindings) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * wird aufgerufen, wenn der OP ausgefuehrt werden soll hier wird verschickt
	 * und auf das Ergebnis gewartet und zurueckgegeben
	 * 
	 * die logik, welcher Knoten diesen Graphen jetzt empfangen soll, ist hier
	 * nicht enhalten. Dies geschiet außen und wird diesem opertator nur
	 * mitgeteilt an welchen knoten es gehen soll (ip). entweder implizit (ueber
	 * rootNodeOfSubGraph (result operator) oder explizit durch String ip als
	 * Parameter)
	 * 
	 * HIER: Stelle 1 & 4
	 * 
	 * @param queryResult
	 *            the query result
	 * @param operandID
	 *            the operand id
	 * @return the query result
	 */

	@Override
	public QueryResult process(int opt, Dataset dataset) {
		SubGraphContainerFormatter serialzer = new SubGraphContainerFormatter();

		JSONObject serializedGraph;
		try {
			// hier wird jetzt tatsaechlich losgeschickt (serialisiert usw.)
			// Schritt 1: Serialisierung
			serializedGraph = serialzer.serialize(rootNodeOfSubGraph, 0);
			String key = generateKey();

			// Versand des Teilbaumes und Empfang des Ergebnisses
			String result = p2pAdapter.sendMessage(key,
					serializedGraph.toString());

			// Schritt 4: Deserialisierung
			MIMEFormatReader deserializier = new XMLFormatReader();

			InputStream is;
			try {
				is = new ByteArrayInputStream(result.getBytes("UTF-8"));
				return deserializier.getQueryResult(is);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
			// der InputStrem ist jetzt der tatsaechliche Stream aus dem P2P
			// Netz
			// ByteArrayInputStream inputStream = new
			// ByteArrayInputStream(arg0):

			// return deserializier.getQueryResult(inputStream);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;
	}

	private String generateKey() {
		StringBuilder key = new StringBuilder();
		ArrayList<TriplePattern> hashbla = new ArrayList<TriplePattern>(
				hashableTriplePatterns);
		for (Item item : hashbla.get(0).getItems()) {
			if (!item.isVariable()) {
				key.append(item.getName());
			}
		}
		return key.toString();
	}

	@Override
	public String toString() {
		return "SubGraphContainer";
	}

	@Override
	public String toString(Prefix prefixInstance) {
		return toString();
	}

	public void setHashableTriplePatterns(
			Collection<TriplePattern> triplePattern) {
		this.hashableTriplePatterns = triplePattern;
	}

}
