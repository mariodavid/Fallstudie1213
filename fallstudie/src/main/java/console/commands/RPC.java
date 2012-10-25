package console.commands;

import java.util.Scanner;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import console.P2PAdapter;

import net.tomp2p.connection.PeerConnection;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.builder.SendDirectBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;
import evaluators.P2PIndexQueryEvaluator;

public class RPC implements Command {

	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		String input = scanner.next();
		String message = scanner.next();

		Number160 contentHash = Number160.createHash(input);

		Number160 destination = peer.getPeerBean().getStorage()
				.findPeerIDForResponsibleContent(contentHash);
		String response = evaluator.getP2PAdapter().sendMessage(destination,
				message);

		System.out.println(response);

	}

	public String getDescription() {
		return "[text] sends a broadcast message to all nodes in its local peer map";
	}

}
