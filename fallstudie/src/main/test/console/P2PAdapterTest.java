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
	private static final int NODES = 50;
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
			p2pAdapter[i].setEvaluator(lup.getEvaluator());

			lup.start(p2pAdapter[i]);
		}
	}

	@Test
	public void testExecute() throws IOException, InterruptedException {
		Random gen = new Random();
		Number160 contentKey = Number160.createHash("anyKey");
		String testMessage = "HAAAAALLLLLLLLOOOO";
		String response = p2pAdapter[gen.nextInt(NODES-1)].sendMessage(contentKey,
				testMessage);
		assertEquals(response, "Deine Nachricht war: " + testMessage);
	}

	@After
	public void tearDown() throws Exception {
		master.shutdown();
	}
}
