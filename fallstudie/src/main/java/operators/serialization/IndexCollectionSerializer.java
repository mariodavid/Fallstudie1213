package operators.serialization;

import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.index.memoryindex.IndexCollection;

import org.json.JSONException;
import org.json.JSONObject;

public class IndexCollectionSerializer implements OperatorSerializer {

	private JSONObject	json;

	public String serialize(BasicOperator operator, int node_id) {
		json = new JSONObject();

		IndexCollection indexCollection = (IndexCollection) operator;
		try {
			json.put("type", IndexCollection.class.getName());
			json.put("node_id", node_id);
			json.put("root", true);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json.toString();
	}

	public BasicOperator deserialize(String serialiezedOperator)
			throws JSONException {
		json = new JSONObject(serialiezedOperator);
		try {
			String className = (String) json.get("type");
			System.out.println(className);
			IndexCollection indexCollection = (IndexCollection) Class.forName(
					className).newInstance();

			return indexCollection;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
