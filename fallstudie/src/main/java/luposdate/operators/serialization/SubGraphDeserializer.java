package luposdate.operators.serialization;

import java.util.HashMap;

import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.OperatorIDTuple;
import lupos.engine.operators.index.BasicIndex;
import lupos.engine.operators.index.IndexCollection;
import lupos.engine.operators.index.memoryindex.MemoryIndex;
import lupos.engine.operators.singleinput.Result;
import luposdate.operators.P2PApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubGraphDeserializer {

	public static BasicOperator deserialize(String serializedGraph,
			P2PApplication p2pApplication) throws JSONException {
		BasicOperator root = null;

		JSONObject rootJson = new JSONObject(serializedGraph);
		HashMap<Integer, BasicOperator> nodes = deserializeNodes(
				p2pApplication, root, rootJson);

		JSONArray edgesJson = (JSONArray) rootJson.get("edges");
		deserializeEdges(edgesJson, nodes, root);

		System.out.println(root);

		return root;
	}

	private static void deserializeEdges(JSONArray edgesJson,
			HashMap<Integer, BasicOperator> nodes, BasicOperator root)
			throws JSONException {

		for (int i = 0; i < edgesJson.length(); i++) {

			JSONObject edgeJson = edgesJson.getJSONObject(i);

			BasicOperator from = nodes.get(edgeJson.getInt("from"));
			BasicOperator to = nodes.get(edgeJson.getInt("to"));

			from.setSucceedingOperator(new OperatorIDTuple(to, 0));
			to.setPrecedingOperator(from);

		}

	}

	private static HashMap<Integer, BasicOperator> deserializeNodes(
			P2PApplication p2pApplication, BasicOperator root,
			JSONObject rootJson) throws JSONException {

		HashMap<Integer, BasicOperator> nodes = new HashMap<Integer, BasicOperator>();
		JSONArray nodesJson = (JSONArray) rootJson.get("nodes");

		OperatorSerializer deserializer;
		for (int i = 0; i < nodesJson.length(); i++) {

			JSONObject nodeJson = nodesJson.getJSONObject(i);

			if (nodeJson.getString("type").equals(Result.class.getName())) {
				deserializer = new ResultSerializer();
				Result node = (Result) deserializer.deserialize(nodeJson);
				// node.addApplication(new P2PApplication(nodeJson
				// .getString("dest_ip")));
				node.addApplication(p2pApplication);

				// root.getSucceedingOperators().get(0).getOperator()
				// .setPrecedingOperator(node);

				nodes.put(nodeJson.getInt("node_id"), node);

			} else if (nodeJson.getString("type").equals(
					BasicIndex.class.getName())) {
				deserializer = new IndexScanSerializer();
				BasicIndex node = (BasicIndex) deserializer
						.deserialize(nodeJson);

				if (root != null) {
					node.setPrecedingOperator(root);
				}

				nodes.put(nodeJson.getInt("node_id"), node);

			} else if (nodeJson.getString("type").equals(
					MemoryIndex.class.getName())) {
				deserializer = new IndexCollectionSerializer();
				IndexCollection node = (IndexCollection) deserializer
						.deserialize(nodeJson);

				root = node;

				nodes.put(nodeJson.getInt("node_id"), node);

			} else {
				System.out.println("falscher type: "
						+ nodeJson.getString("type"));
			}

		}
		return nodes;
	}
}
