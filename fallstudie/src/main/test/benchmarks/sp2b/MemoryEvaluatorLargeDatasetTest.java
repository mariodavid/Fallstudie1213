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
package benchmarks.sp2b;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import lupos.datastructures.items.literal.LiteralFactory;
import lupos.datastructures.items.literal.LiteralFactory.MapType;
import lupos.datastructures.items.literal.URILiteral;
import lupos.datastructures.queryresult.QueryResult;
import lupos.engine.evaluators.MemoryIndexQueryEvaluator;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class MemoryEvaluatorLargeDatasetTest extends Sp2bTest {

	static MemoryIndexQueryEvaluator memoryEvaluator;

	@BeforeClass
	public static void initDatastores() throws IOException {
		memoryEvaluator = initMemoryEvaluator();
		loadInMemory(default_file);

		loadInP2PNetwork(default_file);

	}

	@Test
	public void testQ1() throws Exception {

		String selectQuery = readFile(q1_query_filename);
		// System.out.println("lala: " +
		// ((P2PAdapter)p2pEvaluator.getP2PAdapter()).peer.getPeerBean().getStorage().map().size());
		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);

		// System.out
		// .println(p2pEvaluator
		// .getP2PAdapter()
		// .get("http://localhost/publications/articles/Journal1/1940/Article12"));

		assertEquals(expected, actual);
	}

	@Test
	public void testQ2() throws Exception {
		String selectQuery = readFile(q2_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);
		assertEquals(expected, actual);
	}

	@Test
	public void testQ2WithOptional() throws Exception {

		String selectQuery = readFile(q2_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println(expected);

		System.out.println(expected.toString().length());
		System.out.println(actual);
		System.out.println(actual.toString().length());
		assertEquals(expected, actual);
	}

	@Test
	public void testQ3a() throws Exception {

		String selectQuery = readFile(q3a_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ3b() throws Exception {

		String selectQuery = readFile(q3b_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ3c() throws Exception {

		String selectQuery = readFile(q3c_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ4() throws Exception {

		String selectQuery = readFile(q4_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ5a() throws Exception {

		String selectQuery = readFile(q5a_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ5b() throws Exception {

		String selectQuery = readFile(q5b_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ6() throws Exception {

		String selectQuery = readFile(q6_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);

		assertEquals(expected, actual);
	}

	@Ignore
	@Test
	public void testQ7() throws Exception {

		fail("Diese Anfrage wird nich unterstuetzt");

		String selectQuery = readFile(q7_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ8() throws Exception {

		String selectQuery = readFile(q8_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);

		assertEquals(expected, actual);
	}

	@Ignore
	@Test
	public void testQ9() throws Exception {

		fail("Diese Anfrage wird nich unterstuetzt");

		String selectQuery = readFile(q9_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ10() throws Exception {

		String selectQuery = readFile(q10_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ11() throws Exception {

		String selectQuery = readFile(q11_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ12a() throws Exception {

		String selectQuery = readFile(q12a_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ12b() throws Exception {

		String selectQuery = readFile(q12b_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);

		assertEquals(expected, actual);
	}

	@Test
	public void testQ12c() throws Exception {

		String selectQuery = readFile(q12c_query_filename);

		QueryResult expected = executeQuery(memoryEvaluator, selectQuery);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);

		System.out.println("expected:" + expected);
		System.out.println("actual:  " + actual);

		assertEquals(expected, actual);
	}

	private static MemoryIndexQueryEvaluator initMemoryEvaluator() {
		try {
			memoryEvaluator = new MemoryIndexQueryEvaluator();
			Collection<URILiteral> defaultGraphs = new LinkedList<URILiteral>();
			defaultGraphs.add(LiteralFactory
					.createURILiteralWithoutLazyLiteral("<inlinedata:>"));

			LiteralFactory.setType(MapType.NOCODEMAP);
			Collection<URILiteral> namedGraphs = new LinkedList<URILiteral>();
			memoryEvaluator.prepareInputData(defaultGraphs, namedGraphs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return memoryEvaluator;
	}

	private static void loadInMemory(String file) {
		try {
			memoryEvaluator.getResult(full_filepath(file),
					"SELECT * WHERE {<s> <p> <o>}");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String full_filepath(String filename) {
		return MemoryEvaluatorLargeDatasetTest.class.getClassLoader()
				.getResource(filename).getPath();
	}

}
