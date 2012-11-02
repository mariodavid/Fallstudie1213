package p2p;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.SortedSet;

import lupos.datastructures.items.Triple;
import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.messages.BoundVariablesMessage;
import luposdate.evaluators.P2PIndexQueryEvaluator;
import luposdate.operators.P2PApplication;
import luposdate.operators.formatter.SubGraphContainerFormatter;
import net.tomp2p.connection.PeerConnection;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureChannelCreator;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.futures.FutureRouting;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.p2p.RequestP2PConfiguration;
import net.tomp2p.p2p.builder.SendDirectBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.rpc.RawDataReply;
import net.tomp2p.storage.Data;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.timeout.TimeoutException;
import org.json.JSONObject;

import com.google.protobuf.InvalidProtocolBufferException;

import p2p.distribution.DistributionFactory;
import p2p.distribution.DistributionStrategy;

// TODO: Auto-generated Javadoc
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
	private static final int		DEFAULT_DISTRIBUTION_STRATEGY	= 1;
	/** Timeout in ms. */
	public static final int			TIMEOUT							= 5000;
	/** Lupos Evaluator. */
	public P2PIndexQueryEvaluator	evaluator;
	/** Peer Referenz. */
	public Peer						peer;
	/** aktuelle Verteilungsstrategie. */
	public DistributionStrategy		distributionStrategy;
	private Collection<Triple>		result;
	private String					key;
	private Boolean					isReady;

	/**
	 * Instanziiert ein neuen P2P Adapter. Als Übergabewert wird swohl die Peer
	 * Referenz als auch die Lupos Evaluator Referenz.
	 * 
	 * @param peer
	 *            der Knoten
	 */
	public P2PAdapter(Peer peer) {
		this.peer = peer;
		listenForDataMessages();
		initDistributionStrategy();
	}

	/**
	 * Sets the evaluator.
	 * 
	 * @param evaluator
	 *            the new evaluator
	 */
	public void setEvaluator(P2PIndexQueryEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	/**
	 * Beim Aufrufen dieser Methode wird ein Listener erzeugt. Ankommende
	 * Nachrichten werden hier verarbeitet. Die Kommunikation läuft über Netty.
	 */
	private void listenForDataMessages() {
		peer.setObjectDataReply(new ObjectDataReply() {
			public Object reply(PeerAddress sender, Object request)
					throws Exception {

				System.out.println("received request: " + request);

				P2PApplication p2pApplication = new P2PApplication();
				SubGraphContainerFormatter deserializer = new SubGraphContainerFormatter(
						evaluator.getDataset(), p2pApplication);

				BasicOperator rootNode = deserializer
						.deserialize(new JSONObject((String) request));

				// erzeugt die Vorgaenger der Collection, wie bei
				// addSucceedingOperator
				// (rekursiv fuer den gesamten Baum)
				rootNode.setParents();

				// erkennt zyklen im op graphen (vermutlich nicht
				// relevant,
				// evtl. bei
				// spaeteren erweiterungen relevant)
				rootNode.detectCycles();

				// berechnet an welcher stelle welche variablen gebunden
				// sind und gebunden sein koennen
				rootNode.sendMessage(new BoundVariablesMessage());

				evaluator.setRootNode(rootNode);

				evaluator.evaluateQuery();


				// try {
				// XPref.getInstance(Demo_Applet.class
				// .getResource("/preferencesMenu.xml"));
				// } catch (Exception e) {
				// XPref.getInstance(new URL("file:"
				// + GUI.class.getResource("/preferencesMenu.xml")
				// .getFile()));
				// }
				// new Viewer(new GraphWrapperBasicOperator(evaluator
				// .getRootNode()), "test", true, false);

				return p2pApplication.getResult();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see p2p.DataStoreAdapter#getDistributionStrategy()
	 */
	public DistributionStrategy getDistributionStrategy() {
		return distributionStrategy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see p2p.DataStoreAdapter#setDistributionStrategy(p2p.distribution.
	 * DistributionStrategy)
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

	public String sendMessage(String locationKey, String message) {
		return sendMessage(Number160.createHash(locationKey), message);
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
	
	// testing purpose
	public void listenForDirectDataMessages() {
		this.peer.setRawDataReply(new RawDataReply() {

			public ChannelBuffer reply(final PeerAddress sender,
					final ChannelBuffer requestBuffer)
					throws InvalidProtocolBufferException {
				System.out.println("Empfange ...");
				ChannelBufferInputStream inStream = new ChannelBufferInputStream(
						requestBuffer);
				Scanner inReader = new Scanner(inStream);

				for (int i = 0; i < 1000000; i++) {
					if (inReader.hasNext())
						System.out.println("kam an: " + inReader.nextLine());
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				System.out.println("Uund wieder weg");
				// System.out.println(receivedMessage);

				/**
				 * AN DIESER STELLE MUSS DIE ANKOMMENDE NACHRICHT VERARBEITET
				 * WERDEN!!!
				 */

				// momentan wird einfach die Nachricht zur√ºck geschickt
				return null;
				// return ChannelBuffers
				// .wrappedBuffer(("Deine Nachricht war: " + receivedMessage)
				// .getBytes());
			}
		});
	}

	//testing purpose
	public String sendMessageDirect(Number160 destination, String message) {
		System.out.println("Sende ...");
		SendDirectBuilder sendBuilder = peer.sendDirect();
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer(512);
		ChannelBufferOutputStream outStream = new ChannelBufferOutputStream(
				buffer);
		PeerAddress pa = this.getPeerAddressFromLocationKey(destination);
		PeerConnection pc = peer.createPeerConnection(pa, TIMEOUT);
		sendBuilder.setConnection(pc);
		sendBuilder.setBuffer(buffer);

		final FutureResponse response = sendBuilder.start();

		PrintWriter pw = new PrintWriter(outStream);
		for (int i = 0; i < 2; i++) {
			System.out.println("sende  " + i);
			pw.println("Halllloo " + i);
			pw.flush();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		response.awaitUninterruptibly();
		try {
			outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pc.close();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see p2p.DataStoreAdapter#get(java.lang.String)
	 */
	public Collection<Triple> get(String key) {

		result = new LinkedList<Triple>();
		isReady = false;

		// perform p2p operation
		FutureDHT future = getPeer().get(Number160.createHash(key)).setAll()
				.start();
		future.addListener(new BaseFutureAdapter<FutureDHT>() {
			public void operationComplete(FutureDHT future) throws Exception {
				if (future.isSuccess()) {
					System.out.println("success");

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

					isReady = true;

				} else {
					System.out.println("failure");
					isReady = true;
				}
			}
		});

		while (!isReady) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	// public Collection<Triple> get(String key2) {
	//
	// this.key = key2;
	//
	// result = new LinkedList<Triple>();
	//
	// Runnable run = new Runnable() {
	//
	//
	// public void run() {
	// // perform p2p operation
	// FutureDHT future = getPeer()
	// .get(Number160.createHash(key)).setAll().start();
	// future.awaitUninterruptibly();
	//
	// // add all p2p results to the result collection
	// for (Data r : future.getDataMap().values()) {
	// try {
	// result.add((Triple) r.getObject());
	//
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// }
	// };
	// Thread myTherad = new Thread(run);
	// myTherad.start();
	//
	// try {
	// Thread.sleep(2000);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return result;
	// }
	//

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see p2p.DataStoreAdapter#contains(lupos.datastructures.items.Triple)
	 */
	public boolean contains(Triple triple) {
		try {
			return this.getDistributionStrategy().contains(triple);
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Gibt den Evaluator zurück
	 * 
	 * @return den Evaluator
	 */
	public P2PIndexQueryEvaluator getEvaluator() {
		return this.evaluator;
	}
}
