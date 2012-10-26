package p2p;

import java.io.IOException;
import java.util.Random;
import java.util.SortedSet;

import luposdate.evaluators.P2PIndexQueryEvaluator;
import net.tomp2p.connection.PeerConnection;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureChannelCreator;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.futures.FutureRouting;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.p2p.builder.SendDirectBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.RawDataReply;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.timeout.TimeoutException;

import p2p.distribution.DistributionFactory;
import p2p.distribution.DistributionStrategy;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Der P2P Adapter stellt das Bindeglied zwischen dem P2P Netzwerk und dem
 * Lupos-Server dar. Daher gibt es in dieser Klasse sowohl eine Refenz vom Peer
 * Objekt, sowie von der Lupos Evaluator Instanz.
 */
public class P2PAdapter {
	/**
	 * Die Standard Strategie die genutzt wird zum verteilen der Triple im P2P
	 * Netzwerk.
	 */
	private static final int DEFAULT_DISTRIBUTION_STRATEGY = 1;
	/** Timeout in ms. */
	public static final int TIMEOUT = 5000;
	/** Lupos Evaluator. */
	public P2PIndexQueryEvaluator evaluator;
	/** Peer Referenz. */
	public Peer peer;
	/** aktuelle Strategie. */
	public DistributionStrategy distributionStrategy;

	/**
	 * Instanziiert ein neuen P2P Adapter. Als Übergabewert wird swohl die Peer
	 * Referenz als auch die Lupos Evaluator Referenz.
	 * 
	 * @param peer
	 *            der Knoten
	 * @param evaluator
	 *            Lupos Evaluator
	 */
	public P2PAdapter(Peer peer, P2PIndexQueryEvaluator evaluator) {
		this.evaluator = evaluator;
		this.peer = peer;
		listenForDataMessages();
		initDistributionStrategy();
	}

	/**
	 * Beim Aufrufen dieser Methode wird ein Listener erzeugt. Ankommende
	 * Nachrichten werden hier verarbeitet. Die Kommunikation läuft über Netty.
	 */
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

	/**
	 * Gibt den Peer zurück.
	 * 
	 * @return den Peer
	 */
	public Peer getPeer() {
		return peer;
	}

	/**
	 * Setzt den Peer.
	 * 
	 * @param peer
	 *            neuer Peer
	 */
	public void setPeer(Peer peer) {
		this.peer = peer;
	}

	/**
	 * Gibt die Verteilungsstrategie zurück.
	 * 
	 * @return die Verteilungsstrategie
	 */
	public DistributionStrategy getDistributionStrategy() {
		return distributionStrategy;
	}

	/**
	 * Setzt die Verteilungsstrategie.
	 * 
	 * @param distributionStrategy
	 *            die neue Verteilungsstrategie
	 */
	public void setDistributionStrategy(
			DistributionStrategy distributionStrategy) {
		this.distributionStrategy = distributionStrategy;
		this.distributionStrategy.setPeer(peer);
	}

	/**
	 * Initialisierung der Verteilungsstrategie anhand der Standard Distribution
	 * Strategie.
	 */
	private void initDistributionStrategy() {
		distributionStrategy = DistributionFactory
				.create(DEFAULT_DISTRIBUTION_STRATEGY);
		distributionStrategy.setPeer(peer);
	}

	/**
	 * Mit dieser Methode wird ein neuer Knoten erzeugt und mit dem Netzwerk
	 * verbunden.
	 */
	public void connect() {

		PeerMaker maker = new PeerMaker(new Number160(new Random(500000)))
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

	/**
	 * Mit dieser Methode kann man eine Nachricht an andere Teilnehmer des
	 * P2P-Netzwerk senden. Dabei wird als Ziel-Adresse der locationKey
	 * angegeben. Das heißt, wenn ein bestimmter Knoten für den locationKey
	 * zuständig ist erhällt er die Nachricht. Die Kommunikation läuft synchron
	 * ab.
	 * 
	 * @param locationKey
	 *            der location Key
	 * @param message
	 *            die Nachricht
	 * @return die Antwort auf die Nachricht als String.
	 */
	public String sendMessage(Number160 locationKey, String message) {
		SendDirectBuilder sendBuilder = peer.sendDirect();

		PeerAddress pa = this.getPeerAddressFromLocationKey(locationKey);
		PeerConnection pc = peer.createPeerConnection(pa, TIMEOUT);
		sendBuilder.setConnection(pc);
		sendBuilder.setBuffer(ChannelBuffers.wrappedBuffer(message.getBytes()));

		final FutureResponse response = sendBuilder.start()
				.awaitUninterruptibly();
		pc.close();

		return response.getBuffer().toString("UTF-8");
	}

	/**
	 * Diese Methode gibt die Address Informationen zurück anhand eines Location
	 * Keys. Dabei wird zunächst eine Route durch das gesamte Netzwerk gesucht
	 * und dann der letzte Knoten zurückgegeben.
	 * 
	 * @param locationKey der Location Key
	 * @return die Addresse des zuständigen Knoten als PeerAddress Obejkt
	 */
	public PeerAddress getPeerAddressFromLocationKey(Number160 locationKey) {
		FutureChannelCreator channel = peer.getConnectionBean()
				.getConnectionReservation().reserve(2);
		boolean success = channel.awaitUninterruptibly(TIMEOUT);
		if (!success) {
			peer.getConnectionBean().getConnectionReservation()
					.release(channel.getChannelCreator());
			throw new TimeoutException(
					"Could not find nearest peers. (Timeout)");
		}
		
		FutureRouting fRoute = peer.getDistributedRouting().route(locationKey,
				null, null, net.tomp2p.message.Message.Type.REQUEST_1, 3, 5, 5,
				5, 2, true, channel.getChannelCreator());
		fRoute.awaitUninterruptibly(TIMEOUT);
		SortedSet<PeerAddress> route = fRoute.getRoutingPath();
		peer.getConnectionBean().getConnectionReservation()
				.release(channel.getChannelCreator());
		
		return route.first();
	}
}
