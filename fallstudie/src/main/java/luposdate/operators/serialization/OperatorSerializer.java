package luposdate.operators.serialization;

import lupos.engine.operators.BasicOperator;

import org.json.JSONException;
import org.json.JSONObject;

public interface OperatorSerializer {

	public JSONObject serialize(BasicOperator operator, int node_id)
			throws JSONException;

	public BasicOperator deserialize(JSONObject serialiezedOperator)
			throws JSONException;
}
