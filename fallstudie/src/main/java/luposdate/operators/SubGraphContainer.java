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
package luposdate.operators;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lupos.datastructures.items.Item;
import lupos.datastructures.items.literal.Literal;
import lupos.datastructures.queryresult.QueryResult;
import lupos.endpoint.client.formatreader.JSONFormatReader;
import lupos.endpoint.client.formatreader.MIMEFormatReader;
import lupos.engine.operators.RootChild;
import lupos.engine.operators.index.Dataset;
import lupos.engine.operators.index.Root;
import lupos.engine.operators.messages.BoundVariablesMessage;
import lupos.engine.operators.messages.Message;
import lupos.engine.operators.tripleoperator.TriplePattern;
import lupos.rdf.Prefix;
import luposdate.operators.formatter.SubGraphContainerFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import p2p.P2PAdapter;
import p2p.distribution.AbstractDistributionStrategy;
import p2p.distribution.strategies.SevenKeyDistribution;

/**
 * enthaelt die Operatoren, die alle an den Empfaengerknoten verschickt werden
 * sollen.
 * 
 */
// TODO: extend from RootChild instead of BasicIndexScan
public class SubGraphContainer extends RootChild {

	/** The root node of sub graph. */
	private final Root rootNodeOfSubGraph;

	private Collection<TriplePattern> hashableTriplePatterns;

	private final P2PAdapter p2pAdapter;

	/**
	 * Instantiates a new sub graph container.
	 * 
	 * @param rootNodeOfOuterGraph
	 *            the root node of outer graph
	 * @param rootNodeOfSubGraph
	 *            the root node of sub graph
	 */
	public SubGraphContainer(P2PAdapter p2pAdapter, Root rootNodeOfOuterGraph,
			Root rootNodeOfSubGraph) {
		// super(rootNodeOfOuterGraph);

		this.p2pAdapter = p2pAdapter;
		this.rootNodeOfSubGraph = rootNodeOfSubGraph;

	}

	/**
	 * wird aufgerufen, wenn der OP ausgefuehrt werden soll hier wird verschickt
	 * und auf das Ergebnis gewartet und zurueckgegeben
	 * 
	 * 
	 * @param queryResult
	 *            the query result
	 * @param operandID
	 *            the operand id
	 * @return the query result
	 */

	@Override
	public QueryResult process(Dataset dataset) {
		SubGraphContainerFormatter serialzer = new SubGraphContainerFormatter();

		JSONObject serializedGraph;
		try {
			// Schritt 1: Serialisierung
			serializedGraph = serialzer.serialize(rootNodeOfSubGraph, 0);
			String key = getKey();

			// Versand des Teilbaumes und Empfang des Ergebnisses
			String result = p2pAdapter.sendMessage(key,
					serializedGraph.toString());

			// Schritt 4: Deserialisierung
			MIMEFormatReader deserializier = new JSONFormatReader();

			InputStream is;
			byte[] ba = result.getBytes("UTF-8");

			is = new ByteArrayInputStream(ba);
			QueryResult queryResult = deserializier.getQueryResult(is);

			return queryResult;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private String getKey() {
		ArrayList<TriplePattern> hash = new ArrayList<TriplePattern>(
				hashableTriplePatterns);
		List<Literal> literale = getLiterals(hash.get(0).getItems());
		return ((AbstractDistributionStrategy) p2pAdapter
				.getDistributionStrategy()).generateKey(literale);
	}

	@Override
	public String toString() {
		return "SubGraphContainer";
	}

	@Override
	public String toString(Prefix prefixInstance) {
		return toString();
	}

	public void setHashableTriplePatterns(
			Collection<TriplePattern> triplePattern) {
		this.hashableTriplePatterns = triplePattern;
	}

	@Override
	public Message preProcessMessage(final BoundVariablesMessage msg) {
		BoundVariablesMessage newMsg = new BoundVariablesMessage(msg);
		newMsg.setVariables(getUnionVariables());
		return newMsg;
	}

	/**
	 * Generate key.
	 * 
	 * @param items
	 *            the items
	 * @return the string
	 */
	private String generateKey(List<Literal> items) {
		StringBuilder key = new StringBuilder();
		for (Literal literal : items)
			key.append(literal.toString());
		return key.toString();
	}

	/**
	 * Generate key.
	 * 
	 * @param items
	 *            the items
	 * @return the literal
	 */
	private List<Literal> getLiterals(Item[] items) {
		List<Literal> result = new ArrayList<Literal>();
		for (Item item : items) {
			if (!item.isVariable()) {
				Literal lit = (Literal) item;
				result.add(lit);
			}
		}
		return result;
	}

}
