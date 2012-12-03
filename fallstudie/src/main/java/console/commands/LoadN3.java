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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Scanner;

import lupos.datastructures.items.Triple;
import lupos.engine.evaluators.CommonCoreQueryEvaluator;
import lupos.engine.operators.tripleoperator.TripleConsumer;
import luposdate.evaluators.P2PIndexQueryEvaluator;
import net.tomp2p.p2p.Peer;
import p2p.P2PAdapter;

/**
 * Lädt eine RDF Datei ein und speichert die Triple in das Netzwerk
 * 
 */
public class LoadN3 implements Command {

	private static final int OUTPUT_LIMIT = 100;
	/** The filename. */
	public P2PAdapter adapter;
	private int counter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see P2P.TOM.COMMANDS.Command#execute(java.util.Scanner,
	 * net.tomp2p.p2p.Peer)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		URL url;
		InputStream is = null;
		try {
			url = new URL(scanner.next());
			is = url.openStream();
		} catch (Exception e1) {
			System.out.println("NOT FOUND!");
			e1.printStackTrace();
		}

		load(evaluator, is);

	}

	public int load(P2PIndexQueryEvaluator evaluator, String file) {
		InputStream is = getClass().getClassLoader().getResourceAsStream(file);
		return load(evaluator, is);
	}

	public int load(P2PIndexQueryEvaluator evaluator, InputStream is) {
		this.adapter = (P2PAdapter) evaluator.getP2PAdapter();
		counter = 0;

		final TripleConsumer tc = new TripleConsumer() {
			public void consume(final Triple triple) {
				try {
					counter++;
					adapter.distributionStrategy.distribute(triple);
					if (counter % OUTPUT_LIMIT == 0) {
						System.out.println(counter + " Triple eingelesen ...");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		};

		try {
			CommonCoreQueryEvaluator.readTriples("N3", is, tc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return counter;
	}

	public String getDescription() {
		return "load n3 file from a http link";
	}
}
