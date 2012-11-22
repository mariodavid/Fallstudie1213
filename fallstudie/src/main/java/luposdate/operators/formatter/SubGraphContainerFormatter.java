package luposdate.operators.formatter;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.OperatorIDTuple;
import lupos.engine.operators.index.BasicIndexScan;
import lupos.engine.operators.index.Dataset;
import lupos.engine.operators.index.Root;
import lupos.engine.operators.singleinput.Filter;
import lupos.engine.operators.singleinput.Result;
import luposdate.index.P2PIndexCollection;
import luposdate.index.P2PIndexScan;
import luposdate.operators.P2PApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class SubGraphContainerFormatter.
 */
public class SubGraphContainerFormatter implements OperatorFormatter {

	/** The id_counter. */
	private int				id_counter;

	/** The root. */
	private BasicOperator	root;

	/** The dataset. */
	private Dataset			dataset;

	private P2PApplication	p2pApplication;

	/**
	 * Instantiates a new sub graph container formatter.
	 * 
	 * @param dataset
	 *            the dataset
	 */
	public SubGraphContainerFormatter(Dataset dataset,
			P2PApplication p2pApplication) {
		this.dataset = dataset;
		this.p2pApplication = p2pApplication;
	}

	/**
	 * Instantiates a new sub graph container formatter.
	 */
	public SubGraphContainerFormatter() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * luposdate.operators.formatter.OperatorFormatter#serialize(lupos.engine
	 * .operators.BasicOperator, int)
	 */
	public JSONObject serialize(BasicOperator operator, int node_id)
			throws JSONException {
		Collection<JSONObject> nodesJSON = new LinkedList<JSONObject>();
		Collection<JSONObject> edgesJSON = new LinkedList<JSONObject>();
		id_counter = 0;

		serializeNode(new OperatorIDTuple(operator, 0), nodesJSON, edgesJSON,
				id_counter);
		JSONObject serializedSubGraph = new JSONObject();

		try {
			serializedSubGraph.put("nodes", nodesJSON);
			serializedSubGraph.put("edges", edgesJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return serializedSubGraph;
	}

	/**
	 * Serialize node.
	 * 
	 * @param node
	 *            the node
	 * @param nodesJSON
	 *            the nodes json
	 * @param edgesJSON
	 *            the edges json
	 * @param parent_id
	 *            the parent_id
	 */
	private void serializeNode(OperatorIDTuple node,
			Collection<JSONObject> nodesJSON, Collection<JSONObject> edgesJSON,
			int parent_id) {
		id_counter++;

		int edge_id = node.getId();

		BasicOperator op = node.getOperator();

		if (parent_id > 0) {
			JSONObject edge = new JSONObject();
			try {
				edge.put("from", parent_id);
				edge.put("to", id_counter);
				edge.put("edge_id", edge_id);
				edgesJSON.add(edge);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		OperatorFormatter serializer = null;
		if (op instanceof BasicIndexScan) {
			serializer = new P2PIndexScanFormatter();

		} else if (op instanceof Root) {
			serializer = new P2PIndexCollectionFormatter();

		} else if (op instanceof Result) {
			serializer = new ResultFormatter();

		} else if (op instanceof Filter) {
			serializer = new FilterFormatter();

		}
		try {
			nodesJSON.add(serializer.serialize(op, id_counter));
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(
					"Dieser Operator ist bisher nicht serialisierbar");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (OperatorIDTuple succ : op.getSucceedingOperators()) {
			serializeNode(succ, nodesJSON, edgesJSON, id_counter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * luposdate.operators.formatter.OperatorFormatter#deserialize(org.json.
	 * JSONObject)
	 */
	public BasicOperator deserialize(JSONObject serialiezedOperator)
			throws JSONException {
		root = null;

		HashMap<Integer, BasicOperator> nodes = deserializeNodes(serialiezedOperator);

		JSONArray edgesJson = (JSONArray) serialiezedOperator.get("edges");
		deserializeEdges(edgesJson, nodes);

		return root;
	}

	/**
	 * Deserialize edges.
	 * 
	 * @param edgesJson
	 *            the edges json
	 * @param nodes
	 *            the nodes
	 * @throws JSONException
	 *             the jSON exception
	 */
	private void deserializeEdges(JSONArray edgesJson,
			HashMap<Integer, BasicOperator> nodes) throws JSONException {

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

			succedingOperators.get(from).add(
					new OperatorIDTuple(to, edgeJson.getInt("edge_id")));
			precedingOperators.get(to).add(from);

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

	/**
	 * Deserialize nodes.
	 * 
	 * @param rootJson
	 *            the root json
	 * @param dataset
	 *            the dataset
	 * @return the hash map
	 * @throws JSONException
	 *             the jSON exception
	 */
	private HashMap<Integer, BasicOperator> deserializeNodes(JSONObject rootJson)
			throws JSONException {

		HashMap<Integer, BasicOperator> nodes = new HashMap<Integer, BasicOperator>();
		JSONArray nodesJson = (JSONArray) rootJson.get("nodes");

		HashMap<String, OperatorFormatter> formatters = createFormatters();

		for (int i = 0; i < nodesJson.length(); i++) {
			JSONObject nodeJson = nodesJson.getJSONObject(i);

			// get corresponding formatter from hashmap
			OperatorFormatter formatter = formatters.get(nodeJson
					.getString("type"));

			// add deserialized node to list
			BasicOperator node = formatter.deserialize(nodeJson);
			nodes.put(nodeJson.getInt("node_id"), node);

			// die referenz der index collection wird der index scan instanz
			// zugewiesen
			if (node instanceof P2PIndexCollection) {
				P2PIndexScanFormatter p2pIndexScanFormatter = (P2PIndexScanFormatter) formatters
						.get(P2PIndexScan.class.getName());
				p2pIndexScanFormatter
						.setRoot((P2PIndexCollection) node);
			}

			try {
				if (nodeJson.getBoolean("root")) {
					root = node;
				}
			} catch (JSONException e) {
			}

		}
		return nodes;
	}

	private HashMap<String, OperatorFormatter> createFormatters() {

		HashMap<String, OperatorFormatter> formatters = new HashMap<String, OperatorFormatter>();

		formatters.put(P2PIndexCollection.class.getName(),
				new P2PIndexCollectionFormatter(dataset));
		formatters.put(P2PIndexScan.class.getName(),
				new P2PIndexScanFormatter());
		formatters.put(Filter.class.getName(),
				new FilterFormatter());
		formatters.put(Result.class.getName(), new ResultFormatter(
				p2pApplication));

		return formatters;
	}

}
