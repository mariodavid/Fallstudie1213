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

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import lupos.datastructures.items.Triple;
import luposdate.evaluators.P2PIndexQueryEvaluator;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number480;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;

/**
 * Gibt den lokalen Speicher eines Peers aus.
 */
public class GetLocalStorage implements Command {

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#execute(java.util.Scanner,
	 * net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		printHeader(scanner, peer, evaluator);

		printElements(peer);

		printFooter(peer);
	}

	private void printHeader(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		Command getMyIdCommand = new GetMyID();
		getMyIdCommand.execute(scanner, peer, evaluator);

		System.out.println();
		System.out.println("Key\t\t\t\t\t\tValue");
	}

	private void printFooter(Peer peer) {
		System.out.println();
		System.out.println("local storagesize: "
				+ peer.getPeerBean().getStorage().map().size());
	}

	private void printElements(Peer peer) {
		Map<Number480, Data> map = peer.getPeerBean().getStorage().map();

		for (Entry<Number480, Data> result : map.entrySet()) {
			try {
				System.out.print(result.getKey().getLocationKey() + "\t");

				if (result.getValue().getObject().getClass() == String.class) {
					System.out
							.println(result.getValue().getObject().toString());
				} else if (result.getValue().getObject().getClass() == Triple.class) {
					System.out.println(((Triple) result.getValue().getObject())
							.toN3String());
				} else if (result.getValue().getObject().getClass() == PeerAddress.class) {
					System.out.println("PeerAdress von ID: "
							+ ((PeerAddress) result.getValue().getObject())
									.getID());
				} else {
					System.out.println("Unbekanntes Format!");
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {

		return "returns all elements that are in the local peer storage";
	}

}
