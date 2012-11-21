package luposdate.operators.formatter;

import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.singleinput.Filter;
import lupos.sparql1_1.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Implementiert die Formatierung für den IndexCollection Operator.
 */
public class FilterFormatter implements OperatorFormatter {

	/** The json. */
	private JSONObject	json;

	/**
	 * Instantiates a new index collection formatter.
	 */
	public FilterFormatter() {
		// TODO Auto-generated constructor stub
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
		json = new JSONObject();

		json.put("type", operator.getClass().getName());
		json.put("node_id", node_id);
		json.put("expression", operator.toString());

		return json;
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
		json = serialiezedOperator;

		Filter filter;
		try {
			filter = new Filter(json.getString("expression"));
			return filter;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;

	}

}
