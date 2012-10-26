package console.commands;

import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;

/**
 * Interface Command.
 */
public interface Command {
	
	/**
	 * Führt das Kommando aus.
	 *
	 * @param scanner Scanner Referenz zum einlesen der Parameter
	 * @param peer Peer Referenz
	 * @param evaluator Lupos Evaluator Referenz
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator);

	/**
	 * Gibt die Beschreibung des Kommandos zurück.
	 *
	 * @return the description
	 */
	public String getDescription();

}
