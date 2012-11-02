package operators.formatter;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.LinkedList;

import lupos.datastructures.items.Variable;
import lupos.datastructures.items.literal.LazyLiteral;
import lupos.engine.operators.index.BasicIndex;
import lupos.engine.operators.index.memoryindex.IndexCollection;
import lupos.engine.operators.tripleoperator.TriplePattern;
import luposdate.index.P2PIndexScan;
import luposdate.operators.formatter.P2PIndexScanFormatter;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class IndexScanFormatterTest {

	private BasicIndex			indexScan;
	private P2PIndexScanFormatter	serializer;

	@Before
	public void setUp() throws Exception {
		IndexCollection indexCollection = new IndexCollection();

		this.indexScan = new P2PIndexScan(indexCollection);

		LinkedList<TriplePattern> patterns = new LinkedList<TriplePattern>();
		patterns.add(new TriplePattern(new Variable("s"), LazyLiteral
				.getLiteral("<p>"), new Variable("o")));
		indexScan.setTriplePatterns(patterns);

		this.serializer = new P2PIndexScanFormatter(indexCollection);

	}

	@Test
	public void testSerialize() {

		int node_id = 123;

		try {
			JSONObject obj = this.serializer.serialize(indexScan, node_id);
			assertEquals(P2PIndexScan.class.getName(), obj.get("type"));
			assertEquals(obj.get("node_id"), node_id);


		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDeserialize() {

		int node_id = 123;

		try {
			P2PIndexScan actualIndexScan = (P2PIndexScan) this.serializer
					.deserialize(this.serializer.serialize(indexScan, node_id));

			assertEquals(actualIndexScan.getClass(), indexScan.getClass());

			Iterator<TriplePattern> actualIterator = this.indexScan
					.getTriplePattern().iterator();
			Iterator<TriplePattern> expectedIterator = actualIndexScan
					.getTriplePattern().iterator();

			while (actualIterator.hasNext()) {
				TriplePattern actual = actualIterator.next();
				TriplePattern expected = expectedIterator.next();
				assertEquals(expected.getItems(), actual.getItems());

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
