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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import lupos.datastructures.items.Variable;
import lupos.datastructures.queryresult.QueryResult;
import lupos.endpoint.server.format.Formatter;
import lupos.endpoint.server.format.JSONFormatter;
import lupos.engine.operators.application.Application;

/**
 * Diese Klasse wird mit zusammen mit dem Triple Pattern verschickt und dann bei
 * dem Empfänger von dem ResultOperator aufgerufen (call Methode). Wir befinden
 * uns nach der Zeichnung an der Position (3), also direkt bevor der Empfänger
 * sein Ergebnis an den Sender zurückliefern will.
 * 
 * | (0) | | Teil Anfragegraph -------- (1) ------------------> (2) -----------
 * |Sender| |Empfänger| -------- (4) <------------------ (3) ----------- Anfrage
 * Result | | | | \/
 * 
 * Dazu kann man dem Result Operator (@see
 * lupos.engine.operators.singleinput.Result) sogenannte Applikationen zur
 * Verfügung stellen, die bei einem call Methodenaufruf alle abgearbeitet werden
 * und bei denen wiederrum call aufgerufen wird. So kann man verschiedene
 * Funktionalitäten in einem Result Operator zur Verfügung stellen. Eine
 * Applikation soll dann das Rücksenden durchführen
 * 
 * In der Call Methode ist das Ergebnis der Teilanfrage dann drin und soll an
 * den Ursprünglichen Sender zurück geschickt werden.
 * 
 */
public class P2PApplication implements Application {

	/** The result. */
	private String	result;


	public P2PApplication() {
	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	
	/* (non-Javadoc)
	 * @see lupos.engine.operators.application.Application#call(lupos.datastructures.queryresult.QueryResult)
	 */
	public void call(QueryResult res) {

		// Schritt 3: Ruecksenden des QueryResults
		Formatter formatter = new JSONFormatter();

		// TODO: schlecht, weil hier das Iteratorkonzept verletzt wird. es muss
		// gewartet werden, bis das komplette query result erzeugt wurde
		OutputStream os = new ByteArrayOutputStream();
		Set<Variable> variables = res.getVariableSet();

		try {
			formatter.writeResult(os, variables, res);

			result = os.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/* (non-Javadoc)
	 * @see lupos.engine.operators.application.Application#start(lupos.engine.operators.application.Application.Type)
	 */
	public void start(Type type) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see lupos.engine.operators.application.Application#stop()
	 */
	public void stop() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see lupos.engine.operators.application.Application#deleteResult(lupos.datastructures.queryresult.QueryResult)
	 */
	public void deleteResult(QueryResult res) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see lupos.engine.operators.application.Application#deleteResult()
	 */
	public void deleteResult() {
		// TODO Auto-generated method stub

	}

}
