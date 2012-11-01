package luposdate.operators.formatter;

import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.singleinput.Result;

import org.json.JSONException;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * Implementiert die Formatierung für den Result Operator.
 */
public class ResultFormatter implements OperatorFormatter {

	/** The json. */
	private JSONObject json;
	
	/** The dest_ip. */
	private String dest_ip;
	
	/** The request_id. */
	private final int request_id;

	/**
	 * Use: public ResultSerializer(String dest_ip, int request_id).
	 */
	public ResultFormatter() {
		setDestinationIp("0.0.0.0");
		request_id = 0;
	}

	/**
	 * Instantiates a new result formatter.
	 *
	 * @param dest_ip the dest_ip
	 * @param request_id the request_id
	 */
	public ResultFormatter(String dest_ip, int request_id) {
		this.setDestinationIp(dest_ip);
		this.request_id = request_id;
	}

	/* (non-Javadoc)
	 * @see luposdate.operators.formatter.OperatorFormatter#serialize(lupos.engine.operators.BasicOperator, int)
	 */
	public JSONObject serialize(BasicOperator operator, int node_id) {
		json = new JSONObject();
		Result result = (Result) operator;
		try {
			json.put("type", result.getClass().getName());
			json.put("node_id", node_id);
			json.put("dest_ip", this.getDestinationIp());
			json.put("request_id", this.request_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	/* (non-Javadoc)
	 * @see luposdate.operators.formatter.OperatorFormatter#deserialize(org.json.JSONObject)
	 */
	public BasicOperator deserialize(JSONObject serialiezedOperator)
			throws JSONException {
		// TODO: weg machen diese unnötige zuweisung
		json = serialiezedOperator;
		String className = (String) json.get("type");
		try {
			Result result = (Result) Class.forName(className).newInstance();
			return result;
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
		// className).newInstance();
		return null;
	}

	/**
	 * Gets the destination ip.
	 *
	 * @return the destination ip
	 */
	private String getDestinationIp() {
		return dest_ip;
	}

	/**
	 * Sets the destination ip.
	 *
	 * @param dest_ip the new destination ip
	 */
	private void setDestinationIp(String dest_ip) {
		this.dest_ip = dest_ip;
	}
}
