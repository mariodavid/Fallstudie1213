package luposdate.operators.formatter;

import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.application.Output;
import lupos.engine.operators.singleinput.Result;
import luposdate.operators.P2PApplication;

import org.json.JSONException;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * Implementiert die Formatierung für den Result Operator.
 */
public class ResultFormatter implements OperatorFormatter {

	/** The json. */
	private JSONObject				json;

	/** The dest_ip. */
	private String					dest_ip;

	/** The request_id. */
	private int				request_id;

	private P2PApplication	p2pApplication;

	/**
	 * Use: public ResultSerializer(String dest_ip, int request_id).
	 */
	public ResultFormatter(P2PApplication p2pApplication) {
		this.p2pApplication = p2pApplication;
		request_id = 0;
	}

	public ResultFormatter() {
	}
	/**
	 * Instantiates a new result formatter.
	 * 
	 * @param dest_ip
	 *            the dest_ip
	 * @param request_id
	 *            the request_id
	 */
	public ResultFormatter(P2PApplication p2pApplication, int request_id) {
		this.p2pApplication = p2pApplication;
		this.request_id = request_id;
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
		Result result = (Result) operator;
		try {
			json.put("type", result.getClass().getName());
			json.put("node_id", node_id);
			// json.put("dest_ip", this.getDestinationIp());
			json.put("request_id", this.request_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		Result result = new Result();
		// String ip = json.getString("dest_ip");

		if (this.p2pApplication == null) {
			this.p2pApplication = new P2PApplication();
		}

		// this.p2pApplication.setDestinationIp(ip);

		result.addApplication(new Output());
		result.addApplication(this.p2pApplication);

		return result;

	}

	/**
	 * Gets the destination ip.
	 * 
	 * @return the destination ip
	 */
	// private String getDestinationIp() {
	// return dest_ip;
	// }

	/**
	 * Sets the destination ip.
	 * 
	 * @param dest_ip
	 *            the new destination ip
	 */
	// public void setDestinationIp(String dest_ip) {
	// this.dest_ip = dest_ip;
	// }
}
