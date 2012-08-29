package operatoren;

import lupos.datastructures.queryresult.QueryResult;
import lupos.engine.operators.application.Application;


/**
 * diese Klasse wird mit dem Triple Pattern verschickt und dann bei dem Empf�nger von dem 
 * ResultOperator aufgerufen. In der Call Methode ist das Ergebnis der Teilanfrage dann drin
 * und soll an den �rspr�nglichen Sender zur�ck geschickt werden.
 * Dazu muss der urspr�ngliche Sender beim versenden in dieser Klasse irgendwie seine Quell IP festlegen,
 * damit der Empf�nger an diese dann zur�ckschicken kann.
 * @author Phati
 *
 */
public class P2PApplication implements Application{

	public void call(QueryResult res) {
		// TODO Auto-generated method stub
		// Anfrageresultat kommt an (Empfangsseite)
		// Nun muss das queryresult an den sender verschickt werden
		// package: lupos.endpoint.server.format; -> serialisieren
		// deserialisieren: lupos.endpoint.client.formatreader;
	
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
