package luposdate.operators.serialization;

import lupos.engine.operators.BasicOperator;

import org.json.JSONException;
import org.json.JSONObject;

public interface OperatorSerializer {

	public String serialize(BasicOperator operator, int node_id);

	public BasicOperator deserialize(JSONObject serialiezedOperator)
			throws JSONException;
}
