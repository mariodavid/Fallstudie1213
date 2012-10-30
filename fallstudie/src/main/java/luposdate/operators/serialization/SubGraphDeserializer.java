package luposdate.operators.serialization;

import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.singleinput.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubGraphDeserializer {

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
