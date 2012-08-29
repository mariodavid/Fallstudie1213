package operatoren;

import lupos.datastructures.queryresult.QueryResult;
import lupos.engine.operators.application.Application;

/**
 * Diese Klasse wird mit zusammen mit dem Triple Pattern verschickt und dann bei
 * dem Empf�nger von dem ResultOperator aufgerufen (call Methode). Wir befinden uns
 * nach der Zeichnung an der Position (3), also direkt bevor der Empf�nger sein Ergebnis an
 * den Sender zur�ckliefern will.
 * 
 *                Teil Anfragegraph
 * -------- (1) ------------------> (2) -----------
 * |Sender|                             |Empf�nger|
 * -------- (4) <------------------ (3) -----------
 * 			        Anfrage Result
 * 
 * Dazu kann man dem Result Operator (@see lupos.engine.operators.singleinput.Result)
 * sogenannte Applikationen zur Verf�gung stellen, die bei einem call Methodenaufruf
 * alle abgearbeitet werden und bei denen wiederrum call aufgerufen wird. 
 * So kann man verschiedene Funktionalit�ten in einem Result Operator zur Verf�gung stellen.
 * Eine Applikation soll dann das R�cksenden durchf�hren
 * 
 * In der Call Methode ist das Ergebnis der Teilanfrage dann drin und soll an den
 * �rspr�nglichen Sender zur�ck geschickt werden. Dazu muss der urspr�ngliche
 * Sender beim versenden in dieser Klasse irgendwie seine Quell IP festlegen,
 * damit der Empf�nger an diese dann zur�ckschicken kann.
 * 
 */
public class P2PApplication implements Application {

	public void call(QueryResult res) {
		/*
		 * Anfrageresultat (QueryResult res) kommt an (wir befinden uns auf der
		 * Empfangsseite und wollen jetzt wieder zur�ck schicken). Nun muss das
		 * queryresult an den sender verschickt werden Zum Serialisieren des
		 * QueryResult kann im Packet Endpoint.server/client.format/formatreader
		 * die Klassen verwendet werden:
		 * Serialisierung:  lupos.endpoint.server.format;
		 * Deserialisierung: lupos.endpoint.client.formatreader;
		 */

	}

	public void start(Type type) {
		// TODO Auto-generated method stub

	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public void deleteResult(QueryResult res) {
		// TODO Auto-generated method stub

	}

	public void deleteResult() {
		// TODO Auto-generated method stub

	}

}
