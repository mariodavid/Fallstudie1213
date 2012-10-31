package luposdate.operators.serialization;

import java.util.Collection;
import java.util.LinkedList;

import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.OperatorIDTuple;
import lupos.engine.operators.index.BasicIndex;
import lupos.engine.operators.index.IndexCollection;
import lupos.engine.operators.singleinput.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubGraphDeserializer {

	private static int id_counter;
	
	public static String serialize(BasicOperator graph) {
		
		Collection<JSONObject> nodesJSON = new LinkedList<JSONObject>();
		Collection<JSONObject> edgesJSON = new LinkedList<JSONObject>();
		id_counter = 0;

		serializeNode(graph, nodesJSON, edgesJSON, id_counter);
		JSONObject serilizedSubGraph = new JSONObject();

		try {
			serilizedSubGraph.put("nodes", nodesJSON);
			serilizedSubGraph.put("edges", edgesJSON);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(serilizedSubGraph.toString()); // TEST
		return serilizedSubGraph.toString();
	}
	
	private static void serializeNode(BasicOperator node,
			Collection<JSONObject> nodesJSON, Collection<JSONObject> edgesJSON,
			int parent_id) {
		id_counter++;

		if (parent_id > 0) {
			JSONObject edge = new JSONObject();
			try {
				edge.put("from", parent_id);
				edge.put("to", id_counter);
				edgesJSON.add(edge);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		OperatorSerializer serializer = null;
		if (node instanceof BasicIndex) {
			serializer = new IndexScanSerializer();

		} else if (node instanceof IndexCollection) {
			serializer = new IndexCollectionSerializer();

		} else if (node instanceof Result) {
			serializer = new ResultSerializer();

		}
		try {
			nodesJSON.add(serializer.serialize(node, id_counter));
		} catch (NullPointerException e) {
			/*
			 * Operator ist nicht implementert. Es stehen folgende Operatoren
			 * zur Verfügung: -IndexScanOperator -IndexCollectionOperator
			 * -ResultOperator
			 */
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (OperatorIDTuple succ : node.getSucceedingOperators()) {
			serializeNode(succ.getOperator(), nodesJSON, edgesJSON, id_counter);

		}
	}
	
	
	public static BasicOperator deserialize(String serializedGraph)
			throws JSONException {
		BasicOperator result = null;

		JSONObject rootJson = new JSONObject(serializedGraph);
		JSONArray nodesJson = (JSONArray) rootJson.get("nodes");

		
		OperatorSerializer deserializer;
		for (int i = 0; i < nodesJson.length(); i++) {

			JSONObject nodeJson = nodesJson.getJSONObject(i);

			if (nodeJson.getString("type").equals(Result.class.getName())) {
				deserializer = new ResultSerializer();
				Result node = (Result) deserializer.deserialize(nodeJson);

			}


		}
		
		
		
		
		System.out.println(rootJson);

		return result;
	}
}
