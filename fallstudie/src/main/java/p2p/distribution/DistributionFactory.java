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

import p2p.distribution.strategies.*;

/**
 * This class creates Strategies for the distribution of RDF Triple within the
 * network. The pattern which is used is the Factory Method
 * (http://en.wikipedia.org/wiki/Factory_method)
 */
public class DistributionFactory {

	/**
	 * creates a DistributionStrategy based on the given strategy
	 * 
	 * @param strategy
	 *            1: A distribution strategy with only one element of the triple
	 *            as the hash h(s), h(p), h(o) 2: A distribution strategy with
	 *            two elements of the triple as the hash h(sp), h(po), h(so) 3:
	 *            A distribution strategy with all three elements of the triple
	 *            as the hash h(spo)
	 * @return the corresponding distribution strategy
	 */
	public static DistributionStrategy create(int strategy) {
		switch (strategy) {
			case 1:
				return new OneKeyDistribution();
			case 2:
				return new TwoKeyDistribution();
			case 3:
				return new ThreeKeyDistribution();
			case 4:
				return new FourKeyDistribution();
			case 5:
				return new FiveKeyDistribution();
			case 6:
				return new SixKeyDistribution();
			case 7:
				return new SevenKeyDistribution();
			default:
				return new OneKeyDistribution();
		}
	}
}
