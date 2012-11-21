package operators.formatter;

import static org.junit.Assert.assertEquals;
import lupos.engine.operators.singleinput.Filter;
import lupos.sparql1_1.ASTFilterConstraint;
import luposdate.evaluators.P2PIndexQueryEvaluator;
import luposdate.index.P2PIndexCollection;
import luposdate.operators.formatter.FilterFormatter;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class FilterFormatterTest {

	private Filter			filter;
	private String			filtername;
	private FilterFormatter	serializer;

	@Before
	public void setUp() throws Exception {
	//	filtername = new ASTFilterConstraint(0);
		this.filter = new Filter(filtername);
		this.serializer = new FilterFormatter();

	}

	@Test
	public void testSerialize() {

		int node_id = 123;
		try {
			JSONObject obj = this.serializer
					.serialize(filter, node_id);
			//assertEquals(obj.get("type"), filter.getClass().getName());
			//assertEquals(obj.get("node_id"), node_id);
			//assertEquals(obj.get("expression"), filtername);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	@Test
//	public void testDeserialize() {
//
//		int node_id = 123;
//
//		try {
//			P2PIndexCollection actual = (P2PIndexCollection) this.serializer
//					.deserialize(this.serializer.serialize(filter, node_id));
//
//			assertEquals(actual.getClass(), filter.getClass());
//
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
