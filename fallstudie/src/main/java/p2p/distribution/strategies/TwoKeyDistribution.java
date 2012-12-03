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
 * The Class TwoKeyDistribution is a distribution strategy where two elements of
 * the triple is stored within the key. All combinations are stored h(sp),
 * h(po), h(so)
 * 
 * @author Mario David, Sebastian Walther, Andreas Haller, Thomas Kiencke
 * 
 */
public class TwoKeyDistribution extends AbstractDistributionStrategy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * P2P.TOM.TRIPLE.DISTRIBUTION.AbstractDistributionStrategy#distribute(P2P
	 * .TOM.RDFTriple)
	 */
	@Override
	public void distribute(Triple triple) throws IOException {
		addToNetwork(getSP(triple), triple);
		addToNetwork(getSO(triple), triple);
		addToNetwork(getPO(triple), triple);
	}

	public String[] getDistributeStrings(Triple triple) {
		String[] result = { getSP(triple), getSO(triple), getPO(triple) };
		return result;
	}

	@Override
	public void remove(Triple triple) throws IOException {
		removeFromNetwork(getSP(triple), triple);
		removeFromNetwork(getSO(triple), triple);
		removeFromNetwork(getPO(triple), triple);
	}

	@Override
	public boolean contains(Triple triple) throws IOException {
		return isInNetwork(getSP(triple), triple)
				&& isInNetwork(getSO(triple), triple)
				&& isInNetwork(getPO(triple), triple);
	}

	@Override
	public String toString() {
		return "2: TwoKeyStrategy [h(SP),h(SO),h(PO)]";
	}
	
	protected String getSP(Triple triple) {
		return triple.getSubject().originalString()
				+ triple.getPredicate().originalString();
	}

	protected String getSO(Triple triple) {
		return triple.getSubject().originalString()
				+ triple.getObject().originalString();
	}

	protected String getPO(Triple triple) {
		return triple.getPredicate().originalString()
				+ triple.getObject().originalString();
	}
}
