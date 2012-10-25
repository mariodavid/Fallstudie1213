package luposdate.operators.serialization;

import lupos.engine.operators.BasicOperator;

import org.json.JSONException;

public interface OperatorSerializer {

	public String serialize(BasicOperator operator, int node_id);

	public BasicOperator deserialize(String serialiezedOperator)
			throws JSONException;
}
