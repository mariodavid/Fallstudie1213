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

	private Filter filter;
	private String filtername;
	private FilterFormatter serializer;

	@Before
	public void setUp() throws Exception {
		filtername = "FILTER((20 >= 10))";
		filter = new Filter(filtername);
		this.serializer = new FilterFormatter();

	}

	@Test
	public void testSerialize() {

		int node_id = 123;
		try {
			JSONObject obj = this.serializer.serialize(filter, node_id);
			assertEquals(obj.get("type"), filter.getClass().getName());
			assertEquals(obj.get("node_id"), node_id);
			assertEquals(obj.get("expression"), filtername + " .\n");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	 @Test
	 public void testDeserialize() {
	
	 int node_id = 123;
	
	 try {
	 Filter actual = (Filter) this.serializer
	 .deserialize(this.serializer.serialize(filter, node_id));
	
	 assertEquals(actual.getClass(), filter.getClass());
	 assertEquals(filter.toString(), filtername + " .\n");
	 } catch (JSONException e) {
	 // TODO Auto-generated catch block
	 e.printStackTrace();
	 }
	 }

}
