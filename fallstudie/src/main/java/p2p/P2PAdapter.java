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
package p2p;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;

import lupos.datastructures.items.Triple;
import lupos.datastructures.items.literal.Literal;
import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.index.Root;
import lupos.engine.operators.messages.BoundVariablesMessage;
import luposdate.evaluators.P2PIndexQueryEvaluator;
import luposdate.operators.P2PApplication;
import luposdate.operators.formatter.SubGraphContainerFormatter;
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

import org.jboss.netty.handler.timeout.TimeoutException;
import org.json.JSONObject;

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
	public static int DISTRIBUTION_STRATEGY = 7;
	/** Timeout in ms. */
	public static final int TIMEOUT = 5000;
	/** Lupos Evaluator. */
	public P2PIndexQueryEvaluator evaluator;
	/** Peer Referenz. */
	public Peer peer;
	/** aktuelle Verteilungsstrategie. */
	public DistributionStrategy distributionStrategy;
	private Collection<Triple> result;
	private String key;
	private Boolean isReady;
	private Boolean subGraphStrategy;

	/**
	 * Instanziiert ein neuen P2P Adapter. Als Übergabewert wird swohl die Peer
	 * Referenz als auch die Lupos Evaluator Referenz.
	 * 
	 * @param peer
	 *            der Knoten
	 */
	public P2PAdapter(Peer peer) {
		this.peer = peer;
		this.subGraphStrategy = true;
		listenForDataMessages();
		initDistributionStrategy();
	}

	public void setSubGraphStrategy(Boolean subGraphStrategy) {
		this.subGraphStrategy = subGraphStrategy;
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
				Root oldRoot = evaluator.getRoot();

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

				// hier evtl. gegen loesung austauschen, dass man fur den
				// empfang von nachrichten eine eigene evaluator instanz
				// verwendet
				evaluator.setRoot(oldRoot);

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
		DISTRIBUTION_STRATEGY = distributionStrategy.getStrategyID();
		this.distributionStrategy = distributionStrategy;
		this.distributionStrategy.setPeer(peer);
	}

	/**
	 * Initialisierung der Verteilungsstrategie anhand der Standard Distribution
	 * Strategie.
	 */
	private void initDistributionStrategy() {
		distributionStrategy = DistributionFactory
				.create(DISTRIBUTION_STRATEGY);
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

	public void sendMessage(Number160 locationKey, Object message) {
		RequestP2PConfiguration requestP2PConfiguration = new RequestP2PConfiguration(
				1, 10, 0);
		FutureDHT futureDHT = peer.send(locationKey).setObject(message)
				.setRequestP2PConfiguration(requestP2PConfiguration)
				.setRefreshSeconds(0).setDirectReplication(false).start();
		// futureDHT.awaitUninterruptibly();
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

	public Number160 getPeerIDFromLocationKey(Number160 locationKey) {
		Number160 result = peer.getPeerBean().getStorage()
				.findPeerIDForResponsibleContent(locationKey);

		if (result == null) {
			result = getPeerAddressFromLocationKey(locationKey).getID();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see p2p.DataStoreAdapter#get(java.lang.String)
	 */
	public Collection<Triple> get(List<Literal> key) {
		return this.getDistributionStrategy().get(key);
	}

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

	public boolean isSubGraphStrategy() {
		return subGraphStrategy;
	}
}