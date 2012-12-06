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

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import lupos.datastructures.items.Triple;
import lupos.datastructures.items.literal.Literal;
import lupos.datastructures.items.literal.URILiteral;
import lupos.engine.operators.index.Indices;
import p2p.DataStoreAdapter;
import p2p.P2PAdapter;

/**
 * In dieser Klasse wird der Index verwaltet. Das beinhaltet im wesentlichen die
 * Basisoperationen Add, Remove, Cointains und Get die auf dem P2P-Netzwerk die
 * entsprechenden Methoden ausführen.
 */
public class P2PIndices extends Indices {

	/** Referenz zum P2P-Adapter. */
	private DataStoreAdapter adapter;

	/**
	 * Hier ist wichtig, dass für jedes P2PIndices ein neues P2PNetzwerk
	 * erstellt wird, wobei jedes P2P Netzwerk dann einer Datenbank entspricht,
	 * die angesprochen werden kann.
	 * 
	 * @param uriLiteral
	 *            the uri literal
	 */
	public P2PIndices(URILiteral uriLiteral) {
		super();
		setRdfName(uriLiteral);
		((P2PAdapter) adapter).connect();
	}

	/**
	 * Instantiates a new p2 p indices.
	 * 
	 * @param uriLiteral
	 *            the uri literal
	 * @param adapter
	 *            the adapter
	 */
	public P2PIndices(URILiteral uriLiteral, P2PAdapter adapter) {
		super();
		setRdfName(uriLiteral);
		this.adapter = adapter;
	}

	/**
	 * Gets the all.
	 * 
	 * @param key
	 *            the key
	 * @return the all
	 */
	public Collection<Triple> getAll(List<Literal> key) {
		return adapter.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lupos.engine.operators.index.Indices#add(lupos.datastructures.items.Triple
	 * )
	 */
	@Override
	public boolean add(Triple t) {
		return adapter.add(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lupos.engine.operators.index.Indices#remove(lupos.datastructures.items
	 * .Triple)
	 */
	@Override
	public boolean remove(Triple t) {
		return adapter.remove(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lupos.engine.operators.index.Indices#contains(lupos.datastructures.items
	 * .Triple)
	 */
	@Override
	public boolean contains(Triple t) {
		return adapter.contains(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lupos.engine.operators.index.Indices#init(lupos.engine.operators.index
	 * .Indices.DATA_STRUCT)
	 */
	@Override
	public void init(DATA_STRUCT ds) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lupos.engine.operators.index.Indices#constructCompletely()
	 */
	@Override
	public void constructCompletely() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lupos.engine.operators.index.Indices#writeOutAllModifiedPages()
	 */
	@Override
	public void writeOutAllModifiedPages() throws IOException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lupos.engine.operators.index.Indices#numberOfTriples()
	 */
	@Override
	public int numberOfTriples() {
		return 0;
	}

}
