package console;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;

public class Connection {

	private static final int DEFAULT_PORT = 4000;
	private Peer peer;

	public Peer getPeer() {
		return peer;
	}

	/**
	 * a connection is established via a given ip and port, so that this
	 * connection tries to communicate to the peer to peer network by
	 * bootstrapping to this ip
	 * 
	 * @param ip
	 * @param port
	 * @return true, if connection was established successfully
	 * 
	 * @throws Exception
	 * @throws UnknownHostException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public boolean connect(String ip, int remotePort, int localPort)
			throws Exception, UnknownHostException, ClassNotFoundException,
			IOException {

		this.peer = createPeer(localPort, -1);
		FutureBootstrap fb = this.peer.bootstrap()
				.setInetAddress(InetAddress.getByName(ip)).setPorts(remotePort)
				.start();
		fb.awaitUninterruptibly();
		return true;
	}

	/**
	 * a connection is established via a broadcast request on the DEFAULT_PORT
	 * 
	 * @return true, if connection was established successfully
	 * 
	 * @throws Exception
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public boolean connect() throws Exception, ClassNotFoundException,
			IOException {
		this.peer = this.createPeer(DEFAULT_PORT, -1);

		FutureBootstrap fb = this.peer.bootstrap().setPorts(DEFAULT_PORT)
				.setBroadcast().start();
		fb.awaitUninterruptibly();

		return true;
	}


	/**
	 * a peer object is created with the given parameters. if the id is -1, a
	 * random id is chosen
	 * 
	 * @param port
	 * @param id
	 * @throws Exception
	 */
	private Peer createPeer(int port, int id) throws Exception {

		Random gen = new Random();
		if (id == -1) {
			id = gen.nextInt(50000);
		}

		PeerMaker peer = new PeerMaker(Number160.createHash(id)).setPorts(port).setFileMessageLogger(new File("log.txt"));
		return peer.makeAndListen();
	}

}
