package operators.formatter;

import static org.junit.Assert.assertEquals;
import lupos.engine.operators.index.memoryindex.IndexCollection;
import luposdate.operators.formatter.IndexCollectionFormatter;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class IndexCollectionFormatterTest {

	private IndexCollection				indexCollection;
	private IndexCollectionFormatter	serializer;

	@Before
	public void setUp() throws Exception {
		this.indexCollection = new lupos.engine.operators.index.memoryindex.IndexCollection();
		this.serializer = new IndexCollectionFormatter();

	}

	@Test
	public void testSerialize() {

		int node_id = 123;
		try {
			JSONObject obj = this.serializer
					.serialize(indexCollection, node_id);
			assertEquals(obj.get("type"), IndexCollection.class.getName());
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
			IndexCollection actual = (IndexCollection) this.serializer
					.deserialize(this.serializer.serialize(indexCollection,
							node_id));

			assertEquals(actual.getClass(), indexCollection.getClass());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
