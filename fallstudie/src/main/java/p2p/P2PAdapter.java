package p2p;

import java.io.IOException;
import java.util.Random;
import java.util.SortedSet;

import luposdate.evaluators.P2PIndexQueryEvaluator;
import net.tomp2p.connection.PeerConnection;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureChannelCreator;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.futures.FutureRouting;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.p2p.builder.SendDirectBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.RawDataReply;
import net.tomp2p.storage.Data;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.timeout.TimeoutException;

import p2p.distribution.DistributionFactory;
import p2p.distribution.DistributionStrategy;

import com.google.protobuf.InvalidProtocolBufferException;

public class P2PAdapter {

	/**
	 * the strategy that is used by the p2p network to distribute the triples. 1
	 * = OneKeyDistribution (h(s),h(p),h(o)) 2 = TwoKeyDistribution
	 * (h(sp),h(po),h(so)) 3 = ThreeKeyDistribution (h(spo))
	 */
	private static final int DEFAULT_DISTRIBUTION_STRATEGY = 1;
	public static final int IDLE_TCP_Millis = 5000;
	public P2PIndexQueryEvaluator evaluator;
	public Peer peer;
	public DistributionStrategy distributionStrategy;

	public P2PAdapter(Peer peer, P2PIndexQueryEvaluator evaluator) {
		this.evaluator = evaluator;
		this.peer = peer;
		listenForDataMessages();
		initDistributionStrategy();
	}

	private void listenForDataMessages() {
		this.peer.setRawDataReply(new RawDataReply() {

			public ChannelBuffer reply(final PeerAddress sender,
					final ChannelBuffer requestBuffer)
					throws InvalidProtocolBufferException {
				String receivedMessage = requestBuffer.toString("UTF-8");
				System.out.println(receivedMessage);

				/**
				 * AN DIESER STELLE MUSS DIE ANKOMMENDE NACHRICHT VERARBEITET
				 * WERDEN!!!
				 */

				// momentan wird einfach die Nachricht zurück geschickt
				return ChannelBuffers
						.wrappedBuffer(("Deine Nachricht war: " + receivedMessage)
								.getBytes());
			}
		});
	}

	public Peer getPeer() {
		return peer;
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}

	public DistributionStrategy getDistributionStrategy() {
		return distributionStrategy;
	}

	public void setDistributionStrategy(
			DistributionStrategy distributionStrategy) {
		this.distributionStrategy = distributionStrategy;
		this.distributionStrategy.setPeer(peer);
	}

	private void initDistributionStrategy() {
		distributionStrategy = DistributionFactory
				.create(DEFAULT_DISTRIBUTION_STRATEGY);
		distributionStrategy.setPeer(peer);
	}

	/**
	 * stellt die Verbindung zum P2P Netzwerk her
	 * 
	 * wird nur benoetigt, wenn dem P2P Adapter kein Peer uebergeben wird (wenn
	 * die Console nicht die Verbindung herstellt)
	 */
	public void connect() {

		PeerMaker maker = new PeerMaker(new Number160(new Random(50000)))
				.setPorts(4000);

		try {
			peer = maker.makeAndListen();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FutureBootstrap fb = peer.bootstrap().setBroadcast().setPorts(4000)
				.start();
		fb.awaitUninterruptibly();

	}
	
	public String sendMessage(Number160 locationKey, String message) {
		SendDirectBuilder sendBuilder = peer.sendDirect();

		PeerAddress pa = this.getPeerAddressFromLocationKey(locationKey);
		PeerConnection pc = peer.createPeerConnection(pa, IDLE_TCP_Millis);
		sendBuilder.setConnection(pc);
		sendBuilder.setBuffer(ChannelBuffers.wrappedBuffer(message.getBytes()));

		final FutureResponse response = sendBuilder.start()
				.awaitUninterruptibly();

		pc.close();
		return response.getBuffer().toString("UTF-8");
	}

	public PeerAddress getPeerAddressFromLocationKey(Number160 destination) {
		FutureChannelCreator channel = peer.getConnectionBean()
				.getConnectionReservation().reserve(2);
		boolean success = channel.awaitUninterruptibly(5000);
		if (!success) {
			peer.getConnectionBean().getConnectionReservation()
					.release(channel.getChannelCreator());
			throw new TimeoutException(
					"Could not find nearest peers. (Timeout)");
		}
		FutureRouting fRoute = peer.getDistributedRouting().route(destination,
				null, null, net.tomp2p.message.Message.Type.REQUEST_1, 3, 5, 5,
				5, 2, true, channel.getChannelCreator());
		fRoute.awaitUninterruptibly(5000);
		final SortedSet<PeerAddress> route = fRoute.getRoutingPath();
		peer.getConnectionBean().getConnectionReservation()
				.release(channel.getChannelCreator());
		
		System.out.println("Dieser Knoten ist dafuer zustaendig: "
				+ route.first());
		return route.first();
	}
}