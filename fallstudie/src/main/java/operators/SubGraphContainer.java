package operators;

import lupos.datastructures.bindings.Bindings;
import lupos.datastructures.queryresult.QueryResult;
import lupos.endpoint.client.formatreader.MIMEFormatReader;
import lupos.endpoint.client.formatreader.XMLFormatReader;
import lupos.engine.operators.index.BasicIndex;
import lupos.engine.operators.index.IndexCollection;
import lupos.engine.operators.index.Indices;

/**
 * enthaelt die Operatoren, die alle an den Empfaengerknoten verschickt werden
 * sollen.
 * 
 * @author Mario David, Sebastian Walther
 * 
 */
public class SubGraphContainer extends BasicIndex {
	IndexCollection	rootNodeOfSubGraph;
	private final String	dest_ip;

	public SubGraphContainer(IndexCollection rootNodeOfOuterGraph,
			IndexCollection rootNodeOfSubGraph, String dest_ip) {
		super(rootNodeOfOuterGraph);

		this.dest_ip = dest_ip;
		this.rootNodeOfSubGraph = rootNodeOfSubGraph;

	}

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
	 * nicht enhalten. Dies geschiet au§en und wird diesem opertator nur
	 * mitgeteilt an welchen knoten es gehen soll (ip). entweder implizit (ueber
	 * rootNodeOfSubGraph (result operator) oder explizit durch String ip als
	 * Parameter)
	 * 
	 * HIER: Stelle 1 & 4
	 * 
	 * @param queryResult
	 * @param operandID
	 * @return
	 */
	@Override
	public QueryResult process(QueryResult queryResult, int operandID) {

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
