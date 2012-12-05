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
package p2p.distribution.strategies;

import java.io.IOException;

import p2p.distribution.AbstractDistributionStrategy;

import lupos.datastructures.items.Triple;

/**
 * The Class ThreeKeyDistribution is a distribution strategy where all three
 * elements of the triple is stored within the key. Only one possible
 * combinations are stored h(spo)
 */
public class ThreeKeyDistribution extends AbstractDistributionStrategy {

	public final static int STRATEGY_ID = 3;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * P2P.TOM.TRIPLE.DISTRIBUTION.AbstractDistributionStrategy#distribute(P2P
	 * .TOM.RDFTriple)
	 */
	@Override
	public void distribute(Triple triple) throws IOException {
		addToNetwork(getSPO(triple), triple);
	}

	public String[] getDistributeStrings(Triple triple) {
		String[] result = { getSPO(triple) };
		return result;
	}

	@Override
	public void remove(Triple triple) throws IOException {
		removeFromNetwork(getSPO(triple), triple);
	}

	@Override
	public boolean contains(Triple triple) throws IOException {
		return isInNetwork(getSPO(triple), triple);
	}

	@Override
	public String toString() {
		return "3: ThreeKeyStrategy [h(SPO)]";
	}

	protected String getSPO(Triple triple) {
		return triple.getPos(0).originalString()
				+ triple.getPredicate().originalString()
				+ triple.getObject().originalString();
	}

	public int getStrategyID() {
		return STRATEGY_ID;
	}
}
