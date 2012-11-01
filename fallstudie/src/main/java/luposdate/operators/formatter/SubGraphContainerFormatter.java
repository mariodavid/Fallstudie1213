package luposdate.operators.formatter;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.OperatorIDTuple;
import lupos.engine.operators.index.BasicIndex;
import lupos.engine.operators.index.Dataset;
import lupos.engine.operators.index.IndexCollection;
import lupos.engine.operators.singleinput.Result;
import luposdate.index.P2PIndexCollection;
import luposdate.index.P2PIndexScan;
import luposdate.operators.P2PApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubGraphContainerFormatter implements OperatorFormatter {

	private int				id_counter;
	private BasicOperator	root;

	private Dataset			dataset;

	public SubGraphContainerFormatter(Dataset dataset) {
		this.dataset = dataset;
	}

	public SubGraphContainerFormatter() {
	}


	public JSONObject serialize(BasicOperator operator, int node_id)
			throws JSONException {
		Collection<JSONObject> nodesJSON = new LinkedList<JSONObject>();
		Collection<JSONObject> edgesJSON = new LinkedList<JSONObject>();
		id_counter = 0;

		serializeNode(operator, nodesJSON, edgesJSON, id_counter);
		JSONObject serializedSubGraph = new JSONObject();

		try {
			serializedSubGraph.put("nodes", nodesJSON);
			serializedSubGraph.put("edges", edgesJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return serializedSubGraph;
	}


	private void serializeNode(BasicOperator node,
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
				e.printStackTrace();
			}
		}
		OperatorFormatter serializer = null;
		if (node instanceof BasicIndex) {
			serializer = new IndexScanFormatter();

		} else if (node instanceof IndexCollection) {
			serializer = new IndexCollectionFormatter();

		} else if (node instanceof Result) {
			serializer = new ResultFormatter();

		}
		try {
			nodesJSON.add(serializer.serialize(node, id_counter));
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(
					"Dieser Operator ist bisher nicht serialisierbar");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (OperatorIDTuple succ : node.getSucceedingOperators()) {
			serializeNode(succ.getOperator(), nodesJSON, edgesJSON, id_counter);
		}
	}

	public BasicOperator deserialize(JSONObject serialiezedOperator)
			throws JSONException {
		root = null;

		HashMap<Integer, BasicOperator> nodes = deserializeNodes(
				serialiezedOperator,
				dataset);

		JSONArray edgesJson = (JSONArray) serialiezedOperator.get("edges");
		deserializeEdges(edgesJson, nodes);

		return root;
	}

	private void deserializeEdges(JSONArray edgesJson,
			HashMap<Integer, BasicOperator> nodes)
			throws JSONException {


		HashMap<BasicOperator, List<OperatorIDTuple>> succedingOperators = new HashMap<BasicOperator, List<OperatorIDTuple>>();
		HashMap<BasicOperator, List<BasicOperator>> precedingOperators = new HashMap<BasicOperator, List<BasicOperator>>();
		
		for (int i = 0; i < edgesJson.length(); i++) {

			JSONObject edgeJson = edgesJson.getJSONObject(i);

			BasicOperator from = nodes.get(edgeJson.getInt("from"));
			BasicOperator to = nodes.get(edgeJson.getInt("to"));

			if (succedingOperators.get(from) == null) {
				succedingOperators.put(from, new LinkedList<OperatorIDTuple>());
			}

			if (precedingOperators.get(to) == null) {
				precedingOperators.put(to, new LinkedList<BasicOperator>());
			}

			succedingOperators.get(from).add(new OperatorIDTuple(to, 0));
			precedingOperators.get(to).add(from);

			// from.setSucceedingOperator(new OperatorIDTuple(to, 0));
			// to.setPrecedingOperator(from);

		}

		for (Entry<BasicOperator, List<OperatorIDTuple>> from : succedingOperators
				.entrySet()) {
			from.getKey().setSucceedingOperators(from.getValue());
		}

		for (Entry<BasicOperator, List<BasicOperator>> to : precedingOperators
				.entrySet()) {
			to.getKey().setPrecedingOperators(to.getValue());
		}

	}

	private HashMap<Integer, BasicOperator> deserializeNodes(
			JSONObject rootJson, Dataset dataset) throws JSONException {

		HashMap<Integer, BasicOperator> nodes = new HashMap<Integer, BasicOperator>();
		JSONArray nodesJson = (JSONArray) rootJson.get("nodes");

		OperatorFormatter deserializer;
		for (int i = 0; i < nodesJson.length(); i++) {

			JSONObject nodeJson = nodesJson.getJSONObject(i);

			if (nodeJson.getString("type").equals(Result.class.getName())) {
				deserializer = new ResultFormatter();
				Result node = (Result) deserializer.deserialize(nodeJson);
				// node.addApplication(new P2PApplication(nodeJson
				// .getString("dest_ip")));
				node.addApplication(new P2PApplication("192.168.1.1"));

				nodes.put(nodeJson.getInt("node_id"), node);

			} else if (nodeJson.getString("type").equals(
			// BasicIndex.class.getName())) {
					P2PIndexScan.class.getName())) {
				deserializer = new IndexScanFormatter();
				BasicIndex node = (BasicIndex) deserializer
						.deserialize(nodeJson);

				if (root != null) {
					node.setPrecedingOperator(root);
				}

				nodes.put(nodeJson.getInt("node_id"), node);

			} else if (nodeJson.getString("type").equals(
			// MemoryIndex.class.getName())) {
					P2PIndexCollection.class.getName())) {
				deserializer = new IndexCollectionFormatter(dataset);
				IndexCollection node = (IndexCollection) deserializer
						.deserialize(nodeJson);

				root = node;

				nodes.put(nodeJson.getInt("node_id"), node);

			} else {
				throw new IllegalArgumentException(
						"Dieser Operator ist bisher nicht deserialisierbar");
			}

		}
		return nodes;
	}


}
