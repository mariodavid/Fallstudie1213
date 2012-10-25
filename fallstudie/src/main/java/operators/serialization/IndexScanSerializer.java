package operators.serialization;

import java.util.Collection;
import java.util.LinkedList;

import lupos.datastructures.items.Item;
import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.index.BasicIndex;
import lupos.engine.operators.tripleoperator.TriplePattern;

import org.json.JSONException;
import org.json.JSONObject;

public class IndexScanSerializer implements OperatorSerializer {

	private JSONObject	json;

	public String serialize(BasicOperator operator, int node_id) {
		json = new JSONObject();

		BasicIndex indexScan = (BasicIndex) operator;
		try {
			json.put("type", operator.getClass().getName());
			json.put("node_id", node_id);

			Collection<JSONObject> allTriplePattern = new LinkedList<JSONObject>();

			for (TriplePattern triplePattern : indexScan.getTriplePattern()) {
				JSONObject tripleJson = new JSONObject();
				Collection<JSONObject> tripleItems = createTriplePatternItemsArray(triplePattern);
				
				tripleJson.put("items", tripleItems);
				
				allTriplePattern.add(tripleJson);
			}

			json.put("triple_pattern", allTriplePattern);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json.toString();
	}

	private Collection<JSONObject> createTriplePatternItemsArray(
			TriplePattern triplePattern) throws JSONException {
		Collection<JSONObject> tripleItems = new LinkedList<JSONObject>();

		for (Item item : triplePattern.getItems()) {
			JSONObject itemJson = createTriplePatternItemAsJsonString(item);
			tripleItems.add(itemJson);
		}
		return tripleItems;
	}

	private JSONObject createTriplePatternItemAsJsonString(Item item)
			throws JSONException {
		JSONObject itemJson = new JSONObject();

		if (item.isVariable()) {
			itemJson.put("type", "variable");
			itemJson.put("name", item.getName());
		} else {
			itemJson.put("type", "literal");
			itemJson.put("value", item.getName());
			// item.getName().substring(1, item.getName().length() - 1));
		}
		return itemJson;
	}

	public BasicOperator deserialize(String serialiezedOperator)
			throws JSONException {
		// json = new JSONObject(serialiezedOperator);
		// try {
		// String className = (String) json.get("type");
		// System.out.println(className);
		// IndexCollection indexCollection = (IndexCollection) Class.forName(
		// className).newInstance();
		//
		// return indexCollection;
		// } catch (InstantiationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ClassNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		return null;
	}

}
