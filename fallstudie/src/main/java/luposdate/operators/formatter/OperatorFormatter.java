package luposdate.operators.formatter;

import lupos.engine.operators.BasicOperator;

import org.json.JSONException;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Interface OperatorFormatter.
 */
public interface OperatorFormatter {

	/**
	 * Serialize.
	 *
	 * @param operator the operator
	 * @param node_id the node_id
	 * @return the jSON object
	 * @throws JSONException the jSON exception
	 */
	public JSONObject serialize(BasicOperator operator, int node_id)
			throws JSONException;

	/**
	 * Deserialize.
	 *
	 * @param serialiezedOperator the serialiezed operator
	 * @return the basic operator
	 * @throws JSONException the jSON exception
	 */
	public BasicOperator deserialize(JSONObject serialiezedOperator)
			throws JSONException;

}
