package console.commands;

import java.util.Scanner;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import console.P2PAdapter;

import net.tomp2p.connection.PeerConnection;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import evaluators.P2PIndexQueryEvaluator;

public class RPC implements Command {

	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

//		for (PeerAddress p : peer.getPeerBean().getPeerMap().getAll()) {
//			try {
//				FutureData futureData = peer.send(p, scanner.next());
//
//				futureData.awaitUninterruptibly();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//		}


		String input = scanner.next();
		
		Number160 contentHash = Number160.createHash(input);


//		// FutureTracker ft = peer.getTracker(contentHash).start();
//		//
//		// ft.awaitUninterruptibly();
//		
//		// finds the peer id for the given content hash
//		Number160 destination = peer.getPeerBean().getStorage()
//				.findPeerIDForResponsibleContent(contentHash);
//
//
//		// if destination is null, no peer is responsible for this content
//		// in this case, the destination is this node
//		if (destination == null) {
//			System.out.println("Der null fall ist aufgetreten");
//			destination = peer.getPeerID();
//		}
//
//		System.out.println(destination);
//		evaluator.getP2PAdapter().send(input, destination);
		
//		ChannelBuffers.copiedBuffer(ChannelBuffers.BIG_ENDIAN,"HALLLLOOO", "UTF-8");
		Number160 destination = peer.getPeerBean().getStorage().findPeerIDForResponsibleContent(contentHash);
		ChannelBuffer cb = ChannelBuffers.copiedBuffer("Halllllooooo".getBytes());
		System.out.println("Die Peer ID ist: " + destination);
		PeerAddress pa = evaluator.getP2PAdapter().getPeerAddress(destination);
		System.out.println(pa);
		peer.getDirectDataRPC().send(pa, cb, false, peer.createPeerConnection(pa, 5000).getChannelCreator(), false);

	}

	public String getDescription() {
		return "[text] sends a broadcast message to all nodes in its local peer map";
	}

}
