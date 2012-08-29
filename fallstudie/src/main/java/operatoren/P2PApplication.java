package operatoren;

import lupos.datastructures.queryresult.QueryResult;
import lupos.engine.operators.application.Application;

/**
 * Diese Klasse wird mit zusammen mit dem Triple Pattern verschickt und dann bei
 * dem EmpfŠnger von dem ResultOperator aufgerufen (call Methode). Wir befinden uns
 * nach der Zeichnung an der Position (3), also direkt bevor der EmpfŠnger sein Ergebnis an
 * den Sender zurŸckliefern will.
 * 
 *                Teil Anfragegraph
 * -------- (1) ------------------> (2) -----------
 * |Sender|                             |EmpfŠnger|
 * -------- (4) <------------------ (3) -----------
 * 			        Anfrage Result
 * 
 * Dazu kann man dem Result Operator (@see lupos.engine.operators.singleinput.Result)
 * sogenannte Applikationen zur VerfŸgung stellen, die bei einem call Methodenaufruf
 * alle abgearbeitet werden und bei denen wiederrum call aufgerufen wird. 
 * So kann man verschiedene FunktionalitŠten in einem Result Operator zur VerfŸgung stellen.
 * Eine Applikation soll dann das RŸcksenden durchfŸhren
 * 
 * In der Call Methode ist das Ergebnis der Teilanfrage dann drin und soll an den
 * ŸrsprŸnglichen Sender zurŸck geschickt werden. Dazu muss der ursprŸngliche
 * Sender beim versenden in dieser Klasse irgendwie seine Quell IP festlegen,
 * damit der EmpfŠnger an diese dann zurŸckschicken kann.
 * 
 */
public class P2PApplication implements Application {

	public void call(QueryResult res) {
		/*
		 * Anfrageresultat (QueryResult res) kommt an (wir befinden uns auf der
		 * Empfangsseite und wollen jetzt wieder zurŸck schicken). Nun muss das
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
