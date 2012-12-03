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
package console.commands;

import java.util.Scanner;

import lupos.datastructures.items.Triple;
import luposdate.evaluators.P2PIndexQueryEvaluator;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;

/**
 * Führt ein GET-Befehl im P2P-Netzwerk aus.
 */
public class Get implements Command {
	/** Future Objekt. */
	private FutureDHT future;

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#execute(java.util.Scanner,
	 * net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {
		String key = scanner.next();
		future = peer.get(Number160.createHash(key)).setAll().start();
		future.awaitUninterruptibly();

		if (future.isSuccess()) {
			printResults(peer, Number160.createHash(key));
		} else {
			System.out.println("NOT FOUND!");
		}

	}

	/**
	 * Prints the results.
	 * 
	 * @param peer
	 *            the peer
	 * @param key
	 *            the key
	 */
	private void printResults(Peer peer, Number160 key) {
		try {
			for (Data result : future.getDataMap().values()) {

				System.out.println("Peer id: "
						+ peer.getPeerBean().getStorage()
								.findPeerIDForResponsibleContent(key));
				if (result.getObject().getClass() == String.class) {
					System.out.print(result.getObject().toString());
				} else if (result.getObject().getClass() == Triple.class) {
					System.out.println(((Triple) result.getObject())
							.toN3String());
				} else if (result.getObject().getClass() == PeerAddress.class) {
					System.out.println(((PeerAddress) result.getObject())
							.toString());
				} else {
					System.out.println("Unbekanntes Format!");
				}

				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {
		return "[key] gets a value from a given key";
	}

}
