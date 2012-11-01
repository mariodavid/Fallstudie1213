package luposdate.operators;

import lupos.datastructures.bindings.Bindings;
import lupos.datastructures.queryresult.QueryResult;
import lupos.endpoint.client.formatreader.MIMEFormatReader;
import lupos.endpoint.client.formatreader.XMLFormatReader;
import lupos.engine.operators.index.BasicIndex;
import lupos.engine.operators.index.IndexCollection;
import lupos.engine.operators.index.Indices;
import luposdate.operators.formatter.SubGraphContainerFormatter;

import org.json.JSONException;
import org.json.JSONObject;

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
	IndexCollection rootNodeOfSubGraph;
	
	/** The dest_ip. */
	private final String dest_ip;
	

	/**
	 * Instantiates a new sub graph container.
	 *
	 * @param rootNodeOfOuterGraph the root node of outer graph
	 * @param rootNodeOfSubGraph the root node of sub graph
	 * @param dest_ip the dest_ip
	 */
	public SubGraphContainer(IndexCollection rootNodeOfOuterGraph,
			IndexCollection rootNodeOfSubGraph, String dest_ip) {
		super(rootNodeOfOuterGraph);

		this.dest_ip = dest_ip;
		this.rootNodeOfSubGraph = rootNodeOfSubGraph;

	}

	/* (non-Javadoc)
	 * @see lupos.engine.operators.index.BasicIndex#join(lupos.engine.operators.index.Indices, lupos.datastructures.bindings.Bindings)
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
	 * nicht enhalten. Dies geschiet au�en und wird diesem opertator nur
	 * mitgeteilt an welchen knoten es gehen soll (ip). entweder implizit (ueber
	 * rootNodeOfSubGraph (result operator) oder explizit durch String ip als
	 * Parameter)
	 * 
	 * HIER: Stelle 1 & 4
	 *
	 * @param queryResult the query result
	 * @param operandID the operand id
	 * @return the query result
	 */
	@Override
	public QueryResult process(QueryResult queryResult, int operandID) {
		
		SubGraphContainerFormatter serialzer = new SubGraphContainerFormatter();
		
		JSONObject serializedGraph;
		try {
			serializedGraph = serialzer.serialize(rootNodeOfSubGraph, 0);

			System.out.println(serializedGraph.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(SubGraphContainerSerializer.serialize(rootNodeOfSubGraph));
		// hier wird jetzt tatsaechlich losgeschickt (serialisiert usw.)
		// Schritt 1....

		// Schritt 1.1 serialisierung
		// ...

		// return das Ergebnis des Calls (das QueryResult) & deserialisierung

		// Schritt 4.....
		MIMEFormatReader deserializier = new XMLFormatReader();

		// der InputStrem ist jetzt der tatsaechliche Stream aus dem P2P Netz
		// ByteArrayInputStream inputStream = new ByteArrayInputStream(arg0):

		// return deserializier.getQueryResult(inputStream);
		return null;

	}
	
}
