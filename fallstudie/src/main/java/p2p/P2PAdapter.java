package p2p;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
import java.util.SortedSet;

import lupos.datastructures.items.Triple;
import luposdate.evaluators.P2PIndexQueryEvaluator;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureChannelCreator;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureRouting;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.p2p.RequestP2PConfiguration;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

import org.jboss.netty.handler.timeout.TimeoutException;

import p2p.distribution.DistributionFactory;
import p2p.distribution.DistributionStrategy;

/**
 * Der P2P Adapter stellt das Bindeglied zwischen dem P2P Netzwerk und dem
 * Lupos-Server dar. Daher gibt es in dieser Klasse sowohl eine Refenz vom Peer
 * Objekt, sowie von der Lupos Evaluator Instanz.
 */
public class P2PAdapter implements DataStoreAdapter {
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
		peer.setObjectDataReply(new ObjectDataReply() {
			public Object reply(PeerAddress sender, Object request)
					throws Exception {
				/**
				 * AN DIESER STELLE MUSS DIE ANKOMMENDE NACHRICHT VERARBEITET
				 * WERDEN!!!
				 */
				System.out.println(request);
				return "Deine Nachricht war: " + request;
			}
		});
	}

	/**
	 * Gibt den Peer zurück.
	 * 
	 * @return den Peer
	 */
	private Peer getPeer() {
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

	/* (non-Javadoc)
	 * @see p2p.DataStoreAdapter#getDistributionStrategy()
	 */
	public DistributionStrategy getDistributionStrategy() {
		return distributionStrategy;
	}

	/* (non-Javadoc)
	 * @see p2p.DataStoreAdapter#setDistributionStrategy(p2p.distribution.DistributionStrategy)
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
		RequestP2PConfiguration requestP2PConfiguration = new RequestP2PConfiguration(
				1, 10, 0);
		FutureDHT futureDHT = peer.send(locationKey).setObject(message)
				.setRequestP2PConfiguration(requestP2PConfiguration)
				.setRefreshSeconds(0).setDirectReplication(false).start();
		futureDHT.awaitUninterruptibly();

		for (Object object : futureDHT.getRawDirectData2().values()) {
			return object.toString();
		}

		return null;
	}

	/**
	 * Diese Methode gibt die Address Informationen zurück anhand eines Location
	 * Keys. Dabei wird zunächst eine Route durch das gesamte Netzwerk gesucht
	 * und dann der letzte Knoten zurückgegeben.
	 * 
	 * @param locationKey
	 *            der Location Key
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

	/* (non-Javadoc)
	 * @see p2p.DataStoreAdapter#get(java.lang.String)
	 */
	public Collection<Triple> get(String key) {

		Collection<Triple> result = new LinkedList<Triple>();

		// perform p2p operation
		FutureDHT future = this.getPeer().get(Number160.createHash(key))
				.setAll().start();
		future.awaitUninterruptibly();

		// add all p2p results to the result collection
		for (Data r : future.getDataMap().values()) {
			try {
				result.add((Triple) r.getObject());

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see p2p.DataStoreAdapter#add(lupos.datastructures.items.Triple)
	 */
	public boolean add(Triple triple) {
		try {
			this.getDistributionStrategy().distribute(triple);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see p2p.DataStoreAdapter#remove(lupos.datastructures.items.Triple)
	 */
	public boolean remove(Triple triple) {
		try {
			this.getDistributionStrategy().remove(triple);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see p2p.DataStoreAdapter#contains(lupos.datastructures.items.Triple)
	 */
	public boolean contains(Triple triple) {
		try {
			return this.getDistributionStrategy().contains(triple);
		} catch (IOException e) {
			return false;
		}
	}
}
