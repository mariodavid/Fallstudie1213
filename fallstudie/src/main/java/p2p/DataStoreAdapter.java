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
package p2p;

import java.util.Collection;
import java.util.List;

import lupos.datastructures.items.Triple;
import lupos.datastructures.items.literal.Literal;
import p2p.distribution.DistributionStrategy;

/**
 * Der DataStoreAdapter stellt die Schnittstelle zwischen Lupos und einen
 * darunterliegenden Speicher dar. Dieser kann beispielweise ein P2P Netzwerk
 * sein.
 */
public interface DataStoreAdapter {

	/**
	 * Gibt die Verteilungsstrategie zurück.
	 * 
	 * @return die Verteilungsstrategie
	 */
	public abstract DistributionStrategy getDistributionStrategy();

	/**
	 * Setzt die Verteilungsstrategie.
	 * 
	 * @param distributionStrategy
	 *            die neue Verteilungsstrategie
	 */
	public abstract void setDistributionStrategy(
			DistributionStrategy distributionStrategy);

	/**
	 * Gibt alle Triple zurück die unter einem Key gespeichert sind.
	 * 
	 * @param key
	 *            den Key
	 * @return eine Collection von Triple
	 */
	public abstract Collection<Triple> get(List<Literal> key);

	/**
	 * Fügt ein Triple hinzu.
	 * 
	 * @param triple
	 *            Triple
	 * @return true, wenn erfolgreich hinzugefügt.
	 */
	public abstract boolean add(Triple triple);

	/**
	 * Löscht ein Triple.
	 * 
	 * @param triple
	 *            Triple
	 * @return true, wenn erfolgreich entfernt
	 */
	public abstract boolean remove(Triple triple);

	/**
	 * Gibt True Zurück wenn ein Triple vorhanden ist.
	 * 
	 * @param triple
	 *            Triple
	 * @return true, wenn Triple vorhanden
	 */
	public abstract boolean contains(Triple triple);

}