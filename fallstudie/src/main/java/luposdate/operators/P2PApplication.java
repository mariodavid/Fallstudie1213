package luposdate.operators;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import lupos.datastructures.items.Variable;
import lupos.datastructures.queryresult.QueryResult;
import lupos.endpoint.server.format.Formatter;
import lupos.endpoint.server.format.XMLFormatter;
import lupos.engine.operators.application.Application;

// TODO: Auto-generated Javadoc
/**
 * Diese Klasse wird mit zusammen mit dem Triple Pattern verschickt und dann bei
 * dem Empfänger von dem ResultOperator aufgerufen (call Methode). Wir befinden
 * uns nach der Zeichnung an der Position (3), also direkt bevor der Empfänger
 * sein Ergebnis an den Sender zurückliefern will.
 * 
 * Teil Anfragegraph -------- (1) ------------------> (2) ----------- |Sender|
 * |Empfänger| -------- (4) <------------------ (3) ----------- Anfrage Result
 * 
 * Dazu kann man dem Result Operator (@see
 * lupos.engine.operators.singleinput.Result) sogenannte Applikationen zur
 * Verfügung stellen, die bei einem call Methodenaufruf alle abgearbeitet werden
 * und bei denen wiederrum call aufgerufen wird. So kann man verschiedene
 * Funktionalitäten in einem Result Operator zur Verfügung stellen. Eine
 * Applikation soll dann das Rücksenden durchführen
 * 
 * In der Call Methode ist das Ergebnis der Teilanfrage dann drin und soll an
 * den Ursprünglichen Sender zurück geschickt werden. Dazu muss der
 * ursprüngliche Sender beim versenden in dieser Klasse irgendwie seine Quell IP
 * festlegen, damit der Empfänger an diese dann zurückschicken kann.
 * 
 */
public class P2PApplication implements Application {

	/** The dest_ip. */
	// private String dest_ip;

	/** The result. */
	private String			result;

	/**
	 * Instantiates a new p2 p application.
	 *
	 * @param dest_ip the dest_ip
	 */
	// public P2PApplication(String dest_ip) {
	// this.dest_ip = dest_ip;
	// }

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
		/*
		 * Anfrageresultat (QueryResult res) kommt an (wir befinden uns auf der
		 * Empfangsseite und wollen jetzt wieder zurück schicken). Nun muss das
		 * queryresult an den sender verschickt werden Zum Serialisieren des
		 * QueryResult kann im Packet Endpoint.server/client.format/formatreader
		 * die Klassen verwendet werden: Serialisierung:
		 * lupos.endpoint.server.format; Deserialisierung:
		 * lupos.endpoint.client.formatreader;
		 */

		// Schritt 3: Ruecksenden des QueryResults
		Formatter formatter = new XMLFormatter();

		// hier kommt die Implementierung fur das P2P Netz hin
		OutputStream os = new ByteArrayOutputStream();


		// TODO: schlecht, weil hier das Iteratorkonzept verletzt wird. es muss
		// gewartet werden, bis das komplette query result erzeugt wurde
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

	// public void setDestinationIp(String dest_ip) {
	// this.dest_ip = dest_ip;
	// }

}
