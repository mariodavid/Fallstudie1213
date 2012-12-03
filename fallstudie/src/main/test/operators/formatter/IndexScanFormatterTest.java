/**
 * Copyright (c) 2012, Institute of Telematics, Institute of Information Systems (Dennis Pfisterer, Sven Groppe, Andreas Haller, Thomas Kiencke, Sebastian Walther, Mario David), University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package operators.formatter;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.LinkedList;

import lupos.datastructures.items.Variable;
import lupos.datastructures.items.literal.LazyLiteral;
import lupos.engine.operators.index.BasicIndexScan;
import lupos.engine.operators.index.Root;
import lupos.engine.operators.tripleoperator.TriplePattern;
import luposdate.index.P2PIndexCollection;
import luposdate.index.P2PIndexScan;
import luposdate.operators.formatter.P2PIndexScanFormatter;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class IndexScanFormatterTest {

	private BasicIndexScan			indexScan;
	private P2PIndexScanFormatter	serializer;

	@Before
	public void setUp() throws Exception {
		Root indexCollection = new P2PIndexCollection(null);

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
