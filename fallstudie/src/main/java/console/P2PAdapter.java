package console;

import java.io.IOException;
import java.util.Random;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.p2p.RequestP2PConfiguration;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;
import distribution.DistributionFactory;
import distribution.DistributionStrategy;

public class P2PAdapter {

	/**
	 * the strategy that is used by the p2p network to distribute the triples. 1
	 * = OneKeyDistribution (h(s),h(p),h(o)) 2 = TwoKeyDistribution
	 * (h(sp),h(po),h(so)) 3 = ThreeKeyDistribution (h(spo))
	 */
	private static final int	DEFAULT_DISTRIBUTION_STRATEGY	= 1;
	public Peer					peer;
	public DistributionStrategy	distributionStrategy;

	public P2PAdapter(Peer peer) {
		this.peer = peer;
		/*
		 * Am Anfang wird das PeerAdress Objekt in das Netzwerk gespeichert, so
		 * dass jeder Knoten dazu in der Lage ist anhand der peer id die exakte
		 * Adresse des Knoten heraus zu bekommen.
		 */
		try {
			peer.put(Number160.createHash(peer.getPeerID() + ""))
					.setData(new Data(peer.getPeerAddress())).start()
					.awaitUninterruptibly();
		} catch (IOException e) {
			e.printStackTrace();
		}
		initDistributionStrategy();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		FutureBootstrap fb = peer.bootstrap().setBroadcast().setPorts(4000)
				.start();
		fb.awaitUninterruptibly();

	}

	public void send(String msg, Number160 destination) {

		RequestP2PConfiguration config = new RequestP2PConfiguration(1, 5, 0);
		FutureDHT future = peer.send(destination).setObject(msg)
				.setRequestP2PConfiguration(config).start();

		future.awaitUninterruptibly();

	}
	
	public PeerAddress getPeerAddress(Number160 destination) {
		try {
			FutureDHT future = peer.get(Number160.createHash(destination.toString())).setAll().start();
			future.awaitUninterruptibly();
			if (future.isSuccess()) {
				for (Data result : future.getDataMap().values()) {
					if (result.getObject().getClass() == PeerAddress.class) {
						return (PeerAddress) result.getObject();
					} else {
						System.out.println("Unbekannter Fehler 1!");
						return null;
					}
				}
			} else {
				System.out.println("PeerAddress nicht vorhanden!");
				return null;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Unbekannter Fehler 2!");
		return null;
	}

}
