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
