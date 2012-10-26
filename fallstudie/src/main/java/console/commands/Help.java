package console.commands;

import java.util.Collection;
import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;

/**
 * Gibt jeden Befehl und die dazugehörige Beschreibung zurück.
 */
public class Help implements Command {

	/** The commands. */
	private final Collection<Command> commands;

	/**
	 * Instantiates a new help.
	 * 
	 * @param commands
	 *            the commands
	 */
	public Help(Collection<Command> commands) {
		this.commands = commands;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#execute(java.util.Scanner,
	 * net.tomp2p.p2p.Peer, luposdate.evaluators.P2PIndexQueryEvaluator)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		for (Command c : commands) {
			printCommandDescription(c);
		}

	}

	/**
	 * Prints the command description.
	 * 
	 * @param c
	 *            the c
	 */
	private void printCommandDescription(Command c) {
		System.out.printf("%-20s", c.getClass().getSimpleName());
		System.out.println(c.getDescription());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {
		return "this help dialog";
	}

}
