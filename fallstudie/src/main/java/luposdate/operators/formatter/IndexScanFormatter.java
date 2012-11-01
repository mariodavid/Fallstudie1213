package luposdate.operators.formatter;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;

import lupos.datastructures.items.Item;
import lupos.datastructures.items.Variable;
import lupos.datastructures.items.literal.LazyLiteral;
import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.index.BasicIndex;
import lupos.engine.operators.index.IndexCollection;
import lupos.engine.operators.tripleoperator.TriplePattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IndexScanFormatter implements OperatorFormatter {

	private JSONObject				json;
	private IndexCollection	indexCollection;

	public IndexScanFormatter() {
		// TODO Auto-generated constructor stub
	}

	public IndexScanFormatter(IndexCollection indexCollection) {
		this.setIndexCollection(indexCollection);
	}

	public JSONObject serialize(BasicOperator operator, int node_id) {
		json = new JSONObject();

		BasicIndex indexScan = (BasicIndex) operator;
		try {
			json.put("type", operator.getClass().getName());
			json.put("node_id", node_id);

			Collection<JSONObject> triplePatterns = new LinkedList<JSONObject>();

			for (TriplePattern triplePattern : indexScan.getTriplePattern()) {
				JSONObject tripleJson = new JSONObject();
				Collection<JSONObject> tripleItems = createTriplePatternItemsArray(triplePattern);

				tripleJson.put("items", tripleItems);

				triplePatterns.add(tripleJson);
			}

			json.put("triple_pattern", triplePatterns);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
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

	public BasicOperator deserialize(JSONObject serialiezedOperator)
			throws JSONException {

		json = serialiezedOperator;
		try {
			String className = (String) json.get("type");

			BasicIndex indexScan = (BasicIndex) Class.forName(className)
					.getConstructor(IndexCollection.class)
					.newInstance(this.getIndexCollection());

			Collection<TriplePattern> triplePatterns = createTriplePatternsListFromJSON(json);
			indexScan.setTriplePatterns(triplePatterns);

			return indexScan;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private Collection<TriplePattern> createTriplePatternsListFromJSON(
			JSONObject json) {

		Collection<TriplePattern> result = new LinkedList<TriplePattern>();

		try {
			JSONArray triplePatternsJson = (JSONArray) json
					.get("triple_pattern");

			for (int i = 0; i < triplePatternsJson.length(); i++) {

				JSONObject triplePatternJson = triplePatternsJson
						.getJSONObject(i);

				JSONArray itemsJson = (JSONArray) triplePatternJson
						.get("items");

				Item[] items = new Item[3];

				for (int h = 0; h < 3; h++) {
					JSONObject itemJson = itemsJson.getJSONObject(h);

					if (itemJson.getString("type").equals("variable")) {
						items[h] = new Variable(itemJson.getString("name"));
					} else {
						items[h] = LazyLiteral.getLiteral(itemJson
								.getString("value"));

					}
				}

				TriplePattern triplePattern = new TriplePattern(items[0],
						items[1], items[2]);
				result.add(triplePattern);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private IndexCollection getIndexCollection() {
		return indexCollection;
	}

	private void setIndexCollection(IndexCollection indexCollection) {
		this.indexCollection = indexCollection;
	}

}
