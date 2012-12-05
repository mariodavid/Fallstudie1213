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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Random;

import lupos.datastructures.queryresult.QueryResult;
import lupos.engine.evaluators.BasicIndexQueryEvaluator;
import lupos.gui.Demo_Applet;
import lupos.gui.GUI;
import lupos.gui.operatorgraph.graphwrapper.GraphWrapperBasicOperator;
import lupos.gui.operatorgraph.viewer.Viewer;
import luposdate.LuposServer;
import luposdate.evaluators.P2PIndexQueryEvaluator;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.Number480;
import net.tomp2p.peers.PeerStat;
import net.tomp2p.storage.Data;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import p2p.P2PAdapter;
import p2p.P2PConnection;
import xpref.XPref;
import console.commands.LoadN3;

public class Sp2bTest {

	protected static final String file_100_triples = "benchmarks/sp2b/sp2b_100.n3";
	protected static final String file_1000_triples = "benchmarks/sp2b/sp2b_1000.n3";
	protected static final String file_10000_triples = "benchmarks/sp2b/sp2b_10000.n3";
	protected static final String file_500_triples = "benchmarks/sp2b/sp2b_500.n3";
	protected static final String file_5000_triples = "benchmarks/sp2b/sp2b_5000.n3";
	protected static final String file_50000_triples = "benchmarks/sp2b/sp2b_50000.n3";
	protected static final String file_q1 = "benchmarks/sp2b/sp2b_q1.n3";
	protected static final String file_q2 = "benchmarks/sp2b/sp2b_q2.n3";
	protected static final String file_q2_with_optional = "benchmarks/sp2b/sp2b_q2_with_optional.n3";
	protected static final String file_q3a = "benchmarks/sp2b/sp2b_q3a.n3";
	protected static final String file_q3b = "benchmarks/sp2b/sp2b_q3b.n3";
	protected static final String file_q3c = "benchmarks/sp2b/sp2b_q3c.n3";
	protected static final String file_q4 = "benchmarks/sp2b/sp2b_q4.n3";
	protected static final String file_q5a = "benchmarks/sp2b/sp2b_q5a.n3";
	protected static final String file_q5b = "benchmarks/sp2b/sp2b_q5b.n3";
	protected static final String file_q6 = "benchmarks/sp2b/sp2b_q6.n3";
	protected static final String file_q7 = "benchmarks/sp2b/sp2b_q7.n3";
	protected static final String file_q7_with_optional = "benchmarks/sp2b/sp2b_q7_with_optional.n3";
	protected static final String file_q8 = "benchmarks/sp2b/sp2b_q8.n3";
	protected static final String file_q9 = "benchmarks/sp2b/sp2b_q9.n3";
	protected static final String file_q10 = "benchmarks/sp2b/sp2b_q10.n3";
	protected static final String file_q11 = "benchmarks/sp2b/sp2b_q11.n3";
	protected static final String file_q12a = "benchmarks/sp2b/sp2b_q12a.n3";
	protected static final String file_q12b = "benchmarks/sp2b/sp2b_q12b.n3";
	protected static final String file_q12c = "benchmarks/sp2b/sp2b_q12c.n3";

	protected static final String default_file = file_1000_triples;

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

	protected static P2PIndexQueryEvaluator p2pEvaluator;
	private static P2PAdapter p2pAdapter;

	@BeforeClass
	public static void initP2PNetwork() {
		p2pEvaluator = initP2PQueryEvaluator();

	}

	protected static void loadInP2PNetwork(String file) throws IOException {
		LoadN3 loader = new LoadN3();
		loader.load(p2pEvaluator, file);

		NavigableMap<Number480, Data> p2pMap = ((P2PAdapter) p2pEvaluator
				.getP2PAdapter()).peer.getPeerBean().getStorage().map();

		while (!p2pAdapter.getDistributionStrategy().isDistributionReady()) {
			System.out.println(p2pMap.size());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

//			Thread.sleep(10000);
//			NavigableMap<Number480, Data> p2pMap3 = ((P2PAdapter) adapters[2]).peer
//					.getPeerBean().getStorage().map();
//			System.out.println("fertig 3: " + p2pMap3.size());
		}
//		NavigableMap<Number480, Data> p2pMap2 = ((P2PAdapter) adapters[1]).peer
//				.getPeerBean().getStorage().map();
//		System.out.println("fertig 1: " + p2pMap.size());
//		System.out.println("fertig 2: " + p2pMap2.size());

//		System.out.println("fertig:" + p2pMap.size());

	}

	@AfterClass
	public static void tearDown() {
		p2pAdapter.peer.shutdown();
	}

	protected static P2PIndexQueryEvaluator initP2PQueryEvaluator() {
		int nodes = 1;
		adapters = new P2PAdapter[nodes];
		LuposServer[] luposServer = new LuposServer[nodes];

		try {
			// P2PConnection connection = new P2PConnection();
			// connection.connect();
			Peer[] peers = createAndAttachNodes(nodes, 4567);
			bootstrap(peers);
			// ((P2PAdapter)p2pEvaluator.getP2PAdapter()).peer = peers[0];
			for (int i = 0; i < nodes; i++) {
				luposServer[i] = new LuposServer();
				adapters[i] = new P2PAdapter(peers[i]);
				luposServer[i].start(adapters[i]);
				adapters[i].setEvaluator(luposServer[i].getEvaluator());
			}

			p2pAdapter = adapters[0];
			return luposServer[0].getEvaluator();
			// } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected QueryResult executeQuery(BasicIndexQueryEvaluator evaluator,
			String query) throws Exception {
		evaluator.compileQuery(query);
		evaluator.logicalOptimization();
		evaluator.physicalOptimization();

		return evaluator.getResult();
	}

	protected QueryResult executeQueryWithGraphVisualization(
			BasicIndexQueryEvaluator evaluator, String query) throws Exception {

		evaluator.compileQuery(query);
		evaluator.logicalOptimization();
		evaluator.physicalOptimization();

		try {
			XPref.getInstance(Demo_Applet.class
					.getResource("/preferencesMenu.xml"));
		} catch (Exception e) {
			XPref.getInstance(new URL("file:"
					+ GUI.class.getResource("/preferencesMenu.xml").getFile()));
		}
		new Viewer(new GraphWrapperBasicOperator(evaluator.getRootNode()),
				"test", true, false);

		return evaluator.getResult();

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

	private static final Random RND = new Random(42L);
	private static P2PAdapter[] adapters;

	public static Peer[] createAndAttachNodes(int nr, int port)
			throws IOException {
		Peer[] peers = new Peer[nr];
		for (int i = 0; i < nr; i++) {
			if (i == 0) {
				peers[0] = new PeerMaker(new Number160(RND)).setPorts(port)
						.makeAndListen();
			} else {
				peers[i] = new PeerMaker(new Number160(RND)).setMasterPeer(
						peers[0]).makeAndListen();
			}
		}
		return peers;
	}

	public static void bootstrap(Peer[] peers) {
		List<FutureBootstrap> futures1 = new ArrayList<FutureBootstrap>();
		List<FutureDiscover> futures2 = new ArrayList<FutureDiscover>();
		for (int i = 1; i < peers.length; i++) {
			FutureDiscover tmp = peers[i].discover()
					.setPeerAddress(peers[0].getPeerAddress()).start();
			futures2.add(tmp);
		}
		for (FutureDiscover future : futures2) {
			future.awaitUninterruptibly();
		}
		for (int i = 1; i < peers.length; i++) {
			FutureBootstrap tmp = peers[i].bootstrap()
					.setPeerAddress(peers[0].getPeerAddress()).start();
			futures1.add(tmp);
		}
		for (int i = 1; i < peers.length; i++) {
			FutureBootstrap tmp = peers[0].bootstrap()
					.setPeerAddress(peers[i].getPeerAddress()).start();
			futures1.add(tmp);
		}
		for (FutureBootstrap future : futures1) {
			future.awaitUninterruptibly();
		}
	}

}
