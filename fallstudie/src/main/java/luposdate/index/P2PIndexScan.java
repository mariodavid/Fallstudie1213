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
package luposdate.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lupos.datastructures.bindings.Bindings;
import lupos.datastructures.items.Item;
import lupos.datastructures.items.Triple;
import lupos.datastructures.items.Variable;
import lupos.datastructures.items.literal.Literal;
import lupos.datastructures.queryresult.QueryResult;
import lupos.engine.operators.OperatorIDTuple;
import lupos.engine.operators.index.BasicIndexScan;
import lupos.engine.operators.index.Indices;
import lupos.engine.operators.index.Root;
import lupos.engine.operators.tripleoperator.TriplePattern;

/**
 * ist für die ein oder mehrere Tripelmuster auszuführen.
 */
public class P2PIndexScan extends BasicIndexScan {

	/**
	 * Instantiates a new p2p index scan.
	 * 
	 * @param indexCollection
	 *            the index collection
	 */
	public P2PIndexScan(Root indexCollection) {
		super(indexCollection);
	}

	/**
	 * Instantiates a new p2p index scan.
	 * 
	 * @param succeedingOperator
	 *            the succeeding operator
	 * @param triplePattern
	 *            the triple pattern
	 * @param data
	 *            the data
	 * @param indexCollection
	 *            the index collection
	 */
	public P2PIndexScan(OperatorIDTuple succeedingOperator,
			Collection<TriplePattern> triplePattern, Item data,
			Root indexCollection) {
		super(succeedingOperator, triplePattern, data, indexCollection);
	}

	/**
	 * tripelpattern muss ausgewertet werden. Dazu muss das P2P Netzwerk über
	 * die "indices" variable angesprochenw werden.
	 * 
	 * Als Eingabe kommt: "?s rdf:type ?class" z.b
	 * 
	 * Als Ergebnis soll dann zurückgegeben werden: "?s=<Hallo>, ?class=<Test>"
	 * "?s=<Hallo2>, ?class=<Test2>" "?s=<Hallo>, ?class=<Test3>" ...
	 * 
	 * Bindings ist normalerweise leer, außer wenn die graph operation in der
	 * SPARQL Anfrage verwendet wird. Dabei werden dann bei den bindings an
	 * gegebene Variablen direkt werte gebunden. Dann muss im tripelmuster
	 * nachgeschaut werden, ob diese variable existiert und ggf. ersetzt werden
	 * durch den übergebenen wert. Die Variablen die übergeben werden müssen
	 * dann im Ergebnis auch auftauchen
	 * 
	 * @param indices
	 *            the indices
	 * @param bindings
	 *            the bindings
	 * @return the query result
	 */
	@Override
	public QueryResult join(Indices indices, Bindings bindings) {

		P2PIndices p2pIndices = (P2PIndices) indices;
		QueryResult result = QueryResult.createInstance();

		for (TriplePattern pattern : this.triplePatterns) {

			Item[] items = pattern.getItems();

			List<Literal> literale = getLiterals(items);

			for (Triple triple : p2pIndices.getAll(literale)) {
				Bindings b = addVariablesToBindings(items, triple);
				if (b != null) {
					result.add(b);
				}
			}
		}

		return result;
	}

	/**
	 * Adds the variables to bindings.
	 * 
	 * @param items
	 *            the items
	 * @param t
	 *            the t
	 * @return the bindings
	 */
	private Bindings addVariablesToBindings(Item[] items, Triple t) {
		Bindings b = Bindings.createNewInstance();
		for (int i = 0; i < items.length; i++) {
			Item item = items[i];
			if (item.getClass() == Variable.class) {
				Variable v = (Variable) item;
				b.add(v, t.getPos(i));

			} else {
				if (t.getPos(i)
						.compareToNotNecessarilySPARQLSpecificationConform(
								(Literal) item) != 0) {
					return null;
				}
			}
		}
		return b;
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
