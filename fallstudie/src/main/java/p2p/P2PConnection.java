package p2p;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;

/**
 * Diese Klasse dient zur Initialisierung des Peers und der Verbindung mit dem
 * ganzen Netzwerk.
 */
public class P2PConnection {
	/** Standard Port. */
	private static final int DEFAULT_PORT = 4000;
	/** Peer Referenz. */
	private Peer peer;

	/**
	 * Gibt den Peer zurück.
	 * 
	 * @return den Peer
	 */
	public Peer getPeer() {
		return peer;
	}

	/**
	 * Es wird ein Peer Objekt erzeugt mit den angegebenen lokalen Port. Danach
	 * verbindet sich dieser Knoten mit einem anderen Konoten dessen IP+Port
	 * zusätzlich übergeben wird.
	 * 
	 * @param ip
	 *            IP eines anderen Knoten
	 * @param remotePort
	 *            Port eines anderen Knoten
	 * @param localPort
	 *            lokaler Port
	 * @return true, wenn das Erzeugen des Knotens erfolgreich war
	 */
	public boolean connect(String ip, int remotePort, int localPort)
			throws Exception, UnknownHostException, ClassNotFoundException,
			IOException {

		System.out.println("starting up p2p to " + ip + ":" + remotePort
				+ "...");

		this.peer = createPeer(localPort, -1);
		FutureBootstrap fb = this.peer.bootstrap()
				.setInetAddress(InetAddress.getByName(ip)).setPorts(remotePort)
				.start();
		fb.awaitUninterruptibly();
		return true;
	}

	/**
	 * Ohne Angaben von Parameter wird ein Peer auf dem Standard Port erzeugt
	 * und ein anderer Knoten im Netzwerk via Broadcast gesucht.
	 * 
	 * @return true, wenn das Erzeugen des Knotens erfolgreich war
	 */
	public boolean connect() throws Exception, ClassNotFoundException,
			IOException {

		System.out.println("starting up p2p via broadcast...");

		this.peer = this.createPeer(DEFAULT_PORT, -1);

		FutureBootstrap fb = this.peer.bootstrap().setPorts(DEFAULT_PORT)
				.setBroadcast().start();
		fb.awaitUninterruptibly();

		return true;
	}

	/**
	 * Erzeugt ein Peer Objekt mit den angegebenen Parametern.
	 * 
	 * @param port
	 *            Port
	 * @param id
	 *            ID des Knotens
	 * @return den fertigen Peer
	 */
	private Peer createPeer(int port, int id) throws Exception {

		Random gen = new Random();
		if (id == -1) {
			id = gen.nextInt(50000);
		}

		PeerMaker peer = new PeerMaker(Number160.createHash(id)).setPorts(port);
		return peer.makeAndListen();
	}

}
