package operators.serialization;

import static org.junit.Assert.assertEquals;
import lupos.engine.operators.index.memoryindex.IndexCollection;
import lupos.engine.operators.singleinput.Result;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class ResultSerializerTest {

	private Result				result;
	private ResultSerializer	serializer;

	@Before
	public void setUp() throws Exception {
		this.result = new lupos.engine.operators.singleinput.Result();
		this.serializer = new ResultSerializer();

	}

	@Test
	public void testSerialize() {

//		int node_id = 123;
//		String actual = this.serializer.serialize(result, node_id);
//		try {
//			JSONObject obj = new JSONObject(actual);
//			assertEquals(obj.get("type"), IndexCollection.class.getName());
//			assertEquals(obj.get("node_id"), node_id);
//			assertEquals(obj.get("root"), true);
//
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@Test
	public void testDeserialize() {

//		int node_id = 123;
//		String serializedString = this.serializer.serialize(result,
//				node_id);
//
//		try {
//			IndexCollection actual = (IndexCollection) this.serializer
//					.deserialize(serializedString);
//
//			assertEquals(actual.getClass(), result.getClass());
//
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
