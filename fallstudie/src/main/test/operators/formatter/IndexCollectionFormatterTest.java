package operators.formatter;

import static org.junit.Assert.assertEquals;
import luposdate.evaluators.P2PIndexQueryEvaluator;
import luposdate.index.P2PIndexCollection;
import luposdate.operators.formatter.P2PIndexCollectionFormatter;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class IndexCollectionFormatterTest {

	private P2PIndexCollection			indexCollection;
	private P2PIndexCollectionFormatter	serializer;

	@Before
	public void setUp() throws Exception {

		P2PIndexQueryEvaluator evaluator = new P2PIndexQueryEvaluator();
		this.indexCollection = new P2PIndexCollection(evaluator.getDataset());
		this.serializer = new P2PIndexCollectionFormatter();

	}

	@Test
	public void testSerialize() {

		int node_id = 123;
		try {
			JSONObject obj = this.serializer
					.serialize(indexCollection, node_id);
			assertEquals(obj.get("type"), indexCollection.getClass().getName());
			assertEquals(obj.get("node_id"), node_id);
			assertEquals(obj.get("root"), true);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDeserialize() {

		int node_id = 123;

		try {
			P2PIndexCollection actual = (P2PIndexCollection) this.serializer
					.deserialize(this.serializer.serialize(indexCollection,
							node_id));

			assertEquals(actual.getClass(), indexCollection.getClass());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
