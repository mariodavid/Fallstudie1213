package operators.serialization;

import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.singleinput.Result;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultSerializer implements OperatorSerializer {

	private JSONObject json;
	private String dest_ip;
	private int request_id;

	public ResultSerializer() {
		dest_ip = "0.0.0.0";
		request_id = 0;
	}

	public ResultSerializer(String dest_ip, int request_id) {
		this.dest_ip = dest_ip;
		this.request_id = request_id;
	}

	public String serialize(BasicOperator operator, int node_id) {
		json = new JSONObject();
		Result result = (Result) operator;
		try {
			json.put("type", result.getClass().getName());
			json.put("node_id", node_id);
			json.put("dest_ip", this.dest_ip);
			json.put("request_id", this.request_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}

	public BasicOperator deserialize(String serialiezedOperator)
			throws JSONException {
		json = new JSONObject(serialiezedOperator);
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
}
