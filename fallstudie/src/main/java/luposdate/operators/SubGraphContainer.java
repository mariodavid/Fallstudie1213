package luposdate.operators;

import java.util.Collection;
import java.util.LinkedList;

import lupos.datastructures.bindings.Bindings;
import lupos.datastructures.queryresult.QueryResult;
import lupos.endpoint.client.formatreader.MIMEFormatReader;
import lupos.endpoint.client.formatreader.XMLFormatReader;
import lupos.engine.operators.OperatorIDTuple;
import lupos.engine.operators.index.BasicIndex;
import lupos.engine.operators.index.IndexCollection;
import lupos.engine.operators.index.Indices;
import lupos.engine.operators.singleinput.Result;
import luposdate.operators.serialization.IndexCollectionSerializer;
import luposdate.operators.serialization.IndexScanSerializer;
import luposdate.operators.serialization.OperatorSerializer;
import luposdate.operators.serialization.ResultSerializer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * enthaelt die Operatoren, die alle an den Empfaengerknoten verschickt werden
 * sollen.
 * 
 * @author Mario David, Sebastian Walther
 * 
 */
public class SubGraphContainer extends BasicIndex {
	IndexCollection rootNodeOfSubGraph;
	private final String dest_ip;

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
	 * nicht enhalten. Dies geschiet au�en und wird diesem opertator nur
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
		Collection<JSONObject> nodes = new LinkedList<JSONObject>();
		Collection<JSONObject> edges = new LinkedList<JSONObject>();

		serializeSubGraphContainer(rootNodeOfSubGraph, nodes, edges);

		JSONObject serilizedSubGraph = new JSONObject();

		try {
			serilizedSubGraph.put("nodes", nodes);
			serilizedSubGraph.put("edges", edges);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	private void serializeSubGraphContainer(IndexCollection root,
			Collection<JSONObject> nodes, Collection<JSONObject> edges) {

		for (OperatorIDTuple succ : root.getSucceedingOperators()) {
			serializeNode(succ, nodes, edges, succ.getId());
		}
	}

	private void serializeNode(OperatorIDTuple succ,
			Collection<JSONObject> nodes, Collection<JSONObject> edges,
			int parent_id) {
		JSONObject edge = new JSONObject();
		try {
			edge.put("from", parent_id);
			edge.put("to", succ.getId());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OperatorSerializer serializer = null;
		if (succ.getOperator() instanceof BasicIndex) {
			serializer = new IndexScanSerializer();

		} else if (succ.getOperator() instanceof IndexCollection) {
			serializer = new IndexCollectionSerializer();

		} else if (succ.getOperator() instanceof Result) {
			serializer = new ResultSerializer();

		}
		try {
			// nodes.add(serializer.serialize(succ.getOperator(),
			// succ.getId()));
		} catch (NullPointerException e) {
			/*
			 * Operator ist nicht implementert. Es stehen folgende Operatoren
			 * zur Verfügung: -IndexScanOperator -IndexCollectionOperator
			 * -ResultOperator
			 */
		}
		// succ.processAll(new Bindings()....
		// ODER
		// for (OperatorIDTuple succs : succ....) {
		// serializeNode(succs, nodes, edges, succ.getId());
		// TODO: Hier muss die Traverserung fortgesetzt werden.
		// }
	}
}
