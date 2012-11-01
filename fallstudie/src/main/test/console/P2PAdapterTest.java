package console;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Random;

import luposdate.LuposServer;
import net.tomp2p.examples.ExampleUtils;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import p2p.P2PAdapter;


public class P2PAdapterTest {
	// general
	private static final int	NODES		= 3;
	private Peer[] peers;
	private final P2PAdapter[] p2pAdapter = new P2PAdapter[NODES];
	Peer master = null;

	@Before
	public void setup() throws Exception {
		// create n peers
		this.peers = ExampleUtils.createAndAttachNodes(NODES, 4505);
		ExampleUtils.bootstrap(peers);
		master = peers[0];

		// Lupos Server starten
		LuposServer lup;
		for (int i = 0; i < peers.length; i++) {
			lup = new LuposServer();
			p2pAdapter[i] = new P2PAdapter(peers[i]);

			lup.start(p2pAdapter[i]);

			p2pAdapter[i].setEvaluator(lup.getEvaluator());

		}
	}

	@Test
	public void testExecute() throws IOException, InterruptedException {
		Random gen = new Random();
		Number160 contentKey = Number160.createHash("anyKey");
		String testMessage = "{\"edges\":[{\"to\":2,\"from\":1},{\"to\":3,\"from\":2}],\"nodes\":[{\"root\":true,\"type\":\"luposdate.index.P2PIndexCollection\",\"node_id\":1},{\"type\":\"luposdate.index.P2PIndexScan\",\"triple_pattern\":[{\"items\":[{\"name\":\"s\",\"type\":\"variable\"},{\"value\":\"<p>\",\"type\":\"literal\"},{\"name\":\"o\",\"type\":\"variable\"}]}],\"node_id\":2},{\"request_id\":0,\"dest_ip\":\"0.0.0.0\",\"type\":\"lupos.engine.operators.singleinput.Result\",\"node_id\":3}]}";
		String response = p2pAdapter[gen.nextInt(NODES-1)].sendMessage(contentKey,
				testMessage);
		assertEquals(response, testMessage);
	}

	@After
	public void tearDown() throws Exception {
		master.shutdown();
	}
}
