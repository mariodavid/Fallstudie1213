package console.commands;

import java.io.InputStream;
import java.util.Scanner;

import lupos.datastructures.queryresult.QueryResult;
import lupos.engine.evaluators.BasicIndexQueryEvaluator;
import luposdate.evaluators.P2PIndexQueryEvaluator;
import net.tomp2p.p2p.Peer;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Führt den SP2B Test aus und gibt die Zeit zurück.
 */
public class Sp2b implements Command {

	protected static final String q1_query_filename = "benchmarks/sp2b/queries/q1.sparql";
	protected static final String q2_query_filename = "benchmarks/sp2b/queries/q2.sparql";
	protected static final String q3a_query_filename = "benchmarks/sp2b/queries/q3a.sparql";
	protected static final String q3b_query_filename = "benchmarks/sp2b/queries/q3b.sparql";
	protected static final String q3c_query_filename = "benchmarks/sp2b/queries/q3c.sparql";
	protected static final String q4_query_filename = "benchmarks/sp2b/queries/q4.sparql";
	protected static final String q5a_query_filename = "benchmarks/sp2b/queries/q5a.sparql";
	protected static final String q5b_query_filename = "benchmarks/sp2b/queries/q5b.sparql";
	protected static final String q6_query_filename = "benchmarks/sp2b/queries/q6.sparql";
	protected static final String q7_query_filename = "benchmarks/sp2b/queries/q7.sparql";
	protected static final String q8_query_filename = "benchmarks/sp2b/queries/q8.sparql";
	protected static final String q9_query_filename = "benchmarks/sp2b/queries/q9.sparql";
	protected static final String q10_query_filename = "benchmarks/sp2b/queries/q10.sparql";
	protected static final String q11_query_filename = "benchmarks/sp2b/queries/q11.sparql";
	protected static final String q12a_query_filename = "benchmarks/sp2b/queries/q12a.sparql";
	protected static final String q12b_query_filename = "benchmarks/sp2b/queries/q12b.sparql";
	protected static final String q12c_query_filename = "benchmarks/sp2b/queries/q12c.sparql";

	private P2PIndexQueryEvaluator p2pEvaluator;
	private double[] results = new double[18];

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#execute(java.util.Scanner,
	 * net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {
		p2pEvaluator = evaluator;

		// Tests ausführen:
		try {
			System.out.println("Tests werden ausgeführt:");
			testQ1();
			testQ2();
			testQ2WithOptional();
			testQ3a();
			testQ3b();
			testQ3c();
			testQ4();
			testQ5a();
			testQ5b();
			testQ6();
			testQ7();
			testQ8();
			testQ9();
			testQ10();
			testQ11();
			testQ12a();
			testQ12b();
			testQ12c();

//			printCSV();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printCSV() {
		System.out.println("CSV Format: ");
		for (double result : results) {
			System.out.println(result);
		}
	}

	protected String readFile(String file) {
		InputStream is = getClass().getClassLoader().getResourceAsStream(file);
		String selectQuery = convertStreamToString(is);
		return selectQuery;
	}

	private String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	protected QueryResult executeQuery(BasicIndexQueryEvaluator evaluator,
			String query) throws Exception {
		evaluator.compileQuery(query);
		evaluator.logicalOptimization();
		evaluator.physicalOptimization();

		return evaluator.getResult();
	}

	@Test
	public void testQ1() throws Exception {
		long start = System.currentTimeMillis();
		String selectQuery = readFile(q1_query_filename);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		long stop = System.currentTimeMillis();
		results[0] = (double) ((stop - start) / (double) 1000);
		System.out.println("Test Q1: "
				+ (double) ((stop - start) / (double) 1000) + " Sekunden");
	}

	@Test
	public void testQ2() throws Exception {
		long start = System.currentTimeMillis();
		String selectQuery = readFile(q2_query_filename);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		long stop = System.currentTimeMillis();
		results[1] = (double) ((stop - start) / (double) 1000);
		System.out.println("Test Q2: "
				+ (double) ((stop - start) / (double) 1000) + " Sekunden");
	}

	@Test
	public void testQ2WithOptional() throws Exception {
		long start = System.currentTimeMillis();
		String selectQuery = readFile(q2_query_filename);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		long stop = System.currentTimeMillis();
		results[2] = (double) ((stop - start) / (double) 1000);
		System.out.println("Test Q2 (with Optional): "
				+ (double) ((stop - start) / (double) 1000) + " Sekunden");
	}

	@Test
	public void testQ3a() throws Exception {
		long start = System.currentTimeMillis();
		String selectQuery = readFile(q3a_query_filename);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		long stop = System.currentTimeMillis();
		results[3] = (double) ((stop - start) / (double) 1000);
		System.out.println("Test Q3a: "
				+ (double) ((stop - start) / (double) 1000) + " Sekunden");
	}

	@Test
	public void testQ3b() throws Exception {
		long start = System.currentTimeMillis();
		String selectQuery = readFile(q3b_query_filename);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		long stop = System.currentTimeMillis();
		results[4] = (double) ((stop - start) / (double) 1000);
		System.out.println("Test Q3b: "
				+ (double) ((stop - start) / (double) 1000) + " Sekunden");
	}

	@Test
	public void testQ3c() throws Exception {
		long start = System.currentTimeMillis();
		String selectQuery = readFile(q3c_query_filename);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		long stop = System.currentTimeMillis();
		results[5] = (double) ((stop - start) / (double) 1000);
		System.out.println("Test Q3c: "
				+ (double) ((stop - start) / (double) 1000) + " Sekunden");
	}

	@Test
	public void testQ4() throws Exception {
		long start = System.currentTimeMillis();
		String selectQuery = readFile(q4_query_filename);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		long stop = System.currentTimeMillis();
		results[6] = (double) ((stop - start) / (double) 1000);
		System.out.println("Test Q4 "
				+ (double) ((stop - start) / (double) 1000) + " Sekunden");
	}

	@Test
	public void testQ5a() throws Exception {
		long start = System.currentTimeMillis();
		String selectQuery = readFile(q5a_query_filename);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		long stop = System.currentTimeMillis();
		results[7] = (double) ((stop - start) / (double) 1000);
		System.out.println("Test Q5a: "
				+ (double) ((stop - start) / (double) 1000) + " Sekunden");
	}

	@Test
	public void testQ5b() throws Exception {
		long start = System.currentTimeMillis();
		String selectQuery = readFile(q5b_query_filename);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		long stop = System.currentTimeMillis();
		results[8] = (double) ((stop - start) / (double) 1000);
		System.out.println("Test Q5b: "
				+ (double) ((stop - start) / (double) 1000) + " Sekunden");
	}

	@Test
	public void testQ6() throws Exception {
		long start = System.currentTimeMillis();
		String selectQuery = readFile(q6_query_filename);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		long stop = System.currentTimeMillis();
		results[9] = (double) ((stop - start) / (double) 1000);
		System.out.println("Test Q6: "
				+ (double) ((stop - start) / (double) 1000) + " Sekunden");
	}

	@Ignore
	@Test
	public void testQ7() throws Exception {
		System.out.println("Test Q7: Diese Anfrage wird nich unterstuetzt");
		// String selectQuery = readFile(q7_query_filename);
		// QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		results[10] = 0;
	}

	@Test
	public void testQ8() throws Exception {
		long start = System.currentTimeMillis();
		String selectQuery = readFile(q8_query_filename);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		long stop = System.currentTimeMillis();
		results[11] = (double) ((stop - start) / (double) 1000);
		System.out.println("Test Q8: "
				+ (double) ((stop - start) / (double) 1000) + " Sekunden");
	}

	@Ignore
	@Test
	public void testQ9() throws Exception {
		System.out.println("Test Q9: Diese Anfrage wird nich unterstuetzt");
		// String selectQuery = readFile(q9_query_filename);
		// QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		results[12] = 0;
	}

	@Test
	public void testQ10() throws Exception {
		long start = System.currentTimeMillis();
		String selectQuery = readFile(q10_query_filename);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		long stop = System.currentTimeMillis();
		results[13] = (double) ((stop - start) / (double) 1000);
		System.out.println("Test Q10: "
				+ (double) ((stop - start) / (double) 1000) + " Sekunden");
	}

	@Test
	public void testQ11() throws Exception {
		long start = System.currentTimeMillis();
		String selectQuery = readFile(q11_query_filename);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		long stop = System.currentTimeMillis();
		results[14] = (double) ((stop - start) / (double) 1000);
		System.out.println("Test Q11: "
				+ (double) ((stop - start) / (double) 1000) + " Sekunden");
	}

	@Test
	public void testQ12a() throws Exception {
		long start = System.currentTimeMillis();
		String selectQuery = readFile(q12a_query_filename);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		long stop = System.currentTimeMillis();
		results[15] = (double) ((stop - start) / (double) 1000);
		System.out.println("Test Q12a: "
				+ (double) ((stop - start) / (double) 1000) + " Sekunden");
	}

	@Test
	public void testQ12b() throws Exception {
		long start = System.currentTimeMillis();
		String selectQuery = readFile(q12b_query_filename);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		long stop = System.currentTimeMillis();
		results[16] = (double) ((stop - start) / (double) 1000);
		System.out.println("Test Q12b: "
				+ (double) ((stop - start) / (double) 1000) + " Sekunden");
	}

	@Test
	public void testQ12c() throws Exception {
		long start = System.currentTimeMillis();
		String selectQuery = readFile(q12c_query_filename);
		QueryResult actual = executeQuery(p2pEvaluator, selectQuery);
		// System.out.println("actual:  " + actual);
		long stop = System.currentTimeMillis();
		results[17] = (double) ((stop - start) / (double) 1000);
		System.out.println("Test Q12c: "
				+ (double) ((stop - start) / (double) 1000) + " Sekunden");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {

		return "generate sp2b";
	}

}
