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
package p2p.distribution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import net.tomp2p.p2p.Peer;

import lupos.datastructures.items.Triple;

/**
 * The Class ThreeKeyDistribution is a distribution strategy where all three
 * elements of the triple is stored within the key. Only one possible
 * combinations are stored h(spo)
 */
public class CombinationDistribution extends AbstractDistributionStrategy {

	protected LinkedList<DistributionStrategy> strategies;

	public CombinationDistribution() {
		strategies = new LinkedList<DistributionStrategy>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * P2P.TOM.TRIPLE.DISTRIBUTION.AbstractDistributionStrategy#distribute(P2P
	 * .TOM.RDFTriple)
	 */
	@Override
	public void distribute(Triple triple) throws IOException {
		for (DistributionStrategy distribution : strategies) {
			distribution.distribute(triple);
		}
	}

	public String[] getDistributeStrings(Triple triple) {
		ArrayList<String> result = new ArrayList<String>();
		for (DistributionStrategy distribution : strategies) {
			for (String item : distribution.getDistributeStrings(triple))
				result.add(item);
		}
		return result.toArray(new String[result.size()]);
	}

	@Override
	public void remove(Triple triple) throws IOException {
		for (DistributionStrategy distribution : strategies) {
			distribution.remove(triple);
		}
	}

	@Override
	public boolean contains(Triple triple) throws IOException {

		for (DistributionStrategy distribution : strategies) {
			if (distribution.contains(triple)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void setPeer(Peer peer) {
		super.setPeer(peer);

		for (DistributionStrategy strategy : strategies) {
			strategy.setPeer(peer);
		}
	}

	public int getStrategyID() {
		return 0;
	}
}
