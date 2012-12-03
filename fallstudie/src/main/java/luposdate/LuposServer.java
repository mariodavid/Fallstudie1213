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
package luposdate;

import http.HTTPServer;

import java.util.Collection;
import java.util.LinkedList;

import lupos.datastructures.bindings.Bindings;
import lupos.datastructures.bindings.BindingsMap;
import lupos.datastructures.items.literal.LiteralFactory;
import lupos.datastructures.items.literal.LiteralFactory.MapType;
import lupos.datastructures.items.literal.URILiteral;
import luposdate.evaluators.P2PIndexQueryEvaluator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p.P2PAdapter;
/**
 * In dieser Klasse wird eine Lupos Instanz gestartet.
 */
public class LuposServer {

	/** Evaluator. */
	private P2PIndexQueryEvaluator evaluator;

	/**
	 * Gibt den Evaluator zurück.
	 * 
	 * @return evaluator
	 */
	public P2PIndexQueryEvaluator getEvaluator() {
		return evaluator;
	}

	/**
	 * An dieser Stellt wird die Lupos Instanz gestartet. Zusätzlich wird die
	 * Verbindung von dem Evauator zum P2PAdapter hergestellt.
	 * 
	 * @param p2pAdapter
	 *            den p2pAdapter
	 */
	public void start(P2PAdapter p2pAdapter) {
		Logger logger = LoggerFactory.getLogger(LuposServer.class);
		logger.info("starting up lupos server instance...");

		System.out.println("starting up lupos instance...");
		try {
			Bindings.instanceClass = BindingsMap.class;

			evaluator = new P2PIndexQueryEvaluator();

			evaluator.setP2PAdapter(p2pAdapter);
			Collection<URILiteral> defaultGraphs = new LinkedList<URILiteral>();

			LiteralFactory.setType(MapType.NOCODEMAP);
			defaultGraphs.add(LiteralFactory
					.createURILiteralWithoutLazyLiteral("<inlinedata:>"));
			Collection<URILiteral> namedGraphs = new LinkedList<URILiteral>();
			evaluator.prepareInputData(defaultGraphs, namedGraphs);

		} catch (Exception e) {
			e.printStackTrace();
		}

		HTTPServer.initAndStartServer(evaluator);

	}

}
