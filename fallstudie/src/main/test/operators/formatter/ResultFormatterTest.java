package operators.formatter;

import static org.junit.Assert.assertEquals;
import lupos.engine.operators.singleinput.Result;
import luposdate.operators.P2PApplication;
import luposdate.operators.formatter.ResultFormatter;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class ResultFormatterTest {

	private Result result;
	private ResultFormatter serializerDefault;
	private ResultFormatter serializer;
	private String dest_ip;
	private int request_id;

	@Before
	public void setUp() throws Exception {
		this.request_id = 55;
		this.result = new lupos.engine.operators.singleinput.Result();
		this.serializer = new ResultFormatter(new P2PApplication(), request_id);
		this.serializerDefault = new ResultFormatter();
	}

	@Test
	public void testSerialize() {

		int node_id = 123;
		try {
			JSONObject obj = this.serializer.serialize(result, node_id);
			assertEquals(obj.get("type"), Result.class.getName());
			assertEquals(obj.get("node_id"), node_id);
			assertEquals(obj.get("request_id"), this.request_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testSerializeDefault() {

		int node_id = 123;
		try {
			JSONObject obj = this.serializerDefault.serialize(result, node_id);
			assertEquals(obj.get("type"), Result.class.getName());
			assertEquals(obj.get("node_id"), node_id);
			assertEquals(obj.get("request_id"), 0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDeserialize() {

		int node_id = 123;

		try {
			Result actual = (Result) this.serializer
					.deserialize(this.serializer.serialize(result, node_id));
			
			assertEquals(actual.getClass(), result.getClass());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
